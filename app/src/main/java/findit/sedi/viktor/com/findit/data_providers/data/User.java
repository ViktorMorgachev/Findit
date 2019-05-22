package findit.sedi.viktor.com.findit.data_providers.data;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;


public class User {
    private String phone;
    private String name;
    @PrimaryKey
    @NonNull
    private String ID;
    private String email;
    private long bonus;
    private String photoUrl;
    private String password;
    private long mGender;
    private String TournamentID;
    private String TeamID;
    private long totalBonus;
    private double Latitude;
    private double Longtude;
    private boolean netStatus;
    private ArrayList<String> mFondedQrPointsIDs;
    private ArrayList<String> mDiscoveredQrPointIDs;

    public User(String phone, String name, @NonNull String ID, String email, long bonus,
                String photoUrl, String password, long gender, String tournamentID, String teamID, long totalBonus,
                ArrayList<String> discoveredQrPointIDs, ArrayList<String> fondedQrPointsIDs) {
        this.phone = phone;
        this.name = name;
        this.ID = ID;
        this.email = email;
        this.bonus = bonus;
        this.photoUrl = photoUrl;
        this.password = password;
        mGender = gender;
        TournamentID = tournamentID;
        TeamID = teamID;
        this.totalBonus = totalBonus;
        mDiscoveredQrPointIDs = discoveredQrPointIDs;
        mFondedQrPointsIDs = fondedQrPointsIDs;
    }


    public ArrayList<String> getFondedQrPointsIDs() {
        return mFondedQrPointsIDs;
    }

    public void setFondedQrPointsIDs(ArrayList<String> fondedQrPointsIDs) {
        mFondedQrPointsIDs = fondedQrPointsIDs;
    }

    public ArrayList<String> getDiscoveredQrPointIDs() {
        return mDiscoveredQrPointIDs;
    }

    public void setDiscoveredQrPointIDs(ArrayList<String> discoveredQrPointIDs) {
        mDiscoveredQrPointIDs = discoveredQrPointIDs;
    }

    public User() {
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

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongtude() {
        return Longtude;
    }

    public void setLongtude(double longtude) {
        Longtude = longtude;
    }

    public GeoPoint getGeopoint() {
        return new GeoPoint(Latitude, Longtude);
    }

    public void setGeopoint(double Latitude, double Longtude) {
        this.Latitude = Latitude;
        this.Longtude = Longtude;
    }

    public void setGender(long gender) {
        mGender = gender;
    }

    public long getGender() {
        return mGender;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    private boolean isOffline = true;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getBonus() {
        return bonus;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    public String getID() {
        return ID;
    }

    public void setID(@NonNull String ID) {
        this.ID = ID;
    }

    public boolean isNetStatus() {
        return netStatus;
    }

    public void setNetStatus(boolean netStatus) {
        this.netStatus = netStatus;
    }

}
