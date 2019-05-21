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


        // Отправляем событие для получения Бонусов, которое нашёл пользлователь,  а точнее прибавляем его бонусы беря из БД меток
        // по ID которое он отправил и прибавляем к его бонусам и после этого удаляем это Place из БД
        User user = ManagersFactory.getInstance().getAccountManager().getUser();

        user.setBonus(ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(id).getBonus());


        //Меняем на сервере статус точки по её ID
        ServerManager.getInstance().updateQrPointByID(id, mark);

        // У себя помечаем в БД что место либо найденно, либо обнаруженно,  по ID, добавив его в список обнаруженных тайников или найденных
        // В зависимост и от тега
        if (mark.equalsIgnoreCase("discovered")){
            ManagersFactory.getInstance().getAccountManager().getUser().getDiscoveredQrPointIDs().add(id);
        } else if(mark.equalsIgnoreCase("fond")) {
            // Добавляем в списко обнаруженных, удаляем из списка найденных (переносим его)
            ManagersFactory.getInstance().getAccountManager().getUser().getFondedQrPointsIDs().add(id);
            ManagersFactory.getInstance().getAccountManager().getUser().getDiscoveredQrPointIDs().remove(id);
        }


        updateUserOnServer("qrpoint");


        // Нужно будет отправить на сервер информацию об обновлении бонусов у пользователя, если командная игра, то и добавить бонусы к команде
        // И после обновить пользователя


        // В зависимости от настроек турнира, можем удалить бонусы у QrPoint и обновить на сервере

    }

    private void updateQrPointByID(String id, String mark) {

        CloudFirestoreManager.getInstance().updateQrPointByID(id, mark);

    }

    public void updateUserOnServer(String tag) {

        CloudFirestoreManager.getInstance().updateUser(tag);

    }

    public void getQrPlaces() {

        CloudFirestoreManager.getInstance().getQrPlaces();

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

    public void updateUser(String email, IAction IAction) {

        CloudFirestoreManager.getInstance().initUser(email, IAction);
    }

    public void changeUserNetStatus(boolean status) {

        CloudFirestoreManager.getInstance().changeUserNetStatus(status);

    }

    public void resetQrPlaceBonus(String code) {

        CloudFirestoreManager.getInstance().resetQrPlaceBonus(code);

    }
}
