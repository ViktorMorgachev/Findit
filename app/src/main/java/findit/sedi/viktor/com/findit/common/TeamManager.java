package findit.sedi.viktor.com.findit.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.database.FirebasePlayerStorage;
import findit.sedi.viktor.com.findit.data_providers.data.Player;
import findit.sedi.viktor.com.findit.data_providers.data.Team;

//
public class TeamManager {

    private ArrayList<Team> mTeams = new ArrayList<>();
    private FirebasePlayerStorage mFirebasePlayerStorage = FirebasePlayerStorage.getInstance();

    public TeamManager() {
        // Инициализируем с БД
        init();
    }

    private void init() {
    }


    public void addTeam(Team team) {

        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mTeams.size(); i++) {
            if (mTeams.get(i).getTeamID().equalsIgnoreCase(team.getTeamID()))
                return;
        }

        mTeams.add(team);
    }

    public void deleteTeam(Team team) {

        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mTeams.size(); i++) {
            if (mTeams.get(i).getTeamID().equalsIgnoreCase(team.getTeamID())) {
                mTeams.remove(team);
                return;
            }

        }
        //  mFirebasePlayerStorage.removePlayer(player);

    }

    public Team getTeam(String teamID) {
        // Проверяем содержит ли этот список элемент с таким же айдишником?
        for (int i = 0; i < mTeams.size(); i++) {
            if (mTeams.get(i).getTeamID().equalsIgnoreCase(teamID)) {
                return mTeams.get(i);
            }
        }

        return null;
    }



  /*  // Обновление пользователя по его ID
    public void updateOrTeam(Player player) {

        if (mPlayerMap.containsKey(player.getID())) {
            mPlayerMap.remove(player.getID());
            mPlayerMap.put(player.getID(), player);
            //  mFirebasePlayerStorage.updatePlayer(player);
        } else {
            //  mFirebasePlayerStorage.addPlayer(player);
            mPlayerMap.put(player.getID(), player);
        }

    }*/


}
