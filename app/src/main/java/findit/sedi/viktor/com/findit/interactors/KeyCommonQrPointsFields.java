package findit.sedi.viktor.com.findit.interactors;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */
// Названия полей
public interface KeyCommonQrPointsFields {

    @StringDef({KeysField.QRPOINT_BONUS, KeysField.QRPOINT_TYPE, KeysField.QRPOINT_MARK,
            KeysField.QRPOINT_QUEST_BONUS, KeysField.QRPOINT_QUESTS, KeysField.QRPOINT_TIP_FOR_NEXT,
            KeysField.QRPOINT_TIP_FOR_CURRENT, KeysField.QRPOINT_TOURNAMENT_ID, KeysField.QRPOINT_IS_MAIN,
            KeysField.QRPOINT_TIP_PHOTO, KeysField.QRPOINT_LOCATION, KeysField.QRPOINT_DIFFICULTY, KeysField.QRPOINT_DISTANCE})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String QRPOINT_BONUS = "Bonus";
        String QRPOINT_TYPE = "Type";
        String QRPOINT_MARK = "Mark";
        String QRPOINT_QUEST_BONUS = "QuestBonus";
        String QRPOINT_QUESTS = "Quests";
        String QRPOINT_TIP_FOR_NEXT = "TipForNext";
        String QRPOINT_TIP_FOR_CURRENT = "Tips";
        String QRPOINT_TOURNAMENT_ID = "TournamentID";
        String QRPOINT_IS_MAIN = "isMain";
        String QRPOINT_TIP_PHOTO = "TipPhoto";
        String QRPOINT_LOCATION = "Location";
        String QRPOINT_DIFFICULTY = "Difficulty";
        String QRPOINT_DISTANCE = "Distance";

    }

}
