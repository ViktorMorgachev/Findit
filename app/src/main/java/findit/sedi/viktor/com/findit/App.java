package findit.sedi.viktor.com.findit;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import androidx.work.Configuration;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.room.AppDatabase;

public class App extends Application {

    public AppDatabase getsUser_database() {
        return sUser_database;
    }

    private RefWatcher refWatcher;
    public static App instance;


    private AppDatabase sUser_database;

    public App() {

//        sUser_database = Room.databaseBuilder(getApplicationContext(),
        //   AppDatabase.class, "user_database").build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        // LeakCanary.install(this);
        refWatcher = LeakCanary.install(this);

        ManagersFactory.getInstance().setContext(this);

    }

    public void mustDie(Object object) {
        if (refWatcher != null) {
            refWatcher.watch(object);
        }
    }
}
