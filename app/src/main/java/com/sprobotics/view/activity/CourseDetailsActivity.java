package com.sprobotics.view.activity;

import static com.sprobotics.network.util.Constant.ADD_CART;
import static com.sprobotics.network.util.Constant.GET_CART;
import static com.sprobotics.network.util.Constant.MOBILE_LOGIN;
import static com.sprobotics.network.util.Constant.MOBILE_OTP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.logger.Logger;
import com.sprobotics.R;
import com.sprobotics.adapter.CourseDetailsPageAdapter;
import com.sprobotics.model.cartrespone.CartResponse;
import com.sprobotics.model.courseresponse.DataItem;
import com.sprobotics.model.loginresponse.LogInResponse;
import com.sprobotics.network.util.GsonUtil;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.util.MethodClass;
import com.sprobotics.util.NetworkCallActivity;
import com.sprobotics.view.fragment.CourseDetailsFragment;
import com.sprobotics.view.fragment.CourseLearnFragment;
import com.sprobotics.view.fragment.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CourseDetailsActivity extends NetworkCallActivity {
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private ImageButton imageButtonBack;
    private TextView course_details_title, course_details_age, course_details_money;
    private MaterialCardView materialCardViewEnquire, materialCardViewBuy;
    private BottomSheetDialog bottomSheetDialogForPhone, bottomSheetDialogForOtp, bottomSheetDialogForEmail;
    private DataItem courseDetails;
    private String OTP = "";
    private String mobile = "";
    private Activity activity;

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
    }

    private void findViewById() {
        tabLayout = findViewById(R.id.simpleTabLayout);
        frameLayout = findViewById(R.id.simpleFrameLayout);
        imageButtonBack = findViewById(R.id.back_button);
        materialCardViewEnquire = findViewById(R.id.course_details_enquire_now);
        materialCardViewBuy = findViewById(R.id.course_details_buy_now);


        course_details_title = findViewById(R.id.course_details_title);
        course_details_age = findViewById(R.id.course_details_age);
        course_details_money = findViewById(R.id.course_details_money);
        setData();
        onClick();
    }

    private void onClick() {
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

        course_details_age.setText(courseDetails.getAgeCategory().get(0));
        course_details_money.setText(courseDetails.getPrice().get(0));


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

                for (com.sprobotics.model.cartrespone.DataItem dataItem : response1.getData()) {

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


    }
}