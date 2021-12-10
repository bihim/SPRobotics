package com.sprobotics.network.zubaer;

import android.app.Activity;

import es.dmoral.toasty.Toasty;

public class Global {
    public static final ApiPlaceHolder API_PLACE_HOLDER = ApiClient.getClient().create(ApiPlaceHolder.class);

    public static void SHOW_ERROR_TOAST(Activity activity, String text){
        Toasty.error(activity, text, Toasty.LENGTH_SHORT).show();
    }

    public static void SHOW_SUCCESS_TOAST(Activity activity, String text){
        Toasty.error(activity, text, Toasty.LENGTH_SHORT).show();
    }

}
