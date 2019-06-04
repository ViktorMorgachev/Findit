package findit.sedi.viktor.com.findit.data_providers.data;

import com.google.firebase.firestore.GeoPoint;

public class Player {

    private long bonus;
    private String name;
    private String photoUrl;
    private String ID;
    private boolean net_status;
    private String TournamentID;
    private String TeamID;
    private long totalBonus;
    private double Latitude;
    private long mGender;
    private long sumOfFondedFinds;
    private long sumOFDiscoveredFinds;
    private double Longtude;


    public Player(long bonus, String name, String photoUrl, String ID, boolean net_status,
                  String tournamentID, String teamID, long totalBonus, double latitude, double longtude, long gender, long sumOfFondedFinds, long sumOFDiscoveredFinds) {
        this.bonus = bonus;
        this.name = name;
        this.photoUrl = photoUrl;
        this.ID = ID;
        this.net_status = net_status;
        TournamentID = tournamentID;
        TeamID = teamID;
        this.totalBonus = totalBonus;
        Latitude = latitude;
        mGender = gender;
        Longtude = longtude;
        this.sumOfFondedFinds = sumOfFondedFinds;
        this.sumOFDiscoveredFinds = sumOFDiscoveredFinds;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }

    public boolean isNet_status() {
        return net_status;
    }

    public void setNet_status(boolean net_status) {
        this.net_status = net_status;
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

    public long getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(long totalBonus) {
        this.totalBonus = totalBonus;
    }

    public GeoPoint getGeopoint() {
        return new GeoPoint(Latitude, Longtude);
    }

    public void setGeopoint(double Latitude, double Longtude) {
        this.Latitude = Latitude;
        this.Longtude = Longtude;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setFemale(long gender) {
        this.mGender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public double getBonus() {
        return bonus;
    }

    public String getName() {
        return name;
    }

    public long getGender() {
        return mGender;
    }

    public long getSumOfFondedFinds() {
        return sumOfFondedFinds;
    }

    public long getSumOFDiscoveredFinds() {
        return sumOFDiscoveredFinds;
    }
}
