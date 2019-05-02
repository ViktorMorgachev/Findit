package findit.sedi.viktor.com.findit.data_providers.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import findit.sedi.viktor.com.findit.data_providers.Gender;

public class Player {

    private LatLng mLatLng;
    private double bonus;
    private String name;
    private Gender female;
    private String photoUrl;
    private long ID;

    public long getID() {
        return ID;
    }


    public Player(GeoPoint latLng, double bonus, String name, Gender female, String photoUrl, long id) {
        mLatLng = new LatLng(latLng.getLatitude(), latLng.getLongitude());
        this.bonus = bonus;
        this.name = name;
        this.female = female;
        this.photoUrl = photoUrl;
        ID = id;
    }

    public LatLng getLatLng() {
        return mLatLng;
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
