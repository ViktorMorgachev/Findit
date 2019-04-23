package findit.sedi.viktor.com.findit.data.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
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
