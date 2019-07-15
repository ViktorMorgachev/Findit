package findit.sedi.viktor.com.findit;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;

import findit.sedi.viktor.com.findit.common.AccountManager;
import findit.sedi.viktor.com.findit.common.GoogleAccountStore;
import findit.sedi.viktor.com.findit.common.LocationManager;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.common.PlayersManager;
import findit.sedi.viktor.com.findit.common.QrPointManager;
import findit.sedi.viktor.com.findit.common.TeamManager;
import findit.sedi.viktor.com.findit.common.TournamentManager;
import findit.sedi.viktor.com.findit.common.dialogs.DialogManager;
import findit.sedi.viktor.com.findit.data_providers.Prefs;
import findit.sedi.viktor.com.findit.data_providers.room.AppDatabase;
import io.fabric.sdk.android.Fabric;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

public class App extends Application {

    public AppDatabase getsUser_database() {
        return sUser_database;
    }

    public static App instance;
    private Cicerone<Router> cicerone;
    private AccountManager mAccountManager;
    private QrPointManager mQrPointManager;
    private LocationManager mLocationManager;
    private PlayersManager mPlayersManager;
    private TournamentManager mTournamentManager;
    private TeamManager mTeamManager;
    private DialogManager mDialogManager;

    private GoogleAccountStore mGoogleAccountStore;
    private ManagersFactory mManagersFactory;


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

    public void setGoogleAccountStore(GoogleAccountStore googleAccountStore) {
        mGoogleAccountStore = googleAccountStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        cicerone = Cicerone.create();

        Prefs.getInstance().setContext(getApplicationContext());

        mManagersFactory = ManagersFactory.getInstance();
        mManagersFactory.setContext(this);

        initManagers();


        instance = this;

        Fabric.with(this, new Crashlytics());


        // LeakCanary.install(this);
        // refWatcher = LeakCanary.install(this);

    }


    private void initManagers() {
        mLocationManager = LocationManager.getInstance();
        mLocationManager.setContext(this);

        mAccountManager = mManagersFactory.getAccountManager();
        mPlayersManager = mManagersFactory.getPlayersManager();
        mQrPointManager = mManagersFactory.getQrPointManager();
        mTeamManager = mManagersFactory.getTeamManager();
        mGoogleAccountStore = mManagersFactory.getGoogleStore();
        mTournamentManager = mManagersFactory.getTournamentManager();
        mDialogManager = DialogManager.getInstance();
        mDialogManager.setContext(getApplicationContext());
    }

    public NavigatorHolder getNavigationHolder() {
        return cicerone.getNavigatorHolder();
    }


    public Router getRouter() {
        return cicerone.getRouter();
    }


    public boolean hasNet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public AccountManager getAccountManager() {
        return mAccountManager;
    }

    public QrPointManager getQrPointManager() {
        return mQrPointManager;
    }

    public LocationManager getLocationManager() {
        return mLocationManager;
    }

    public PlayersManager getPlayersManager() {
        return mPlayersManager;
    }

    public TournamentManager getTournamentManager() {
        return mTournamentManager;
    }

    public TeamManager getTeamManager() {
        return mTeamManager;
    }


    public GoogleAccountStore getGoogleStore() {
        return mGoogleAccountStore;
    }

    public DialogManager getDialogManager() {
        return mDialogManager;
    }

}
