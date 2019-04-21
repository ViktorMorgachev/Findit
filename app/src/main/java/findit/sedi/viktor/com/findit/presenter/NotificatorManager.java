package findit.sedi.viktor.com.findit.presenter;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


import findit.sedi.viktor.com.findit.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificatorManager {

    private Context mContext;
    private String place;

    public NotificatorManager(String place) {
        this.place = place;
    }

    public NotificatorManager() {

    }

    public void notify(Context context, double distance) {

        mContext = context;
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);

        showCompatibilityNotification(distance, builder);


    }

    private void showCompatibilityNotification(double distance, NotificationCompat.Builder builder) {


        if (distance <= 1000 && distance >= 500) {
            builder.setContentTitle("Ты почти рядом");
            builder.setContentText("Осталось: " + (int) distance + " м");
        }


        if (place != null || place != "") {
            builder.setContentTitle("Ты рядом c " + place);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setContentText("Осталось: " + (int) distance + " м");
        }


        if (distance <= 100 && distance >= 70) {
            builder.setContentTitle("Дистанция почти достигнута");
            builder.setContentText("Осталось: " + (int) distance + " м");
        } else if (distance < 70 && distance >= 30) {
            builder.setContentTitle("Осталось совсем чуть чуть");
            builder.setContentText("Осталось: " + (int) distance + " м");
        } else if (distance < 30 && distance >= 0) {
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setContentTitle("Красавчик");
            Toast.makeText(mContext, "Красавчик", Toast.LENGTH_LONG).show();
            builder.setContentText("Осталось: " + (int) distance + " м");
        }

        builder.setSmallIcon(R.drawable.ic_beenhere_24dp);
        Notification notification = builder.build();
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }

}
