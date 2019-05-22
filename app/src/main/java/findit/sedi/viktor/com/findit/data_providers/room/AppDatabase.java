package findit.sedi.viktor.com.findit.data_providers.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import findit.sedi.viktor.com.findit.data_providers.data.User;

//@Database(entities = {User.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
