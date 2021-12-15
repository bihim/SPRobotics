package com.sproboticworks.network.util;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;

public class ToastUtils {

    public ToastUtils() {
    }

    private static void a(Activity var0, final int var1, final int var2) {
        if (var0 != null) {
            final Application var3 = var0.getApplication();
            var0.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(var3, var1, var2).show();
                }
            });
        }
    }

    private static void a(Activity var0, final String var1, final int var2) {
        if (var0 != null) {
            if (!TextUtils.isEmpty(var1)) {
                final Application var3 = var0.getApplication();
                var0.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(var3, var1, var2).show();
                    }
                });
            }
        }
    }

    public static void showLong(Activity activity, int resId) {
        a(activity, resId, 1);
    }

    public static void showShort(Activity activity, int resId) {
        a(activity, resId, 0);
    }

    public static void showLong(Activity activity, String message, boolean showSnackbar) {
        if (activity != null && message != null && !TextUtils.isEmpty(message)) {
            if (showSnackbar) {
                View view = activity.findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                a(activity, message, 1);
            }

        }
    }

    public static void showShort(Activity activity, String message, boolean showSnackbar) {
        if (activity != null && message != null && !TextUtils.isEmpty(message)) {
            if (showSnackbar) {
                View view = activity.findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                a(activity, message, 0);
            }

        }
    }

    public static void showLong(Activity activity, String message, Object... args) {
        String var3 = MessageFormat.format(message, args);
        a(activity, var3, 1);
    }

    public static void showShort(Activity activity, String message, Object... args) {
        String var3 = MessageFormat.format(message, args);
        a(activity, var3, 0);
    }

    public static void showLong(Activity activity, int resId, Object... args) {
        if (activity != null) {
            String var3 = activity.getString(resId);
            showLong(activity, var3, args);
        }
    }

    public static void showShort(Activity activity, int resId, Object... args) {
        if (activity != null) {
            String var3 = activity.getString(resId);
            showShort(activity, var3, args);
        }
    }
}

