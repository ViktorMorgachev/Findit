package findit.sedi.viktor.com.findit.common.background_services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import findit.sedi.viktor.com.findit.R;


public class HideNotificationService extends Service {


    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.i("Test", "HideNotificationService: onCreate()");

        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "CNANNEL_ID");
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence chanelName = "channelName";
            String description = "channelDescription";
            NotificationChannel notificationChannel = new NotificationChannel("CNANNEL_ID", chanelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }


        startForeground(777, mBuilder.build());
        stopForeground(true);
        stopSelf();
    }
}
