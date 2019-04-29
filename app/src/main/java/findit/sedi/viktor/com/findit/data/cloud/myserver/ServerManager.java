package findit.sedi.viktor.com.findit.data.cloud.myserver;

import android.widget.Toast;

import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data.cloud.firebase.firestore.CloudFirestoreManager;
import findit.sedi.viktor.com.findit.ui.main.MainActivity;

public class ServerManager {
    private static final ServerManager ourInstance = new ServerManager();

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
    }


    public void sendCode(String code) {
        // Код по отправке qr кода на сервер
        //........
        CloudFirestoreManager.getInstance().addPoint(MainActivity.sLatLng, "fond", 267354);
        // Отправляем событие для получения Бонусов, которое нашёл пользлователь,  а точнее прибавляем его бонусы беря из БД меток
        // по ID которое он отправил и прибавляем к его бонусам и после этого удаляем это Place из БД


    }
}
