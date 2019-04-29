package findit.sedi.viktor.com.findit.common.background_services;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import findit.sedi.viktor.com.findit.data.cloud.firebase.firestore.CloudFirestoreManager;
import findit.sedi.viktor.com.findit.ui.main.MainActivity;

public class MyWorker extends Worker {


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        CloudFirestoreManager.getInstance().getPoint();

        return Result.retry();
    }
}
