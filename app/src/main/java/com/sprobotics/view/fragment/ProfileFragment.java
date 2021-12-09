package com.sprobotics.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sprobotics.R;
import com.sprobotics.view.activity.ProfileEditActivity;

public class ProfileFragment extends Fragment {

    private LinearLayout profileButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViewById(view);
        setButtonCallBacks();
        return view;
    }

    private void findViewById(View view) {
        profileButton = view.findViewById(R.id.profile_go);
    }

    private void setButtonCallBacks() {
        profileButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProfileEditActivity.class));
        });
    }
}
