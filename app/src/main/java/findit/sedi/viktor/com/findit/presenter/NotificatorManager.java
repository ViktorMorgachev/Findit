package findit.sedi.viktor.com.findit.presenter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import findit.sedi.viktor.com.findit.R;

public class NotificatorManager {


    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat mNotificationManagerCompat;


    public NotificatorManager() {

    }



    public void showCompatibilityNotification(Context context, String message, int icon, String CNANNEL_ID, String title) {


        mBuilder = new NotificationCompat.Builder(context, CNANNEL_ID);
        mNotificationManagerCompat = NotificationManagerCompat.from(context);


        mBuilder.setSmallIcon(icon);

        if (title != null)
            mBuilder.setContentTitle(title);

        if (message != null)
            mBuilder.setContentText(message);

        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = context.getResources().getString(R.string.channel_name);
            String description = context.getResources().getString(R.string.channel_descrioption);
            NotificationChannel notificationChannel = new NotificationChannel(CNANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(description);
            mBuilder.setChannelId(CNANNEL_ID);
        }

       mNotificationManagerCompat.notify(1, mBuilder.build());

    }


}
