package findit.sedi.viktor.com.findit.common.background_services;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.firestore.CloudFirestoreManager;

// Он будет в фоне работать с TimerTaskами обновлять информацию с сервера в фоне постояянно, если это необходимо
public class MyWorker extends Worker {


    // Будем получать айдишники тех точек. которые нашли другие пользователи
    public static final String EXTRA_OUTPUT_DATA = "output_value";
    private static Data mData;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Аналогично, но тут мы получаем информацию о других пользователях,
        // Кто в онлайн, офлайн, их местоположение и рейтинги, но это подгружаем при
        // Условии что игра имеет место
        if (true) {
          //  CloudFirestoreManager.getInstance().getPoint();
            CloudFirestoreManager.getInstance().getPlayers();
        }
        // Нужно будет обновлять с помощью Bus заставить обновится Активности RatingActivity

        return Result.retry();
    }
}
