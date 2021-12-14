package com.sprobotics.view.fragment;

import static com.sprobotics.network.zubaer.Global.API_PLACE_HOLDER;
import static com.sprobotics.network.zubaer.Global.SHOW_ERROR_TOAST;
import static com.sprobotics.network.zubaer.Global.SHOW_INFO_TOAST;
import static com.sprobotics.network.zubaer.Global.SHOW_SUCCESS_TOAST;
import static com.sprobotics.preferences.SessionManager.CHILD_NAME;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sprobotics.R;
import com.sprobotics.model.AddressModel;
import com.sprobotics.model.ProfileEditModel;
import com.sprobotics.model.StateCityModel;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.view.activity.AboutUsActivity;
import com.sprobotics.view.activity.ParsingHtmlActivity;
import com.sprobotics.view.activity.ProfileEditActivity;
import com.sprobotics.view.activity.SplashScreenActivity;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private LinearLayout profileButton, logoutButton, addressButton;
    private TextView textViewProfileName, textViewEmailAddress;
    private Activity activity;
    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();
    private TextInputEditText textInputEditTextName, textInputEditTextEmail,
            textInputEditTextStudentName, textInputEditTextStudentEmail, textInputEditTextStudentContactNo, textInputEditTextStudentDateOfBirth;
    private Calendar myCalendar = Calendar.getInstance();
    private RadioGroup radioGroupGender;
    private EditText textInputEditTextContactNo;
    private MaterialButton materialButtonUpdateStudentAndProfile;
    private String gender = "Male";
    private String dob;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViewById(view);
        setButtonCallBacks();
        getProfileInfo();
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
        otherButtonsOfAboutUs(termsAndCondition, "terms");
        otherButtonsOfAboutUs(privacyPolicy, "privacy");
        otherButtonsOfAboutUs(disclaimer, "disclaimer");
        otherButtonsOfAboutUs(shippingAndDelivery, "shipping");
        otherButtonsOfAboutUs(returnPolicy, "returnPolicy");
    }

    private void otherButtonsOfAboutUs(MaterialButton materialButton, String tag){
        materialButton.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), ParsingHtmlActivity.class).putExtra("source", tag));
        });
    }

    private void setButtonCallBacks() {
        about_us.setOnClickListener(v->{
            if (expandableLayoutAboutus.isExpanded()){
                expandableLayoutAboutus.collapse();
                imageViewAbout.setImageResource(R.drawable.ic_round_arrow_forward_ios_24);
            }
            else{
                expandableLayoutAboutus.expand();
                imageViewAbout.setImageResource(R.drawable.ic_down);
            }
        });

        aboutSp.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
        });



        profileButton.setOnClickListener(v -> {
            //startActivity(new Intent(getContext(), ProfileEditActivity.class));
            if (expandableLayoutProfile.isExpanded()){
                expandableLayoutProfile.collapse();
                imageViewProfile.setImageResource(R.drawable.ic_round_arrow_forward_ios_24);
            }
            else{
                expandableLayoutProfile.expand();
                imageViewProfile.setImageResource(R.drawable.ic_down);
            }
        });

        addressButton.setOnClickListener(v->{
            if (expandableLayoutAddress.isExpanded()){
                expandableLayoutAddress.collapse();
                imageViewAddress.setImageResource(R.drawable.ic_round_arrow_forward_ios_24);
            }
            else{
                expandableLayoutAddress.expand();
                imageViewAddress.setImageResource(R.drawable.ic_down);
            }
        });

        logoutButton.setOnClickListener(v->{
            SessionManager.setValue(CHILD_NAME,"");
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
            if (textInputEditTextContactNo.getText().toString().length() < 10 || textInputEditTextStudentContactNo.getText().toString().length() < 10) {
                SHOW_ERROR_TOAST(activity, "Invalid Number");
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
        });

        materialButtonUpdateAddress.setOnClickListener(v->{
            if (textInputEditTextContactNoAddress.getText().toString().length()<10){
                SHOW_ERROR_TOAST(activity, "Invalid Number");
            }
            else{
                setAddress(textInputEditTextAddress.getText().toString(), textInputEditTextPostalCode.getText().toString(), textInputEditTextContactNoAddress.getText().toString());
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        String neededFormat = "yy/dd/MM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdfNeeded = new SimpleDateFormat(neededFormat, Locale.US);
        textInputEditTextStudentDateOfBirth.setText(sdf.format(myCalendar.getTime()));
        dob = sdfNeeded.format(myCalendar.getTime());
    }
    

    @Override
    public void onResume() {
        super.onResume();
        getProfileInfo();
        getAndSetProfileInfo(true, "", "", "", "", "", "", "", "");
        getStates();
        getAddress();
    }

    private void setAddress(String address, String postalCode, String contactNo) {
        progressBarStudentAddress.setVisibility(View.VISIBLE);
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
                        if (addressModel.getResponse()){
                            textInputEditTextAddress.setText(addressModel.getData().get(0).getAddress());
                            textInputEditTextPostalCode.setText(addressModel.getData().get(0).getPostalCode());
                            textInputEditTextContactNoAddress.setText(addressModel.getData().get(0).getContactNo());
                            autoCompleteTextViewState.setText(addressModel.getData().get(0).getStateName().get(0));
                            autoCompleteTextViewCity.setText(addressModel.getData().get(0).getCityName().get(0));
                            stateId = addressModel.getData().get(0).getStateId().toString();
                            cityId = addressModel.getData().get(0).getCityId().toString();
                            getCity(cityId);
                            SHOW_SUCCESS_TOAST(activity, "Address Updated Successfully");
                        }
                        else{
                            SHOW_ERROR_TOAST(activity, "Could not update");
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
                        if (addressModel.getResponse()){
                            textInputEditTextAddress.setText(addressModel.getData().get(0).getAddress());
                            textInputEditTextPostalCode.setText(addressModel.getData().get(0).getPostalCode());
                            textInputEditTextContactNoAddress.setText(addressModel.getData().get(0).getContactNo());
                            autoCompleteTextViewState.setText(addressModel.getData().get(0).getStateName().get(0));
                            autoCompleteTextViewCity.setText(addressModel.getData().get(0).getCityName().get(0));
                            stateId = addressModel.getData().get(0).getStateId().toString();
                            cityId = addressModel.getData().get(0).getCityId().toString();
                            getCity(cityId);
                        }
                        else{
                            SHOW_INFO_TOAST(activity, "Please update your address");
                        }
                    } else {
                        SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                }
            }

            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {
                SHOW_ERROR_TOAST(activity, "Server error");
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });
    }


    private void getCity(String stateId) {
        cityArrayList.clear();
        cityArrayListString.clear();
        autoCompleteTextViewCity.setText("");
        cityId = null;
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Cities");
        progressDialog.show();
        Call<StateCityModel> call = API_PLACE_HOLDER.getCity(stateId);
        call.enqueue(new Callback<StateCityModel>() {
            @Override
            public void onResponse(Call<StateCityModel> call, Response<StateCityModel> response) {
                progressDialog.dismiss();
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
                        SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Data Fetch Error");
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
                        SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Data Fetch Error");
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
                    Logger.d(responseString);
                    progressBarStudentProfile.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResponse()) {
                                ProfileEditModel profileEditModel = response.body();
                                textInputEditTextName.setText(profileEditModel.getData().get(0).getDetails().getCustomerName());
                                textInputEditTextContactNo.setText(profileEditModel.getData().get(0).getDetails().getCustomerContactNo());
                                textInputEditTextEmail.setText(profileEditModel.getData().get(0).getDetails().getCustomerEmail());
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
                                SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                            }
                        } else {
                            SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                        }
                    } else {
                        SHOW_ERROR_TOAST(activity, "No Response");
                    }
                }

                @Override
                public void onFailure(Call<ProfileEditModel> call, Throwable t) {
                    SHOW_ERROR_TOAST(activity, "Something went wrong");
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
                                textInputEditTextContactNo.setText(profileEditModel.getData().get(0).getDetails().getCustomerContactNo());
                                textInputEditTextEmail.setText(profileEditModel.getData().get(0).getDetails().getCustomerEmail());
                                textInputEditTextStudentName.setText(profileEditModel.getData().get(0).getDetails().getStudentName());
                                textInputEditTextStudentEmail.setText(profileEditModel.getData().get(0).getDetails().getStudentEmailId());
                                textInputEditTextStudentContactNo.setText(profileEditModel.getData().get(0).getDetails().getStudentContactNo());
                                textInputEditTextStudentDateOfBirth.setText(profileEditModel.getData().get(0).getDetails().getDob());
                                dob = profileEditModel.getData().get(0).getDetails().getDob();
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
                                SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                            }
                        } else {
                            SHOW_ERROR_TOAST(activity, "Data Fetch Error");
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

    private void getProfileInfo(){
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
                            textViewProfileName.setText(profileEditModel.getData().get(0).getDetails().getCustomerName());
                            textViewEmailAddress.setText(profileEditModel.getData().get(0).getDetails().getCustomerEmail());
                        } else {
                            SHOW_ERROR_TOAST(activity, "Data Fetch Error");
                        }
                    } else {
                        SHOW_ERROR_TOAST(activity, "Data Fetch Error");
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
