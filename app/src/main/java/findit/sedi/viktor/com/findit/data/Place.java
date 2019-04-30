package findit.sedi.viktor.com.findit.data;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import findit.sedi.viktor.com.findit.exceptions.UncopatibleValue;


public class Place {

    @NonNull
    private long bonus; // Сколько бонусов у этой бутылочки
    @NonNull
    private String IDs; // Идентификатор бутылочки 98797089687
    @NonNull
    private LatLng mLatLng;
    @NonNull
    private String about;
    private long distance = 500; // Дистанция в метрах до точки
    private List<String> tips;  // Перед тем как пользователь нашёл на место
    private String imageUrl; // Думаю показывать позже когда пользователь найдёт и просканирует штрих код

    // Mark - три состояния, 0 - нету информации, 1 - набрели на место, но бутылочку не нашли, 2 - нашли бутылочку
    private long mark;

    public Place(LatLng latLng, String about, String imageUrl, String ID, int mark, int bonus, int distance, List<String> tips) {
        mLatLng = latLng;
        this.about = about;
        this.imageUrl = imageUrl;
        this.IDs = ID;
        this.mark = mark;
        this.bonus = bonus;
        this.distance = distance;
        this.tips = tips;
    }


    public void setMark(long mark) {
        if (mark >= 3 || mark < 0) {
            try {
                throw new UncopatibleValue();
            } catch (UncopatibleValue e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
        this.mark = mark;
    }

    @NonNull
    public long getBonus() {
        return bonus;
    }

    public long getMark() {
        return mark;
    }

    @NonNull
    public String getIDs() {
        return IDs;
    }

    @NonNull
    public LatLng getLatLng() {
        return mLatLng;
    }

    public long getDistance() {
        return distance;
    }
}
