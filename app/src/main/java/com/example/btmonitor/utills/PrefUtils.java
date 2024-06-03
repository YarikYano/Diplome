package com.example.btmonitor.utills;


import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.btmonitor.App;

public class PrefUtils {
    private static final String PREF_KEY_PASSWORD = "pref_password";
    public static final String PREF_MAC = "pref_mac";

    private static SharedPreferences getPrefers() {
        return PreferenceManager.getDefaultSharedPreferences(App.getAppInstance());
    }

    public static String getPassword(){
        return getString(PREF_KEY_PASSWORD, "1234567890");
    }

    public static void setPassword(String password){
         put(PREF_KEY_PASSWORD, password);
    }
    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        try {
            return getPrefers().getString(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return getPrefers().getInt(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public static float getFloat(String key, float defaultValue) {
        try {
            return getPrefers().getFloat(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        try {
            return getPrefers().getBoolean(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static void put(String key, Object obj) {
        if (obj == null) return;
        if (obj instanceof String) {
            getPrefers().edit().putString(key, (String) obj).apply();
        } else if (obj instanceof Boolean) {
            getPrefers().edit().putBoolean(key, (Boolean) obj).apply();
        } else if (obj instanceof Float) {
            getPrefers().edit().putFloat(key, (Float) obj).apply();
        } else if (obj instanceof Integer) {
            getPrefers().edit().putInt(key, (Integer) obj).apply();
        } else if (obj instanceof Long) {
            getPrefers().edit().putLong(key, (Long) obj).apply();
        }
    }

    public static void setMac(String address) {
        put(PREF_MAC, address);
    }

    public static String getMac() {
        return getString(PREF_MAC);
    }
}
