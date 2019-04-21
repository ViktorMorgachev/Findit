package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyCommonProfile {

    @StringDef({KeysField.USER_PASSWORD, KeysField.USER_EMAIL, KeysField.USER_BONUS, KeysField.USER_NAME, KeysField.USER_PHOTO})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String USER_PASSWORD = "USER_PASSWORD";
        String USER_EMAIL = "USER_EMAIL";
        String USER_NAME = "USER_NAME";
        String USER_BONUS = "USER_BONUS";
        String USER_PHOTO = "USER_PHOTO";
    }
}
