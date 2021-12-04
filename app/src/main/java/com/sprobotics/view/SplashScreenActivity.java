package com.sprobotics.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sprobotics.R;
import com.sprobotics.view.welcome.WelcomeOneActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> startActivity(new Intent(this, WelcomeOneActivity.class)), 2000);
    }
}