package findit.sedi.viktor.com.findit.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.database.FirebasePlayerStorage;
import findit.sedi.viktor.com.findit.data_providers.data.Player;
import findit.sedi.viktor.com.findit.data_providers.data.Tournament;

//
public class TournamentManager {

    private List<Tournament> mTournaments = new ArrayList<>();
    private FirebasePlayerStorage mFirebasePlayerStorage = FirebasePlayerStorage.getInstance();

    public TournamentManager() {
        // Инициализируем с БД
        init();
    }

    private void init() {
    }


    public void addTournament(Tournament tournament) {

        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mTournaments.size(); i++) {
            if (mTournaments.get(i).getID().equalsIgnoreCase(tournament.getID()))
                return;
        }
        mTournaments.add(tournament);
    }


    public void deleteTournament(Tournament tournament) {

        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mTournaments.size(); i++) {
            if (mTournaments.get(i).getID().equalsIgnoreCase(tournament.getID())) {
                mTournaments.remove(tournament);
                return;
            }
        }


        //  mFirebasePlayerStorage.removePlayer(player);

    }


   /* // Обновление пользователя по его ID
    public void updateOrAddUser(Tournament tournament) {

        if (mTournamentMap.containsKey(tournament.getID())) {
            mTournamentMap.remove(tournament.getID());
            mTournamentMap.put(tournament.getID(), tournament);
            //  mFirebasePlayerStorage.updatePlayer(player);
        } else {
            //  mFirebasePlayerStorage.addPlayer(player);
            mTournamentMap.put(tournament.getID(), tournament);
        }

    }*/


    public Tournament getTournament(String tournamentID) {

        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mTournaments.size(); i++) {
            if (mTournaments.get(i).getID().equalsIgnoreCase(tournamentID))
                return mTournaments.get(i);
        }

        return null;

    }
}
