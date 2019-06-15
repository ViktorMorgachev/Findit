package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyAccountOfType {

    @StringDef({KeysField.KEY_FIREBASE_EMAIL_ACCOUNT, KeysField.KEY_GOOGLE_ACCOUNT})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_GOOGLE_ACCOUNT = "byGoogle";
        String KEY_FIREBASE_EMAIL_ACCOUNT = "firebase";

    }
}
