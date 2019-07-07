package findit.sedi.viktor.com.findit.data_providers.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//@Database(entities = {User.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
