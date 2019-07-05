package findit.sedi.viktor.com.findit.data_providers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {
    private static final Prefs ourInstance = new Prefs();

    public static Prefs getInstance() {
        return ourInstance;
    }

    private SharedPreferences mSharedPreferences;
    private Context mContext;


    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    private Prefs() {
    }

    private void setDefaultSettings() {

        // По умолчанию игры нету
    }


    public <T> void savePrefs(String key, T value) {

        if (value instanceof String) {
            mSharedPreferences.edit().putString(key, (String) value).apply();

        } else if (value instanceof Integer) {
            mSharedPreferences.edit().putInt(key, (Integer) value).apply();
        }

    }

    public String getStringValue(String key) {

        if (mSharedPreferences.contains(key)) {
            if (!mSharedPreferences.getString(key, "").equals("")) {
                return mSharedPreferences.getString(key, "");
            }
        }

        return "";

    }

    public Integer getIntValue() {
        return null;
    }
}
