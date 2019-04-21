package findit.sedi.viktor.com.findit.data.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import findit.sedi.viktor.com.findit.data.User;

@Dao
public interface UserDao {


    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user")
    List<User> getAllUser();

}
