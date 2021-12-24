package com.sproboticworks.view.activity;

import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.adapter.NotificationAdapter;
import com.sproboticworks.model.NotificationModel;
import com.sproboticworks.preferences.SessionManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private RecyclerView recyclerView;
    private ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    private ProgressDialog progressDialog;
    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        imageButton= findViewById(R.id.back_button);
        recyclerView= findViewById(R.id.reyclerview_noti);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        imageButton.setOnClickListener(v->onBackPressed());
        setRecyclerView();
    }


    private void setRecyclerView(){
        progressDialog.show();
        Call<NotificationModel> call = API_PLACE_HOLDER.getNotification(userId, "", "");
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                progressDialog.dismiss();
                String json = new Gson().toJson(response.body());
                Logger.d(json);
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        NotificationModel notificationModel = response.body();

                    }
                    else{
                        Toasty.error(NotificationActivity.this, "No Data", Toasty.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toasty.error(NotificationActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}