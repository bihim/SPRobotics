package com.sproboticworks.util;

import android.content.Intent;

import com.onesignal.OneSignal;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.view.activity.MainActivity;

public class Application extends android.app.Application {
    private static final String ONESIGNAL_APP_ID = "88e29035-6da0-4c5a-9cdf-c2f261658a99";
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.setInAppMessageClickHandler(result -> startActivity(new Intent(this, MainActivity.class)));
        new SessionManager(this);
    }
}
