package findit.sedi.viktor.com.findit.data_providers.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import findit.sedi.viktor.com.findit.data_providers.Gender;

@Entity
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


    public User(String phone, String name, @NonNull String ID, String email, long bonus, String photoUrl, String password, boolean isOffline, long gender) {
        this.phone = phone;
        this.name = name;
        this.ID = ID;
        this.email = email;
        this.bonus = bonus;
        this.photoUrl = photoUrl;
        this.password = password;
        this.isOffline = isOffline;
        mGender = gender;
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
}
