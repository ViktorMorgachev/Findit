package findit.sedi.viktor.com.findit.common;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.dialogs.DialogManager;
import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.database.FirebasePlacesStorage;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.NotificatorManager;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;
import findit.sedi.viktor.com.findit.ui.find_tainik.NearbyTainikActivity;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_DISCOVERED_QR_POINTS;
import static findit.sedi.viktor.com.findit.ui.find_tainik.NearbyTainikActivity.POINT_ID;

public class QrPointManager {

    private static final QrPointManager ourInstance = new QrPointManager();

    public static QrPointManager getInstance() {
        return ourInstance;
    }

    public QrPointManager() {
        init();
    }

    private List<QrPoint> mQrPoints = new ArrayList<>();

    private HashSet<String> mDiscoveredPointID = new HashSet<>();
    private FirebasePlacesStorage mFirebasePlacesStorage = FirebasePlacesStorage.getInstance();
    private NotificatorManager mNotificatorManager;

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
    //  TODO Иногда выкидывает ""
    public String getNearbyOfQrPlaced(LatLng latLng) {

        // Вытаскивает только те точки, рядом с которым пользователь
        for (int i = 0; i < mQrPoints.size(); i++) {
            if (((Util.getInstance().getDistance(latLng, mQrPoints.get(i).getLatLong())) <= mQrPoints.get(i).getDistance())) {
                return mQrPoints.get(i).getID();
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

    public String getQrPlaceIDByLatLong(LatLng position) {

        for (int i = 0; i < mQrPoints.size(); i++) {
            if (mQrPoints.get(i).getLatLong().equals(position))
                return mQrPoints.get(i).getID();
        }
        return null;

    }

    // Сюда попадают тольеко те указатели, которые относятся к нашему турниру
    public void checkNearbyQrPlaces(Context context, String ID, LatLng userLocation, IAction action) {

        if (ID.equalsIgnoreCase(""))
            return;


        QrPoint qrPoint = ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(ID);
        User user = ManagersFactory.getInstance().getAccountManager().getUser();


        for (int i = 0; i < user.getFondedQrPointsIDs().size(); i++) {
            if (user.getFondedQrPointsIDs().get(i).equalsIgnoreCase(qrPoint.getID())) {

                Toast.makeText(context, "Вы его находили ранее", Toast.LENGTH_LONG).show();
                return;
            }
        }

        for (int i = 0; i < user.getDiscoveredQrPointIDs().size(); i++) {
            if (user.getDiscoveredQrPointIDs().get(i).equalsIgnoreCase(qrPoint.getID())) {
                if (!mDiscoveredPointID.contains(qrPoint.getID())) {
                    DialogManager.getInstance().showDialog("Вы его обнануживали ранее, можете нажать на вопрос для подсказки", null, null, null, null, null, true, false);
                    // Добавляем в список найденных тайников тут в активности чтобы это сообщение больше не показывать
                    mDiscoveredPointID.add(qrPoint.getID());
                }
                return;
            }
        }


        Toast.makeText(context, "Расстояние до тайника: " + Math.round(Util.getInstance().getDistance(userLocation, qrPoint.getLatLong())) + " м", Toast.LENGTH_LONG).show();

        // Если тайник новый и мы его не обнаруживали и не находили,  то показывает диалоговое окно

        if (qrPoint.getMark().equalsIgnoreCase("none")) {


            if (mNotificatorManager == null) {
                mNotificatorManager = new NotificatorManager();
            }

            // Запускаем активность и отправляем ID в неё
            Intent intent = new Intent(context, NearbyTainikActivity.class);
            intent.putExtra(POINT_ID, qrPoint.getID());
            context.startActivity(intent);

            if (mNotificatorManager != null)
                mNotificatorManager.showCompatibilityNotification(context,
                        "Вы набрели на место где спрятан тайник", R.drawable.ic_explore_24dp, "CHANNEL_ID",
                        null, context.getResources().getString(R.string.channel_name), context.getResources().getString(R.string.channel_descrioption), intent);


            // Помечаем у себя в списке QrPoints что она detecteds
            ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(ID).setMark("discovered");

            // Одновременно меняем статус на сервере что на это место набрели
            ServerManager.getInstance().sendCode(qrPoint.getID(), "discovered");

            // Синхронизация с сервером
            ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_DISCOVERED_QR_POINTS);


            // Обновление информации на карте, просто передадим action, если с сервиса будет вызываться, Action будет null
            if (action != null)
                action.action();


            return;


        } else { // Иначе его обнаруживал кто-то другой
            // Проверям обнаруживали ли мы ранее это точку?

            if (qrPoint.getMark().equalsIgnoreCase("fond")) {
                Toast.makeText(context, "Кто-то уже нашёл данный тайник", Toast.LENGTH_LONG).show();
                return;
            } else {
                // Одновременно меняем статус что мы тут побывали
                ServerManager.getInstance().sendCode(qrPoint.getID(), "discovered");
                ServerManager.getInstance().updateUserOnServer(KEY_UPDATE_DISCOVERED_QR_POINTS);


                if (mNotificatorManager == null) {
                    mNotificatorManager = new NotificatorManager();
                }

                mNotificatorManager.showCompatibilityNotification(context,
                        "Вы набрели на место где спрятан тайник", R.drawable.ic_explore_24dp, "CHANNEL_ID",
                        null, context.getResources().getString(R.string.channel_name), context.getResources().getString(R.string.channel_descrioption), null);

                // Значит набрели на знак вопроса, который кто-то уже его обнаруживал
                // Запускаем активность и отправляем ID в неё
                Intent intent = new Intent(context, NearbyTainikActivity.class);
                intent.putExtra(POINT_ID, qrPoint.getID());
                context.startActivity(intent);


            }


        }


    }


}
