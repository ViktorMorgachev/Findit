package findit.sedi.viktor.com.findit.data_providers.data;

import com.google.firebase.firestore.GeoPoint;

import findit.sedi.viktor.com.findit.data_providers.Gender;

public class Player {

    private GeoPoint mGeoPoint;
    private double bonus;
    private String name;
    private Gender female;
    private String photoUrl;
    private String ID;

    public Player(GeoPoint geoPoint, double bonus, String name, Gender female, String photoUrl, String ID) {
        mGeoPoint = geoPoint;
        this.bonus = bonus;
        this.name = name;
        this.female = female;
        this.photoUrl = photoUrl;
        this.ID = ID;
    }

    public GeoPoint getGeoPoint() {
        return mGeoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        mGeoPoint = geoPoint;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFemale(Gender female) {
        this.female = female;
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

    public Gender getFemale() {
        return female;
    }
}
