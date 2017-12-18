package heejunlee.edu.kw.android.heejunapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SelectTypeActivity extends AppCompatActivity {

    private TimePicker tp;
    private Button btn;
    private int selectNum;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_select_type);
        final Context  a = this;
        btn = (Button) findViewById(R.id.okBtn);


        // Spinner
        Spinner typeSpinner = (Spinner)findViewById(R.id.spinner_type);
        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.sleep_type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        tp=(TimePicker)findViewById(R.id.tp);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int h, int m) {
                hour = h;
                minute = m;
            }
        });
      typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

              //Toast.makeText(getApplicationContext(), ""+(i)+"", Toast.LENGTH_LONG).show();
              selectNum = i;

          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
      });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(selectNum==1){
                    intent = new Intent(SelectTypeActivity.this,Type01Activity.class);
                    intent.putExtra("hour",hour);
                    intent.putExtra("minute",minute);
                    startActivity(intent);
                } else if(selectNum==2){
                    intent = new Intent(SelectTypeActivity.this,Type02Activity.class);
                    intent.putExtra("hour",hour);
                    intent.putExtra("minute",minute);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "모드를 선택하세요", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("정말로 앱을 종료 시키시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }

                })
                .setNegativeButton("아니요", null)
                .show();

    }
}
