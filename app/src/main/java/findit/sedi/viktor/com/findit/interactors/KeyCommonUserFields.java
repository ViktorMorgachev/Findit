package findit.sedi.viktor.com.findit.interactors;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */
// Названия полей
public interface KeyCommonUserFields {

    @StringDef({KeysField.USER_PASSWORD, KeysField.USER_EMAIL, KeysField.USER_BONUS,
            KeysField.USER_NAME, KeysField.USER_PHOTO, KeysField.USER_PHONE,
            KeysField.USER_GENDER, KeysField.USER_NET_STATUS, KeysField.USER_LOCATION,
            KeysField.USER_TOURNAMENT_ID, KeysField.USER_TEAM_ID, KeysField.USER_TOTAL_BONUS,
            KeysField.USER_ACCOUNT_TYPE, KeysField.USER_DISCOVERED_QR_POINTS, KeysField.USER_FONDED_QR_POINTS,
            KeysField.USER_SUM_OF_FONDED_POINTS, KeysField.USER_SUM_OF_DISCOVERED_POINTS})
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
        String USER_LOCATION = "Location";
        String USER_TOURNAMENT_ID = "TournamentID";
        String USER_TEAM_ID = "TeamID";
        String USER_TOTAL_BONUS = "TotalBonus";
        String USER_ACCOUNT_TYPE = "AccountType";
        String USER_DISCOVERED_QR_POINTS = "DiscoveredQrPointsIDs";
        String USER_FONDED_QR_POINTS = "FondedQrPointsIDs";
        String USER_SUM_OF_FONDED_POINTS = "SumOfFondedPoints";
        String USER_SUM_OF_DISCOVERED_POINTS = "SumOfDiscoveredPoints";

    }

}
