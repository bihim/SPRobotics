package com.sprobotics.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.sprobotics.R;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.view.activity.welcome.WelcomeOneActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Logger.addLogAdapter(new AndroidLogAdapter());



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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            return false;

        }
        return true;
    }



}