package com.sproboticworks.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.sproboticworks.R;
import com.sproboticworks.model.loginresponse.LogInResponse;
import com.sproboticworks.network.util.GsonUtil;

public class SessionManager {

    public static final String CHILD_NAME = "CHILD_NAME";
    public static final String CHILD_AGE = "CHILD_AGE";
    public static final String LOGIN_RESPONSE = "LOGIN_RESPONSE";
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;


    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        editor = preferences.edit();
    }

    public static void clearSessionManager() {
        editor.clear().apply();
    }

    public static boolean isLoggedIn() {
        return preferences.getBoolean("isLoggedIn", false);
    }

    public static void setLoggedIn(boolean is) {
        editor = preferences.edit();
        editor.putBoolean("isLoggedIn", is);
        editor.commit();
    }

    public static void setValue(Context context, String key, String value) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValue(Context context, String key) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        return preferences.getString(key, "");
    }

    public static LogInResponse getLoginResponse() {
        LogInResponse response1 = (LogInResponse) GsonUtil.toObject(preferences.getString(LOGIN_RESPONSE, ""), LogInResponse.class);
        return response1;
    }


}
