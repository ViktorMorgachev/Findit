package findit.sedi.viktor.com.findit.presenter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

public class NotificatorManager {


    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;


    public NotificatorManager() {

    }


    public void showCompatibilityNotification(Context context, String message, int icon, String CNANNEL_ID, String title, @NonNull String channelName, @NonNull String channelDescription, Intent intent) {


        mBuilder = new NotificationCompat.Builder(context, CNANNEL_ID);
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        mBuilder.setSmallIcon(icon);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);

        if (title != null)
            mBuilder.setContentTitle(title);

        if (message != null)
            mBuilder.setContentText(message);

        mBuilder.setUsesChronometer(true);


        if (intent != null) {
            mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence chanelName = channelName;
            String description = channelDescription;
            NotificationChannel notificationChannel = new NotificationChannel(CNANNEL_ID, chanelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(description);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
            mBuilder.setColorized(true);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }


        mNotificationManager.notify(1, mBuilder.build());

    }


}
