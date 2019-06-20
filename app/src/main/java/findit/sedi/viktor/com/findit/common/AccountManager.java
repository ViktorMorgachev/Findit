package findit.sedi.viktor.com.findit.common;

import android.util.Log;

import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.Tournament;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.interactors.KeyCommonUpdateUserRequests;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;


// Подпишем на измененения самого обьекта
public class AccountManager {

    private User mUser;
    private PublishSubject<User> mChangeObservable = PublishSubject.create();


    public AccountManager() {
        // Вытаскиваем из БД

        init();
        observe();
    }

    private void observe() {
        mUser.getChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        mChangeObservable.onNext(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "RegisterActivity onComplete  " + ManagersFactory.getInstance().getAccountManager().getUser());
                    }
                });
    }


    private void init() {
        mUser = new User();
    }


    // Только когда пользователь удаляет аккаунт
    public void deleteUser() {
        mUser = null;
        // FirebaseUserStorage.getInstance().deleteUserInDatabase(mUser);
    }

    public void clearUser() {
        mUser = null;
    }


    public User getUser() {
        return mUser;
    }


    public void updateUserByEmail(String email) {
        ServerManager.getInstance().updateUser(email);
    }


    public void initUser(User user) {
        Log.d(LOG_TAG, "Init User " + " => " + user.getID());
        mUser = user;
        mChangeObservable.onNext(mUser);
    }

    public Observable<User> getChanges() {
        return mChangeObservable;
    }

    public void joinToTournament(String tournamentID, String teamID) {

        if (teamID != null)
            mUser.setTeamID(teamID);


        mUser.setTournamentID(tournamentID);


        // Добавим  к турниру s нового пользователя
        Tournament tournament = ManagersFactory.getInstance().getTournamentManager().getTournament(tournamentID);
        tournament.addPlayer(ManagersFactory.getInstance().getAccountManager().getUser().getID());


        ServerManager.getInstance().updateUserOnServer(KeyCommonUpdateUserRequests.KeysField.KEY_UPDATE_TOURNAMENT);

    }
}
