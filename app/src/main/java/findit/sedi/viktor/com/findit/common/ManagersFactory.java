package findit.sedi.viktor.com.findit.common;

import android.content.Context;

import findit.sedi.viktor.com.findit.data.PlaceManager;

public class ManagersFactory {

    private PlaceManager mPlaceManager;
    private UsersManager mUsersManager;
    private PlayerManager mPlayerManager;
    private Context mContext;


    private static final ManagersFactory ourInstance = new ManagersFactory();

    public static ManagersFactory getInstance() {
        return ourInstance;
    }

    private ManagersFactory() {
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public UsersManager getUsersManager() {
        if (mContext != null) {
            if (mUsersManager != null)
                return mUsersManager;
            else {
                mUsersManager = new UsersManager();
                return mUsersManager;
            }
        } else return null;


    }


    public PlaceManager getPlaceManager() {
        if (mContext != null) {
            if (mPlaceManager != null)
                return mPlaceManager;
            else {
                mPlaceManager = new PlaceManager();
                return mPlaceManager;
            }
        } else return null;
    }

    public PlayerManager getPlayerManager() {
        if (mContext != null) {
            if (mPlayerManager != null)
                return mPlayerManager;
            else {
                mPlayerManager = new PlayerManager();
                return mPlayerManager;
            }
        } else return null;
    }


}
