package com.sproboticworks.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.onesignal.OSInAppMessageAction;
import com.onesignal.OneSignal;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.view.activity.welcome.WelcomeOneActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "88e29035-6da0-4c5a-9cdf-c2f261658a99";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Logger.addLogAdapter(new AndroidLogAdapter());
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.setInAppMessageClickHandler(result -> startActivity(new Intent(this, MainActivity.class)));


        if (!isLocationPermissionGranted()) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    10);

        }
        else if (isLocationPermissionGranted()) {
            /*if (session.isUserLoggedIn()) {
                Constant.CURRENT_DISTRICT = session.getData(Session.KEY_CITY);
                redirect(2000);
            } else if (!session.getData(Session.KEY_SAVED_DISTRICT).isEmpty()) {
                Constant.CURRENT_DISTRICT = session.getData(Session.KEY_SAVED_DISTRICT);
                redirect(2000);
            } else {

                if (ApiConfig.isGPSEnable(this)) {

                    redirect(2000);

                } else {
                    ApiConfig.displayLocationSettingsRequest(this);
                }

            }*/

            Logger.d("Granted");
        }

        new Handler().postDelayed(() -> {

            if (SessionManager.getValue(SessionManager.CHILD_NAME).isEmpty())
                startActivity(
                        new Intent(this, WelcomeOneActivity.class));
            else
                startActivity(
                        new Intent(this, MainActivity.class));

            finish();

        }, 2000);

    }

    public boolean isLocationPermissionGranted() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }



}