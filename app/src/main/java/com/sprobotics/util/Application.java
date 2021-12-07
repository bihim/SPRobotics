package com.sprobotics.util;

import com.sprobotics.preferences.SessionManager;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new SessionManager(this);
    }
}
