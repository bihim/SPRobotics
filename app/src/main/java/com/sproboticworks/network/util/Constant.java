package com.sproboticworks.network.util;

import android.location.Address;

public class Constant {

    public static final String MAIN_URL = "https://api.sproboticworks.com/mobile/api/";
    public static final String TEST_URL = "https://spr.vertitect.in/api/";
    public static final String BASE_URL=MAIN_URL;
    public static final String MOBILE_OTP=BASE_URL+"mobile-otp";
    public static final String EMAIL_OTP=BASE_URL+"email-otp";
    public static final String MOBILE_LOGIN=BASE_URL+"login-mobile";
    public static final String EMAIL_LOGIN=BASE_URL+"login-email";
    public static final String GET_AGE_GROUP=BASE_URL+"age-category";
    public static final String PRODUCT_LIST=BASE_URL+"product";
    public static final String GET_CART=BASE_URL+"get-cart";
    public static final String ADD_CART=BASE_URL+"cart";
    public static final String APPLY_COUPON=BASE_URL+"coupon";
    public static final String DISCOUNT_COUPON=BASE_URL+"coupon-deduction";
    public static final String DELETE_ITEM_FROM_CART=BASE_URL+"delete-cart";
    public static final String UPDATE_CART_ITEM=BASE_URL+"cart-update";
    public static final String PLACE_ORDER=BASE_URL+"order-place";
    public static final String SET_GET_ADDRESS=BASE_URL+"customer-address-save";
    public static final String GET_ALL_COURSES= BASE_URL+"all-course";
    public static final String GET_VIDEO=BASE_URL+"dashboard-video";
    public static final String profileUpdate = "profile-update";
    public static final String customerAddressSave = "customer-address-save";
    public static final String state = "state";
    public static final String city = "city";
    public static final String orderHistory = "order-history";
    public static final String allCourse = "all-course";
    public static final String enquiry = "enquiry";
    public static final String notification = "enquiry"; //Notifications
    public static final String mobileChecking = "mobile-checking";
    public static final String emailChecking = "email-checking";



    /*KEY ID*/
    //public static final String RAZORPAY_KEY_ID = "rzp_live_zeCnYRxnAYtxmu"; //Live
    public static final String RAZORPAY_KEY_ID = "rzp_test_OuMQ2SQXd10GX7"; //Test
    
    public static String JUNIOR_AGE_ID="";
    public static String SENIOR_AGE_ID="";
    public static String SUPER_SENIOR_AGE_ID="";
    public static String PINCODE="";
    public static String ADDRESS="";

    public static Address ADDRESS_DETAILS;

    public static String LOCATION="";
    public static String STATE="";
    public static String CITY="";
    public static String STATE_ID="1";
    public static String COUNTRY_ID="1";
    public static String DEMOCITY="";
    //public static String PINCODE="";

}
