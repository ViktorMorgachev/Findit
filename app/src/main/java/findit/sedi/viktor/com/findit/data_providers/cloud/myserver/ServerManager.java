package findit.sedi.viktor.com.findit.data_providers.cloud.myserver;

import findit.sedi.viktor.com.findit.App;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.cloud.firebase.firestore.CloudFirestoreManager;
import findit.sedi.viktor.com.findit.data_providers.data.Team;
import findit.sedi.viktor.com.findit.data_providers.data.Tournament;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;
import io.reactivex.observers.DisposableObserver;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_BONUS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_DISCOVERED_QR_POINTS;
import static findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_FONDED_QR_POINTS;

public class ServerManager {
    private static final ServerManager ourInstance = new ServerManager();


    // Logic

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
    }


    public void sendCode(String id, String mark) {


        // Отправляем событие для получения Бонусов, которое нашёл пользлователь,  а точнее прибавляем его бонусы беря из БД меток
        // по ID которое он отправил и прибавляем к его бонусам и после этого удаляем это Place из БД
        User user = App.instance.getAccountManager().getUser();


        //Меняем на сервере статус точки по её ID
        ServerManager.getInstance().updateQrPointByID(id, mark);

        // Если игра командная то команде добавляем бонусы
        if (App.instance.getTournamentManager().getTournament(user.getTournamentID()).getTournamentType() == Tournament.TournamentType.Teams) {
            Team team = App.instance.getTeamManager().getTeam(user.getTeamID());
        }


        // У себя помечаем в БД что место либо найденно, либо обнаруженно,  по ID, добавив его в список обнаруженных тайников или найденных
        // В зависимост и от тега
        if (mark.equalsIgnoreCase("discovered")) {
            user.getDiscoveredQrPointIDs().add(id);
            updateUserOnServer(KEY_UPDATE_DISCOVERED_QR_POINTS);
        } else if (mark.equalsIgnoreCase("fond")) {
            user.addBonus(ManagersFactory.getInstance().getAccountManager().getUser().getBonus() + ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(id).getBonus());
            updateUserOnServer(KEY_UPDATE_BONUS);
            // Добавляем в списко обнаруженных, удаляем из списка найденных (переносим его)
            user.getFondedQrPointsIDs().add(id);
            user.getDiscoveredQrPointIDs().remove(id);
            updateUserOnServer(KEY_UPDATE_FONDED_QR_POINTS);
            updateUserOnServer(KEY_UPDATE_DISCOVERED_QR_POINTS);
        }


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

    public void getBonusByCode(String code, IAction iAction) {

        CloudFirestoreManager.getInstance().getBonusByCode(code, iAction);

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

    public void createNewUser(String email, String password, String name, String photoUrl) {

        CloudFirestoreManager.getInstance().createUser(email, password, name, photoUrl);

    }

    public void updateUser(String email) {
        CloudFirestoreManager.getInstance().initUser(email);
    }

    public void changeUserNetStatus(boolean status) {

        CloudFirestoreManager.getInstance().changeUserNetStatus(status);

    }

    public void resetQrPlaceBonus(String code) {

        CloudFirestoreManager.getInstance().resetQrPlaceBonus(code);

    }

    public void getPlayers() {

        CloudFirestoreManager.getInstance().getPlayers();

    }

    public void updateTournament(String id, String tag) {

        CloudFirestoreManager.getInstance().updateTournament(id, tag);

    }

    public void checkProfile(String email, DisposableObserver<Boolean> booleanDisposableObserver) {

        CloudFirestoreManager.getInstance().checkProfile(email, booleanDisposableObserver);

    }

    public void updateTeams() {
        CloudFirestoreManager.getInstance().updateTeams();
    }
}
