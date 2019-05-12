package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */
// Названия полей
public interface KeyCommonAccountFields {

    @StringDef({KeysField.USER_PASSWORD, KeysField.USER_EMAIL, KeysField.USER_BONUS, KeysField.USER_NAME, KeysField.USER_PHOTO, KeysField.USER_PHONE, KeysField.USER_GENDER, KeysField.USER_NET_STATUS})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String USER_PASSWORD = "Password";
        String USER_EMAIL = "Email";
        String USER_NAME = "Name";
        String USER_BONUS = "Bonus";
        String USER_PHOTO = "PhotoUrl";
        String USER_PHONE = "Phone";
        String USER_NET_STATUS = "isOnline";
        String USER_GENDER = "Gender";

    }

}
