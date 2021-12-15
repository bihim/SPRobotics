package com.sproboticworks.view.activity;

import static com.sproboticworks.network.util.Constant.ADD_CART;
import static com.sproboticworks.network.util.Constant.GET_CART;
import static com.sproboticworks.network.util.Constant.MOBILE_LOGIN;
import static com.sproboticworks.network.util.Constant.MOBILE_OTP;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.CourseDetailsStaticModel;
import com.sproboticworks.model.cartrespone.CartResponse;
import com.sproboticworks.model.courseresponse.DataItem;
import com.sproboticworks.model.loginresponse.LogInResponse;
import com.sproboticworks.model.mobileotp.PhoneOtpSentResponse;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.network.util.ToastUtils;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.MethodClass;
import com.sproboticworks.util.NetworkCallActivity;
import com.sproboticworks.view.fragment.CourseDetailsFragment;
import com.sproboticworks.view.fragment.CourseLearnFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CourseDetailsActivity extends NetworkCallActivity {
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private ImageButton imageButtonBack;
    private TextView course_details_title, course_details_age, course_details_money;
    private MaterialButton materialCardViewEnquire;
    private MaterialButton materialCardViewBuy;
    private BottomSheetDialog bottomSheetDialogForPhone, bottomSheetDialogForOtp, bottomSheetDialogForEmail;
    private DataItem courseDetails;
    private String OTP = "";
    private String mobile = "";
    private Activity activity;
    private TextView textViewCourseDetails;
    private ImageView imageViewCourseDetails;


    /*What will you learn*/
    private MaterialButton firstItemButton, secondItemButton, thirdItemButton, fourthItemButton;
    private ExpandableLayout firstItemLayout, secondItemLayout, thirdItemLayout, fourthItemLayout;
    private TextView firstItemText, secondItemText, thirdItemText, fourthItemText;
    //private ImageView firstItemIcon, secondItemIcon, thirdItemIcon, fourthItemIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        activity = this;
        courseDetails = (DataItem) getIntent().getSerializableExtra("MyClass");
        findViewById();
        setButtonCallBacks();
        setBottomSheet();
        initLoginBottomSheet();
        setTabLayout(savedInstanceState);
    }

    private void setButtonCallBacks() {
        imageButtonBack.setOnClickListener(v -> {
            onBackPressed();
        });
        materialCardViewEnquire.setOnClickListener(v -> {

        });
        /*materialCardViewBuy.setOnClickListener(v -> {

        });*/

        String scratch = getCourseDetails("Learn to Code an online Robot to Dance, solve Mazes and 25+ more fun projects. No Prior Coding Knowledge is required", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        textViewCourseDetails.setText(Html.fromHtml(scratch));
        setStaticCourseDetailsAndWhatWillYouLearn();
    }

    private void setStaticCourseDetailsAndWhatWillYouLearn() {
        String scratch = getCourseDetails("Learn to Code an online Robot to Dance, solve Mazes and 25+ more fun projects. No Prior Coding Knowledge is required", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        int scratchImage = R.drawable.scratch;
        List<String> scratchListTitle = Arrays.asList("Robotic Calculator", "Voice Recognition App", "Vacuum Cleaner Robot", "Maze Solver");
        List<String> scratchList = Arrays.asList("Navigate the Robot on a grid based on the Math operations using Coding!", "Code your Dancing Robot to create some cool Dance Moves!", "Program your Robot to move like a Vacuum cleaner in a room!", "Program the Robot to move through the maze from Start to Finish");

        String android = getCourseDetails("Learn to Code & Build Android Apps like GPS based Apps, Gaming Apps, Sensor & Camera based Apps & 50+ Apps. No Prior coding knowledge needed. ", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 100+ Projects, no limitation..");
        int androidImage = R.drawable.android;
        List<String> androidListTitle = Arrays.asList("Build Your Own Game", "Voice Recognition App", "GPS Locator App", "Pedometer App");
        List<String> androidList = Arrays.asList("Let your child learn to build cool Android game apps like Football!", "Let your child interact with the computer using voice recognition", "Let them acquire the skills to build a Live Location tracker", "A simple application to track the step count while we walk");

        String codey = getCourseDetails("Learn to Code (C-Language) with Codey Robot. Fire-fighting Robot, Robot Vacuum Cleaner & 100+ fun Projects. No Prior coding knowledge needed. ", " Programmable Robotic Maker Kit ", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        int codeyImage = R.drawable.codey;
        List<String> codeyListTitle = Arrays.asList("Cleaning Robot", "Drawing Robot", "Gardening Robot", "Automatic Parking");
        List<String> codeyList = Arrays.asList("Let your child attach a mop to the Robot and code it to clean your house in a perfect algorithm", "Let your child give a pen to the Robot and code it to draw your name or a pattern of choice", "Let your child code the Robot to follow the road/line perfectly and water the garden alongside", "Let the Robot be coded to sense the walls of a garage and park itself neatly");

        String electro = getCourseDetails("Learn & Experiment with sensors, motors, & more. Build Burglar Alarms, Automatic Lighting systems, Security systems & 50+ fun circuits. ", "Modular Electronic Maker Kit", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        int electroImage = R.drawable.electro;
        List<String> electroListTitle = Arrays.asList("Electro-Block", "Electro-Block", "Electro-Block", "Electro-Block");
        List<String> electroList = Arrays.asList("The fundamentals to ensure a solid footing in the tech-world!", "Practical understanding of basic electronics, circuit building and circuit debugging techniques.", "Conceptual understanding of theories related to Voltage, LEDs, Switch mechanisms, Sensor controlled buzzers and more…", "With over 75 real-world related experiments to develop - a truly experiential induction into electronics!");

        String drone = getCourseDetails("Be a part of the revolution of drones, which are becoming commonplace in delivery and agricultural sectors. Visualize and learn drone physics with Scratch, Block-based Visual Programming language. ", "Programmable Drone Kit", "Lifetime Access with 1:1 Guidance", "Certification upon completion", "Upto 50+ Projects, no limitation..");
        int droneImage = R.drawable.drone;
        List<String> droneListTitle = Arrays.asList("LOGICAL THINKING", "DEBUGGING", "PHYSICS", "CODING");
        List<String> droneList = Arrays.asList("Enter the world of Drones with the extensively programmable Tello Drone.", "Conceptual understanding of Drone physics such as forces of flight, flight movement terms such as roll, pitch and yaw, components of a Drone etc. ", "Learn the basics of programming in a playful, interactive way using Scratch - the most popular block-based visual programming language ", "Learn about building logics to program your Drone to serve various applications such as Farming, Security, Transportation, etc. ");


        List<CourseDetailsStaticModel> courseDetailsStaticModels = new ArrayList<>();
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(scratch, scratchImage, scratchList, scratchListTitle));
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(android, androidImage, androidList, androidListTitle));
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(codey, codeyImage, codeyList, codeyListTitle));
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(electro, electroImage, electroList, electroListTitle));
        courseDetailsStaticModels.add(new CourseDetailsStaticModel(drone, droneImage, droneList, droneListTitle));

        switch (courseDetails.getSlug()) {
            case "scratch-programming-online-course":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(0));
                break;

            case "android-kit":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(1));
                break;

            case "codey-inventor-kit":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(2));
                break;

            case "electro-blocks":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(3));
                break;

            case "drone-kit":
                setTextImageAndWhatWillYouLearn(courseDetailsStaticModels.get(4));
                break;

        }
    }

    private void setTextImageAndWhatWillYouLearn(CourseDetailsStaticModel courseDetailsStaticModels) {
        imageViewCourseDetails.setImageResource(courseDetailsStaticModels.getCourseImageId());
        textViewCourseDetails.setText(Html.fromHtml(courseDetailsStaticModels.getCourseDetails()));

        firstItemButton.setText(courseDetailsStaticModels.getWhatWillMyChildLearnTitle().get(0));
        secondItemButton.setText(courseDetailsStaticModels.getWhatWillMyChildLearnTitle().get(1));
        thirdItemButton.setText(courseDetailsStaticModels.getWhatWillMyChildLearnTitle().get(2));
        fourthItemButton.setText(courseDetailsStaticModels.getWhatWillMyChildLearnTitle().get(3));

        firstItemText.setText(courseDetailsStaticModels.getWhatWillMyChildLearn().get(0));
        secondItemText.setText(courseDetailsStaticModels.getWhatWillMyChildLearn().get(1));
        thirdItemText.setText(courseDetailsStaticModels.getWhatWillMyChildLearn().get(2));
        fourthItemText.setText(courseDetailsStaticModels.getWhatWillMyChildLearn().get(3));
    }

    private String getCourseDetails(String title, String... items) {
        String demo = "<p>Learn to Code (C-Language) with Codey Robot. Fire-fighting Robot, Robot Vacuum Cleaner &amp; 100+ fun Projects. No Prior coding knowledge needed.</p>\n" +
                "<div class=\"feature-title\">Includes:</div>\n" +
                "<ul>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Programmable Robotic Maker Kit</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Lifetime Access with 1:1 Guidance</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Upto 100+ Projects, no limitation..</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\" id=\"isPasted\">Certification upon completion</div>\n" +
                "    </li>\n" +
                "</ul>";
        String paraTitle = "<p>" + title + "</p>\n";
        StringBuilder stringBuilder = new StringBuilder();
        for (String itemList : items) {
            stringBuilder.append("    <li>\n");
            stringBuilder.append("        <div class=\"feature\">&nbsp;").append(itemList).append("</div>\n");
            stringBuilder.append("    <li>\n");
        }
        String finalItem = stringBuilder.toString();
        return paraTitle +
                "<div class=\"feature-title\">Includes:</div>\n" +
                "<ul>\n" +
                finalItem +
                "</ul>";
    }

    private void findViewById() {
        tabLayout = findViewById(R.id.simpleTabLayout);
        frameLayout = findViewById(R.id.simpleFrameLayout);
        imageButtonBack = findViewById(R.id.back_button);
        materialCardViewEnquire = findViewById(R.id.course_details_enquire_now);
        materialCardViewBuy = findViewById(R.id.course_details_buy_now);
        textViewCourseDetails = findViewById(R.id.course_details_text);
        imageViewCourseDetails = findViewById(R.id.course_details_image);


        course_details_title = findViewById(R.id.course_details_title);
        course_details_age = findViewById(R.id.course_details_age);
        course_details_money = findViewById(R.id.course_details_money);

        firstItemButton = findViewById(R.id.firstItemButton);
        secondItemButton = findViewById(R.id.secondItemButton);
        thirdItemButton = findViewById(R.id.thirdItemButton);
        fourthItemButton = findViewById(R.id.fourthItemButton);

        firstItemLayout = findViewById(R.id.firstItemLayout);
        secondItemLayout = findViewById(R.id.secondItemLayout);
        thirdItemLayout = findViewById(R.id.thirdItemLayout);
        fourthItemLayout = findViewById(R.id.fourthItemLayout);

        firstItemText = findViewById(R.id.firstItemText);
        secondItemText = findViewById(R.id.secondItemText);
        thirdItemText = findViewById(R.id.thirdItemText);
        fourthItemText = findViewById(R.id.fourthItemText);

        /*firstItemIcon = findViewById(R.id.firstItemIcon);
        secondItemIcon = findViewById(R.id.secondItemIcon);
        thirdItemIcon = findViewById(R.id.thirdItemIcon);
        fourthItemIcon = findViewById(R.id.fourthItemIcon);*/

        setData();
        onClick();
    }

    private void onClick() {

        firstItemButton.setOnClickListener(v -> {
            if (firstItemLayout.isExpanded()) {
                firstItemLayout.collapse();
                firstItemButton.setIcon(getDrawable(R.drawable.ic_round_arrow_forward_ios_24));
            } else {
                firstItemButton.setIcon(getDrawable(R.drawable.ic_down));
                firstItemLayout.expand();
            }
        });

        secondItemButton.setOnClickListener(v -> {
            if (secondItemLayout.isExpanded()) {
                secondItemLayout.collapse();
                secondItemButton.setIcon(getDrawable(R.drawable.ic_round_arrow_forward_ios_24));
            } else {
                secondItemButton.setIcon(getDrawable(R.drawable.ic_down));
                secondItemLayout.expand();
            }
        });

        thirdItemButton.setOnClickListener(v -> {
            if (thirdItemLayout.isExpanded()) {
                thirdItemLayout.collapse();
                thirdItemButton.setIcon(getDrawable(R.drawable.ic_round_arrow_forward_ios_24));
            } else {
                thirdItemButton.setIcon(getDrawable(R.drawable.ic_down));
                thirdItemLayout.expand();
            }
        });

        fourthItemButton.setOnClickListener(v -> {
            if (fourthItemLayout.isExpanded()) {
                fourthItemLayout.collapse();
                fourthItemButton.setIcon(getDrawable(R.drawable.ic_round_arrow_forward_ios_24));
            } else {
                fourthItemButton.setIcon(getDrawable(R.drawable.ic_down));

                fourthItemLayout.expand();
            }
        });


        materialCardViewBuy.setOnClickListener(v -> {

            if (!SessionManager.isLoggedIn()) {
                bottomSheetDialogForPhone.show();
            } else {
                // go to cart page
                getCartData();
                Logger.d("I am here");
            }

        });


    }

    private void setData() {

        course_details_title.setText(courseDetails.getName());
        // course_details_age.setText(courseDetails.getAgeCategory().get(0));
        course_details_money.setText("Rs. " + courseDetails.getPrice().get(0));


        switch (courseDetails.getAgeCategory().get(0)) {
            case "Junior":
                course_details_age.setText("10");
                break;
            case "Senior":
                course_details_age.setText("13");
                break;

            default:
                course_details_age.setText("13+");
        }


    }

    private void setTabLayout(Bundle savedInstanceState) {
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Course Details");
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("What will you learn");
        tabLayout.addTab(secondTab);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new CourseDetailsFragment();
                        break;
                    case 1:
                        fragment = new CourseLearnFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.getTabAt(0).select();
        tabLayout.getTabAt(1).select();
    }

    private void setBottomSheet() {

        bottomSheetDialogForOtp = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForEmail = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);


        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
        bottomSheetDialogForEmail.setContentView(R.layout.bottomsheet_email_picker);


        MaterialButton gotoEmail = bottomSheetDialogForOtp.findViewById(R.id.gotoEmail);
        MaterialButton gotoPhone = bottomSheetDialogForEmail.findViewById(R.id.gotoPhone);


        if (gotoEmail != null)
            gotoEmail.setOnClickListener(v -> {
                bottomSheetDialogForOtp.dismiss();
                bottomSheetDialogForEmail.show();
            });

        if (gotoPhone != null)
            gotoPhone.setOnClickListener(v -> {
                bottomSheetDialogForEmail.dismiss();
                bottomSheetDialogForPhone.show();
            });


        /*OTP*/
        OtpView otpView = bottomSheetDialogForOtp.findViewById(R.id.otp_view);
        TextView textView = bottomSheetDialogForOtp.findViewById(R.id.otp_text);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                if (OTP.equalsIgnoreCase(otp)) {
                    bottomSheetDialogForOtp.dismiss();
                    MethodClass.hideKeyboard(activity);
                    MethodClass.showAlertDialog(activity, true, "OTP verified", "OTP verified successfully", false);
                    loginWithEmailOrMobile();
                } else
                    MethodClass.showAlertDialog(activity, true, "Invalid OTP", "Invalid OTP", false);


            }
        });
        MaterialButton materialButtonContinueOtp = bottomSheetDialogForOtp.findViewById(R.id.otp_continue);
        materialButtonContinueOtp.setOnClickListener(v -> {

        });
        /*Email*/
        TextInputEditText textInputEditTextEmail = bottomSheetDialogForEmail.findViewById(R.id.bottom_email);
        TextInputEditText textInputEditTextPassword = bottomSheetDialogForEmail.findViewById(R.id.bottom_password);
        MaterialButton materialButtonContinueEmail = bottomSheetDialogForEmail.findViewById(R.id.button_continue);
    }


    private void initLoginBottomSheet() {
        bottomSheetDialogForPhone = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForPhone.setContentView(R.layout.bottomsheet_countrycode_picker);

        MaterialButton gotoOtp = bottomSheetDialogForPhone.findViewById(R.id.gotoOtp);
        EditText editText_carrierNumber = bottomSheetDialogForPhone.findViewById(R.id.editText_carrierNumber);

        editText_carrierNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 10) {
                    requestForMobileOTP(s.toString());
                }
            }
        });


    }

    public void loginWithEmailOrMobile() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", SessionManager.getValue(SessionManager.CHILD_NAME));
        map.put("child_age", SessionManager.getValue(SessionManager.CHILD_AGE));
        map.put("mobile", mobile);
        apiRequest.postRequest(MOBILE_LOGIN, map, MOBILE_LOGIN);


    }

    public void requestForMobileOTP(String mobile) {
        this.mobile = mobile;
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        apiRequest.postRequest(MOBILE_OTP, map, MOBILE_OTP);
    }

    public void getCartData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);

    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);

        if (tag.equalsIgnoreCase(GET_CART)) {
            CartResponse response1 = (CartResponse) GsonUtil.toObject(response, CartResponse.class);

            if (response1.getData().size() == 0) {

                HashMap<String, String> map = new HashMap<>();
                map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
                map.put("total_local_price", courseDetails.getPrice().get(0));
                map.put("billing_address_id", "");
                map.put("delivery_address_id", "");
                map.put("cart_item_id", "");
                map.put("cart_id", "");
                map.put("product_id", "" + courseDetails.getProductId());
                map.put("quantity", "1");
                map.put("local_price", courseDetails.getPrice().get(0));
                apiRequest.postRequest(ADD_CART, map, ADD_CART);


            } else {

                String courseId = "";

                for (com.sproboticworks.model.cartrespone.DataItem dataItem : response1.getData()) {

                    if (dataItem.getProductId().equalsIgnoreCase("" + courseDetails.getProductId())) {
                        courseId = dataItem.getItemId();
                        break;
                    }

                }


                int totalPrice = (int) (Double.parseDouble(response1.getData1().getProductTotalPrice()) + Double.parseDouble(courseDetails.getPrice().get(0)));

                if (courseId.equals("")) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
                    map.put("total_local_price", "" + totalPrice);
                    map.put("billing_address_id", "");
                    map.put("delivery_address_id", "");
                    map.put("cart_item_id", "");
                    map.put("cart_id", "");
                    map.put("product_id", "" + courseDetails.getProductId());
                    map.put("quantity", "1");
                    map.put("local_price", courseDetails.getPrice().get(0));
                    apiRequest.postRequest(ADD_CART, map, ADD_CART);

                } else {

                    Toast.makeText(CourseDetailsActivity.this, "You already have this course in your cart", Toast.LENGTH_SHORT).show();
                    MethodClass.go_to_next_activity(CourseDetailsActivity.this, CartActivity.class);

                }


            }


        }
        if (tag.equalsIgnoreCase(ADD_CART)) {

            try {
                JSONObject object = new JSONObject(response);
                Toast.makeText(CourseDetailsActivity.this, object.getString("data"), Toast.LENGTH_SHORT).show();
                MethodClass.go_to_next_activity(CourseDetailsActivity.this, CartActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        if (tag.equalsIgnoreCase(MOBILE_OTP)) {
            PhoneOtpSentResponse response1 = (PhoneOtpSentResponse) GsonUtil.toObject(response, PhoneOtpSentResponse.class);
            ToastUtils.showLong(activity, response1.getData().getOtp());
            OTP = response1.getData().getOtp();
            bottomSheetDialogForPhone.dismiss();
            bottomSheetDialogForOtp.show();
        }
        if (tag.equalsIgnoreCase(MOBILE_LOGIN)) {
            LogInResponse response1 = (LogInResponse) GsonUtil.toObject(response, LogInResponse.class);
            SessionManager.setValue(SessionManager.LOGIN_RESPONSE, GsonUtil.toJsonString(response1));
            SessionManager.setLoggedIn(true);
        }


    }
}