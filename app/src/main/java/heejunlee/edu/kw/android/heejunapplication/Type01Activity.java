package heejunlee.edu.kw.android.heejunapplication;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Type01Activity extends AppCompatActivity {
    BroadcastReceiver mBR;
    private static PowerManager.WakeLock sCpuWakeLock;
    SeekBar seekvol;
    final SimpleDateFormat sdfnow = new SimpleDateFormat("HH:mm a");
    TextView sdfnow_for_tv;
    AudioManager audio;

    Context context;
    int h,m;
    SeekBar.OnSeekBarChangeListener voseek=new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmManager alarmManager = (AlarmManager)getSystemService(this.ALARM_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Calendar calendar = Calendar.getInstance();

        setContentView(R.layout.activity_type01);
        this.context=this;
        //Alarm setting
        Button stopBtn = (Button)findViewById(R.id.stopBtn);
        Intent intent = getIntent();
        h = intent.getExtras().getInt("hour");
        m = intent.getExtras().getInt("minute");
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), h, m, 0);
        seekvol=(SeekBar) findViewById(R.id.seekvolumn);
        sdfnow_for_tv = (TextView)findViewById(R.id.currentTime);
        sdfnow_for_tv.setText(sdfnow.format(new Date()));
        audio=(AudioManager) getSystemService(AUDIO_SERVICE);
        int aMax=audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int aVol=audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekvol.setMax(aMax);
        seekvol.setProgress(aVol);
        seekvol.setOnSeekBarChangeListener(voseek); // 볼륨조정
        final Intent serintent = new Intent(this, kooljamservice.class);
        serintent.putExtra("type",1);
        startService(serintent);
        serintent.putExtra("type",3);
        PendingIntent sender = PendingIntent.getService(this,0,serintent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("정말로 알람을 종료 시키시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "모드 선택 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
                                stopService(serintent);
                                int pid = android.os.Process.myPid();
                                android.os.Process.killProcess(pid);
                            }
                        })
                        .setNegativeButton("아니요", null)
                        .show();

            }
        });

    }
    @Override
    public void onStart()
    {
        super.onStart();
        final Intent serintent = new Intent(this, kooljamservice.class);
        mBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent)
            {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    sdfnow_for_tv.setText(sdfnow.format(new Date()));
                    // Update your thing
                    long now =System.currentTimeMillis();
                    Date date = new Date(now);
                    String getTime = sdfnow.format(date);
                    String wakeTime = String.valueOf(h)+":"+String.valueOf(m);

                    if(getTime.contains(wakeTime)){
                        TextView wakeUpText = (TextView)findViewById(R.id.wakeUpText);
                        wakeUpText.setText("Wake Up!!!!!");
                        stopService(serintent);
                        serintent.putExtra("type",3);
                        startService(serintent);
                    }

                }
            }
        };
        registerReceiver(mBR, new IntentFilter(Intent.ACTION_TIME_TICK));
    }
    @Override
    public void onStop()
    {
        super.onStop();
        final Intent serintent = new Intent(this, kooljamservice.class);
        mBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent)
            {
                if (sCpuWakeLock != null) {
                    return;
                }
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                sCpuWakeLock = pm.newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                PowerManager.ON_AFTER_RELEASE, "hi");

                sCpuWakeLock.acquire();


                if (sCpuWakeLock != null) {
                    sCpuWakeLock.release();
                    sCpuWakeLock = null;
                }
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    sdfnow_for_tv.setText(sdfnow.format(new Date()));
                    // Update your thing
                    long now =System.currentTimeMillis();
                    Date date = new Date(now);
                    String getTime = sdfnow.format(date);
                    String wakeTime = String.valueOf(h)+":"+String.valueOf(m);

                    if(getTime.contains(wakeTime)){
                        TextView wakeUpText = (TextView)findViewById(R.id.wakeUpText);
                        wakeUpText.setText("Wake Up!!!!!");
                        stopService(serintent);
                        serintent.putExtra("type",3);
                        startService(serintent);
                    }

                }
            }
        };

    }
    @Override
    public void onBackPressed() {

    }

}
