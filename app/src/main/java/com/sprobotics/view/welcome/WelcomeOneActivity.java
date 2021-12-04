package com.sprobotics.view.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.sprobotics.R;

public class WelcomeOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_one);
        MaterialButton materialButton = findViewById(R.id.next);
        materialButton.setOnClickListener(v->{
            startActivity(new Intent(this, WelcomeTwoActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}