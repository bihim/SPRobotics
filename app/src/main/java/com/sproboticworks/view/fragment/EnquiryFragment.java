package com.sproboticworks.view.fragment;

import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.AllCoursesModel;
import com.sproboticworks.model.EnquiryModel;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.view.activity.EnquiryActivity;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnquiryFragment extends Fragment {
    private String courseId = "1";
    private String courseName = "ELECTRO-BLOCKS MINI KIT";
    private String bottomTag = "page_1";
    private AutoCompleteTextView autoCompleteTextViewCourses;
    private EditText enquireQuery;
    private MaterialButton submit;
    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_enquaries, container, false);
        autoCompleteTextViewCourses = view.findViewById(R.id.enquire_courses);
        enquireQuery = view.findViewById(R.id.enquire_query);
        submit = view.findViewById(R.id.submit_enquire);
        submit.setOnClickListener(v->{
            if (enquireQuery.getText().toString().isEmpty()){
                Toasty.error(getActivity(), "Enter your query", Toasty.LENGTH_SHORT).show();
            }
            else{
                sendEnquiry();
            }
        });
        getCourses();
        return view;
    }
    private void getCourses(){
        Context context = getContext();
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, courseNames);
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
        Context context = getContext();
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
}
