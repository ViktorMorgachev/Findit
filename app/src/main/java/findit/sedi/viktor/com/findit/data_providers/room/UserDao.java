package findit.sedi.viktor.com.findit.data_providers.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import findit.sedi.viktor.com.findit.data_providers.data.User;


public interface UserDao {



    void insert(User user);


    void delete(User user);


    List<User> getAllUser();

}
