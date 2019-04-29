package findit.sedi.viktor.com.findit.common;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import findit.sedi.viktor.com.findit.data.Place;
import findit.sedi.viktor.com.findit.data.cloud.firebase.database.FirebasePlacesStorage;

public class PlaceManager {

    private static final PlaceManager ourInstance = new PlaceManager();

    public static PlaceManager getInstance() {
        return ourInstance;
    }

    public PlaceManager() {
        init();
    }

    private static Map<Long, Place> mPlaces = new HashMap<>();


    private FirebasePlacesStorage mFirebasePlacesStorage = FirebasePlacesStorage.getInstance();

    public PlaceManager(FirebasePlacesStorage firebasePlacesStorage) {
        mFirebasePlacesStorage = firebasePlacesStorage;
    }

    public void savePlaces(HashMap<Long, Place> places) {
        mFirebasePlacesStorage.savеPlacesToDatabase(places);
    }

    public void markPlace(long id, int mark) {
        if (mPlaces.get(id) != null)
            mPlaces.get(id).setMark(mark);
    }

    public Map<Long, Place> getPlaces() {
        return mPlaces;
    }

    private void init() {
        // Инициализация с БД на клиенте
    }


    // Получить id
    public Place getPlaceByID(long id) {
        return mPlaces.get(id);
    }

    // Обновляем информацию с сервера и сохраняем в БД
    public void updatePlacesFromServer() {
        // savePlaces();
    }


    public long getIDsPlaceIfValid(LatLng latLng) {

        // Получить список мест с Мапы

        // Print values
        ArrayList<Place> places = new ArrayList<>(mPlaces.values());

        for (int i = 0; i < places.size(); i++) {
            // Если тут не были и растоянеи до места не больше 300м
            if (places.get(i).getMark() == 0 && (Util.getInstance().getDistance(places.get(i).getLatLng(), latLng) <= 300)) {
                return mPlaces.get(places.get(i).getID()).getID();
            }
        }

        return 0;


    }
}
