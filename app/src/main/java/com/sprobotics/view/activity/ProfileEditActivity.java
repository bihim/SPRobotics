package com.sprobotics.view.activity;

import static com.sprobotics.network.zubaer.Global.API_PLACE_HOLDER;
import static com.sprobotics.network.zubaer.Global.SHOW_ERROR_TOAST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sprobotics.R;
import com.sprobotics.model.ProfileEditModel;
import com.sprobotics.preferences.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {
    private TextInputEditText textInputEditTextName, textInputEditTextContactNo, textInputEditTextEmail,
            textInputEditTextStudentName, textInputEditTextStudentEmail, textInputEditTextStudentContactNo, textInputEditTextStudentDateOfBirth;
    private Calendar myCalendar = Calendar.getInstance();
    private RadioGroup radioGroupGender;
    private Activity activity;
    private MaterialButton materialButtonUpdateStudentAndProfile;
    private ProgressBar progressBarStudentProfile;
    private String gender = "Male";
    private String dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        findViewById();
        setButtonCallBacks();
    }

    private void getAndSetProfileInfo(boolean isGetting, String name, String contactNo, String email, String studentName,
                                      String studentEmail, String studentContactNo, String dob, String gender) {
        String userId = SessionManager.getLoginResponse().getData().getCustomerId();
        if (!isGetting) {
            progressBarStudentProfile.setVisibility(View.VISIBLE);
            Logger.d("User Id: " + userId);
            Call<ProfileEditModel> call = API_PLACE_HOLDER.setProfile(userId, name, email, contactNo, studentName, studentEmail, studentContactNo, dob, gender);
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
        materialButtonUpdateStudentAndProfile = findViewById(R.id.profile_and_student_update);
    }

    private void setButtonCallBacks() {
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
        getAndSetProfileInfo(true, "", "", "", "", "", "", "", "");
    }
}