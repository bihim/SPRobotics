package com.sproboticworks.view.fragment;

import static com.sproboticworks.network.util.Constant.GET_AGE_GROUP;
import static com.sproboticworks.network.util.Constant.GET_CART;
import static com.sproboticworks.network.util.Constant.PRODUCT_LIST;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.sproboticworks.R;
import com.sproboticworks.adapter.CourseAdapter;
import com.sproboticworks.adapter.VideoCarouselAdapter;
import com.sproboticworks.model.cartrespone.CartResponse;
import com.sproboticworks.model.courseresponse.CourseListResponse;
import com.sproboticworks.network.util.Constant;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.MethodClass;
import com.sproboticworks.util.NetworkCallFragment;
import com.sproboticworks.view.activity.CartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends NetworkCallFragment {

    private TextView textViewName;
    private ImageButton imageButtonCart;
    private LinearLayout cartCountView;
    private TextView textview_cartCount;

    private ImageButton imageButtonNoti;
    private TextView NotiCount;

    private RecyclerView recyclerViewCourse;
    private RecyclerView recyclerViewCoursePopular;
    private SliderView sliderView;

    /*3 Top buttons*/
    private MaterialCardView materialCardViewJunior, materialCardViewSenior;
    private TextView textViewJunior, textViewSenior;
    private List<String> youtubeIds = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViewById(view);
        setButtonCallbacks();
        setSliderView();
        return view;
    }

    private void findViewById(View view) {
        textViewName = view.findViewById(R.id.home_fragment_name);
        imageButtonCart = view.findViewById(R.id.cart_button);
        cartCountView = view.findViewById(R.id.cart_count_view);
        textview_cartCount = view.findViewById(R.id.cart_count);

        imageButtonNoti = view.findViewById(R.id.notification_button);
        LinearLayout notiCountView = view.findViewById(R.id.noti_count_view);
        NotiCount = view.findViewById(R.id.noti_count);

        recyclerViewCourse = view.findViewById(R.id.course_recyclerview);
        sliderView = view.findViewById(R.id.imageSlider);
        recyclerViewCoursePopular = view.findViewById(R.id.course_recyclerview_popular);
        textViewName.setText("Hi, " + SessionManager.getValue(SessionManager.CHILD_NAME));

        materialCardViewJunior = view.findViewById(R.id.junior_course_button);
        materialCardViewSenior = view.findViewById(R.id.senior_course_button);
        //materialCardViewSuperSenior = view.findViewById(R.id.super_senior_course_button);
        textViewJunior = view.findViewById(R.id.junior_course_text);
        textViewSenior = view.findViewById(R.id.senior_course_text);
        //textViewSuperSenior = view.findViewById(R.id.super_senior_course_text);

        getAgeGroupId();
    }

    private void setSliderView(){
        youtubeIds.add("cAU-HT0OJH0"); //6
        youtubeIds.add("tnNIKwmW7J8"); //4
        youtubeIds.add("O_YR7_yHXiU"); //3
        youtubeIds.add("u7H0_39uZw0"); //1
        youtubeIds.add("3JujmVbkA2A"); //2
        youtubeIds.add("ubjOu0hmtLs"); //5

        VideoCarouselAdapter videoCarouselAdapter = new VideoCarouselAdapter(getActivity());
        sliderView.setSliderAdapter(videoCarouselAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        /*videoCarouselAdapter.addItem("u7H0_39uZw0");
        videoCarouselAdapter.addItem("3JujmVbkA2A");
        videoCarouselAdapter.addItem("O_YR7_yHXiU");
        videoCarouselAdapter.addItem("tnNIKwmW7J8");
        videoCarouselAdapter.addItem("ubjOu0hmtLs");
        videoCarouselAdapter.addItem("cAU-HT0OJH0");*/
        videoCarouselAdapter.renewItems(youtubeIds);
    }

    @Override
    public void onResume() {
        if (SessionManager.isLoggedIn()) {
            getCartData();
        }
        super.onResume();
    }

    public void getCartData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);

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

        imageButtonNoti.setOnClickListener(v -> {

        });


        /*materialCardViewSuperSenior.setOnClickListener(v -> {
            Logger.d(materialCardViewSuperSenior.getStrokeWidth());
            HashMap<String, String> map = new HashMap<>();
            map.put("age_category_id", Constant.SUPER_SENIOR_AGE_ID);
            apiRequest.postRequest(PRODUCT_LIST, map, PRODUCT_LIST);
            unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
            unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
            selectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
        });*/

        materialCardViewJunior.setOnClickListener(v -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("age_category_id", Constant.JUNIOR_AGE_ID);
            apiRequest.postRequest(PRODUCT_LIST, map, PRODUCT_LIST);
            selectedTopButtonColor(materialCardViewJunior, textViewJunior);
            unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
            //unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
        });

        materialCardViewSenior.setOnClickListener(v -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("age_category_id", Constant.SENIOR_AGE_ID);
            apiRequest.postRequest(PRODUCT_LIST, map, PRODUCT_LIST);
            unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
            selectedTopButtonColor(materialCardViewSenior, textViewSenior);
            //unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
        });


    }

    public void getCourseList() {


        HashMap<String, String> map = new HashMap<>();

        switch (SessionManager.getValue(SessionManager.CHILD_AGE)) {

            case "10":
                selectedTopButtonColor(materialCardViewJunior, textViewJunior);
                unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
                //unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
                map.put("age_category_id", Constant.JUNIOR_AGE_ID);
                break;

            case "13":
                unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
                selectedTopButtonColor(materialCardViewSenior, textViewSenior);
                //unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
                map.put("age_category_id", Constant.SENIOR_AGE_ID);
                break;

            case "14":
                unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
                unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
                //selectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
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
            recyclerViewCoursePopular.setAdapter(new CourseAdapter(response1.getPopular(), requireContext()));
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
        if (tag.equalsIgnoreCase(GET_CART)) {
            CartResponse response1 = (CartResponse) GsonUtil.toObject(response, CartResponse.class);

            if (response1.getData().size() > 0) {
                cartCountView.setVisibility(View.VISIBLE);
                textview_cartCount.setText("" + response1.getData().size());
                imageButtonCart.setOnClickListener(v -> {
                    MethodClass.go_to_next_activity(getActivity(), CartActivity.class);
                });

            } else cartCountView.setVisibility(View.GONE);


        }
    }
}
