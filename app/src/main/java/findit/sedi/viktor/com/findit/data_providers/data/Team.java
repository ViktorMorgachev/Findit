package findit.sedi.viktor.com.findit.data_providers.data;

import java.util.List;

public class Team {

    private String TournamentID;
    private List<String> playersIds;
    private String TeamID;
    private String name;
    private long bonus;


    public Team(String tournamentID, List<String> playersIds, String teamID, String name, long bonus) {
        TournamentID = tournamentID.trim();
        this.playersIds = playersIds;
        TeamID = teamID.trim();
        this.name = name.trim();
        this.bonus = bonus;
    }


    public List<String> getPlayersIds() {
        return playersIds;
    }

    public void setPlayersIds(List<String> playersIds) {
        this.playersIds = playersIds;
    }

    public String getTournamentID() {
        return TournamentID;
    }

    public void setTournamentID(String tournamentID) {
        TournamentID = tournamentID;
    }


    public String getTeamID() {
        return TeamID;
    }

    public void setTeamID(String teamID) {
        TeamID = teamID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBonus() {
        return bonus;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }
}
