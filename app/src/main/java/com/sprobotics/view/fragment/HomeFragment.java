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
import com.google.android.material.card.MaterialCardView;
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
    private TextView NotiCount;

    private RecyclerView recyclerViewCourse;
    private RecyclerView recyclerViewCoursePopular;

    /*3 Top buttons*/
    private MaterialCardView materialCardViewJunior, materialCardViewSenior, materialCardViewSuperSenior;
    private TextView textViewJunior, textViewSenior, textViewSuperSenior;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
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
        LinearLayout notiCountView = view.findViewById(R.id.noti_count_view);
        NotiCount = view.findViewById(R.id.noti_count);

        recyclerViewCourse = view.findViewById(R.id.course_recyclerview);
        recyclerViewCoursePopular = view.findViewById(R.id.course_recyclerview_popular);
        textViewName.setText("Hi, " + SessionManager.getValue(SessionManager.CHILD_NAME));

        materialCardViewJunior = view.findViewById(R.id.junior_course_button);
        materialCardViewSenior = view.findViewById(R.id.senior_course_button);
        materialCardViewSuperSenior = view.findViewById(R.id.super_senior_course_button);
        textViewJunior = view.findViewById(R.id.junior_course_text);
        textViewSenior = view.findViewById(R.id.senior_course_text);
        textViewSuperSenior = view.findViewById(R.id.super_senior_course_text);

        getAgeGroupId();
    }

    private void selectedTopButtonColor(MaterialCardView materialCardView, TextView textView) {
        materialCardView.setStrokeWidth(0);
        materialCardView.setStrokeColor(getResources().getColor(R.color.transparent));
        materialCardView.invalidate();
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setBackgroundColor(getResources().getColor(R.color.colors1));
    }

    private void unSelectedTopButtonColor(MaterialCardView materialCardView, TextView textView) {
        materialCardView.setStrokeWidth(2);
        materialCardView.setStrokeColor(getResources().getColor(R.color.fragment_home_card_text_color));
        materialCardView.invalidate();
        textView.setTextColor(getResources().getColor(R.color.fragment_home_card_text_color));
        textView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setButtonCallbacks() {
        imageButtonCart.setOnClickListener(v -> {

        });
        imageButtonNoti.setOnClickListener(v -> {

        });


        materialCardViewSuperSenior.setOnClickListener(v->{
            Logger.d(materialCardViewSuperSenior.getStrokeWidth());
            HashMap<String, String> map = new HashMap<>();
            map.put("age_category_id", "253");
            apiRequest.postRequest(PRODUCT_LIST, map, PRODUCT_LIST);
            unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
            unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
            selectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
        });

        materialCardViewJunior.setOnClickListener(v->{
            HashMap<String, String> map = new HashMap<>();
            map.put("age_category_id", "251");
            apiRequest.postRequest(PRODUCT_LIST, map, PRODUCT_LIST);
            selectedTopButtonColor(materialCardViewJunior, textViewJunior);
            unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
            unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
        });

        materialCardViewSenior.setOnClickListener(v->{
            HashMap<String, String> map = new HashMap<>();
            map.put("age_category_id", "252");
            apiRequest.postRequest(PRODUCT_LIST, map, PRODUCT_LIST);
            unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
            selectedTopButtonColor(materialCardViewSenior, textViewSenior);
            unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
        });


    }

    public void getCourseList() {


        HashMap<String, String> map = new HashMap<>();

        switch (SessionManager.getValue(SessionManager.CHILD_AGE)) {

            case "10":
                selectedTopButtonColor(materialCardViewJunior, textViewJunior);
                unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
                unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
                map.put("age_category_id", Constant.JUNIOR_AGE_ID);
                break;

            case "13":
                unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
                selectedTopButtonColor(materialCardViewSenior, textViewSenior);
                unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
                map.put("age_category_id", Constant.SENIOR_AGE_ID);
                break;

            case "14":
                unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
                unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
                selectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
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
