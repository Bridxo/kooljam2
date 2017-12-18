package heejunlee.edu.kw.android.heejunapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lhjun on 2017-12-17.
 */

public class kooljamservice extends Service {
    MediaPlayer type1,type2,alarm;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        type1 = MediaPlayer.create(this, R.raw.type1);
        type2 = MediaPlayer.create(this, R.raw.type2);
        alarm = MediaPlayer.create(this, R.raw.alarm);
        type1.setLooping(true); // 반복재생
        type2.setLooping(true); // 반복재생
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        int type = intent.getIntExtra("type",0);
        if(type == 1)
        {
            type1.start();
        }

        else if(type==2)
        {
            type2.start();
        }
        else if (type==3)
        {
            alarm.start();
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        type1.stop(); // 음악 종료
        type2.stop(); // 음악 종료
    }
}