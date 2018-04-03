package com.yunshu.baseandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tyrese on 2017/12/19.
 */

public class SharePreferenceUtil {

    private static final String PREFERENCE_NAME = "preference_yunshuweilai";
    private SharedPreferences preferences;

    public SharePreferenceUtil(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
