package findit.sedi.viktor.com.findit;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import findit.sedi.viktor.com.findit.common.LocationManager;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.Prefs;
import findit.sedi.viktor.com.findit.data_providers.room.AppDatabase;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

public class App extends Application {

    public AppDatabase getsUser_database() {
        return sUser_database;
    }

    private RefWatcher refWatcher;
    public static App instance;
    private Cicerone<Router> cicerone;


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
        cicerone = Cicerone.create();

        Prefs.getInstance().setContext(getApplicationContext());

        ManagersFactory.getInstance().setContext(this);
        LocationManager.getInstance().setContext(getApplicationContext());


        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        // LeakCanary.install(this);
        refWatcher = LeakCanary.install(this);

        ManagersFactory.getInstance().setContext(this);

    }

    public NavigatorHolder getNavigationHolder() {
        return cicerone.getNavigatorHolder();
    }


    public Router getRouter() {
        return cicerone.getRouter();
    }

    public void mustDie(Object object) {
        if (refWatcher != null) {
            refWatcher.watch(object);
        }
    }

    public boolean hasNet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
