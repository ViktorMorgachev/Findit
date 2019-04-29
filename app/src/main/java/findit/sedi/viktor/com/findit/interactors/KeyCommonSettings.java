package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyCommonSettings {

    @StringDef({KeysField.USER_PASSWORD, KeysField.USER_EMAIL, KeysField.LOG_TAG})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String USER_PASSWORD = "USER_PASSWORD";
        String USER_EMAIL = "USER_EMAIL";
        String LOG_TAG = "FINDIT_LOG";
        String KEY_ACTYIVE_GAME = "GAME_IS_ACTIVE";

    }
}
