package findit.sedi.viktor.com.findit.common.background_services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.presenter.NotificatorManager;
import findit.sedi.viktor.com.findit.ui.main.MainActivity;
import findit.sedi.viktor.com.findit.ui.tournament.TounamentActivity;

// Он будет в фоне работать с TimerTaskами обновлять информацию с сервера в фоне постояянно, если это необходимо
public class MyWorker extends Worker {


    // Будем получать айдишники тех точек. которые нашли другие пользователи
    public static final String EXTRA_OUTPUT_DATA = "output_value";
    private static Data mData;
    private NotificatorManager mNotificatorManager;
    private Context mContext;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {


        // Тут настроим на обновление через некторое время, он будет управлять потоками обновлями и устанавливать время
        // Так же всё это будет работать только при наличии интернета
        getDataFromServer();

        return Result.retry();
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

        final Calendar systemCalendar = Calendar.getInstance();
        Calendar tournamentCalendar = Calendar.getInstance();
        // Если пользователь в турнире, то показывать что турнир скоро начнётся
        // События: Турнир начался, турнир закончился, создан новый турнир
        // Если появился новый турнир, как-то отслеживать, то показывать оповещение, что появился новый турнир

        for (int i = 0; i < ManagersFactory.getInstance().getTournamentManager().getTournaments().size(); i++) {

            tournamentCalendar.setTimeInMillis(ManagersFactory.getInstance().getTournamentManager().getTournaments().get(i).getDateFrom().getSeconds() * 100);

            mNotificatorManager = new NotificatorManager();

            if (systemCalendar.before(tournamentCalendar))
                mNotificatorManager
                        .showCompatibilityNotification(mContext,
                                mContext.getResources().getString(R.string.soon_will_starting_tournament) + ManagersFactory.getInstance().getTournamentManager().getTournaments().get(i).getDescribe(),
                                R.drawable.ic_stars_24dp, "CHANNEL_ID",
                                null, mContext.getResources().getString(R.string.channel_name),
                                mContext.getResources().getString(R.string.channel_descrioption), new Intent(mContext, TounamentActivity.class));
            else if (systemCalendar.equals(tournamentCalendar)) {
                mNotificatorManager
                        .showCompatibilityNotification(mContext,
                                mContext.getResources().getString(R.string.now_started_tournament) + ManagersFactory.getInstance().getTournamentManager().getTournaments().get(i).getDescribe(),
                                R.drawable.ic_stars_24dp, "CHANNEL_ID",
                                null, mContext.getResources().getString(R.string.channel_name),
                                mContext.getResources().getString(R.string.channel_descrioption), new Intent(mContext, MainActivity.class));
            } else {
                mNotificatorManager
                        .showCompatibilityNotification(mContext,
                                "Турнир прошёл" + ManagersFactory.getInstance().getTournamentManager().getTournaments().get(i).getDescribe(),
                                R.drawable.ic_stars_24dp, "CHANNEL_ID",
                                null, mContext.getResources().getString(R.string.channel_name),
                                mContext.getResources().getString(R.string.channel_descrioption), new Intent(mContext, MainActivity.class));
            }
        }


    }
}
