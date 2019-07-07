package findit.sedi.viktor.com.findit.interactors;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyCommonUpdateUserRequests {

    @StringDef({KeysField.KEY_UPDATE_PROFILE, KeysField.KEY_UPDATE_LOCATION,
            KeysField.KEY_UPDATE_NET_STATUS, KeysField.KEY_UPDATE_FONDED_QR_POINTS,
            KeysField.KEY_UPDATE_BONUS, KeysField.KEY_UPDATE_DISCOVERED_QR_POINTS,
            KeysField.KEY_UPDATE_TOURNAMENT})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_UPDATE_PROFILE = "profile";
        String KEY_UPDATE_LOCATION = "location";
        String KEY_UPDATE_NET_STATUS = "net_status";
        String KEY_UPDATE_FONDED_QR_POINTS = "fonded_qrpoint";
        String KEY_UPDATE_DISCOVERED_QR_POINTS = "discovered_qrpoint";
        String KEY_UPDATE_BONUS = "bonus";
        String KEY_UPDATE_TOURNAMENT = "tournament_team";
    }
}
