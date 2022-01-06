package com.sproboticworks.view.activity.welcome;

import static com.sproboticworks.preferences.SessionManager.CHILD_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sproboticworks.R;
import com.sproboticworks.preferences.SessionManager;

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
                String str = editTextName.getText().toString();
                String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
                SessionManager.setValue(this, CHILD_NAME,cap);
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