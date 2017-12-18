package heejunlee.edu.kw.android.heejunapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Type02Activity extends AppCompatActivity {

    final SimpleDateFormat sdfnow = new SimpleDateFormat("HH:mm a");
    TextView sdfnow_for_tv;
    MediaPlayer mediaPlayer;
    BroadcastReceiver mBR;
    Context context;
    int h,hh,m;
    boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        final Intent serintent = new Intent(this, kooljamservice.class);
        setContentView(R.layout.activity_type02);
        this.context=this;
        Intent intent = getIntent();
        flag = false;
        h = intent.getExtras().getInt("hour");
        hh = h-1;
        m = intent.getExtras().getInt("minute");
        sdfnow_for_tv = (TextView)findViewById(R.id.currentTime02);
        sdfnow_for_tv.setText(sdfnow.format(new Date()));
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        AlarmManager alarm2 = (AlarmManager)getSystemService(ALARM_SERVICE);
        serintent.putExtra("type",2);
        PendingIntent pendingIntent = PendingIntent.getService(context,0,serintent,0);
        Calendar mCal = Calendar.getInstance();
        Calendar mCal2 = Calendar.getInstance();
        mCal.set(mCal.get(Calendar.YEAR),mCal.get(Calendar.MONTH),mCal.get(Calendar.DATE),h-1,m);//before 1 hour
        Toast.makeText(getApplicationContext(), h-1+":"+m, Toast.LENGTH_LONG).show();
        alarm.set(AlarmManager.RTC_WAKEUP|AlarmManager.RTC,mCal.getTimeInMillis(),pendingIntent);
        mCal2.set(mCal2.get(Calendar.YEAR),mCal2.get(Calendar.MONTH),mCal2.get(Calendar.DATE),h,m);//wake up
        serintent.putExtra("type",3);
        PendingIntent pendingIntent2 = PendingIntent.getService(context,0,serintent,0);
        alarm2.set(AlarmManager.RTC_WAKEUP|AlarmManager.RTC,mCal2.getTimeInMillis(),pendingIntent2);

        final TextView textView = (TextView)findViewById(R.id.text1);
        textView.setText("Lucid dreaming is the ability to consciously observe and/or control your dreams.\n" +
                "\n" +
                "It transforms your inner dream world into a living alternate reality - where everything you see, hear, feel, taste and even smell is as authentic as real life.\n" +
                "\n" +
                "Lucidity occurs during altered states of consciousness when you realize you are dreaming - and your brain switches into waking mode inside the dream.\n" +
                "\n" +
                "In normal dreams, your self awareness is shut down. That's why they often feel fuzzy and distant. But when lucid, the conscious brain wakes up during sleep.\n" +
                "\n" +
                "This is a safe and natural state. It is not anything spooky or paranormal (in fact, out of body experiences are thought to be explained by the lucid dream state). With lucid dreams, you are always asleep in bed.\n" +
                "\n" +
                "And if you want to, you can wake yourself up.");


        final Button startBtn = (Button)findViewById(R.id.lucidStart);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart();
                long now =System.currentTimeMillis();
                Date date = new Date(now);
                String getTime = sdfnow.format(date);
                String wakeTime = String.valueOf(h)+":"+String.valueOf(m);


                DrawerLayout linearLayout =(DrawerLayout)findViewById(R.id.back);
                linearLayout.setBackgroundColor(Color.parseColor("#000000"));
                textView.setText("");
                startBtn.setVisibility(View.INVISIBLE);
                sdfnow_for_tv.setVisibility(View.VISIBLE);

            }
        });
        findViewById(R.id.lucidStop).setOnClickListener(new View.OnClickListener() {
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

                    long now =System.currentTimeMillis();
                    Date date = new Date(now);
                    String getTime = sdfnow.format(date);
                    String wakeTime = String.valueOf(h)+":"+String.valueOf(m);
                    if(getTime.contains(wakeTime)){
                        TextView wakeUpText = (TextView)findViewById(R.id.wakeUpText02);
                        wakeUpText.setVisibility(View.VISIBLE);
                        wakeUpText.setText("Wake Up!!!!!");
                        serintent.putExtra("type",3);
                        startService(serintent);
                    }

                }
            }
        };

        registerReceiver(mBR, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        final Intent serintent = new Intent(this, kooljamservice.class);
        mBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {

                    sdfnow_for_tv.setText(sdfnow.format(new Date()));

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    String getTime = sdfnow.format(date);
                    String wakeTime = String.valueOf(h) + ":" + String.valueOf(m);
                    if (getTime.contains(wakeTime)) {
                        TextView wakeUpText = (TextView) findViewById(R.id.wakeUpText02);
                        wakeUpText.setVisibility(View.VISIBLE);
                        wakeUpText.setText("Wake Up!!!!!");
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
