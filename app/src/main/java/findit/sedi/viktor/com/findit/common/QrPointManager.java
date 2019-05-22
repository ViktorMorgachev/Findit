package findit.sedi.viktor.com.findit.common;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.database.FirebasePlacesStorage;
import findit.sedi.viktor.com.findit.data_providers.data.Player;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.presenter.otto.FinditBus;
import findit.sedi.viktor.com.findit.presenter.otto.events.PlaceAboutEvent;

public class QrPointManager {

    private static final QrPointManager ourInstance = new QrPointManager();

    public static QrPointManager getInstance() {
        return ourInstance;
    }

    public QrPointManager() {
        init();
    }

    private List<QrPoint> mQrPoints = new ArrayList<>();


    private FirebasePlacesStorage mFirebasePlacesStorage = FirebasePlacesStorage.getInstance();

    public QrPointManager(FirebasePlacesStorage firebasePlacesStorage) {
        mFirebasePlacesStorage = firebasePlacesStorage;
    }

    public void saveQrPoints(ArrayList<QrPoint> points) {
        mFirebasePlacesStorage.savеPlacesToDatabase(points);
    }

    public void markQrPoint(String id, String mark) {

        for (int i = 0; i < mQrPoints.size(); i++) {
            if (mQrPoints.get(i).getID().equalsIgnoreCase(String.valueOf(id))) {
                mQrPoints.get(i).setMark(mark);
            }
        }

    }

    public List<QrPoint> getQrPlaces() {
        return mQrPoints;
    }

    private void init() {
        // Инициализация с БД на клиенте
    }


    public QrPoint getQrPlaceByID(String id) {

        for (int i = 0; i < mQrPoints.size(); i++) {
            if (mQrPoints.get(i).getID().equalsIgnoreCase(id))
                return mQrPoints.get(i);
        }
        return null;
    }

    // Обновляем информацию с сервера и сохраняем в БД
    public void updatePlacesFromServer() {
        // saveQrPoints();
    }


    /**
     * Перебираем все точки и показываем подсказки только тех тайников, которые принадлежат к нашему турниру
     * (И расстояние до тайников не более чем в них указано)
     */
    public String getNearbyOfQrPlaced(LatLng latLng) {

        // Вытаскивает только те точки, рядом с которым пользователь
        for (int i = 0; i < mQrPoints.size(); i++) {
            if (((Util.getInstance().getDistance(latLng, mQrPoints.get(i).getLatLong())) <= mQrPoints.get(i).getDistance())) {

                FinditBus.getInstance().post(new PlaceAboutEvent(mQrPoints.get(i).getID()));

            }

        }

        return "";

    }

    public void addQrPoint(QrPoint point) {

        // Проверяем есть ли в списке такач точка?\
        for (int i = 0; i < mQrPoints.size(); i++) {
            if (mQrPoints.get(i).getID().equalsIgnoreCase(point.getID())) {
                return;
            }
        }

        mQrPoints.add(point);


    }

    public void clearQrPoints() {
        mQrPoints.clear();
    }
}
