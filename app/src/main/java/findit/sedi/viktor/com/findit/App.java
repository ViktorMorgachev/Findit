package findit.sedi.viktor.com.findit;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.firebase.firestore.FirebaseFirestore;

import findit.sedi.viktor.com.findit.data.room.AppDatabase;

public class App extends Application {

    public AppDatabase getsUser_database() {
        return sUser_database;
    }


    private AppDatabase sUser_database;

    public App() {
        sUser_database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user_database").build();
    }
}
