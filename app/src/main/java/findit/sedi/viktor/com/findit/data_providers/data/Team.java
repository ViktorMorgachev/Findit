package findit.sedi.viktor.com.findit.data_providers.data;

public class Team {

    private String TournamentID;
    private long players;


    public Team(String tournamentID, long players, String name) {
        TournamentID = tournamentID;
        this.players = players;
        this.name = name;
    }

    private String name;

}
