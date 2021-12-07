package com.sprobotics.view.activity.welcome;

import static com.sprobotics.preferences.SessionManager.CHILD_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sprobotics.R;
import com.sprobotics.preferences.SessionManager;

public class WelcomeOneActivity extends AppCompatActivity {

    private TextInputEditText editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_one);
        MaterialButton materialButton = findViewById(R.id.next);

        editTextName = findViewById(R.id.editTextName);

        materialButton.setOnClickListener(v -> {

            if (validate()){
                SessionManager.setValue(CHILD_NAME,editTextName.getText().toString());
                startActivity(new Intent(this, WelcomeTwoActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }




        });
    }

    private boolean validate() {
        if (editTextName.getText().toString().isEmpty()) {
            editTextName.setError("Please enter your child name");
            return false;
        }
        return true;
    }

}