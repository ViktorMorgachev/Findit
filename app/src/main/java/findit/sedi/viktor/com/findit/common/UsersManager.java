package findit.sedi.viktor.com.findit.common;

import findit.sedi.viktor.com.findit.data.User;
import findit.sedi.viktor.com.findit.data.cloud.firebase.database.FirebaseUserStorage;

public class UsersManager {

    private User mUser;

    public void saveUser(User user) {
        mUser = user;
        // FirebaseUserStorage.getInstance().savеUserToDatabase(user);

    }

    public UsersManager() {
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
}
