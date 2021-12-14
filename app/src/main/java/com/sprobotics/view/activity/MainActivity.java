package com.sprobotics.view.activity;

import static com.sprobotics.network.util.Constant.MOBILE_LOGIN;
import static com.sprobotics.network.util.Constant.MOBILE_OTP;
import static com.sprobotics.util.MethodClass.getAddress;
import static com.sprobotics.util.MethodClass.getPincode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
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
import com.sprobotics.view.fragment.FragmentEnquaries;
import com.sprobotics.view.fragment.HomeFragment;
import com.sprobotics.view.fragment.ProfileFragment;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends NetworkCallActivity {

    private String OTP = "";
    private String mobile = "";

    private Activity activity;
    private BottomNavigationView bottomNavigationView;
    private BottomSheetDialog bottomSheetDialogForPhone, bottomSheetDialogForOtp, bottomSheetDialogForEmail;
    private FragmentManager fragmentManager;
    private String str_links;

    ProgressDialog progressDialog;

    ////Firebase
    String phone_number, firebase_otp, otpFor = "";
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    MaterialTextView textview_otp_sent_to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("please wait...");


        setContentView(R.layout.activity_main);

        MethodClass.getLocation(activity);

        if (MethodClass.isGPSEnable(activity)) {

            gettingCurrentLocationByButtonClick();

        } else {
            MethodClass.displayLocationSettingsRequest(activity);
        }


        findViewById();
        setButtonCallBacks();
        setBottomSheet();
        initLoginBottomSheet();
        setBottomNavigationView();
        setFragment(savedInstanceState);
        startFirebaseLogin();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MethodClass.REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    gettingCurrentLocationByButtonClick();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(activity, "You have to enable GPS", Toast.LENGTH_SHORT).show();
                    MethodClass.displayLocationSettingsRequest(activity);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                new androidx.appcompat.app.AlertDialog.Builder(activity)
                        .setTitle(activity.getResources().getString(R.string.location_permission))
                        .setMessage(activity.getResources().getString(R.string.location_permission_message))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        10);
                            }
                        })
                        .create()
                        .show();

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    private void gettingCurrentLocationByButtonClick() {


        if ((ActivityCompat.checkSelfPermission((Activity) activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission((Activity) activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(activity)
                        .setTitle(getString(R.string.location_permission))
                        .setMessage(getString(R.string.location_permission_message))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        10);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        10);
            }
        }

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location == null) {

                    RequestLocation();

                    //  ToastUtils.showLong(activity, "Unable to get Location", true);
                    return;
                }

                if (location != null) {

                    String pincode = getPincode(location.getLatitude(), location.getLongitude(), activity);
                    String address = getAddress(location.getLatitude(), location.getLongitude(), activity);


                    //  ToastUtils.showLong(activity,getPincode(location.getLatitude(),location.getLongitude(),activity),true);
                    // ToastUtils.showLong(activity, "Your current address is  " + address, true);
                    Constant.PINCODE = pincode;
                    Constant.ADDRESS = address;


                }
            }
        });

    }

    private void RequestLocation() {

        LocationRequest mLocationRequest = LocationRequest.create();


        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    ToastUtils.showLong(activity, "Unable to get your current location", true);


                    return;
                }
                //  for (Location location : locationResult.getLocations()) {
                if (locationResult != null) {

                    gettingCurrentLocationByButtonClick();


                    //  }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }


    private void findViewById() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        str_links = "";//Privacy Policy
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
                    /*startActivity(new Intent(this, AboutUsActivity.class));*/
                    //startActivity(new Intent(this, ParsingHtmlActivity.class).putExtra("source", "terms"));
                    return true;
                case R.id.page_3:
                    //bottomSheetDialogForPhone.show();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, new FragmentEnquaries()).commit();
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
        TextView textview_resend_otp = bottomSheetDialogForOtp.findViewById(R.id.textview_resend_otp);
        textview_otp_sent_to = bottomSheetDialogForOtp.findViewById(R.id.textview_otp_sent_to);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                OTP_Verification(otp);

                /*if (OTP.equalsIgnoreCase(otp)) {
                    bottomSheetDialogForOtp.dismiss();
                    MethodClass.hideKeyboard(activity);
                    MethodClass.showAlertDialog(activity, true, "OTP verified", "OTP verified successfully", false);
                    loginWithEmailOrMobile();
                } else
                    MethodClass.showAlertDialog(activity, true, "Invalid OTP", "Invalid OTP", false);
*/

            }
        });
        MaterialButton materialButtonContinueOtp = bottomSheetDialogForOtp.findViewById(R.id.otp_continue);
        materialButtonContinueOtp.setOnClickListener(v -> {

        });
        textview_resend_otp.setOnClickListener(v -> {
            sentOTPRequest(phone_number);
        });
        /*Email*/
        TextInputEditText textInputEditTextEmail = bottomSheetDialogForEmail.findViewById(R.id.bottom_email);
        TextInputEditText textInputEditTextPassword = bottomSheetDialogForEmail.findViewById(R.id.bottom_password);
        MaterialButton materialButtonContinueEmail = bottomSheetDialogForEmail.findViewById(R.id.button_continue);
    }

    protected void setTextViewHTML(TextView textView,String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        textView.setText(strBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(span.getURL()));
                startActivity(i);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void initLoginBottomSheet() {
        bottomSheetDialogForPhone = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForPhone.setContentView(R.layout.bottomsheet_countrycode_picker);

        MaterialButton gotoOtp = bottomSheetDialogForPhone.findViewById(R.id.gotoOtp);
        EditText editText_carrierNumber = bottomSheetDialogForPhone.findViewById(R.id.editText_carrierNumber);



        TextView text_view = bottomSheetDialogForPhone.findViewById(R.id.privacyPolicy);
        setTextViewHTML(text_view, str_links);

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
                    sentOTPRequest(s.toString());
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
        /*this.mobile = mobile;
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        apiRequest.postRequest(MOBILE_OTP, map, MOBILE_OTP);*/

    }


    public void loginWithEmailOrMobile() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", SessionManager.getValue(SessionManager.CHILD_NAME));
        map.put("child_age", SessionManager.getValue(SessionManager.CHILD_AGE));
        map.put("mobile", phone_number);
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
            ToastUtils.showLong(MainActivity.this, "Logged in successfully");


        }


    }

    public void sentOTPRequest(String phoneNumber) {
        progressDialog.show();
        phone_number = phoneNumber;


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                activity,        // Activity (for callback binding)
                mCallback);
    }


    String verificationCode;


    private void startFirebaseLogin() {
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {
                //System.out.println ("====verification complete call  " + phoneAuthCredential.getSmsCode ());
                progressDialog.dismiss();
                if (bottomSheetDialogForPhone.isShowing())
                    bottomSheetDialogForPhone.dismiss();
                if (bottomSheetDialogForOtp.isShowing())
                    bottomSheetDialogForOtp.dismiss();

                loginWithEmailOrMobile();
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                setSnackBar(e.getLocalizedMessage(), getString(R.string.btn_ok), "failed");
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(getApplicationContext(), getString(R.string.otp_sent), Toast.LENGTH_SHORT).show();

                if (bottomSheetDialogForPhone.isShowing())
                    bottomSheetDialogForPhone.dismiss();

                if (!bottomSheetDialogForOtp.isShowing())
                    bottomSheetDialogForOtp.show();


                textview_otp_sent_to.setText("OTP has been sent to +91" + phone_number);
                progressDialog.dismiss();

            }
        };
    }


    public void OTP_Verification(String otptext) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otptext);
        signInWithPhoneAuthCredential(credential, otptext);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String otptext) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            loginWithEmailOrMobile();
                           if (bottomSheetDialogForOtp.isShowing())bottomSheetDialogForOtp.dismiss();
                           if (bottomSheetDialogForPhone.isShowing())bottomSheetDialogForPhone.dismiss();


                        } else {

                            //verification unsuccessful.. display an error message
                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            ToastUtils.showLong(MainActivity.this, message);

                        }
                    }
                });
    }


    public void setSnackBar(String message, String action, final String type) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("reset_pass") || type.equals("forgot") || type.equals("register")) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }

}