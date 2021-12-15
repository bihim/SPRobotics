package com.sproboticworks.util;

import com.sproboticworks.preferences.SessionManager;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new SessionManager(this);
    }
}
