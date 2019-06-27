package findit.sedi.viktor.com.findit.common.background_services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.firestore.CloudFirestoreManager;

// Он будет в фоне работать с TimerTaskами обновлять информацию с сервера в фоне постояянно, если это необходимо
public class MyWorker extends Worker {


    // Будем получать айдишники тех точек. которые нашли другие пользователи
    public static final String EXTRA_OUTPUT_DATA = "output_value";
    private static Data mData;
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
        if (true) {

            if (ManagersFactory.getInstance().getAccountManager().getUser().getTournamentID() != null &&
                    !ManagersFactory.getInstance().getAccountManager().getUser().getTournamentID().equalsIgnoreCase(""))
                CloudFirestoreManager.getInstance().getQrPlaces();
                CloudFirestoreManager.getInstance().updateTeams();
        }

        return Result.retry();
    }
}
