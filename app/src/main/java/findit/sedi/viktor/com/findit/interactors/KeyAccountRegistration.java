package findit.sedi.viktor.com.findit.interactors;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyAccountRegistration {

    @StringDef({KeysField.KEY_USER_NAME, KeysField.KEY_PHOTO_URL, KeysField.KEY_ACCOUNT_TYPE, KeysField.KEY_EMAIL})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_USER_NAME = "KEY_USER_NAME";
        String KEY_PHOTO_URL = "KEY_PHOTO_URL";
        String KEY_EMAIL = "KEY_EMAIL";
        String KEY_ACCOUNT_TYPE = "KEY_ACCOUNT_TYPE";

    }
}
