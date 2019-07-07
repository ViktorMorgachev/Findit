package findit.sedi.viktor.com.findit.interactors;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */
// Названия полей
public interface KeyCommonTournamentsFields {

    @StringDef({KeysField.TOURNAMENTS_DATE_FROM, KeysField.TOURNAMENTS_DATE_TO, KeysField.TOURNAMENTS_DESCRIBE,
            KeysField.TOURNAMENTS_DIFFICULTY, KeysField.TOURNAMENTS_NAME, KeysField.TOURNAMENTS_PLAYERS_IDS,
            KeysField.TOURNAMENTS_TEAMS_IDS, KeysField.TOURNAMENTS_TIPS, KeysField.TOURNAMENTS_TOTAL_BONUSES, KeysField.TOURNAMENTS_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {

        String TOURNAMENTS_NAME = "";
        String TOURNAMENTS_DATE_FROM = "DateFrom";
        String TOURNAMENTS_DATE_TO = "DateTo";
        String TOURNAMENTS_DESCRIBE = "Describe";
        String TOURNAMENTS_TIPS = "Tips";
        String TOURNAMENTS_TOTAL_BONUSES = "TotalBonuses";
        String TOURNAMENTS_TYPE = "Type";
        String TOURNAMENTS_DIFFICULTY = "Difficulty";
        String TOURNAMENTS_PLAYERS_IDS = "PlayerIDs";
        String TOURNAMENTS_TEAMS_IDS = "TeamsIDs";

    }

}
