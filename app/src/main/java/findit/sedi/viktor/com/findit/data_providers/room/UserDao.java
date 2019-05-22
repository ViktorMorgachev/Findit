package findit.sedi.viktor.com.findit.data_providers.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import findit.sedi.viktor.com.findit.data_providers.data.User;


public interface UserDao {



    void insert(User user);


    void delete(User user);


    List<User> getAllUser();

}
