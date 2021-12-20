package com.sproboticworks.view.activity;

import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.adapter.OrderHistoryAdapter;
import com.sproboticworks.model.OrderHistoryModel;
import com.sproboticworks.preferences.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();
    private ImageButton imageButton;
    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<OrderHistoryModel.Datum> orderHistoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        imageButton = findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.orderHistoryRecyler);
        linearLayout = findViewById(R.id.no_order);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Call<OrderHistoryModel> call = API_PLACE_HOLDER.getOrderHistory(userId);
        call.enqueue(new Callback<OrderHistoryModel>() {
            @Override
            public void onResponse(Call<OrderHistoryModel> call, Response<OrderHistoryModel> response) {
                String json = new Gson().toJson(response.body());
                Logger.json(json);
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().getData().isEmpty()){
                            orderHistoryList.addAll(response.body().getData());
                            orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryList, OrderHistoryActivity.this);
                            recyclerView.setAdapter(orderHistoryAdapter);
                        }
                        else{
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryModel> call, Throwable t) {
                progressDialog.setMessage("Loading error");
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });

        imageButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}