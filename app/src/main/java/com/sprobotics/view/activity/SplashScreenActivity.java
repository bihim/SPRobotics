package com.sprobotics.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sprobotics.R;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.view.activity.welcome.WelcomeOneActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


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
}