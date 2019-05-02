package findit.sedi.viktor.com.findit.common;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import findit.sedi.viktor.com.findit.data_providers.data.Place;
import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.database.FirebasePlacesStorage;

public class PlaceManager {

    private static final PlaceManager ourInstance = new PlaceManager();

    public static PlaceManager getInstance() {
        return ourInstance;
    }

    public PlaceManager() {
        init();
    }

    private static List<Place> mPlaces = new ArrayList<>();


    private FirebasePlacesStorage mFirebasePlacesStorage = FirebasePlacesStorage.getInstance();

    public PlaceManager(FirebasePlacesStorage firebasePlacesStorage) {
        mFirebasePlacesStorage = firebasePlacesStorage;
    }

    public void savePlaces(HashMap<Long, Place> places) {
        mFirebasePlacesStorage.savеPlacesToDatabase(places);
    }

    public void markPlace(long id, long mark) {

        for (int i = 0; i < mPlaces.size(); i++) {
            if (mPlaces.get(i).getIDs().equalsIgnoreCase(String.valueOf(id))) {
                mPlaces.get(i).setMark(mark);
            }
        }

    }

    public List<Place> getPlaces() {
        return mPlaces;
    }

    private void init() {
        // Инициализация с БД на клиенте
    }


    public Place getPlaceByID(String id) {

        for (int i = 0; i < mPlaces.size(); i++) {
            if (mPlaces.get(i).getIDs().equalsIgnoreCase(id))
                return mPlaces.get(i);
        }
        return null;
    }

    // Обновляем информацию с сервера и сохраняем в БД
    public void updatePlacesFromServer() {
        // savePlaces();
    }


    /**
     * Бегаем по всей точкам и соотвественно получаем все идентификаторы точек, рядом с которым пользователь, точек которые ещё не нашли
     */
    public String getValidIDsOfPlaced(LatLng latLng) {

        for (int i = 0; i < mPlaces.size(); i++) {
            // Если тут не были и растоянеи до места не больше от настроек
            if (mPlaces.get(i).getMark() == 0 && (Util.getInstance().getDistance(mPlaces.get(i).getLatLng(), latLng) <= mPlaces.get(i).getDistance())) {
                return mPlaces.get(i).getIDs();
            }
        }

        return "";


    }
}
