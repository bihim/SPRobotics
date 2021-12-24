package com.sproboticworks.view.fragment;

import static com.sproboticworks.network.util.Constant.GET_AGE_GROUP;
import static com.sproboticworks.network.util.Constant.GET_CART;
import static com.sproboticworks.network.util.Constant.GET_VIDEO;
import static com.sproboticworks.network.util.Constant.PRODUCT_LIST;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.orhanobut.logger.Logger;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
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
import com.sproboticworks.view.activity.AboutUsActivity;
import com.sproboticworks.view.activity.AllCoursesActivity;
import com.sproboticworks.view.activity.CartActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends NetworkCallFragment implements VideoCarouselAdapter.AddLifecycleCallbackListener {

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
    private MaterialButton materialCardViewLearnMore;
    private VideoCarouselAdapter videoCarouselAdapter;
    private List<String> youtubeIds = new ArrayList<>();
    private MaterialCardView course_main;

    /*Video*/
    private VideoView simpleVideoView;
    private ProgressBar progress;
    private ImageView iv_play, iv_volume;

    private Boolean isSenior = true, isMute = true;

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViewById(view);
        setButtonCallbacks();
        return view;
    }

    private void findViewById(View view) {

        simpleVideoView = view.findViewById(R.id.simpleVideoView);
        progress = view.findViewById(R.id.progress);
        iv_play = view.findViewById(R.id.iv_play);
        iv_volume = view.findViewById(R.id.iv_volume);

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
        String str = SessionManager.getValue(SessionManager.CHILD_NAME);
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        textViewName.setText("Hi, " + cap);

        materialCardViewJunior = view.findViewById(R.id.junior_course_button);
        materialCardViewSenior = view.findViewById(R.id.senior_course_button);
        //materialCardViewSuperSenior = view.findViewById(R.id.super_senior_course_button);
        textViewJunior = view.findViewById(R.id.junior_course_text);
        textViewSenior = view.findViewById(R.id.senior_course_text);
        materialCardViewLearnMore = view.findViewById(R.id.goto_about_us);
        course_main = view.findViewById(R.id.course_main);
        //textViewSuperSenior = view.findViewById(R.id.super_senior_course_text);

        getAgeGroupId();
        setNotifications();
    }


    private void setNotifications(){
        //notiCountView for layout
        //NotiCount for number of noti
        //imageButtonNoti to go to notification
    }

    private void setSliderView(List<String> videosList) {
        videoCarouselAdapter = new VideoCarouselAdapter(getActivity());
        sliderView.setSliderAdapter(videoCarouselAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);


        videoCarouselAdapter.renewItems(videosList);
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

        materialCardViewLearnMore.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), AllCoursesActivity.class));
        });

        course_main.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AboutUsActivity.class));
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
            isSenior = false;
            HashMap<String, String> map = new HashMap<>();
            map.put("age_category_id", Constant.JUNIOR_AGE_ID);
            apiRequest.postRequest(PRODUCT_LIST, map, PRODUCT_LIST);
            selectedTopButtonColor(materialCardViewJunior, textViewJunior);
            unSelectedTopButtonColor(materialCardViewSenior, textViewSenior);
            //unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
        });

        materialCardViewSenior.setOnClickListener(v -> {
            isSenior = true;
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
                isSenior = false;
                map.put("age_category_id", Constant.JUNIOR_AGE_ID);
                break;

            case "13":
                unSelectedTopButtonColor(materialCardViewJunior, textViewJunior);
                selectedTopButtonColor(materialCardViewSenior, textViewSenior);
                //unSelectedTopButtonColor(materialCardViewSuperSenior, textViewSuperSenior);
                isSenior = true;
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
        apiRequest.postRequest(GET_VIDEO, map, GET_VIDEO);


    }


    public void getAgeGroupId() {
        apiRequest.callGetRequest(GET_AGE_GROUP, GET_AGE_GROUP);
    }


    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);
        if (tag.equalsIgnoreCase(PRODUCT_LIST)) {
            CourseListResponse response1 = (CourseListResponse) GsonUtil.toObject(response, CourseListResponse.class);
            recyclerViewCourse.setAdapter(new CourseAdapter(response1.getData(), requireContext(), isSenior));
            recyclerViewCoursePopular.setAdapter(new CourseAdapter(response1.getPopular(), requireContext(), !isSenior));
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
        if (tag.equalsIgnoreCase(GET_VIDEO)) {

            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean status = jsonObject.getBoolean("response");
                if (status) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    List<String> videoUrls = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        videoUrls.add(data.getString(i));
                    }
//                    setSliderView(videoUrls);

                    playVideo(videoUrls.get(4));

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void addLifeCycleCallBack(YouTubePlayerView youTubePlayerView) {
        getLifecycle().addObserver(youTubePlayerView);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        /*videoCarouselAdapter.deleteAll();
        videoCarouselAdapter.notifyDataSetChanged();*/

        Logger.d("Detach Home Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
//        andExoPlayerView.stopPlayer();
    }

    private void playVideo(String url) {


//        andExoPlayerView.setSource(url, new HashMap<>());
//        andExoPlayerView.setShowControllers(true);

//        MediaController mediaController = new MediaController(getActivity());
//        mediaController.setAnchorView(simpleVideoView);
//        simpleVideoView.setMediaController(mediaController);


//        scrollView2.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//
//            @Override
//            public void onScrollChanged() {
//                mediaController.hide();
//            }
//        });

        simpleVideoView.setVideoURI(Uri.parse(url));

        simpleVideoView.setOnPreparedListener(mp -> {
            progress.setVisibility(View.GONE);
            simpleVideoView.start();
            mediaPlayer = mp;
            setVolume(0);
        });

        simpleVideoView.setOnClickListener(view -> {
            if (iv_play.getVisibility() == View.VISIBLE)
                iv_play.setVisibility(View.GONE);
            else
                iv_play.setVisibility(View.VISIBLE);
        });
        iv_volume.setOnClickListener(view -> {
            if(isMute){
//                mediaPlayer.setVolume(50,50);
                setVolume(100);
                iv_volume.setImageResource(R.drawable.ic_unmute);
            }
            else{
                setVolume(0);
//                mediaPlayer.setVolume(0,0);
                iv_volume.setImageResource(R.drawable.ic_mute);
            }

            isMute = !isMute;
        });





        iv_play.setOnClickListener(view -> {
            if (simpleVideoView.isPlaying()) {
                simpleVideoView.pause();
                iv_play.setImageResource(R.drawable.ic_play);
            } else {
                simpleVideoView.start();
                iv_play.setImageResource(R.drawable.ic_pause);
            }
        });




        simpleVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                switch (i) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                        progress.setVisibility(View.GONE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                        progress.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                        progress.setVisibility(View.GONE);
                        return true;
                    }
                }

                return false;
            }
        });

    }
    private void setVolume(int amount) {
        final int max = 100;
        final double numerator = max - amount > 0 ? Math.log(max - amount) : 0;
        final float volume = (float) (1 - (numerator / Math.log(max)));

        this.mediaPlayer.setVolume(volume, volume);
    }

}
