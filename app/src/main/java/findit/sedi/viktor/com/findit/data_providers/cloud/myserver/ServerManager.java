package findit.sedi.viktor.com.findit.data_providers.cloud.myserver;

import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.firestore.CloudFirestoreManager;

public class ServerManager {
    private static final ServerManager ourInstance = new ServerManager();

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
    }


    public void sendCode(String id, String mark) {

        // Код по отправке qr кода на сервер
        CloudFirestoreManager.getInstance().updatePoint("fond", id);

        // У себя помечаем в БД что место найденно по ID
        ManagersFactory.getInstance().getPlaceManager().getPlaceByID(id).setMark(2);
        // Отправляем событие для получения Бонусов, которое нашёл пользлователь,  а точнее прибавляем его бонусы беря из БД меток
        // по ID которое он отправил и прибавляем к его бонусам и после этого удаляем это Place из БД
        User user = ManagersFactory.getInstance().getUsersManager().getUser();

        user.setBonus(ManagersFactory.getInstance().getPlaceManager().getPlaceByID(id).getBonus());


        ManagersFactory.getInstance().getUsersManager().updateUser(user);

    }

    public void updateUserOnServer(User user) {

        CloudFirestoreManager.getInstance().updateUser(user);

    }

    public void getPlaces() {


    }

    public void getTeams() {

        CloudFirestoreManager.getInstance().getTeams();

    }

    public void getTournaments() {

        CloudFirestoreManager.getInstance().getTournaments();
    }
}
