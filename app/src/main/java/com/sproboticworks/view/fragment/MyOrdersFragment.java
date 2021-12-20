package com.sproboticworks.view.fragment;

import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.adapter.OrderHistoryAdapter;
import com.sproboticworks.model.OrderHistoryModel;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.view.activity.OrderHistoryActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersFragment extends Fragment {
    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();
    private OrderHistoryAdapter orderHistoryAdapter;
    private ArrayList<OrderHistoryModel.Datum> orderHistoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        recyclerView = view.findViewById(R.id.orderHistoryRecyler);
        linearLayout = view.findViewById(R.id.no_order);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        Call<OrderHistoryModel> call = API_PLACE_HOLDER.getOrderHistory(userId);
        call.enqueue(new Callback<OrderHistoryModel>() {
            @Override
            public void onResponse(Call<OrderHistoryModel> call, Response<OrderHistoryModel> response) {
                String json = new Gson().toJson(response.body());
                Logger.d(json);
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().getData().isEmpty()) {
                            orderHistoryList.addAll(response.body().getData());
                            orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryList, getActivity());
                            recyclerView.setAdapter(orderHistoryAdapter);
                        } else {
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryModel> call, Throwable t) {
                progressDialog.setMessage("Loading error");
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });
        return view;
    }
}
