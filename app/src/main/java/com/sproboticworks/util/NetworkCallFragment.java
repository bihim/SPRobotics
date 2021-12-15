package com.sproboticworks.util;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sproboticworks.network.interfaces.OnCallBackListner;
import com.sproboticworks.network.util.ApiRequest;

public class NetworkCallFragment extends Fragment implements OnCallBackListner {

    public ApiRequest apiRequest;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        apiRequest = new ApiRequest(getActivity(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {

    }

    @Override
    public void OnCallBackError(String tag, String error, int i) {

    }
}
