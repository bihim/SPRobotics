package com.sproboticworks.util;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sproboticworks.network.interfaces.OnCallBackListner;
import com.sproboticworks.network.util.ApiRequest;

public class NetworkCallActivity extends AppCompatActivity implements OnCallBackListner {

   public ApiRequest apiRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiRequest=new ApiRequest(this,this);

    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {

    }

    @Override
    public void OnCallBackError(String tag, String error, int i) {

    }
}
