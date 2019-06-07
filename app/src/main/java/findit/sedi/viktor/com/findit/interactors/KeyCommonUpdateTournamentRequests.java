package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyCommonUpdateTournamentRequests {

    @StringDef({KeysField.KEY_UPDATE_PLAYERS})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_UPDATE_PLAYERS = "players";

    }
}
