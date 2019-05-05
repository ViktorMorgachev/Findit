package findit.sedi.viktor.com.findit.common;

import findit.sedi.viktor.com.findit.data_providers.data.User;

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

    public void updateUserByEmail() {

        // Запрос на сервер пойдёт и информация соответсвенно сохранится в нашем менеджере пользователя
        // И возможно эта информация сохранится в БД

    }
}
