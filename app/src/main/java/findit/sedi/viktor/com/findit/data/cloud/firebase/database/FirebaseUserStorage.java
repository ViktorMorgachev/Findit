package findit.sedi.viktor.com.findit.data.cloud.firebase.database;

import java.util.Map;

import findit.sedi.viktor.com.findit.data.Place;

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
    public void savеUserToDatabase(Map<Long, Place> places) {
        // сохраняем в БД
    }


    private void updateUserInDatabase(int id, int mark) {
        // Обновляем в бд информацию
    }

}
