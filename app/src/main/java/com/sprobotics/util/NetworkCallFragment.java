package com.sprobotics.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sprobotics.network.interfaces.OnCallBackListner;
import com.sprobotics.network.util.ApiRequest;

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
