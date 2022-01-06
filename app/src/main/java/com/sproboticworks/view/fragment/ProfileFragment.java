package com.sproboticworks.view.fragment;

import static com.sproboticworks.network.util.Constant.EMAIL_OTP;
import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;
import static com.sproboticworks.network.zubaer.Global.SHOW_ERROR_TOAST;
import static com.sproboticworks.network.zubaer.Global.SHOW_INFO_TOAST;
import static com.sproboticworks.network.zubaer.Global.SHOW_SUCCESS_TOAST;
import static com.sproboticworks.preferences.SessionManager.CHILD_NAME;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.AddressModel;
import com.sproboticworks.model.EmailCheckingModel;
import com.sproboticworks.model.MobileCheckingModel;
import com.sproboticworks.model.ProfileEditModel;
import com.sproboticworks.model.StateCityModel;
import com.sproboticworks.model.loginresponse.LogInResponse;
import com.sproboticworks.model.mobileotp.PhoneOtpSentResponse;
import com.sproboticworks.network.util.Constant;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.network.util.ToastUtils;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.MethodClass;
import com.sproboticworks.util.NetworkCallFragment;
import com.sproboticworks.view.activity.AboutUsActivity;
import com.sproboticworks.view.activity.EnquiryActivity;
import com.sproboticworks.view.activity.MainActivity;
import com.sproboticworks.view.activity.OrderHistoryActivity;
import com.sproboticworks.view.activity.ParsingHtmlActivity;
import com.sproboticworks.view.activity.SplashScreenActivity;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends NetworkCallFragment {

    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();
    String phone_number, firebase_otp, otpFor = "";
    private LinearLayout profileButton, logoutButton, addressButton;
    private TextView textViewProfileName, textViewEmailAddress;
    private Activity activity;
    private TextInputEditText textInputEditTextName, textInputEditTextEmail,
            textInputEditTextStudentName, textInputEditTextStudentEmail, textInputEditTextStudentContactNo, textInputEditTextStudentDateOfBirth;
    private Calendar myCalendar = Calendar.getInstance();
    private RadioGroup radioGroupGender;
    private EditText textInputEditTextContactNo;
    private MaterialButton materialButtonUpdateStudentAndProfile;
    private String gender = "Male";
    private String dob;
    private String loginType = "M";
    private FirebaseAuth auth;

    private ProgressBar progressBarStudentProfile;
    private ProgressBar progressBarStudentAddress;

    private ArrayList<StateCityModel.Datum> stateArrayList = new ArrayList<>();
    private ArrayList<StateCityModel.Datum> cityArrayList = new ArrayList<>();
    private ArrayList<String> stateArrayListString = new ArrayList<>();
    private ArrayList<String> cityArrayListString = new ArrayList<>();
    private AutoCompleteTextView autoCompleteTextViewState;
    private AutoCompleteTextView autoCompleteTextViewCity;
    private String stateId;
    private String cityId;
    private TextInputEditText textInputEditTextAddress, textInputEditTextPostalCode, textInputEditTextContactNoAddress;
    private MaterialButton materialButtonUpdateAddress;
    private ExpandableLayout expandableLayoutProfile;
    private ExpandableLayout expandableLayoutAddress;
    private ImageView imageViewProfile, imageViewAddress;
    private MaterialButton aboutSp, termsAndCondition, privacyPolicy, disclaimer, shippingAndDelivery, returnPolicy;
    private LinearLayout about_us;
    private ExpandableLayout expandableLayoutAboutus;
    private ImageView imageViewAbout;
    private TextView phoneNumber, verifyStatus;
    private ImageView emailOrPhone;
    private LinearLayout linearLayout;
    private boolean isEmailVerified, isPhoneVerified;
    private EditText profile_verify, profile_verify_emails;
    private String OTP = "";
    private BottomSheetDialog bottomSheetDialogForPhone, bottomSheetDialogForOtp, bottomSheetDialogForEmail;
    private MaterialTextView textview_otp_sent_to;
    private MaterialButton gotoEmail;
    private String email = "";
    private OtpView otpView;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    private LinearLayout orderHistoryButton, enquireNow, about_us_another;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViewById(view);
        setButtonCallBacks();
        getProfileInfo();
        //getProfileInfo();
        Logger.d("User Id: "+userId);
        getAndSetProfileInfo(true, "", "", "", "", "", "", "", "");
        getStates();
        getAddress();
        setBottomSheet();
        startFirebaseLogin();
        return view;
    }

    private void findViewById(View view) {
        activity = getActivity();
        profileButton = view.findViewById(R.id.profile_go);
        textViewProfileName = view.findViewById(R.id.profile_name_main);
        textViewEmailAddress = view.findViewById(R.id.profile_email_main);
        logoutButton = view.findViewById(R.id.logout);
        addressButton = view.findViewById(R.id.manage_address);
        textInputEditTextName = view.findViewById(R.id.profile_name);
        textInputEditTextContactNo = view.findViewById(R.id.profile_contact_no);
        textInputEditTextEmail = view.findViewById(R.id.profile_email);
        textInputEditTextStudentName = view.findViewById(R.id.profile_student_name);
        textInputEditTextStudentEmail = view.findViewById(R.id.profile_student_email);
        textInputEditTextStudentContactNo = view.findViewById(R.id.profile_student_contact_no);
        textInputEditTextStudentDateOfBirth = view.findViewById(R.id.profile_student_date_of_birth);
        radioGroupGender = view.findViewById(R.id.profile_student_gender);
        progressBarStudentProfile = view.findViewById(R.id.profile_progress_bar);
        progressBarStudentAddress = view.findViewById(R.id.profile_progress_bar_address);
        materialButtonUpdateStudentAndProfile = view.findViewById(R.id.profile_and_student_update);
        materialButtonUpdateAddress = view.findViewById(R.id.profile_address_update);
        autoCompleteTextViewState = view.findViewById(R.id.profile_state);
        autoCompleteTextViewCity = view.findViewById(R.id.profile_city);
        textInputEditTextAddress = view.findViewById(R.id.profile_address);
        textInputEditTextPostalCode = view.findViewById(R.id.profile_postalcode);
        textInputEditTextContactNoAddress = view.findViewById(R.id.profile_contact_no_address);
        expandableLayoutProfile = view.findViewById(R.id.profile_expand);
        expandableLayoutAddress = view.findViewById(R.id.address_expand);
        imageViewProfile = view.findViewById(R.id.profileImageView);
        imageViewAddress = view.findViewById(R.id.addressImageView);
        aboutSp = view.findViewById(R.id.aboutSp);
        termsAndCondition = view.findViewById(R.id.termsAndCondition);
        privacyPolicy = view.findViewById(R.id.privacyPolicy);
        disclaimer = view.findViewById(R.id.disclaimer);
        shippingAndDelivery = view.findViewById(R.id.shippingAndDelivery);
        returnPolicy = view.findViewById(R.id.returnPolicy);
        expandableLayoutAboutus = view.findViewById(R.id.about_expand);
        about_us = view.findViewById(R.id.about_us);
        imageViewAbout = view.findViewById(R.id.about_icon);
        phoneNumber = view.findViewById(R.id.phone_number);
        verifyStatus = view.findViewById(R.id.verifyStatus);
        emailOrPhone = view.findViewById(R.id.emailPhoneLogo);
        linearLayout = view.findViewById(R.id.verifyLayout);
        profile_verify = view.findViewById(R.id.profile_verify);
        profile_verify_emails = view.findViewById(R.id.profile_verify_emails);
        bottomSheetDialogForPhone = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForPhone.setContentView(R.layout.bottomsheet_countrycode_picker);
        bottomSheetDialogForOtp = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForEmail = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
        bottomSheetDialogForEmail.setContentView(R.layout.bottomsheet_email_picker);
        textview_otp_sent_to = bottomSheetDialogForOtp.findViewById(R.id.textview_otp_sent_to);
        gotoEmail = bottomSheetDialogForOtp.findViewById(R.id.gotoEmail);
        otpView = bottomSheetDialogForOtp.findViewById(R.id.otp_view);
        progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("please wait...");
        orderHistoryButton = view.findViewById(R.id.orderHistoryButton);
        enquireNow = view.findViewById(R.id.enquire_now);
        about_us_another = view.findViewById(R.id.about_us_another);
        otherButtonsOfAboutUs(termsAndCondition, "terms");
        otherButtonsOfAboutUs(privacyPolicy, "privacy");
        otherButtonsOfAboutUs(disclaimer, "disclaimer");
        otherButtonsOfAboutUs(shippingAndDelivery, "shipping");
        otherButtonsOfAboutUs(returnPolicy, "returnPolicy");
    }

    private void otherButtonsOfAboutUs(MaterialButton materialButton, String tag) {
        materialButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ParsingHtmlActivity.class).putExtra("source", tag));
        });
    }

    private void setButtonCallBacks() {

        enquireNow.setOnClickListener(v->{
            getActivity().startActivity(new Intent(getActivity(), EnquiryActivity.class).putExtra("bottomTag", "page_4"));
            getActivity().overridePendingTransition(0, 0);
        });
        about_us_another.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
        });

        about_us.setOnClickListener(v -> {
            if (expandableLayoutAboutus.isExpanded()) {
                expandableLayoutAboutus.collapse();
                imageViewAbout.setImageResource(R.drawable.ic_round_arrow_forward_ios_24);
            } else {
                expandableLayoutAboutus.expand();
                imageViewAbout.setImageResource(R.drawable.ic_down);
            }
        });

        orderHistoryButton.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
        });

        /*aboutSp.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
        });*/


        profileButton.setOnClickListener(v -> {
            //startActivity(new Intent(getContext(), ProfileEditActivity.class));
            if (expandableLayoutProfile.isExpanded()) {
                expandableLayoutProfile.collapse();
                imageViewProfile.setImageResource(R.drawable.ic_round_arrow_forward_ios_24);
            } else {
                expandableLayoutProfile.expand();
                imageViewProfile.setImageResource(R.drawable.ic_down);
            }
        });

        addressButton.setOnClickListener(v -> {
            if (expandableLayoutAddress.isExpanded()) {
                expandableLayoutAddress.collapse();
                imageViewAddress.setImageResource(R.drawable.ic_round_arrow_forward_ios_24);
            } else {
                expandableLayoutAddress.expand();
                imageViewAddress.setImageResource(R.drawable.ic_down);
            }
        });

        logoutButton.setOnClickListener(v -> {
            SessionManager.setValue(getActivity(), CHILD_NAME, "");
            SessionManager.setLoggedIn(false);
            startActivity(new Intent(getActivity(), SplashScreenActivity.class));
            getActivity().finish();
        });

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        textInputEditTextStudentDateOfBirth.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(activity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        radioGroupGender.setOnCheckedChangeListener((radioGroup, i) -> {
            gender = i == R.id.profile_student_male ? "Male" : "Female";
            Logger.d(gender);
        });

        materialButtonUpdateStudentAndProfile.setOnClickListener(v -> {
            if (textInputEditTextContactNo.getText().toString().length() < 10) {
                SHOW_ERROR_TOAST(activity, "Invalid Number");
            }else if(textInputEditTextStudentContactNo.getText().toString().length() < 10){
                SHOW_ERROR_TOAST(activity, "Invalid Student Number");
            } else if (textInputEditTextEmail.getText().toString().isEmpty()) {
                SHOW_ERROR_TOAST(getActivity(), "Insert your email first");
            } else if (textInputEditTextStudentEmail.getText().toString().isEmpty()) {
                SHOW_ERROR_TOAST(getActivity(), "Insert student email first");
            } else {
                if (!isEmailVerified) {
                    checkEmail();
                    /*loginType = "E";
                    requestForEmailOtp(textInputEditTextEmail.getText().toString());*/
                } else if (!isPhoneVerified) {
                    checkMobileNumber();
                    /*loginType = "M";
                    sentOTPRequest(textInputEditTextContactNo.getText().toString());*/
                } else {
                    getAndSetProfileInfo(false, textInputEditTextName.getText().toString(),
                            textInputEditTextContactNo.getText().toString(),
                            textInputEditTextEmail.getText().toString(),
                            textInputEditTextStudentName.getText().toString(),
                            textInputEditTextStudentEmail.getText().toString(),
                            textInputEditTextStudentContactNo.getText().toString(),
                            dob, gender
                    );
                }
            }
        });

        materialButtonUpdateAddress.setOnClickListener(v -> {
            /*if (textInputEditTextContactNoAddress.getText().toString().length() < 10) {
                SHOW_ERROR_TOAST(activity, "Invalid Number");
            }
            else*/ if (textInputEditTextAddress.getText().toString().isEmpty()){
                SHOW_ERROR_TOAST(activity, "Input Address");
            }
            else if (textInputEditTextPostalCode.getText().toString().isEmpty()){
                SHOW_ERROR_TOAST(activity, "Input Postal Code");
            }
            /*else if (textInputEditTextContactNoAddress.getText().toString().isEmpty()){
                SHOW_ERROR_TOAST(activity, "Input Contact No");
            }*/
            else if (stateId == null){
                SHOW_ERROR_TOAST(activity, "Select State");
            }
            else if (cityId == null){
                SHOW_ERROR_TOAST(activity, "Select City");
            }

            else {
                setAddress(textInputEditTextAddress.getText().toString(), textInputEditTextPostalCode.getText().toString(), textInputEditTextContactNoAddress.getText().toString());
            }
        });
    }

    private void checkMobileNumber(){
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        Call<MobileCheckingModel> call = API_PLACE_HOLDER.getMobileChecking(textInputEditTextContactNo.getText().toString());
        call.enqueue(new Callback<MobileCheckingModel>() {
            @Override
            public void onResponse(Call<MobileCheckingModel> call, Response<MobileCheckingModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        MobileCheckingModel mobileCheckingModel = response.body();
                        if (mobileCheckingModel.getResponse()){
                            loginType = "M";
                            sentOTPRequest(textInputEditTextContactNo.getText().toString());
                        }
                        else{
                            SHOW_ERROR_TOAST(activity, mobileCheckingModel.getMessage());
                        }
                    }
                    else{
                        SHOW_ERROR_TOAST(activity, "Failed to check number");
                    }
                }
                else{
                    SHOW_ERROR_TOAST(activity, "Response Error");
                }
            }

            @Override
            public void onFailure(Call<MobileCheckingModel> call, Throwable t) {
                progressDialog.dismiss();
                SHOW_ERROR_TOAST(activity, "Something went wrong");
            }
        });
    }

    private void checkEmail(){
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        Call<EmailCheckingModel> call = API_PLACE_HOLDER.getEmailChecking(textInputEditTextEmail.getText().toString());
        call.enqueue(new Callback<EmailCheckingModel>() {
            @Override
            public void onResponse(Call<EmailCheckingModel> call, Response<EmailCheckingModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        EmailCheckingModel emailCheckingModel = response.body();
                        if (emailCheckingModel.getResponse()){
                            loginType = "E";
                            requestForEmailOtp(textInputEditTextEmail.getText().toString());
                        }
                        else{
                            SHOW_ERROR_TOAST(activity, emailCheckingModel.getMessage());
                        }
                    }
                    else{
                        SHOW_ERROR_TOAST(activity, "Failed to check email");
                    }
                }
                else{
                    SHOW_ERROR_TOAST(activity, "Response Error");
                }
            }

            @Override
            public void onFailure(Call<EmailCheckingModel> call, Throwable t) {
                progressDialog.dismiss();
                SHOW_ERROR_TOAST(activity, "Something went wrong");
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        String neededFormat = "yyyy-dd-MM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfNeeded = new SimpleDateFormat(neededFormat, Locale.US);
        textInputEditTextStudentDateOfBirth.setText(sdf.format(myCalendar.getTime()));
        dob = sdfNeeded.format(myCalendar.getTime());
    }


    @Override
    public void onResume() {
        super.onResume();
        /*getProfileInfo();
        getAndSetProfileInfo(true, "", "", "", "", "", "", "", "");
        getStates();
        getAddress();*/
    }

    private void setAddress(String address, String postalCode, String contactNo) {
        progressBarStudentAddress.setVisibility(View.VISIBLE);
        Logger.d("City: "+cityId+" State: "+stateId);
        Logger.d("User Id: "+userId);
        Call<AddressModel> call = API_PLACE_HOLDER.setAddress(userId, address, cityId, stateId, postalCode, contactNo);
        call.enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                progressBarStudentAddress.setVisibility(View.GONE);
                String json = new Gson().toJson(response.body());
                Logger.json(json);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AddressModel addressModel = response.body();
                        if (addressModel.getData().size()!=0){
                            if (addressModel.getResponse()) {
                                textInputEditTextAddress.setText(addressModel.getData().get(0).getAddress());
                                textInputEditTextPostalCode.setText(addressModel.getData().get(0).getPostalCode());
                                textInputEditTextContactNoAddress.setText(addressModel.getData().get(0).getContactNo());
                                if (!addressModel.getData().get(0).getStateName().isEmpty()){
                                    autoCompleteTextViewState.setText(addressModel.getData().get(0).getStateName().get(0), false);
                                }
                                if (!addressModel.getData().get(0).getCityName().isEmpty()){
                                    autoCompleteTextViewCity.setText(addressModel.getData().get(0).getCityName().get(0), false);
                                }
                                if (!addressModel.getData().get(0).getStateName().isEmpty()){
                                    stateId = addressModel.getData().get(0).getStateId().toString();
                                }
                                if (!addressModel.getData().get(0).getCityName().isEmpty()){
                                    cityId = addressModel.getData().get(0).getCityId().toString();
                                }
                                getCity(stateId);
                                SHOW_SUCCESS_TOAST(activity, "Address Updated Successfully");
                            } else {
                                SHOW_ERROR_TOAST(activity, "Could not update");
                            }
                        }

                    } else {
                        SHOW_ERROR_TOAST(activity, "Could not update");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Could not update");
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {
                SHOW_ERROR_TOAST(activity, "Server error");
                progressBarStudentAddress.setVisibility(View.GONE);
                Logger.e(t.getMessage());
            }
        });
    }

    private void getAddress() {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        Call<AddressModel> call = API_PLACE_HOLDER.getAddress(userId);
        call.enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                progressDialog.dismiss();
                String json = new Gson().toJson(response.body());
                Logger.json(json);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AddressModel addressModel = response.body();
                        if (!addressModel.getData().isEmpty()) {
                            if (addressModel.getResponse()) {
                                textInputEditTextAddress.setText(addressModel.getData().get(0).getAddress());
                                textInputEditTextPostalCode.setText(addressModel.getData().get(0).getPostalCode());
                                textInputEditTextContactNoAddress.setText(addressModel.getData().get(0).getContactNo());
                                if (!addressModel.getData().get(0).getStateName().isEmpty()){
                                    autoCompleteTextViewState.setText(addressModel.getData().get(0).getStateName().get(0), false);
                                }
                                if (!addressModel.getData().get(0).getCityName().isEmpty()){
                                    autoCompleteTextViewCity.setText(addressModel.getData().get(0).getCityName().get(0), false);
                                }
                                if (!addressModel.getData().get(0).getStateName().isEmpty()){
                                    stateId = addressModel.getData().get(0).getStateId().toString();
                                }
                                if (!addressModel.getData().get(0).getCityName().isEmpty()){
                                    cityId = addressModel.getData().get(0).getCityId().toString();
                                }
                                getCity(stateId);
                            } else {
                                textInputEditTextAddress.setText(Constant.ADDRESS);
                                textInputEditTextPostalCode.setText(Constant.PINCODE);
                                //SHOW_INFO_TOAST(activity, "Please update your address");
                            }
                        } else {
                            SHOW_INFO_TOAST(activity, "Please update your address");
                            textInputEditTextAddress.setText(Constant.ADDRESS);
                            textInputEditTextPostalCode.setText(Constant.PINCODE);
                            //getStatesIfNoAddressSaved();
                        }
                    } else {
                        //SHOW_ERROR_TOAST(activity, "Address could not fetch");
                        textInputEditTextAddress.setText(Constant.ADDRESS);
                        textInputEditTextPostalCode.setText(Constant.PINCODE);
                    }
                } else {
                    //SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                    textInputEditTextAddress.setText(Constant.ADDRESS);
                    textInputEditTextPostalCode.setText(Constant.PINCODE);
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {
                //SHOW_ERROR_TOAST(activity, "Server error");
                progressDialog.dismiss();
                Logger.e(t.getMessage());
                textInputEditTextAddress.setText(Constant.ADDRESS);
                textInputEditTextPostalCode.setText(Constant.PINCODE);
            }
        });
    }


    private void getCity(String stateId) {
        cityArrayList.clear();
        cityArrayListString.clear();
        //autoCompleteTextViewCity.setText("");
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Cities");
        progressDialog.show();
        Call<StateCityModel> call = API_PLACE_HOLDER.getCity(stateId);
        call.enqueue(new Callback<StateCityModel>() {
            @Override
            public void onResponse(Call<StateCityModel> call, Response<StateCityModel> response) {
                progressDialog.dismiss();
                String json = new Gson().toJson(response.body());
                Logger.d(json);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        StateCityModel stateCityModel = response.body();
                        cityArrayList = new ArrayList<>(stateCityModel.getData());
                        for (StateCityModel.Datum data : cityArrayList) {
                            cityArrayListString.add(data.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, cityArrayListString);
                        autoCompleteTextViewCity.setAdapter(adapter);
                        autoCompleteTextViewCity.setOnItemClickListener((adapterView, view, i, l) -> {
                            Logger.d("Selected State: " + cityArrayList.get(i).getName());
                            cityId = cityArrayList.get(i).getId().toString();
                        });
                    } else {
                        SHOW_ERROR_TOAST(activity, "Could not load city");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Could not load city");
                }
            }

            @Override
            public void onFailure(Call<StateCityModel> call, Throwable t) {
                SHOW_ERROR_TOAST(activity, "Server error");
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });
    }


    private void getStates() {
        stateArrayList.clear();
        stateArrayListString.clear();
        Call<StateCityModel> call = API_PLACE_HOLDER.getStates();
        call.enqueue(new Callback<StateCityModel>() {
            @Override
            public void onResponse(Call<StateCityModel> call, Response<StateCityModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        StateCityModel stateCityModel = response.body();
                        stateArrayList = new ArrayList<>(stateCityModel.getData());
                        for (StateCityModel.Datum data : stateArrayList) {
                            stateArrayListString.add(data.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, stateArrayListString);
                        autoCompleteTextViewState.setAdapter(adapter);
                        autoCompleteTextViewState.setOnItemClickListener((adapterView, view, i, l) -> {
                            Logger.d("Selected State: " + stateArrayList.get(i).getName());
                            stateId = stateArrayList.get(i).getId().toString();
                            getCity(stateId);
                        });
                    } else {
                        SHOW_ERROR_TOAST(activity, "Could not get states");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Could not get states");
                }
            }

            @Override
            public void onFailure(Call<StateCityModel> call, Throwable t) {
                SHOW_ERROR_TOAST(activity, "Server error");
                Logger.e(t.getMessage());
            }
        });
    }

    private void getStatesIfNoAddressSaved() {
        stateArrayList.clear();
        stateArrayListString.clear();
        Call<StateCityModel> call = API_PLACE_HOLDER.getStates();
        call.enqueue(new Callback<StateCityModel>() {
            @Override
            public void onResponse(Call<StateCityModel> call, Response<StateCityModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        StateCityModel stateCityModel = response.body();
                        stateArrayList = new ArrayList<>(stateCityModel.getData());
                        for (StateCityModel.Datum data : stateArrayList) {
                            stateArrayListString.add(data.getName());
                        }
                        if (stateArrayListString.contains(Constant.STATE)){
                            int index = stateArrayListString.indexOf(Constant.STATE);
                            stateId = stateArrayList.get(index).getId().toString();
                            getCity(stateId);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, stateArrayListString);
                        autoCompleteTextViewState.setAdapter(adapter);
                        autoCompleteTextViewState.setOnItemClickListener((adapterView, view, i, l) -> {
                            Logger.d("Selected State: " + stateArrayList.get(i).getName());
                            stateId = stateArrayList.get(i).getId().toString();
                            getCity(stateId);
                        });
                    } else {
                        SHOW_ERROR_TOAST(activity, "Could not get states");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Could not get states");
                }
            }

            @Override
            public void onFailure(Call<StateCityModel> call, Throwable t) {
                SHOW_ERROR_TOAST(activity, "Server error");
                Logger.e(t.getMessage());
            }
        });
    }

    private void getAndSetProfileInfo(boolean isGetting, String name, String contactNo, String email, String studentName,
                                      String studentEmail, String studentContactNo, String getDob, String gender) {
        Logger.d("User Id: " + userId);
        if (!isGetting) {
            progressBarStudentProfile.setVisibility(View.VISIBLE);
            Logger.d("User Id: " + userId);
            Call<ProfileEditModel> call = API_PLACE_HOLDER.setProfile(userId, name, email, contactNo, studentName, studentEmail, studentContactNo, getDob, gender);
            call.enqueue(new Callback<ProfileEditModel>() {
                @Override
                public void onResponse(Call<ProfileEditModel> call, Response<ProfileEditModel> response) {
                    String responseString = new Gson().toJson(response.body());
                    Logger.d(response.body());
                    Logger.d(response.message());
                    Logger.d(response.errorBody());
                    Logger.d(response.code());
                    Logger.d(responseString);
                    progressBarStudentProfile.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResponse()) {
                                ProfileEditModel profileEditModel = response.body();
                                textInputEditTextName.setText(profileEditModel.getData().get(0).getDetails().getCustomerName());
                                if (!profileEditModel.getData().get(0).getDetails().getCustomerContactNo().isEmpty()) {
                                    textInputEditTextContactNo.setText(profileEditModel.getData().get(0).getDetails().getCustomerContactNo());
                                    isPhoneVerified = true;
                                    textInputEditTextContactNo.setEnabled(false);
                                    profile_verify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0);
                                    profile_verify.setText("Verified");
                                } else {
                                    emailOrPhone.setImageResource(R.drawable.ic_phone);
                                    verifyStatus.setText("Add Your Phone");
                                    isPhoneVerified = false;
                                    profile_verify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear, 0, 0, 0);
                                    profile_verify.setText("Unverified");
                                }
                                if (!profileEditModel.getData().get(0).getDetails().getCustomerEmail().isEmpty()) {
                                    textInputEditTextEmail.setText(profileEditModel.getData().get(0).getDetails().getCustomerEmail());
                                    isEmailVerified = true;
                                    textInputEditTextEmail.setEnabled(false);
                                    profile_verify_emails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0);
                                    profile_verify_emails.setText("Verified");
                                } else {
                                    emailOrPhone.setImageResource(R.drawable.ic_email);
                                    verifyStatus.setText("Add Your Email Address");
                                    isEmailVerified = false;
                                    profile_verify_emails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear, 0, 0, 0);
                                    profile_verify_emails.setText("Unverified");
                                }
                                Logger.d("Is verified: " + (!profileEditModel.getData().get(0).getDetails().getCustomerContactNo().isEmpty() && !profileEditModel.getData().get(0).getDetails().getCustomerEmail().isEmpty()));
                                if (!profileEditModel.getData().get(0).getDetails().getCustomerContactNo().isEmpty() && !profileEditModel.getData().get(0).getDetails().getCustomerEmail().isEmpty()) {
                                    linearLayout.setVisibility(View.GONE);
                                    /*isPhoneVerified = true;
                                    isEmailVerified = true;
                                    textInputEditTextContactNo.setEnabled(false);
                                    textInputEditTextEmail.setEnabled(false);*/
                                } else {
                                    /*isPhoneVerified = false;
                                    isEmailVerified = false;
                                    textInputEditTextContactNo.setEnabled(true);
                                    textInputEditTextEmail.setEnabled(true);*/
                                    linearLayout.setVisibility(View.VISIBLE);
                                }
                                textInputEditTextStudentName.setText(profileEditModel.getData().get(0).getDetails().getStudentName());
                                textInputEditTextStudentEmail.setText(profileEditModel.getData().get(0).getDetails().getStudentEmailId());
                                textInputEditTextStudentContactNo.setText(profileEditModel.getData().get(0).getDetails().getStudentContactNo());
                                textInputEditTextStudentDateOfBirth.setText(profileEditModel.getData().get(0).getDetails().getDob());
                                if (profileEditModel.getData().get(0).getDetails().getGender() != null) {
                                    if (profileEditModel.getData().get(0).getDetails().getGender().equals("MALE")) {
                                        radioGroupGender.check(R.id.profile_student_male);
                                    } else {
                                        radioGroupGender.check(R.id.profile_student_female);
                                    }
                                } else {
                                    radioGroupGender.check(R.id.profile_student_male);
                                }
                                SHOW_SUCCESS_TOAST(activity, "Updated Successfully");

                            } else {
                                SHOW_ERROR_TOAST(activity, "Profile Update Error");
                            }
                        } else {
                            SHOW_ERROR_TOAST(activity, "Profile Update Error");
                        }
                    } else {
                        SHOW_ERROR_TOAST(activity, "No Response");
                    }
                }

                @Override
                public void onFailure(Call<ProfileEditModel> call, Throwable t) {
                    SHOW_ERROR_TOAST(activity, t.getMessage());
                    progressBarStudentProfile.setVisibility(View.GONE);
                    Logger.e(t.getMessage());
                }
            });
        } else {
            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Please wait");
            progressDialog.show();

            Call<ProfileEditModel> call = API_PLACE_HOLDER.getProfile(userId);
            call.enqueue(new Callback<ProfileEditModel>() {
                @Override
                public void onResponse(Call<ProfileEditModel> call, Response<ProfileEditModel> response) {
                    String responseString = new Gson().toJson(response.body());
                    Logger.d(responseString);
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResponse()) {
                                ProfileEditModel profileEditModel = response.body();
                                textInputEditTextName.setText(profileEditModel.getData().get(0).getDetails().getCustomerName());
                                if (profileEditModel.getData().get(0).getDetails().getCustomerContactNo()!=null){
                                    if (!profileEditModel.getData().get(0).getDetails().getCustomerContactNo().isEmpty()) {
                                        textInputEditTextContactNo.setText(profileEditModel.getData().get(0).getDetails().getCustomerContactNo());
                                        isPhoneVerified = true;
                                        textInputEditTextContactNo.setEnabled(false);
                                        profile_verify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0);
                                        profile_verify.setText("Verified");
                                    } else {
                                        emailOrPhone.setImageResource(R.drawable.ic_phone);
                                        verifyStatus.setText("Add Your Phone");
                                        isPhoneVerified = false;
                                        profile_verify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear, 0, 0, 0);
                                        profile_verify.setText("Unverified");
                                    }
                                }
                                else{
                                    emailOrPhone.setImageResource(R.drawable.ic_phone);
                                    verifyStatus.setText("Add Your Phone");
                                    isPhoneVerified = false;
                                    profile_verify.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear, 0, 0, 0);
                                    profile_verify.setText("Unverified");
                                }
                                if (profileEditModel.getData().get(0).getDetails().getCustomerEmail()!=null){
                                    if (!profileEditModel.getData().get(0).getDetails().getCustomerEmail().isEmpty()) {
                                        textInputEditTextEmail.setText(profileEditModel.getData().get(0).getDetails().getCustomerEmail());
                                        isEmailVerified = true;
                                        textInputEditTextEmail.setEnabled(false);
                                        profile_verify_emails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0);
                                        profile_verify_emails.setText("Verified");
                                    } else {
                                        emailOrPhone.setImageResource(R.drawable.ic_email);
                                        verifyStatus.setText("Add Your Email Address");
                                        isEmailVerified = false;
                                        profile_verify_emails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear, 0, 0, 0);
                                        profile_verify_emails.setText("Unverified");
                                    }
                                }
                                else{
                                    emailOrPhone.setImageResource(R.drawable.ic_email);
                                    verifyStatus.setText("Add Your Email Address");
                                    isEmailVerified = false;
                                    profile_verify_emails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear, 0, 0, 0);
                                    profile_verify_emails.setText("Unverified");
                                }
                                //Logger.d("Is verified: " + (!profileEditModel.getData().get(0).getDetails().getCustomerContactNo().isEmpty() && !profileEditModel.getData().get(0).getDetails().getCustomerEmail().isEmpty()));
                                if (profileEditModel.getData().get(0).getDetails().getCustomerContactNo()!=null && profileEditModel.getData().get(0).getDetails().getCustomerEmail()!=null){
                                    if (!profileEditModel.getData().get(0).getDetails().getCustomerContactNo().isEmpty() && !profileEditModel.getData().get(0).getDetails().getCustomerEmail().isEmpty()) {
                                        linearLayout.setVisibility(View.GONE);
                                    /*isPhoneVerified = true;
                                    isEmailVerified = true;
                                    textInputEditTextContactNo.setEnabled(false);
                                    textInputEditTextEmail.setEnabled(false);*/
                                    } else {
                                    /*isPhoneVerified = false;
                                    isEmailVerified = false;
                                    textInputEditTextContactNo.setEnabled(true);
                                    textInputEditTextEmail.setEnabled(true);*/
                                        linearLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                                else{
                                    linearLayout.setVisibility(View.VISIBLE);
                                }
                                textInputEditTextStudentName.setText(profileEditModel.getData().get(0).getDetails().getStudentName());
                                textInputEditTextStudentEmail.setText(profileEditModel.getData().get(0).getDetails().getStudentEmailId());
                                textInputEditTextStudentContactNo.setText(profileEditModel.getData().get(0).getDetails().getStudentContactNo());
                                textInputEditTextStudentDateOfBirth.setText(profileEditModel.getData().get(0).getDetails().getDob());
                                String gettingDob = profileEditModel.getData().get(0).getDetails().getDob();
                                String[] dateSplit = gettingDob.split("-");
                                dob = dateSplit[2]+"-"+dateSplit[1]+"-"+dateSplit[0];
                                if (profileEditModel.getData().get(0).getDetails().getGender() != null) {
                                    if (profileEditModel.getData().get(0).getDetails().getGender().equals("MALE")) {
                                        radioGroupGender.check(R.id.profile_student_male);
                                    } else {
                                        radioGroupGender.check(R.id.profile_student_female);
                                    }
                                } else {
                                    radioGroupGender.check(R.id.profile_student_male);
                                }

                            } else {
                                SHOW_ERROR_TOAST(activity, "Profile getting error");
                            }
                        } else {
                            SHOW_ERROR_TOAST(activity, "Profile getting error");
                        }
                    } else {
                        SHOW_ERROR_TOAST(activity, "No Response");
                    }
                }

                @Override
                public void onFailure(Call<ProfileEditModel> call, Throwable t) {
                    SHOW_ERROR_TOAST(activity, "Something went wrong");
                    progressDialog.dismiss();
                    Logger.e(t.getMessage());
                }
            });
        }

    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);

        if (tag.equalsIgnoreCase(EMAIL_OTP)) {
            PhoneOtpSentResponse response1 = (PhoneOtpSentResponse) GsonUtil.toObject(response, PhoneOtpSentResponse.class);
            // ToastUtils.showLong(activity, response1.getData().getOtp());
            OTP = response1.getData().getOtp();
            //bottomSheetDialogForPhone.dismiss();
            textview_otp_sent_to.setText("OTP has been sent to " + email);
            gotoEmail.setText("Proceed with Mobile Number");
            bottomSheetDialogForOtp.show();
        }
        /*if (tag.equalsIgnoreCase(MOBILE_LOGIN)) {
            LogInResponse response1 = (LogInResponse) GsonUtil.toObject(response, LogInResponse.class);
            SessionManager.setValue(SessionManager.LOGIN_RESPONSE, GsonUtil.toJsonString(response1));
            SessionManager.setLoggedIn(true);
            ToastUtils.showLong(activity, "Logged in successfully");
        }*/
    }

    public void requestForEmailOtp(String email_id) {
        this.email = email_id;
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email_id);
        apiRequest.postRequest(EMAIL_OTP, map, EMAIL_OTP);
    }

    private void getProfileInfo() {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        Logger.d(userId);
        Call<ProfileEditModel> call = API_PLACE_HOLDER.getProfile(userId);
        call.enqueue(new Callback<ProfileEditModel>() {
            @Override
            public void onResponse(Call<ProfileEditModel> call, Response<ProfileEditModel> response) {
                String responseString = new Gson().toJson(response.body());
                Logger.d(responseString);
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse()) {
                            ProfileEditModel profileEditModel = response.body();
                            textViewProfileName.setText(profileEditModel.getData().get(0).getDetails().getCustomerName());
                            if (profileEditModel.getData().get(0).getDetails().getCustomerEmail() != null) {
                                if (!profileEditModel.getData().get(0).getDetails().getCustomerEmail().isEmpty()) {
                                    textViewEmailAddress.setText(profileEditModel.getData().get(0).getDetails().getCustomerEmail());
                                    textViewEmailAddress.setVisibility(View.VISIBLE);
                                } else {
                                    textViewEmailAddress.setVisibility(View.GONE);
                                }
                            }
                            else{
                                textViewEmailAddress.setVisibility(View.GONE);
                            }

                            if (profileEditModel.getData().get(0).getDetails().getCustomerContactNo() != null) {
                                if (!profileEditModel.getData().get(0).getDetails().getCustomerContactNo().isEmpty()) {
                                    phoneNumber.setText(profileEditModel.getData().get(0).getDetails().getCustomerContactNo());
                                    phoneNumber.setVisibility(View.VISIBLE);
                                } else {
                                    phoneNumber.setVisibility(View.GONE);
                                }
                            }
                            else{
                                phoneNumber.setVisibility(View.GONE);
                            }

                            //phoneNumber.setText(profileEditModel.getData().get(0).getDetails().getCustomerContactNo());
                        } else {
                            SHOW_ERROR_TOAST(activity, "Profile getting error");
                        }
                    } else {
                        SHOW_ERROR_TOAST(activity, "Profile getting error");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "No Response");
                }
            }

            @Override
            public void onFailure(Call<ProfileEditModel> call, Throwable t) {
                SHOW_ERROR_TOAST(activity, "Something went wrong");
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });
    }

    private void setBottomSheet() {

        bottomSheetDialogForOtp = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForEmail = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);


        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
        bottomSheetDialogForEmail.setContentView(R.layout.bottomsheet_email_picker);


        gotoEmail = bottomSheetDialogForOtp.findViewById(R.id.gotoEmail);
        MaterialButton gotoPhone = bottomSheetDialogForEmail.findViewById(R.id.gotoPhone);

        MaterialButton gotoEmailAgain = bottomSheetDialogForPhone.findViewById(R.id.login_using_phone);

        gotoEmailAgain.setOnClickListener(v -> {
            bottomSheetDialogForEmail.show();
            bottomSheetDialogForPhone.dismiss();
        });

        if (gotoEmail != null)
            gotoEmail.setOnClickListener(v -> {

                if (loginType.equalsIgnoreCase("M"))
                    bottomSheetDialogForEmail.show();
                else bottomSheetDialogForPhone.show();
                bottomSheetDialogForOtp.dismiss();

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
                if (loginType == "M")
                    OTP_Verification(otp);
                else {
                    if (OTP.equalsIgnoreCase(otp)) {
                        if (bottomSheetDialogForEmail.isShowing())
                            bottomSheetDialogForEmail.dismiss();
                        bottomSheetDialogForOtp.dismiss();
                        MethodClass.hideKeyboard(activity);
                        MethodClass.showAlertDialog(activity, true, "OTP verified", "OTP verified successfully", false);
                        //loginWithEmailOrMobile("E");
                        getAndSetProfileInfo(false, textInputEditTextName.getText().toString(),
                                textInputEditTextContactNo.getText().toString(),
                                textInputEditTextEmail.getText().toString(),
                                textInputEditTextStudentName.getText().toString(),
                                textInputEditTextStudentEmail.getText().toString(),
                                textInputEditTextStudentContactNo.getText().toString(),
                                dob, gender
                        );
                    } else
                        MethodClass.showAlertDialog(activity, true, "Invalid OTP", "Invalid OTP", false);
                }


            }
        });
        MaterialButton materialButtonContinueOtp = bottomSheetDialogForOtp.findViewById(R.id.otp_continue);
        materialButtonContinueOtp.setOnClickListener(v -> {

        });
        textview_resend_otp.setOnClickListener(v -> {
            if (loginType == "M")
                sentOTPRequest(phone_number);
        });
        /*Email*/
        TextInputEditText textInputEditTextEmail = bottomSheetDialogForEmail.findViewById(R.id.bottom_email);
        TextInputEditText textInputEditTextPassword = bottomSheetDialogForEmail.findViewById(R.id.bottom_password);
        MaterialButton materialButtonContinueEmail = bottomSheetDialogForEmail.findViewById(R.id.button_continue);

        materialButtonContinueEmail.setOnClickListener(v -> {
            if (textInputEditTextEmail.getText().toString().length() > 0) {
                loginType = "E";
                requestForEmailOtp(textInputEditTextEmail.getText().toString());
            }
        });
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


    public void OTP_Verification(String otptext) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otptext);
        signInWithPhoneAuthCredential(credential, otptext);
    }

    private void startFirebaseLogin() {
        FirebaseApp.initializeApp(activity);
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

                //loginWithEmailOrMobile("M");
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                setSnackBar(e.getLocalizedMessage(), getString(R.string.btn_ok), "failed");
                Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(activity.getApplicationContext(), getString(R.string.otp_sent), Toast.LENGTH_SHORT).show();

                if (bottomSheetDialogForPhone.isShowing())
                    bottomSheetDialogForPhone.dismiss();

                if (!bottomSheetDialogForOtp.isShowing())
                    bottomSheetDialogForOtp.show();
                gotoEmail.setText("Proceed with Email ID");


                textview_otp_sent_to.setText("OTP has been sent to +91" + phone_number);
                progressDialog.dismiss();

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String otptext) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            //loginWithEmailOrMobile("M");
                            if (bottomSheetDialogForOtp.isShowing())
                                bottomSheetDialogForOtp.dismiss();
                            if (bottomSheetDialogForPhone.isShowing())
                                bottomSheetDialogForPhone.dismiss();
                            getAndSetProfileInfo(false, textInputEditTextName.getText().toString(),
                                    textInputEditTextContactNo.getText().toString(),
                                    textInputEditTextEmail.getText().toString(),
                                    textInputEditTextStudentName.getText().toString(),
                                    textInputEditTextStudentEmail.getText().toString(),
                                    textInputEditTextStudentContactNo.getText().toString(),
                                    dob, gender
                            );

                        } else {

                            //verification unsuccessful.. display an error message
                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            ToastUtils.showLong(activity, message);

                        }
                    }
                });
    }

    public void setSnackBar(String message, String action, final String type) {
        final Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
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
