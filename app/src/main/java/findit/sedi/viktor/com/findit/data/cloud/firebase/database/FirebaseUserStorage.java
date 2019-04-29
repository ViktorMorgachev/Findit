package findit.sedi.viktor.com.findit.data.cloud.firebase.database;

import java.util.Map;

import findit.sedi.viktor.com.findit.data.Place;
import findit.sedi.viktor.com.findit.data.User;

// Работает с БД, обновляет, добавляет, хранит, удаляет....
public class FirebaseUserStorage {

    private static final FirebaseUserStorage ourInstance = new FirebaseUserStorage();

    public static FirebaseUserStorage getInstance() {
        return ourInstance;
    }


    private FirebaseUserStorage() {
        // Тут инициализация с бд позже
    }


    // при закрытии приложения сохранять в БД
    public void savеUserToDatabase(User user) {
        // сохраняем в БД
    }


    public void updateUserInDatabase(User user) {
        // Обновляем в бд информацию
    }

    public User getUserByUserID(long id) {
        new UnsupportedOperationException();
        return null;
    }


    public void deleteUserInDatabase(User user) {
        new UnsupportedOperationException();
    }
}
