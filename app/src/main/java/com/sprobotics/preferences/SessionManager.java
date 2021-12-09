package com.sprobotics.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.sprobotics.R;
import com.sprobotics.model.loginresponse.LogInResponse;
import com.sprobotics.network.util.GsonUtil;

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


    public static void setLoggedIn(boolean is) {
        editor.putBoolean("isLoggedIn", is);
        editor.commit();
    }

    public static boolean isLoggedIn() {
        return preferences.getBoolean("isLoggedIn", false);
    }


    public static void setValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValue(String key) {
        return preferences.getString(key, "");
    }


    public static LogInResponse getLoginResponse() {
        LogInResponse response1 = (LogInResponse) GsonUtil.toObject(preferences.getString(LOGIN_RESPONSE, ""), LogInResponse.class);
        return response1;
    }


}
