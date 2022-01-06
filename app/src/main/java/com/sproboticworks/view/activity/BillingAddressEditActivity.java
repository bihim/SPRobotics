package com.sproboticworks.view.activity;

import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;
import static com.sproboticworks.network.zubaer.Global.SHOW_ERROR_TOAST;
import static com.sproboticworks.network.zubaer.Global.SHOW_INFO_TOAST;
import static com.sproboticworks.network.zubaer.Global.SHOW_SUCCESS_TOAST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.AddressModel;
import com.sproboticworks.model.ProfileEditModel;
import com.sproboticworks.model.StateCityModel;
import com.sproboticworks.network.util.Constant;
import com.sproboticworks.preferences.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingAddressEditActivity extends AppCompatActivity {
    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();
    private TextInputEditText textInputEditTextName, textInputEditTextContactNo, textInputEditTextEmail,
            textInputEditTextStudentName, textInputEditTextStudentEmail, textInputEditTextStudentContactNo, textInputEditTextStudentDateOfBirth;
    private Calendar myCalendar = Calendar.getInstance();
    private RadioGroup radioGroupGender;
    private Activity activity;
    private MaterialButton materialButtonUpdateStudentAndProfile;
    private MaterialButton materialButtonUpdateAddress;
    private ProgressBar progressBarStudentProfile;
    private ProgressBar progressBarStudentAddress;
    private String gender = "Male";
    private String dob;
    private ArrayList<StateCityModel.Datum> stateArrayList = new ArrayList<>();
    private ArrayList<StateCityModel.Datum> cityArrayList = new ArrayList<>();
    private ArrayList<String> stateArrayListString = new ArrayList<>();
    private ArrayList<String> cityArrayListString = new ArrayList<>();
    private AutoCompleteTextView autoCompleteTextViewState;
    private AutoCompleteTextView autoCompleteTextViewCity;
    private String stateId;
    private String cityId;
    private TextInputEditText textInputEditTextAddress, textInputEditTextPostalCode, textInputEditTextContactNoAddress;
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        findViewById();
        setButtonCallBacks();
        getStates();
        getAddress();
    }

    private void setAddress(String address, String postalCode, String contactNo) {
        progressBarStudentAddress.setVisibility(View.VISIBLE);
        Logger.d("City Id: " + cityId);
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
                        if (addressModel.getData().size() != 0) {
                            if (addressModel.getResponse()) {
                                textInputEditTextAddress.setText(addressModel.getData().get(0).getAddress());
                                textInputEditTextPostalCode.setText(addressModel.getData().get(0).getPostalCode());
                                textInputEditTextContactNoAddress.setText(addressModel.getData().get(0).getContactNo());
                                if (!addressModel.getData().get(0).getStateName().isEmpty()) {
                                    autoCompleteTextViewState.setText(addressModel.getData().get(0).getStateName().get(0), false);
                                }
                                if (!addressModel.getData().get(0).getCityName().isEmpty()) {
                                    autoCompleteTextViewCity.setText(addressModel.getData().get(0).getCityName().get(0), false);
                                }
                                if (!addressModel.getData().get(0).getStateName().isEmpty()) {
                                    stateId = addressModel.getData().get(0).getStateId().toString();
                                }
                                if (!addressModel.getData().get(0).getCityName().isEmpty()) {
                                    cityId = addressModel.getData().get(0).getCityId().toString();
                                }
                                getCity(stateId);
                                SHOW_SUCCESS_TOAST(activity, "Address Updated Successfully");
                                onBackPressed();
                            } else {
                                SHOW_ERROR_TOAST(activity, "Could not update");
                            }
                        } else {
                            setAddress(address, postalCode, contactNo);
                            /*textInputEditTextAddress.setText(Constant.ADDRESS);
                            textInputEditTextPostalCode.setText(Constant.PINCODE);*/
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
        ProgressDialog progressDialog = new ProgressDialog(this);
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
                        if (addressModel.getResponse()) {
                            if (addressModel.getData().size() > 0) {
                                textInputEditTextAddress.setText(addressModel.getData().get(0).getAddress());
                                textInputEditTextPostalCode.setText(addressModel.getData().get(0).getPostalCode());
                                textInputEditTextContactNoAddress.setText(addressModel.getData().get(0).getContactNo());
                                if (!addressModel.getData().get(0).getStateName().isEmpty()) {
                                    autoCompleteTextViewState.setText(addressModel.getData().get(0).getStateName().get(0), false);
                                }
                                if (!addressModel.getData().get(0).getCityName().isEmpty()) {
                                    autoCompleteTextViewCity.setText(addressModel.getData().get(0).getCityName().get(0), false);
                                }
                                if (!addressModel.getData().get(0).getStateName().isEmpty()) {
                                    stateId = addressModel.getData().get(0).getStateId().toString();
                                }
                                if (addressModel.getData().get(0).getCityName().size() != 0) {
                                    Logger.wtf("If " + addressModel.getData().get(0).getCityId().toString());
                                    Logger.wtf("Halar size " + addressModel.getData().get(0).getCityName().size());
                                    cityId = addressModel.getData().get(0).getCityId().toString();
                                } else {
                                    Logger.wtf("Else " + addressModel.getData().get(0).getCityName());
                                    Logger.wtf("Halar size " + addressModel.getData().get(0).getCityName().size());
                                }
                                getCity(stateId);
                            } else {
                                textInputEditTextAddress.setText(Constant.ADDRESS);
                                textInputEditTextPostalCode.setText(Constant.PINCODE);
                            }
                        } else {
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
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Cities");
        progressDialog.show();
        Call<StateCityModel> call = API_PLACE_HOLDER.getCity(stateId);
        call.enqueue(new Callback<StateCityModel>() {
            @Override
            public void onResponse(Call<StateCityModel> call, Response<StateCityModel> response) {
                try {
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                }
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        StateCityModel stateCityModel = response.body();
                        cityArrayList = new ArrayList<>(stateCityModel.getData());
                        for (StateCityModel.Datum data : cityArrayList) {
                            cityArrayListString.add(data.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(BillingAddressEditActivity.this, android.R.layout.simple_list_item_1, cityArrayListString);
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(BillingAddressEditActivity.this, android.R.layout.simple_list_item_1, stateArrayListString);
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
            ProgressDialog progressDialog = new ProgressDialog(this);
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

    private void findViewById() {
        activity = this;
        textInputEditTextName = findViewById(R.id.profile_name);
        textInputEditTextContactNo = findViewById(R.id.profile_contact_no);
        textInputEditTextEmail = findViewById(R.id.profile_email);
        textInputEditTextStudentName = findViewById(R.id.profile_student_name);
        textInputEditTextStudentEmail = findViewById(R.id.profile_student_email);
        textInputEditTextStudentContactNo = findViewById(R.id.profile_student_contact_no);
        textInputEditTextStudentDateOfBirth = findViewById(R.id.profile_student_date_of_birth);
        radioGroupGender = findViewById(R.id.profile_student_gender);
        progressBarStudentProfile = findViewById(R.id.profile_progress_bar);
        progressBarStudentAddress = findViewById(R.id.profile_progress_bar_address);
        materialButtonUpdateStudentAndProfile = findViewById(R.id.profile_and_student_update);
        materialButtonUpdateAddress = findViewById(R.id.profile_address_update);
        autoCompleteTextViewState = findViewById(R.id.profile_state);
        autoCompleteTextViewCity = findViewById(R.id.profile_city);
        textInputEditTextAddress = findViewById(R.id.profile_address);
        textInputEditTextPostalCode = findViewById(R.id.profile_postalcode);
        textInputEditTextContactNoAddress = findViewById(R.id.profile_contact_no_address);
        imageButton = findViewById(R.id.back_button);
    }

    private void setButtonCallBacks() {

        imageButton.setOnClickListener(v -> {
            onBackPressed();
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
            new DatePickerDialog(this, date, myCalendar
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
                /*getAndSetProfileInfo(false, textInputEditTextName.getText().toString(),
                        textInputEditTextContactNo.getText().toString(),
                        textInputEditTextEmail.getText().toString(),
                        textInputEditTextStudentName.getText().toString(),
                        textInputEditTextStudentEmail.getText().toString(),
                        textInputEditTextStudentContactNo.getText().toString(),
                        dob, gender
                );*/
            }
        });

        materialButtonUpdateAddress.setOnClickListener(v -> {
            /*if (textInputEditTextContactNoAddress.getText().toString().length()<10){
                SHOW_ERROR_TOAST(activity, "Invalid Number");
            }*/
            if (textInputEditTextAddress.getText().toString().isEmpty()) {
                SHOW_ERROR_TOAST(activity, "Input Address");
            } else if (textInputEditTextPostalCode.getText().toString().isEmpty()) {
                SHOW_ERROR_TOAST(activity, "Input Postal Code");
            }
            /*else if (textInputEditTextContactNoAddress.getText().toString().isEmpty()){
                SHOW_ERROR_TOAST(activity, "Input Contact No");
            }*/
            else if (stateId == null) {
                SHOW_ERROR_TOAST(activity, "Select State");
            } else if (cityId == null) {
                SHOW_ERROR_TOAST(activity, "Select City");
            } else {
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
    protected void onResume() {
        super.onResume();
        //getAndSetProfileInfo(true, "", "", "", "", "", "", "", "");
    }

    @Override
    public void onBackPressed() {
        this.startActivity(new Intent(this, CartActivity.class));
        this.overridePendingTransition(0, 0);
        finish();
    }
}