package com.sprobotics.view.activity.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.sprobotics.R;
import com.sprobotics.view.activity.MainActivity;

public class WelcomeTwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_two);
        MaterialButton materialButton = findViewById(R.id.finish);
        materialButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}