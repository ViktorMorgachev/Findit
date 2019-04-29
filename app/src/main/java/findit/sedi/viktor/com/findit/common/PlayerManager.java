package findit.sedi.viktor.com.findit.common;

import java.util.HashMap;
import java.util.Map;

import findit.sedi.viktor.com.findit.data.Player;
import findit.sedi.viktor.com.findit.data.cloud.firebase.database.FirebasePlayerStorage;

//
public class PlayerManager {

    private Map<Integer, Player> mPlayerMap = new HashMap<>();
    private FirebasePlayerStorage mFirebasePlayerStorage = FirebasePlayerStorage.getInstance();


    public void addPlayer(Player player) {
        mPlayerMap.put(player.getID(), player);
    }

    // При удалении пользователя из программы (Удаления аккаунта в будущем, удалить его из списка
    public void deletePlayer(Player player) {

        mPlayerMap.remove(player.getID());

        //  mFirebasePlayerStorage.removePlayer(player);

    }


    // Обновление пользователя по его ID
    public void updateOrAddUser(Player player) {

        if (mPlayerMap.containsKey(player.getID())) {
            mPlayerMap.remove(player.getID());
            mPlayerMap.put(player.getID(), player);
            //  mFirebasePlayerStorage.updatePlayer(player);
        } else {
            //  mFirebasePlayerStorage.addPlayer(player);
            mPlayerMap.put(player.getID(), player);
        }

    }


}
