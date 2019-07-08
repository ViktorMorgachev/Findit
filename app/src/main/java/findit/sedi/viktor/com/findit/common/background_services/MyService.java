package findit.sedi.viktor.com.findit.common.background_services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.LocationManager;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.common.QrPointManager;
import findit.sedi.viktor.com.findit.common.interfaces.ILocationListener;
import findit.sedi.viktor.com.findit.data_providers.Prefs;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.interactors.KeyPrefs;
import findit.sedi.viktor.com.findit.presenter.NotificatorManager;
import findit.sedi.viktor.com.findit.ui.main.MainActivity;
import findit.sedi.viktor.com.findit.ui.tournament.TounamentActivity;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_LOCATION;


public class MyService extends Service implements ILocationListener {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private NotificatorManager mNotificatorManager;
    private LocationManager mLocationManager;
    private LatLng mLatLng;
    private QrPointManager mQrPointManager;
    private final Calendar tournamentCalendarBegin = Calendar.getInstance();
    private final Calendar tournamentCalendarEnd = Calendar.getInstance();
    private final Calendar systemCalendar = Calendar.getInstance();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


        mLocationManager = LocationManager.getInstance();
        mLocationManager.subscribe(this);
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), "CNANNEL_ID");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setAutoCancel(true);

        mQrPointManager = ManagersFactory.getInstance().getQrPointManager();


        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence chanelName = "channelName";
            String description = "channelDescription";
            NotificationChannel notificationChannel = new NotificationChannel("CNANNEL_ID", chanelName, NotificationManager.IMPORTANCE_NONE);
            notificationChannel.setDescription(description);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }


        mAsyncTask.execute();

        startForeground(777, mBuilder.build());

        Intent intent = new Intent(this, HideNotificationService.class);
        startService(intent);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
    }

    @SuppressLint("StaticFieldLeak")
    private final AsyncTask<Void, Void, Void> mAsyncTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {

            while (true) {
                if (isCancelled()) return null;

                Log.d(LOG_TAG, "AsynkTask is working");
                getDataFromServer();

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }


        public void getDataFromServer() {

            if (true) {

                if (ManagersFactory.getInstance().getAccountManager().getUser().getTournamentID() != null &&
                        !ManagersFactory.getInstance().getAccountManager().getUser().getTournamentID().equalsIgnoreCase("")) {
                    ServerManager.getInstance().getQrPlaces();
                    checkActiveTournaments();
                }

                ServerManager.getInstance().getTournaments();
                ServerManager.getInstance().updateTeams();
            }

        }

        public void checkActiveTournaments() {


            // Если пользователь в турнире, то показывать что турнир скоро начнётся
            // События: Турнир начался, турнир закончился, создан новый турнир
            // Если появился новый турнир, как-то отслеживать, то показывать оповещение, что появился новый турнир

            for (int i = 0; i < ManagersFactory.getInstance().getTournamentManager().getTournaments().size(); i++) {

                tournamentCalendarBegin.setTimeInMillis(ManagersFactory.getInstance().getTournamentManager().getTournaments().get(i).getDateFrom().getSeconds() * 1000);
                tournamentCalendarEnd.setTimeInMillis(ManagersFactory.getInstance().getTournamentManager().getTournaments().get(i).getDateTo().getSeconds() * 1000);

                mNotificatorManager = new NotificatorManager();


                // Смотрим, оповещали ли мы пользователя о турнире?
                // Если нет, то оповещаем, иначе не оповещаем
                // Привязка по преференсам будет

                if (systemCalendar.before(tournamentCalendarBegin) &&
                        !Prefs.getInstance().getStringValue(KeyPrefs.KeysField.KEY_USER_NOTIFICATED_ABOUT_TOURNAMENT).equalsIgnoreCase(KeyPrefs.KeysField.KEY_SOON_TOURNAMENT)) {


                    mNotificatorManager
                            .showCompatibilityNotification(getApplicationContext(),
                                    getApplicationContext().getResources().getString(R.string.soon_will_starting_tournament) + " " + ManagersFactory.getInstance().getTournamentManager().getTournaments().get(i).getDescribe(),
                                    R.drawable.ic_stars_24dp, "CHANNEL_ID",
                                    null, getApplicationContext().getResources().getString(R.string.channel_name),
                                    getApplicationContext().getResources().getString(R.string.channel_descrioption), new Intent(getApplicationContext(), TounamentActivity.class));

                    Prefs.getInstance().savePrefs(KeyPrefs.KeysField.KEY_USER_NOTIFICATED_ABOUT_TOURNAMENT, KeyPrefs.KeysField.KEY_SOON_TOURNAMENT);


                } else if ((systemCalendar.equals(tournamentCalendarBegin) || (systemCalendar.after(tournamentCalendarBegin) && systemCalendar.before(tournamentCalendarEnd)) &&
                        !Prefs.getInstance().getStringValue(KeyPrefs.KeysField.KEY_USER_NOTIFICATED_ABOUT_TOURNAMENT).equalsIgnoreCase(KeyPrefs.KeysField.KEY_BEGUN_TOURNAMENT))) {
                    // Если время в пределах времени начала и конца турнира
                    mNotificatorManager
                            .showCompatibilityNotification(getApplicationContext(),
                                    getApplicationContext().getResources().getString(R.string.now_started_tournament) + " " + ManagersFactory.getInstance().getTournamentManager().getTournaments().get(i).getDescribe(),
                                    R.drawable.ic_stars_24dp, "CHANNEL_ID",
                                    null, getApplicationContext().getResources().getString(R.string.channel_name),
                                    getApplicationContext().getResources().getString(R.string.channel_descrioption), new Intent(getApplicationContext(), MainActivity.class));

                    Prefs.getInstance().savePrefs(KeyPrefs.KeysField.KEY_USER_NOTIFICATED_ABOUT_TOURNAMENT, KeyPrefs.KeysField.KEY_BEGUN_TOURNAMENT);

                }

                // Отправляем информацию о оповещении пользователя в Перференсы, Тип: Прошёл турнир, Начался, Скоро турнир, и его ID, сохраняем мапу

            }


        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAsyncTask.cancel(true);
        mLocationManager.unsubscribe(this);

    }

    @Override
    public void updateLocation(LatLng latLng) {


        Log.i(LOG_TAG, "MyService Locations was updated");

        mLatLng = latLng;

        if (ManagersFactory.getInstance().getAccountManager().getUser() != null) {

            ManagersFactory.getInstance().getAccountManager().getUser().setGeopoint(latLng.latitude, latLng.longitude);

            // Отправляем на сервер если расстояние изменилось более чем на 50м

            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_LOCATION);


            if (!ManagersFactory.getInstance().getQrPointManager().getQrPlaces().isEmpty() &&
                    ManagersFactory.getInstance().getAccountManager().getUser() != null &&
                    ManagersFactory.getInstance().getAccountManager().getUser().getTournamentID() != null &&
                    !ManagersFactory.getInstance().getAccountManager().getUser().getTournamentID().equalsIgnoreCase("")) {

                String nearbyQrPointID = mQrPointManager.getNearbyOfQrPlaced(latLng);
                mQrPointManager.checkNearbyQrPlaces(getApplicationContext(), nearbyQrPointID, latLng, null);
            }


        }

    }


}
