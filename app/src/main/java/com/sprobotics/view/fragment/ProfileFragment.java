package com.sprobotics.view.fragment;

import static com.sprobotics.network.zubaer.Global.API_PLACE_HOLDER;
import static com.sprobotics.network.zubaer.Global.SHOW_ERROR_TOAST;
import static com.sprobotics.preferences.SessionManager.CHILD_NAME;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sprobotics.R;
import com.sprobotics.model.ProfileEditModel;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.view.activity.ProfileEditActivity;
import com.sprobotics.view.activity.SplashScreenActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private LinearLayout profileButton, logoutButton;
    private TextView textViewProfileName, textViewEmailAddress;
    private Activity activity;
    private final String userId = SessionManager.getLoginResponse().getData().getCustomerId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViewById(view);
        setButtonCallBacks();
        return view;
    }

    private void findViewById(View view) {
        activity = getActivity();
        profileButton = view.findViewById(R.id.profile_go);
        textViewProfileName = view.findViewById(R.id.profile_name);
        textViewEmailAddress = view.findViewById(R.id.profile_email);
        logoutButton = view.findViewById(R.id.logout);
    }

    private void setButtonCallBacks() {
        profileButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProfileEditActivity.class));
        });
        logoutButton.setOnClickListener(v->{
            SessionManager.setValue(CHILD_NAME,"");
            SessionManager.setLoggedIn(false);
            startActivity(new Intent(getActivity(), SplashScreenActivity.class));
            getActivity().finish();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfileInfo();
    }

    private void getProfileInfo(){
        ProgressDialog progressDialog = new ProgressDialog(activity);
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
                            textViewProfileName.setText(profileEditModel.getData().get(0).getDetails().getCustomerName());
                            textViewEmailAddress.setText(profileEditModel.getData().get(0).getDetails().getCustomerEmail());
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
