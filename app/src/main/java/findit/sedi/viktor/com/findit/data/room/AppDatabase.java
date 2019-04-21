package findit.sedi.viktor.com.findit.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import findit.sedi.viktor.com.findit.data.User;

@Database(entities = {User.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
