package findit.sedi.viktor.com.findit.data.cloud.firebase.database;

import java.util.Map;

import findit.sedi.viktor.com.findit.data.Place;
import findit.sedi.viktor.com.findit.data.Player;

// Работает с БД, обновляет, добавляет, хранит, удаляет....
public class FirebasePlayerStorage {

    private static final FirebasePlayerStorage ourInstance = new FirebasePlayerStorage();

    public static FirebasePlayerStorage getInstance() {
        return ourInstance;
    }


    private FirebasePlayerStorage() {
        // Тут инициализация с бд позже
    }



    public void updatePlayer(Player player) {
        new UnsupportedOperationException();
    }

    public void removePlayer(Player player) {
        new UnsupportedOperationException();
    }

    public void addPlayer(Player player) {
        new UnsupportedOperationException();
    }
}
