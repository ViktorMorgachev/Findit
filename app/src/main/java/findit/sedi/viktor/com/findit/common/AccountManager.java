package findit.sedi.viktor.com.findit.common;

import android.util.Log;

import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.User;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;

public class AccountManager {

    private User mUser;

    public void saveUser(User user) {
        mUser = user;
        // FirebaseUserStorage.getInstance().savеUserToDatabase(user);

    }

    public AccountManager() {
        // Вытаскиваем из БД
        init();
    }

    private void init() {
    }


    public void updateUser(User user) {
        mUser = user;
        //FirebaseUserStorage.getInstance().updateUserInDatabase(mUser);
    }

    // Только когда пользователь удаляет аккаунт
    public void deleteUser() {
        mUser = null;
        // FirebaseUserStorage.getInstance().deleteUserInDatabase(mUser);
    }


    public User getUser() {
        return mUser;
    }

    public void updateUserByEmail(String email, IAction IAction) {

        ServerManager.getInstance().updateUser(email, IAction);

    }

    public void createUser(User user) {

        this.mUser = user;

    }

    public void initUser(User user) {
        Log.d(LOG_TAG, "Init User "  + " => " + user.getID());
        this.mUser = user;
    }
}
