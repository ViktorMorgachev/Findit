package findit.sedi.viktor.com.findit.interactors;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Developer on 14.04.2018.
 */

public interface KeyCommonBonusFields {

    @StringDef({KeysField.KEY_BONUS_CODE_GIFT_BONUS})
    @Retention(RetentionPolicy.SOURCE)
    @interface KeysField {
        String KEY_BONUS_CODE_GIFT_BONUS = "GiftBonus";
    }
}
