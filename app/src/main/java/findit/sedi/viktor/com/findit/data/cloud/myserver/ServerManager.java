package findit.sedi.viktor.com.findit.data.cloud.myserver;

import android.widget.Toast;

import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data.User;
import findit.sedi.viktor.com.findit.data.cloud.firebase.firestore.CloudFirestoreManager;
import findit.sedi.viktor.com.findit.ui.main.MainActivity;

public class ServerManager {
    private static final ServerManager ourInstance = new ServerManager();

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
    }


    public void sendCode(Long code) {

        // Код по отправке qr кода на сервер
        CloudFirestoreManager.getInstance().addPoint(MainActivity.sLatLng, "fond", code);

        // У себя помечаем в БД что место найденно по ID
        ManagersFactory.getInstance().getPlaceManager().getPlaceByID(code).setMark(2);
        // Отправляем событие для получения Бонусов, которое нашёл пользлователь,  а точнее прибавляем его бонусы беря из БД меток
        // по ID которое он отправил и прибавляем к его бонусам и после этого удаляем это Place из БД
        User user = ManagersFactory.getInstance().getUsersManager().getUser();
        user.setBonus(ManagersFactory.getInstance().getPlaceManager().getPlaceByID(code).getBonus());
        ManagersFactory.getInstance().getUsersManager().updateUser(user);

    }
}
