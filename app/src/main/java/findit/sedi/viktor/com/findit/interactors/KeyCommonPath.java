package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import kotlin.reflect.KFunction;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyCommonPath {

    @StringDef({KeysField.KEY_PLAYER_PATH, KeysField.KEY_POINTS_PATH, KeysField.KEY_TEAMS_PATH, KeysField.KEY_TOURNAMENTS_PATH, KeysField.KEY_USERS_PATH})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_POINTS_PATH = "points";
        String KEY_USERS_PATH = "users";
        String KEY_PLAYER_PATH = "players";
        String KEY_TOURNAMENTS_PATH = "tournament";
        String KEY_TEAMS_PATH = "tournamentTeams";

    }
}
