package findit.sedi.viktor.com.findit.data_providers.data;

import java.util.List;

public class Tournament {

    private String DateFrom;
    private String DateTo;
    private String Describe;
    private List<String> tips;
    private long totalBonuses;
    private TournamentType mTournamentType;
    private long difficulty;
    private long players;
    private String ID;
    // Количество команд
    private long teams;
    private List<Team> namesOfTeams;

    public Tournament(String dateFrom, String dateTo, String describe, List<String> tips, long totalBonuses,
                      long tournamentType, long difficulty, long players, String ID, long teams, List<Team> namesOfTeams) {
        DateFrom = dateFrom;
        DateTo = dateTo;
        Describe = describe;
        this.tips = tips;
        this.totalBonuses = totalBonuses;
        mTournamentType = convertIntToTournamentType(tournamentType);
        this.difficulty = difficulty;
        this.players = players;
        this.ID = ID;
        this.teams = teams;
        this.namesOfTeams = namesOfTeams;
    }

    public TournamentType convertIntToTournamentType(long value) {

        if (value == 0)
            return TournamentType.One_By_One;
        if (value == 1)
            return TournamentType.Teams;
        else
            throw new UnsupportedOperationException();

    }

    public enum TournamentType {

        One_By_One(0),
        Teams(1);

        long i;

        TournamentType(long i) {
            this.i = i;
        }
    }


}


