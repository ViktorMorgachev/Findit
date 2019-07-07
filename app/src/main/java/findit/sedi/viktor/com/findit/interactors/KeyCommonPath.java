package findit.sedi.viktor.com.findit.interactors;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyCommonPath {

    @StringDef({KeysField.KEY_QRPOINTS_PATH, KeysField.KEY_TEAMS_PATH, KeysField.KEY_TOURNAMENTS_PATH, KeysField.KEY_USERS_PATH})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_QRPOINTS_PATH = "qrpoints";
        String KEY_USERS_PATH = "users";
        String KEY_TOURNAMENTS_PATH = "tournament";
        String KEY_TEAMS_PATH = "tournamentTeams";

    }
}
