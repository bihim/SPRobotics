package com.sprobotics.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sprobotics.R;

public class HomeFragment extends Fragment {

    private TextView textViewName;
    private ImageButton imageButtonCart;
    private LinearLayout cartCountView;
    private TextView cartCount;

    private ImageButton imageButtonNoti;
    private LinearLayout NotiCountView;
    private TextView NotiCount;

    private RecyclerView recyclerViewCourse;
    private RecyclerView recyclerViewCoursePopular;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViewById(view);
        setButtonCallbacks();
        return view;
    }
    private void findViewById(View view){
        textViewName = view.findViewById(R.id.home_fragment_name);
        imageButtonCart = view.findViewById(R.id.cart_button);
        cartCountView = view.findViewById(R.id.cart_count_view);
        cartCount = view.findViewById(R.id.cart_count);

        imageButtonNoti = view.findViewById(R.id.notification_button);
        NotiCountView = view.findViewById(R.id.noti_count_view);
        NotiCount = view.findViewById(R.id.noti_count);

        recyclerViewCourse = view.findViewById(R.id.course_recyclerview);
        recyclerViewCoursePopular = view.findViewById(R.id.course_recyclerview_popular);
    }

    private void setButtonCallbacks(){
        imageButtonCart.setOnClickListener(v->{

        });
        imageButtonNoti.setOnClickListener(v->{

        });
    }
}
