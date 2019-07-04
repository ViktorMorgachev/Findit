package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyPrefs {

    @StringDef({KeysField.KEY_USER_NOTIFICATED_ABOUT_TOURNAMENT, KeysField.KEY_SOON_TOURNAMENT, KeysField.KEY_BEGUN_TOURNAMENT})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_USER_NOTIFICATED_ABOUT_TOURNAMENT = "key_user_type_notificated";
        String KEY_SOON_TOURNAMENT = "key_soon_tournament";
        String KEY_BEGUN_TOURNAMENT = "key_begun_tournament";
    }
}
