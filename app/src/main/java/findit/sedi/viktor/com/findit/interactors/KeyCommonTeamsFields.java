package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyCommonTeamsFields {

    @StringDef({KeysField.KEY_TEAM_NAME, KeysField.KEY_TEAM_BONUS, KeysField.KEY_TEAM_PLAYER_IDS, KeysField.KEY_TEAM_TOURNAMENT_ID})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_TEAM_NAME = "Name";
        String KEY_TEAM_PLAYER_IDS = "PlayersIDs";
        String KEY_TEAM_TOURNAMENT_ID = "TournamentID";
        String KEY_TEAM_BONUS = "Bonus";

    }
}
