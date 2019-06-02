package findit.sedi.viktor.com.findit.common;

import android.util.Log;


import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.internal.operators.single.SingleJust;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;

public class AccountManager {

    private Single<User> mUser;


    public AccountManager() {
        // Вытаскиваем из БД

        init();
    }


    private void init() {
        mUser = Single.just(new User());
    }


    // Только когда пользователь удаляет аккаунт
    public void deleteUser() {
        mUser = null;
        // FirebaseUserStorage.getInstance().deleteUserInDatabase(mUser);
    }


    public Single<User> getUser() {
        return mUser ;
    }


    public void updateUserByEmail(String email) {
        ServerManager.getInstance().updateUser(email);
    }


    public void initUser(User user) {
        Log.d(LOG_TAG, "Init User " + " => " + user.getID());
        mUser = Single.just(user);
    }
}
