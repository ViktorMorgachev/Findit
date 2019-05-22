package findit.sedi.viktor.com.findit.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findit.sedi.viktor.com.findit.data_providers.data.Player;
import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.database.FirebasePlayerStorage;
import findit.sedi.viktor.com.findit.data_providers.data.Tournament;

//
public class PlayersManager {

    private ArrayList<Player> mPlayers = new ArrayList<>();
    private FirebasePlayerStorage mFirebasePlayerStorage = FirebasePlayerStorage.getInstance();

    public PlayersManager() {
        // Инициализируем с БД
        init();
    }

    private void init() {
    }


    public void addPlayer(Player player) {

        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mPlayers.size(); i++) {
            if (mPlayers.get(i).getID().equalsIgnoreCase(player.getID()))
                return;
        }

        mPlayers.add(player);
    }

    // При удалении пользователя из программы (Удаления аккаунта в будущем, удалить его из списка
    public void deletePlayer(Player player) {

        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mPlayers.size(); i++) {
            if (mPlayers.get(i).getID().equalsIgnoreCase(player.getID())) {
                mPlayers.remove(player);
                return;
            }

        }
        //  mFirebasePlayerStorage.removePlayer(player);

    }

    public Player getPlayer(String playerID) {
        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mPlayers.size(); i++) {
            if (mPlayers.get(i).getID().equalsIgnoreCase(playerID)) {
                return mPlayers.get(i);
            }
        }

        return null;
    }


    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public void clearPlayers() {
        mPlayers.clear();
    }
}
