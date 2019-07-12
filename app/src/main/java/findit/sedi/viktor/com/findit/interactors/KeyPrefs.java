package findit.sedi.viktor.com.findit.interactors;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyPrefs {

    @StringDef({KeysField.KEY_USER_NOTIFICATED_ABOUT_TOURNAMENT, KeysField.KEY_SOON_TOURNAMENT,
            KeysField.KEY_BEGUN_TOURNAMENT, KeysField.KEY_GET_BONUS_FROM_DISCOVERED_QRPOINT, KeysField.KEY_TOURNAMENT_WAS_ENDED})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_USER_NOTIFICATED_ABOUT_TOURNAMENT = "key_user_type_notificated";
        String KEY_SOON_TOURNAMENT = "key_soon_tournament";
        String KEY_BEGUN_TOURNAMENT = "key_begun_tournament";
        String KEY_TOURNAMENT_WAS_ENDED = "key_ended_tournament";
        String KEY_GET_BONUS_FROM_DISCOVERED_QRPOINT = "key_bonus_and_answered_to_question";
    }
}
