package com.sprobotics.util;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.sprobotics.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MethodClass {


    public static void showAlertDialog(Activity activity, boolean isSuccess, String title, String description, boolean isCancelable) {
        LottieAlertDialog dialog = new LottieAlertDialog.Builder(activity, isSuccess ? DialogTypes.TYPE_SUCCESS : DialogTypes.TYPE_ERROR)
                .setTitle(title)
                .setDescription(description)
                .setNoneText("OK")
                .setNoneListener(new ClickListener() {
                    @Override
                    public void onClick(@NonNull LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();


                    }
                }).build();
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
        dialog.show();
    }


    public static void turnOffNavigationGesture(DrawerLayout layout) {
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    public static void go_to_next_activity(Activity activity, Class next_activity) {
        activity.startActivity(new Intent(activity, next_activity));

    }


    public static void go_to_next_activity_clear_top(Activity activity, Class next_activity) {
        Intent intent = new Intent(activity, next_activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);

    }

    public static void go_to_next_activity_clear_task(Activity activity, Class next_activity) {
        Intent intent = new Intent(activity, next_activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }

    public static void showSoftKeyboard(View view, Activity activity) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void hide_keyboard(Activity activity) {
        (activity).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    public static JSONObject Json_rpc_format(HashMap<String, String> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("params", new JSONObject(params));
        main_param.put("jsonrpc", "2.0");
        Log.e("request", new JSONObject(main_param).toString());
        return new JSONObject(main_param);
    }

    public static HashMap<String, Object> Json_rpc_format_hashmap(HashMap<String, String> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("jsonrpc", "2.0");
        main_param.put("params", params);
        Log.e("request", main_param.toString());
        return main_param;
    }

    public static JSONObject Json_rpc_format_obj(HashMap<String, Object> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("params", new JSONObject(params));
        main_param.put("jsonrpc", "2.0");
        Log.e("request", new JSONObject(main_param).toString());
        return new JSONObject(main_param);
    }

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    public static String getRightAngleImage(String photoPath) {

        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree, photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoPath;
    }

    public static String rotateImage(int degree, String imagePath) {

        if (degree <= 0) {
            return imagePath;
        }
        try {
            Bitmap b = BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if (b.getWidth() > b.getHeight()) {
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }


    public static String checkNull(Object data) {
        if (Objects.equals(data, null) || Objects.equals(data, "null") || Objects.equals(data, "") || Objects.equals(data, "Select")) {
            return "";
        } else {
            return String.valueOf(data);
        }
    }

    public static String checkNull_1(Object data) {
        if (Objects.equals(data, null) || Objects.equals(data, "null") || Objects.equals(data, "") || Objects.equals(data, "Select")) {
            return "";
        } else {
            return " $" + data;
        }
    }


    public static void set_bg_color(Activity activity, View view, int color) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(ContextCompat.getColor(activity, color));

        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, color));
        }
    }


    public static String getHTMLText(String data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return String.valueOf(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
        } else {
            return String.valueOf(Html.fromHtml(data));
        }
    }


    public static class StringWithTag {
        public String sting;
        public Object tag;

        public StringWithTag(String sting, Object tagPart) {
            this.sting = sting;
            this.tag = tagPart;
        }

        @Override
        public String toString() {
            return sting;
        }
    }

    public static long get_diff_between_two_dates_in_milisec(String date_str) {
        Log.e("date_str", "_" + date_str);
        Date current_date = Calendar.getInstance().getTime();
        Date from_date = null;
        long current_date_in_mil_sec = 0, from_date_in_mil_sec = 0, diff = 0;


        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));//set Kuwait time zone

        try {
            if (!Objects.equals(date_str, "") && !Objects.equals(date_str, "null") && !Objects.equals(date_str, null)) {
                from_date = inputFormat.parse(date_str);
            } else {
                from_date = current_date;
            }
            from_date_in_mil_sec = from_date.getTime();
            current_date_in_mil_sec = current_date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        diff = current_date_in_mil_sec - from_date_in_mil_sec;
        Log.e("diff", "_" + (diff / 60000));

        return diff;
    }


    public static String get_time_ago(String date_formate, String date_str) {

        Log.e("date_str", "_" + date_str);
        Date current_date = Calendar.getInstance().getTime();
        Date from_date = null;
        long current_date_in_mil_sec = 0, from_date_in_mil_sec = 0, diff = 0;


        String inputPattern = date_formate;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));//set india time zone

        try {
            if (!Objects.equals(date_str, "") && !Objects.equals(date_str, "null") && !Objects.equals(date_str, null)) {
                from_date = inputFormat.parse(date_str);
            } else {
                from_date = current_date;
            }
            from_date_in_mil_sec = from_date.getTime();
            current_date_in_mil_sec = current_date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        diff = current_date_in_mil_sec - from_date_in_mil_sec;

        long inSecTime = diff / 1000;
        long inMinTime = inSecTime / 60;
        String time_ago_str = "0 minute ago";
        try {
            int time_ago_in_min = 0;
            time_ago_in_min = Integer.parseInt(String.valueOf(inMinTime));
            if (time_ago_in_min >= 60) {
                int hour = 0, day = 0, year = 0;
                hour = time_ago_in_min / 60;

                if (hour >= 24) {

                    day = hour / 24;
                    if (day >= 365) {
                        year = day / 365;
                        if (year == 1)
                            time_ago_str = year + " year ago";
                        else
                            time_ago_str = year + " years ago";

                    } else {
                        if (day == 1)
                            time_ago_str = day + " day ago";
                        else
                            time_ago_str = day + " days ago";
                    }

                } else {
                    if (time_ago_in_min == 1)
                        time_ago_str = hour + " hour ago";
                    else
                        time_ago_str = hour + " hours ago";
                }

            } else {
                if (time_ago_in_min == 0 || time_ago_in_min == 1)
                    time_ago_str = time_ago_in_min + " minute ago";
                else
                    time_ago_str = time_ago_in_min + " minutes ago";

            }


        } catch (Exception e) {
            e.printStackTrace();

        }


        return time_ago_str;
    }

    public static String getDate(String inputPattern, String outputPattern, String data) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            Date date = inputFormat.parse(data);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ParseException", e.toString());
        }
        return data;
    }


    public static String getTime(String inputPattern, String data) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            Date date = inputFormat.parse(data);

            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");

            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ParseException", e.toString());
        }
        return data;
    }


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static <T> T defaultWhenNull(@Nullable T object, @NonNull T def) {
        if (object instanceof String) {
            if (((String) object).isEmpty()) {
                return def;
            }
        }
        return (object == null) ? def : object;
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


}