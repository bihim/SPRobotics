package com.sprobotics.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.sprobotics.R;

public class SessionManager {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;


    public static final String CHILD_NAME = "CHILD_NAME";
    public static final String CHILD_AGE = "CHILD_AGE";
    public static final String LOGIN_RESPONSE = "LOGIN_RESPONSE";


    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        editor = preferences.edit();
    }

    public static void clearSessionManager() {
        editor.clear().apply();
    }


    public static void isLoggedIn(boolean is) {
        editor.putBoolean("isLoggedIn", is);
        editor.commit();
    }

    public static boolean getToken() {
        return preferences.getBoolean("isLoggedIn", false);
    }

    public static void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValue(String key) {
        return preferences.getString(key, "");
    }


}
