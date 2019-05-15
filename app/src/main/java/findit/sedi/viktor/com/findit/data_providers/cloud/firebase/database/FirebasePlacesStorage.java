package findit.sedi.viktor.com.findit.data_providers.cloud.firebase.database;

import java.util.ArrayList;
import java.util.Map;

import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;

// Работает с БД, обновляет, добавляет, хранит, удаляет....
public class FirebasePlacesStorage {

    private static final FirebasePlacesStorage ourInstance = new FirebasePlacesStorage();

    public static FirebasePlacesStorage getInstance() {
        return ourInstance;
    }


    private FirebasePlacesStorage() {
        // Тут инициализация с бд позже
    }


    // при закрытии приложения сохранять в БД
    public void savеPlacesToDatabase(ArrayList<QrPoint> points) {
        // сохраняем в БД
    }

    private void markPlace(int id, int mark) {
        //  mPlaces.get(id).setMark(mark);
        if (mark == 2) {
            // Отправляем на сервер что одну бутылочку нашли
        }
        updatePlaceInDatabase(id, mark);
    }

    private void updatePlaceInDatabase(int id, int mark) {
        // Обновляем в бд информацию
    }

}
