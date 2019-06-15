package findit.sedi.viktor.com.findit.common;

import findit.sedi.viktor.com.findit.data_providers.data.User;


// Подпишем на измененения самого обьекта
public class GoogleAccountStore {

    private User mUser;


    public GoogleAccountStore() {
    }


    public User getUser() {
        return mUser;
    }

    public void initUser(User user) {
        mUser = user;
    }


}
