package findit.sedi.viktor.com.findit.common;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.database.FirebasePlacesStorage;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;

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
     * и не зафиксированны у нас что мы открыли локацию тайника, (то есть каждому новому пользователю набревшему на знак вопроса или открывшего
     * локацию тайника) (И расстояние до тайников не более чем в них указано)
     */
    public String getValidIDsOfPlaced(LatLng latLng) {

        for (int i = 0; i < mQrPoints.size(); i++) {
            if (checkWithOurDiscoveredPoints(mQrPoints.get(i).getID()) &&
                    checkForTournamentID(mQrPoints.get(i).getTournamentID()) &&
                    (Util.getInstance().getDistance(latLng, mQrPoints.get(i).getLatLong())) <= mQrPoints.get(i).getDistance()) {
                // Показываем оповещение
            }

        }

        return "";


    }

    private boolean checkForTournamentID(String tournamentID) {
        return ManagersFactory.getInstance().getAccountManager().getUser().getTournamentsID().equalsIgnoreCase(tournamentID);
    }

    private boolean checkWithOurDiscoveredPoints(String id) {

        boolean isOpened = false;
        boolean isFonded = false;

        for (int i = 0; i < ManagersFactory.getInstance().getAccountManager()
                .getUser().getDiscoveredPointIDs().get("Discovered").size(); i++) {

            if (id.equalsIgnoreCase(ManagersFactory.getInstance().getAccountManager()
                    .getUser().getDiscoveredPointIDs().get("Discovered").get(i))) {
                isOpened = true;
                break;
            }
        }

        for (int i = 0; i < ManagersFactory.getInstance().getAccountManager()
                .getUser().getDiscoveredPointIDs().get("Fond").size(); i++) {

            if (id.equalsIgnoreCase(ManagersFactory.getInstance().getAccountManager()
                    .getUser().getDiscoveredPointIDs().get("Fond").get(i))) {
                isFonded = true;
                break;
            }
        }

        return isFonded || isOpened;

    }
}
