package com.sproboticworks.view.activity;

import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.AllCoursesModel;
import com.sproboticworks.model.EnquiryModel;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.view.fragment.HomeFragment;
import com.sproboticworks.view.fragment.ProfileFragment;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnquiryActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private boolean isComingFromCourseDetails = false;
    private String courseId = "1";
    private String courseName = "ELECTRO-BLOCKS MINI KIT";
    private String bottomTag = "page_1";
    private AutoCompleteTextView autoCompleteTextViewCourses;
    private EditText enquireQuery;
    private MaterialButton submit;
    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        bottomTag = getIntent().getStringExtra("bottomTag");
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        autoCompleteTextViewCourses = findViewById(R.id.enquire_courses);
        enquireQuery = findViewById(R.id.enquire_query);
        submit = findViewById(R.id.submit_enquire);
        bottomNavigationView.setSelectedItemId(R.id.page_3);
        setBottomNavigationView();
        submit.setOnClickListener(v->{
            if (enquireQuery.getText().toString().isEmpty()){
                Toasty.error(this, "Enter your query", Toasty.LENGTH_SHORT).show();
            }
            else{
                sendEnquiry();
            }
        });
    }
    private void getCourses(){
        Context context = this;
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        Call<AllCoursesModel> call = API_PLACE_HOLDER.getAllCourses();
        call.enqueue(new Callback<AllCoursesModel>() {
            @Override
            public void onResponse(Call<AllCoursesModel> call, Response<AllCoursesModel> response) {
                progressDialog.dismiss();
                String json = new Gson().toJson(response.body());
                Logger.json(json);
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        AllCoursesModel allCoursesModel = response.body();
                        ArrayList<String> courseNames = new ArrayList<>();
                        ArrayList<AllCoursesModel.Datum> coursesDatum = new ArrayList<>(allCoursesModel.getData());
                        for (AllCoursesModel.Datum data: coursesDatum){
                            courseNames.add(data.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EnquiryActivity.this, android.R.layout.simple_list_item_1, courseNames);
                        autoCompleteTextViewCourses.setAdapter(adapter);
                        //autoCompleteTextViewCourses.setText(courseNames.get(0));
                        autoCompleteTextViewCourses.setOnItemClickListener((adapterView, view, i, l) -> courseId = coursesDatum.get(i).getProductId().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<AllCoursesModel> call, Throwable t) {
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });
    }

    private void sendEnquiry(){
        Context context = this;
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        Call<EnquiryModel> call = API_PLACE_HOLDER.setEnquiry(userId, courseId, enquireQuery.getText().toString());
        call.enqueue(new Callback<EnquiryModel>() {
            @Override
            public void onResponse(Call<EnquiryModel> call, Response<EnquiryModel> response) {
                progressDialog.dismiss();
                String json = new Gson().toJson(response.body());
                Logger.json(json);
                if (response.isSuccessful())
                    if (response.body()!=null){
                        EnquiryModel enquiryModel = response.body();
                        if (enquiryModel.getResponse()){
                            Toasty.success(context, "Thank you, We will get back to you shortly.", Toasty.LENGTH_SHORT).show();
                            enquireQuery.setText("");
                        }
                        else{
                            Toasty.error(context, enquiryModel.getError(), Toasty.LENGTH_SHORT).show();
                        }
                    }

            }

            @Override
            public void onFailure(Call<EnquiryModel> call, Throwable t) {
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });
    }

    private void setBottomNavigationView() {

        bottomTag = getIntent().getStringExtra("bottomTag");
        isComingFromCourseDetails = getIntent().getBooleanExtra("comingFromCourseDetails", false);
        if (isComingFromCourseDetails){
            courseId = getIntent().getStringExtra("course");
            courseName = getIntent().getStringExtra("courseName");
            autoCompleteTextViewCourses.setText(courseName);
            autoCompleteTextViewCourses.setEnabled(false);
        }
        else{
            getCourses();
        }
        bottomNavigationView.setSelectedItemId(R.id.page_3);
        /*switch (bottomTag) {
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
        }*/

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