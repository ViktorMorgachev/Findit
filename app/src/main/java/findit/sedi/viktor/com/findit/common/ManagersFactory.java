package findit.sedi.viktor.com.findit.common;

import android.content.Context;

public class ManagersFactory {

    private PlaceManager mPlaceManager;
    private AccountManager mAccountManager;
    private TeamManager mTeamManager;
    private TournamentManager mTournamentManager;
    private PlayersManager mPlayersManager;
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

    public AccountManager getAccountManager() {
        if (mContext != null) {
            if (mAccountManager != null)
                return mAccountManager;
            else {
                mAccountManager = new AccountManager();
                return mAccountManager;
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

    public PlayersManager getPlayersManager() {
        if (mContext != null) {
            if (mPlayersManager != null)
                return mPlayersManager;
            else {
                mPlayersManager = new PlayersManager();
                return mPlayersManager;
            }
        } else return null;
    }

    public TournamentManager getTournamentManager() {
        if (mContext != null) {
            if (mTournamentManager != null)
                return mTournamentManager;
            else {
                mTournamentManager = new TournamentManager();
                return mTournamentManager;
            }
        } else return null;
    }

    public TeamManager getTeamManager() {
        if (mContext != null) {
            if (mTeamManager != null)
                return mTeamManager;
            else {
                mTeamManager = new TeamManager();
                return mTeamManager;
            }
        } else return null;
    }


}
