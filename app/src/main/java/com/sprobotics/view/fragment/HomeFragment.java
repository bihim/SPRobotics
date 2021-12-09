package com.sprobotics.view.fragment;

import static com.sprobotics.network.util.Constant.GET_AGE_GROUP;
import static com.sprobotics.network.util.Constant.MOBILE_OTP;
import static com.sprobotics.network.util.Constant.PRODUCT_LIST;

import android.net.Uri;

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

import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.orhanobut.logger.Logger;
import com.sprobotics.R;
import com.sprobotics.adapter.CourseAdapter;
import com.sprobotics.model.courseresponse.CourseListResponse;
import com.sprobotics.model.mobileotp.PhoneOtpSentResponse;
import com.sprobotics.network.util.Constant;
import com.sprobotics.network.util.GsonUtil;
import com.sprobotics.network.util.ToastUtils;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.util.NetworkCallFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeFragment extends NetworkCallFragment {

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
        String url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

        findViewById(view);
        setButtonCallbacks();
        return view;
    }

    private void findViewById(View view) {
        textViewName = view.findViewById(R.id.home_fragment_name);
        imageButtonCart = view.findViewById(R.id.cart_button);
        cartCountView = view.findViewById(R.id.cart_count_view);
        cartCount = view.findViewById(R.id.cart_count);

        imageButtonNoti = view.findViewById(R.id.notification_button);
        NotiCountView = view.findViewById(R.id.noti_count_view);
        NotiCount = view.findViewById(R.id.noti_count);

        recyclerViewCourse = view.findViewById(R.id.course_recyclerview);
        recyclerViewCoursePopular = view.findViewById(R.id.course_recyclerview_popular);


        textViewName.setText("HI " + SessionManager.getValue(SessionManager.CHILD_NAME));

        getAgeGroupId();


    }

    private void setButtonCallbacks() {
        imageButtonCart.setOnClickListener(v -> {

        });
        imageButtonNoti.setOnClickListener(v -> {

        });
    }

    public void getCourseList() {


        HashMap<String, String> map = new HashMap<>();

        switch (SessionManager.getValue(SessionManager.CHILD_AGE)) {

            case "10":
                map.put("age_category_id", Constant.JUNIOR_AGE_ID);
                break;

            case "13":
                map.put("age_category_id", Constant.SENIOR_AGE_ID);
                break;

            case "14":
                map.put("age_category_id", Constant.SUPER_SENIOR_AGE_ID);
                break;


        }

        apiRequest.postRequest(PRODUCT_LIST, map, PRODUCT_LIST);


    }


    public void getAgeGroupId() {
        apiRequest.callGetRequest(GET_AGE_GROUP, GET_AGE_GROUP);
    }


    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);
        if (tag.equalsIgnoreCase(PRODUCT_LIST)) {
            CourseListResponse response1 = (CourseListResponse) GsonUtil.toObject(response, CourseListResponse.class);
            recyclerViewCourse.setAdapter(new CourseAdapter(response1.getData(), requireContext()));


        }
        if (tag.equalsIgnoreCase(GET_AGE_GROUP)) {

            try {
                JSONObject object = new JSONObject(response.toString());

                Constant.JUNIOR_AGE_ID = object.getString("Junior");
                Constant.SENIOR_AGE_ID = object.getString("Senior");
                Constant.SUPER_SENIOR_AGE_ID = object.getString("Super Senior");

                getCourseList();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
