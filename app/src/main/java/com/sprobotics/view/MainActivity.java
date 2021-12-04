package com.sprobotics.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.sprobotics.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        final BottomSheetDialog bottomSheetDialogForPhone = new BottomSheetDialog(this);
        bottomSheetDialogForPhone.setContentView(R.layout.layout_code_picker);
        final BottomSheetDialog bottomSheetDialogForOtp = new BottomSheetDialog(this);
        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
        final BottomSheetDialog bottomSheetDialogForEmail = new BottomSheetDialog(this);
        bottomSheetDialogForEmail.setContentView(R.layout.bottomsheet_email_picker);

        MaterialButton gotoOtp = bottomSheetDialogForPhone.findViewById(R.id.gotoOtp);
        MaterialButton gotoEmail = bottomSheetDialogForOtp.findViewById(R.id.gotoEmail);
        MaterialButton gotoPhone = bottomSheetDialogForEmail.findViewById(R.id.gotoPhone);

        if (gotoOtp != null)
            gotoOtp.setOnClickListener(v -> {
                bottomSheetDialogForOtp.show();
                bottomSheetDialogForPhone.dismiss();
            });

        if (gotoEmail != null)
            gotoEmail.setOnClickListener(v -> {
                bottomSheetDialogForOtp.dismiss();
                bottomSheetDialogForEmail.show();
            });

        if (gotoPhone != null)
            gotoPhone.setOnClickListener(v -> {
                bottomSheetDialogForEmail.dismiss();
                bottomSheetDialogForPhone.show();
            });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_1:
                    return true;
                case R.id.page_2:
                    return true;
                case R.id.page_3:
                    return true;
                case R.id.page_4:
                    bottomSheetDialogForPhone.show();
                    return true;
                default:
                    return false;
            }
        });
    }
}