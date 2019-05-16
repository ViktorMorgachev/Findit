package findit.sedi.viktor.com.findit.data_providers.cloud.myserver;

import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.firestore.CloudFirestoreManager;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;

public class ServerManager {
    private static final ServerManager ourInstance = new ServerManager();

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
    }


    public void sendCode(String id, String mark) {

        // Код по отправке qr кода на сервер
        //  CloudFirestoreManager.getInstance().updatePoint("fond", id);

        // У себя помечаем в БД что место либо найденно, либо обнаруженно,  по ID
        ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(id).setMark(mark);
        // Отправляем событие для получения Бонусов, которое нашёл пользлователь,  а точнее прибавляем его бонусы беря из БД меток
        // по ID которое он отправил и прибавляем к его бонусам и после этого удаляем это Place из БД
        User user = ManagersFactory.getInstance().getAccountManager().getUser();

        user.setBonus(ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(id).getBonus());

        // Нужно будет отправить на сервер информацию об обновлении бонусов у пользователя, если командная игра, то и добавить бонусы к команде
        // И после обновить пользователя


        // В зависимости от настроек турнира, можем удалить бонусы у QrPoint и обновить на сервере

    }

    public void updateUserOnServer(String tag) {

        CloudFirestoreManager.getInstance().updateUser(tag);

    }

    public void getQrPlaces() {


    }

    public void getTeams() {

        CloudFirestoreManager.getInstance().getTeams();

    }

    public void getTournaments() {

        CloudFirestoreManager.getInstance().getTournaments();
    }

    public void createNewUser(String name, String password, IAction IAction) {

        CloudFirestoreManager.getInstance().createUser(name, password, IAction);

    }

    public void initUser(String email, IAction IAction) {
        CloudFirestoreManager.getInstance().initUser(email, IAction);
    }

    public void changeUserNetStatus(boolean status) {

        CloudFirestoreManager.getInstance().changeUserNetStatus(status);

    }
}
