package com.sprobotics.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.logger.Logger;
import com.sprobotics.R;
import com.sprobotics.view.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private BottomSheetDialog bottomSheetDialogForPhone, bottomSheetDialogForOtp, bottomSheetDialogForEmail;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        setButtonCallBacks();
        setBottomSheet();
        setBottomNavigationView();
        setFragment(savedInstanceState);
    }

    private void findViewById() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
    }

    private void setButtonCallBacks() {

    }

    private void setBottomNavigationView() {
        bottomNavigationView.setItemIconTintList(null);
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
                    //bottomDialog.show();
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setBottomSheet() {
        bottomSheetDialogForPhone = new BottomSheetDialog(this);
        bottomSheetDialogForOtp = new BottomSheetDialog(this);
        bottomSheetDialogForEmail = new BottomSheetDialog(this);

        bottomSheetDialogForPhone.setContentView(R.layout.bottomsheet_countrycode_picker);
        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
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

        /*Login Bottom sheet*/
        EditText editTextCarrierNumber = bottomSheetDialogForPhone.findViewById(R.id.editText_carrierNumber);
        MaterialButton materialButtonLoginUsingPhone = bottomSheetDialogForPhone.findViewById(R.id.login_using_phone);
        materialButtonLoginUsingPhone.setOnClickListener(v -> {

        });
        /*OTP*/
        OtpView otpView = bottomSheetDialogForOtp.findViewById(R.id.otp_view);
        TextView textView = bottomSheetDialogForOtp.findViewById(R.id.otp_text);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                //Get OTP
                Logger.d("OTP: " + otp);
            }
        });
        MaterialButton materialButtonContinueOtp = bottomSheetDialogForOtp.findViewById(R.id.otp_continue);
        materialButtonContinueOtp.setOnClickListener(v -> {

        });
        /*Email*/
        TextInputEditText textInputEditTextEmail = bottomSheetDialogForEmail.findViewById(R.id.bottom_email);
        TextInputEditText textInputEditTextPassword = bottomSheetDialogForEmail.findViewById(R.id.bottom_password);
        MaterialButton materialButtonContinueEmail = bottomSheetDialogForEmail.findViewById(R.id.button_continue);
    }

    private void setFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }
}