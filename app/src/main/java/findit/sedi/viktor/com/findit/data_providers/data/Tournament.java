package findit.sedi.viktor.com.findit.data_providers.data;

import com.google.firebase.Timestamp;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Tournament {

    private Timestamp DateFrom;
    private Timestamp DateTo;
    private String Describe;
    private List<String> tips;
    private long totalBonuses;
    private TournamentType mTournamentType;
    private long difficulty;
    private List<String> playersIDs;
    private String ID;
    // Количество команд
    private List<String> mTeamsIDs;

    public Timestamp getDateFrom() {
        return DateFrom;
    }

    public void setDateFrom(Timestamp dateFrom) {
        DateFrom = dateFrom;
    }

    public Timestamp getDateTo() {
        return DateTo;
    }

    public void setDateTo(Timestamp dateTo) {
        DateTo = dateTo;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public List<String> getTips() {
        return tips;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }

    public long getTotalBonuses() {
        return totalBonuses;
    }

    public void setTotalBonuses(long totalBonuses) {
        this.totalBonuses = totalBonuses;
    }

    public TournamentType getTournamentType() {
        return mTournamentType;
    }

    public void setTournamentType(TournamentType tournamentType) {
        mTournamentType = tournamentType;
    }

    public long getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(long difficulty) {
        this.difficulty = difficulty;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }



    public Tournament(Timestamp dateFrom, Timestamp dateTo, String describe, List<String> tips, long totalBonuses,
                      TournamentType tournamentType, long difficulty, List<String> playersIDs, String ID, List<String> teamsIDs) {
        DateFrom = dateFrom;
        DateTo = dateTo;
        Describe = describe;
        this.tips = tips;
        this.totalBonuses = totalBonuses;
        mTournamentType = tournamentType;
        this.difficulty = difficulty;
        this.playersIDs = playersIDs;
        this.ID = ID;
        mTeamsIDs = teamsIDs;
    }


    public List<String> getPlayersIDs() {
        return playersIDs;
    }

    public void setPlayersIDs(List<String> playersIDs) {
        this.playersIDs = playersIDs;
    }

    public List<String> getTeamsIDs() {
        return mTeamsIDs;
    }

    public void setTeamsIDs(List<String> teamsIDs) {
        mTeamsIDs = teamsIDs;
    }

    public static TournamentType convertIntToTournamentType(long value) {

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


