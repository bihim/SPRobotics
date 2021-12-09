package com.sprobotics.view.activity;

import static com.sprobotics.network.util.Constant.GET_AGE_GROUP;
import static com.sprobotics.network.util.Constant.MOBILE_LOGIN;
import static com.sprobotics.network.util.Constant.MOBILE_OTP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.sprobotics.R;
import com.sprobotics.model.loginresponse.LogInResponse;
import com.sprobotics.model.mobileotp.PhoneOtpSentResponse;
import com.sprobotics.network.util.Constant;
import com.sprobotics.network.util.GsonUtil;
import com.sprobotics.network.util.ToastUtils;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.util.MethodClass;
import com.sprobotics.util.NetworkCallActivity;
import com.sprobotics.view.fragment.CourseDetailsFragment;
import com.sprobotics.view.fragment.HomeFragment;
import com.sprobotics.view.fragment.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends NetworkCallActivity {

    private String OTP = "";
    private String mobile = "";

    private Activity activity;
    private BottomNavigationView bottomNavigationView;
    private BottomSheetDialog bottomSheetDialogForPhone, bottomSheetDialogForOtp, bottomSheetDialogForEmail;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;


        setContentView(R.layout.activity_main);
        findViewById();
        setButtonCallBacks();
        setBottomSheet();
        initLoginBottomSheet();
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
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_1:
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    return true;
                case R.id.page_2:
                    //startActivity(new Intent(this, CourseDetailsActivity.class));
                    return true;
                case R.id.page_3:
                    bottomSheetDialogForPhone.show();
                    return true;
                case R.id.page_4:
                    if (SessionManager.isLoggedIn())
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    else bottomSheetDialogForPhone.show();
                    //bottomDialog.show();
                    return true;
                default:
                    return false;
            }

        });
    }

    private void setBottomSheet() {

        bottomSheetDialogForOtp = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForEmail = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);


        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
        bottomSheetDialogForEmail.setContentView(R.layout.bottomsheet_email_picker);


        MaterialButton gotoEmail = bottomSheetDialogForOtp.findViewById(R.id.gotoEmail);
        MaterialButton gotoPhone = bottomSheetDialogForEmail.findViewById(R.id.gotoPhone);


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


        /*OTP*/
        OtpView otpView = bottomSheetDialogForOtp.findViewById(R.id.otp_view);
        TextView textView = bottomSheetDialogForOtp.findViewById(R.id.otp_text);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                if (OTP.equalsIgnoreCase(otp)) {
                    bottomSheetDialogForOtp.dismiss();
                    MethodClass.hideKeyboard(activity);
                    MethodClass.showAlertDialog(activity, true, "OTP verified", "OTP verified successfully", false);
                    loginWithEmailOrMobile();
                } else
                    MethodClass.showAlertDialog(activity, true, "Invalid OTP", "Invalid OTP", false);


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


    private void initLoginBottomSheet() {
        bottomSheetDialogForPhone = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForPhone.setContentView(R.layout.bottomsheet_countrycode_picker);

        MaterialButton gotoOtp = bottomSheetDialogForPhone.findViewById(R.id.gotoOtp);
        EditText editText_carrierNumber = bottomSheetDialogForPhone.findViewById(R.id.editText_carrierNumber);

        editText_carrierNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 10) {
                    requestForMobileOTP(s.toString());
                }


            }
        });


    }


    private void setFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }


    // API calling

    public void requestForMobileOTP(String mobile) {
        this.mobile = mobile;
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        apiRequest.postRequest(MOBILE_OTP, map, MOBILE_OTP);


    }


    public void loginWithEmailOrMobile() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", SessionManager.getValue(SessionManager.CHILD_NAME));
        map.put("child_age", SessionManager.getValue(SessionManager.CHILD_AGE));
        map.put("mobile", mobile);
        apiRequest.postRequest(MOBILE_LOGIN, map, MOBILE_LOGIN);


    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);

        if (tag.equalsIgnoreCase(MOBILE_OTP)) {
            PhoneOtpSentResponse response1 = (PhoneOtpSentResponse) GsonUtil.toObject(response, PhoneOtpSentResponse.class);
            ToastUtils.showLong(activity, response1.getData().getOtp());
            OTP = response1.getData().getOtp();
            bottomSheetDialogForPhone.dismiss();
            bottomSheetDialogForOtp.show();


        }
        if (tag.equalsIgnoreCase(MOBILE_LOGIN)) {
            LogInResponse response1 = (LogInResponse) GsonUtil.toObject(response, LogInResponse.class);
            SessionManager.setValue(SessionManager.LOGIN_RESPONSE, GsonUtil.toJsonString(response1));
            SessionManager.setLoggedIn(true);


        }


    }
}