package findit.sedi.viktor.com.findit.common;

import android.content.Context;

import findit.sedi.viktor.com.findit.data.PlaceManager;

public class ManagersFactory {

    private PlaceManager mPlaceManager;
    private UserManager mUserManager;
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

    public UserManager getUserManager() {
        if (mContext != null) {
            if (mUserManager != null)
                return mUserManager;
            else {
                mUserManager = new UserManager();
                return mUserManager;
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


}
