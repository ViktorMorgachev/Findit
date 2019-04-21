package findit.sedi.viktor.com.findit.data;

import com.google.android.gms.maps.model.LatLng;


public class Place {
    private int bonus; // Сколько бонусов у этой бутылочки
    private long IDs; // Идентификатор бутылочки 98797089687
    private LatLng mLatLng;
    private String about;
    // Перед тем как пользователь нашёл на место
    private String tips;
    // Думаю показывать позже когда пользователь найдёт и просканирует штрих код
    private String imageUrl;


    // Mark - три состояния, 0 - нету информации, 1 - набрели на место, но бутылочку не нашли, 2 - нашли бутылочку
    private int mark;

    public Place(LatLng latLng, String about, String tips, String imageUrl, long ID, int mark, int bonus) {
        mLatLng = latLng;
        this.about = about;
        this.imageUrl = imageUrl;
        this.IDs = ID;
        this.mark = mark;
        this.tips = tips;
        this.bonus = bonus;
    }

    public Place() {
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getID() {
        return IDs;
    }

    public void setID(int ID) {
        this.IDs = ID;
    }


    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

}
