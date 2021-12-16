package com.sproboticworks.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sproboticworks.R;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.view.fragment.HomeFragment;
import com.sproboticworks.view.fragment.ProfileFragment;

public class EnquiryActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String bottomTag = "page_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_3);
        setBottomNavigationView();
    }

    private void setBottomNavigationView() {

        bottomTag = getIntent().getStringExtra("bottomTag");

        switch (bottomTag) {
            case "page_1":
                bottomNavigationView.setSelectedItemId(R.id.page_1);
                break;
            case "page_2":
                bottomNavigationView.setSelectedItemId(R.id.page_2);
                break;
            case "page_4":
                bottomNavigationView.setSelectedItemId(R.id.page_4);
                break;
            default:
                bottomNavigationView.setSelectedItemId(R.id.page_3);
                break;
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_1:
                    bottomTag = "page_1";
                    this.startActivity(new Intent(this, MainActivity.class).putExtra("bottomTag", bottomTag));
                    this.overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.page_2:
                    bottomTag = "page_2";
                    this.startActivity(new Intent(this, MainActivity.class).putExtra("bottomTag", bottomTag));
                    this.overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.page_3:

                    return true;
                case R.id.page_4:
                    bottomTag = "page_4";
                    this.startActivity(new Intent(this, MainActivity.class).putExtra("bottomTag", bottomTag));
                    this.overridePendingTransition(0, 0);
                    finish();
                    return true;
                default:
                    return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.startActivity(new Intent(this, MainActivity.class).putExtra("bottomTag", bottomTag));
        this.overridePendingTransition(0, 0);
        finish();
        //super.onBackPressed();
    }
}