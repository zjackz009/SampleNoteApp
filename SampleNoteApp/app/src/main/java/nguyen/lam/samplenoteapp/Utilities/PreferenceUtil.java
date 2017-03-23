package nguyen.lam.samplenoteapp.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import nguyen.lam.samplenoteapp.R;

public class PreferenceUtil {

    private static final PreferenceUtil instance = new PreferenceUtil();
    private static SharedPreferences settingPreferences;

    public static PreferenceUtil getInstance() {
        return instance;
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = settingPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = settingPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = settingPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = settingPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = settingPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return settingPreferences.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return settingPreferences.getInt(key, defaultValue);
    }

    public  float getFloat(String key, float defaultValue) {
        return settingPreferences.getFloat(key, defaultValue);
    }

    @SuppressWarnings("SameParameterValue")
    public boolean getBoolean(String key, boolean defaultValue) {
        return settingPreferences.getBoolean(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return settingPreferences.getLong(key, defaultValue);
    }

    public void remove(String key) {
        settingPreferences.edit().remove(key).apply();
    }

    public void clearAll() {

        Set<String> keySet = settingPreferences.getAll().keySet();

        SharedPreferences.Editor editor = settingPreferences.edit();
        for (String deleteKey : keySet) {
            editor.remove(deleteKey).apply();
        }
    }

    public void init(Context context) {
        if (settingPreferences == null)
            settingPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }
}
