package findit.sedi.viktor.com.findit;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.leakcanary.LeakCanary;

import findit.sedi.viktor.com.findit.data.room.AppDatabase;

public class App extends Application {

    public AppDatabase getsUser_database() {
        return sUser_database;
    }


    private AppDatabase sUser_database;

    public App() {

//        sUser_database = Room.databaseBuilder(getApplicationContext(),
        //   AppDatabase.class, "user_database").build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

       // LeakCanary.install(this);
        MultiDex.install(this);
    }
}
