package com.sproboticworks.adapter;

import static com.sproboticworks.network.util.Constant.ADD_CART;
import static com.sproboticworks.network.util.Constant.EMAIL_LOGIN;
import static com.sproboticworks.network.util.Constant.EMAIL_OTP;
import static com.sproboticworks.network.util.Constant.GET_CART;
import static com.sproboticworks.network.util.Constant.MOBILE_LOGIN;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.cartrespone.CartResponse;
import com.sproboticworks.model.courseresponse.DataItem;
import com.sproboticworks.model.loginresponse.LogInResponse;
import com.sproboticworks.model.mobileotp.PhoneOtpSentResponse;
import com.sproboticworks.network.interfaces.OnCallBackListner;
import com.sproboticworks.network.util.ApiRequest;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.network.util.ToastUtils;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.MethodClass;
import com.sproboticworks.view.activity.CartActivity;
import com.sproboticworks.view.activity.CourseDetailsActivity;
import com.sproboticworks.view.activity.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AllCourseAdapter extends RecyclerView.Adapter<AllCourseAdapter.CourseViewHolder> implements OnCallBackListner {

    private List<DataItem> list;
    private Context context;
    private ApiRequest apiRequest;
    private int selectedPosition;
    private Activity activity;
    private BottomSheetDialog bottomSheetDialogForPhone, bottomSheetDialogForOtp, bottomSheetDialogForEmail;
    private String loginType = "M";
    private ProgressDialog progressDialog;
    private String phone_number, firebase_otp, otpFor = "";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private FirebaseAuth auth;
    private String verificationCode;
    private MaterialButton gotoEmail;
    private MaterialTextView textview_otp_sent_to;
    private String email = "";
    private String OTP = "";

    public AllCourseAdapter(List<DataItem> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        apiRequest = new ApiRequest(context, this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        initLoginBottomSheet();
        setBottomSheet();
        startFirebaseLogin();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_all_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        DataItem item = list.get(position);
        holder.course_name.setText(item.getName());

        /*holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("MyClass", item);
            intent.putExtra("comingFromCourseAdapter", false);
            context.startActivity(intent);

        });*/

        holder.imageView.setOnClickListener(v->{
            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("MyClass", item);
            intent.putExtra("comingFromCourseAdapter", false);
            context.startActivity(intent);
            //Logger.d("Position "+selectedPosition);
        });

        holder.course_view.setOnClickListener(v->{

            if (!SessionManager.isLoggedIn()) {
                bottomSheetDialogForPhone.show();
            } else {
                // go to cart page
                getCartData();
                selectedPosition = position;
                Logger.d("I am here");
            }


        });

        if (position == list.size() - 1) {
            setMargins(holder.materialCardView, 30);
        }

        String url = item.getMobile_app_image();
        Logger.d(item.getName());
        /*if (item.getAgeCategory().get(0).equals("Junior")){
            holder.textViewYear.setText("7+ years");
        }
        else{
            holder.textViewYear.setText("10+ years");
        }*/

        holder.textViewYear.setText(item.getAge_group() + " years");


        Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.sprobotics_recyclerview)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setMargins(View view, int end) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            final float scale = context.getResources().getDisplayMetrics().density;
            // convert the DP into pixel
            int e = (int) (end * scale + 0.5f);

            //p.setMargins(l, t, r, b);
            p.setMarginEnd(e);
            view.requestLayout();
        }
    }

    private void initLoginBottomSheet() {
        bottomSheetDialogForPhone = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);
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

                    if (s.toString().startsWith("6") || s.toString().startsWith("7") || s.toString().startsWith("8") || s.toString().startsWith("9")) {
                        loginType = "M";
                        sentOTPRequest(s.toString());
                    } else {
                        ToastUtils.showLong(activity, "Enter a valid Mobile Number");
                    }
                }
            }
        });
    }

    private void setBottomSheet() {

        bottomSheetDialogForOtp = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);
        bottomSheetDialogForEmail = new BottomSheetDialog(activity, R.style.BottomSheetDialogThemeNoFloating);


        bottomSheetDialogForOtp.setContentView(R.layout.bottomsheet_otp_picker);
        bottomSheetDialogForEmail.setContentView(R.layout.bottomsheet_email_picker);


        gotoEmail = bottomSheetDialogForOtp.findViewById(R.id.gotoEmail);
        MaterialButton gotoPhone = bottomSheetDialogForEmail.findViewById(R.id.gotoPhone);

        MaterialButton gotoEmailAgain = bottomSheetDialogForPhone.findViewById(R.id.login_using_phone);

        gotoEmailAgain.setOnClickListener(v -> {
            bottomSheetDialogForEmail.show();
            bottomSheetDialogForPhone.dismiss();
        });

        if (gotoEmail != null)
            gotoEmail.setOnClickListener(v -> {

                if (loginType.equalsIgnoreCase("M"))
                    bottomSheetDialogForEmail.show();
                else bottomSheetDialogForPhone.show();
                bottomSheetDialogForOtp.dismiss();

            });

        if (gotoPhone != null)
            gotoPhone.setOnClickListener(v -> {
                bottomSheetDialogForEmail.dismiss();
                bottomSheetDialogForPhone.show();
            });


        /*OTP*/
        OtpView otpView = bottomSheetDialogForOtp.findViewById(R.id.otp_view);
        TextView textView = bottomSheetDialogForOtp.findViewById(R.id.otp_text);
        TextView textview_resend_otp = bottomSheetDialogForOtp.findViewById(R.id.textview_resend_otp);
        textview_otp_sent_to = bottomSheetDialogForOtp.findViewById(R.id.textview_otp_sent_to);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                if (loginType == "M")
                    OTP_Verification(otp);
                else {
                    if (OTP.equalsIgnoreCase(otp)) {
                        if (bottomSheetDialogForEmail.isShowing())
                            bottomSheetDialogForEmail.dismiss();
                        bottomSheetDialogForOtp.dismiss();
                        MethodClass.hideKeyboard(activity);
                        MethodClass.showAlertDialog(activity, true, "OTP verified", "OTP verified successfully", false);
                        loginWithEmailOrMobile("E");

                    } else
                        MethodClass.showAlertDialog(activity, true, "Invalid OTP", "Invalid OTP", false);
                }


            }
        });
        MaterialButton materialButtonContinueOtp = bottomSheetDialogForOtp.findViewById(R.id.otp_continue);
        materialButtonContinueOtp.setOnClickListener(v -> {

        });
        textview_resend_otp.setOnClickListener(v -> {
            if (loginType == "M")
                sentOTPRequest(phone_number);
        });
        /*Email*/
        TextInputEditText textInputEditTextEmail = bottomSheetDialogForEmail.findViewById(R.id.bottom_email);
        TextInputEditText textInputEditTextPassword = bottomSheetDialogForEmail.findViewById(R.id.bottom_password);
        MaterialButton materialButtonContinueEmail = bottomSheetDialogForEmail.findViewById(R.id.button_continue);

        materialButtonContinueEmail.setOnClickListener(v -> {
            if (textInputEditTextEmail.getText().toString().length() > 0) {
                loginType = "E";
                requestForEmailOtp(textInputEditTextEmail.getText().toString());
            }
        });
    }

    public void requestForEmailOtp(String email_id) {
        this.email = email_id;
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email_id);
        apiRequest.postRequest(EMAIL_OTP, map, EMAIL_OTP);
    }

    private void startFirebaseLogin() {
        FirebaseApp.initializeApp(activity);
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential phoneAuthCredential) {
                //System.out.println ("====verification complete call  " + phoneAuthCredential.getSmsCode ());
                progressDialog.dismiss();
                if (bottomSheetDialogForPhone.isShowing())
                    bottomSheetDialogForPhone.dismiss();
                if (bottomSheetDialogForOtp.isShowing())
                    bottomSheetDialogForOtp.dismiss();

                loginWithEmailOrMobile("M");
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                setSnackBar(e.getLocalizedMessage(), context.getString(R.string.btn_ok), "failed");
                Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(activity.getApplicationContext(), context.getString(R.string.otp_sent), Toast.LENGTH_SHORT).show();

                if (bottomSheetDialogForPhone.isShowing())
                    bottomSheetDialogForPhone.dismiss();

                if (!bottomSheetDialogForOtp.isShowing())
                    bottomSheetDialogForOtp.show();
                gotoEmail.setText("Proceed with Email ID");


                textview_otp_sent_to.setText("OTP has been sent to +91" + phone_number);
                progressDialog.dismiss();

            }
        };
    }

    public void OTP_Verification(String otptext) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otptext);
        signInWithPhoneAuthCredential(credential, otptext);
    }

    public void loginWithEmailOrMobile(String type) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", SessionManager.getValue(context, SessionManager.CHILD_NAME));
        map.put("child_age", SessionManager.getValue(context, SessionManager.CHILD_AGE));
        if (type.equalsIgnoreCase("M"))
            map.put("mobile", phone_number);
        else map.put("email", email);
        apiRequest.postRequest(type.equalsIgnoreCase("M") ? MOBILE_LOGIN : EMAIL_LOGIN, map, MOBILE_LOGIN);
    }

    public void setSnackBar(String message, String action, final String type) {
        final Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("reset_pass") || type.equals("forgot") || type.equals("register")) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String otptext) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            loginWithEmailOrMobile("M");
                            if (bottomSheetDialogForOtp.isShowing())
                                bottomSheetDialogForOtp.dismiss();
                            if (bottomSheetDialogForPhone.isShowing())
                                bottomSheetDialogForPhone.dismiss();

                        } else {

                            //verification unsuccessful.. display an error message
                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            ToastUtils.showLong(activity, message);

                        }
                    }
                });
    }

    public void sentOTPRequest(String phoneNumber) {
        progressDialog.show();
        phone_number = phoneNumber;


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                activity,        // Activity (for callback binding)
                mCallback);
    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {

        if (tag.equalsIgnoreCase(EMAIL_OTP)) {
            PhoneOtpSentResponse response1 = (PhoneOtpSentResponse) GsonUtil.toObject(response, PhoneOtpSentResponse.class);
            // ToastUtils.showLong(activity, response1.getData().getOtp());
            OTP = response1.getData().getOtp();
            //bottomSheetDialogForPhone.dismiss();
            textview_otp_sent_to.setText("OTP has been sent to " + email);
            gotoEmail.setText("Proceed with Mobile Number");
            bottomSheetDialogForOtp.show();
        }
        if (tag.equalsIgnoreCase(MOBILE_LOGIN)) {
            LogInResponse response1 = (LogInResponse) GsonUtil.toObject(response, LogInResponse.class);
            SessionManager.setValue(context, SessionManager.LOGIN_RESPONSE, GsonUtil.toJsonString(response1));
            SessionManager.setLoggedIn(true);
            ToastUtils.showLong(activity, "Logged in successfully");
        }


        if (tag.equalsIgnoreCase(GET_CART)) {
            CartResponse response1 = (CartResponse) GsonUtil.toObject(response, CartResponse.class);

            if (response1.getData().size() == 0) {

                HashMap<String, String> map = new HashMap<>();
                map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
                map.put("total_local_price", list.get(selectedPosition).getPrice().get(0));
                map.put("billing_address_id", "");
                map.put("delivery_address_id", "");
                map.put("cart_item_id", "");
                map.put("cart_id", "");
                map.put("product_id", "" + list.get(selectedPosition).getProductId());
                map.put("quantity", "1");
                map.put("local_price", list.get(selectedPosition).getPrice().get(0));
                apiRequest.postRequest(ADD_CART, map, ADD_CART);


            } else {

                String courseId = "";

                for (com.sproboticworks.model.cartrespone.DataItem dataItem : response1.getData()) {

                    if (dataItem.getProductId().equalsIgnoreCase("" + list.get(selectedPosition).getProductId())) {
                        courseId = dataItem.getItemId();
                        break;
                    }

                }


                int totalPrice = (int) (Double.parseDouble(response1.getData1().getProductTotalPrice()) + Double.parseDouble(list.get(selectedPosition).getPrice().get(0)));

                if (courseId.equals("")) {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
                    map.put("total_local_price", "" + totalPrice);
                    map.put("billing_address_id", "");
                    map.put("delivery_address_id", "");
                    map.put("cart_item_id", "");
                    map.put("cart_id", "");
                    map.put("product_id", "" + list.get(selectedPosition).getProductId());
                    map.put("quantity", "1");
                    map.put("local_price", list.get(selectedPosition).getPrice().get(0));
                    apiRequest.postRequest(ADD_CART, map, ADD_CART);

                } else {

                    Toast.makeText(context, "You already have this course in your cart", Toast.LENGTH_SHORT).show();
                    MethodClass.go_to_next_activity_with_finish(activity, CartActivity.class);

                }


            }


        }
        if (tag.equalsIgnoreCase(ADD_CART)) {

            try {
                JSONObject object = new JSONObject(response);
                Toast.makeText(context, object.getString("data"), Toast.LENGTH_SHORT).show();
                MethodClass.go_to_next_activity_with_finish(activity, CartActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void OnCallBackError(String tag, String error, int i) {

    }

    public void getCartData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);

    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewYear, course_name;
        ImageView imageView;
        LinearLayout lyt_root;
        MaterialButton course_view;
        MaterialCardView materialCardView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewYear = itemView.findViewById(R.id.course_year);
            imageView = itemView.findViewById(R.id.course_image);
            course_view = itemView.findViewById(R.id.course_view);
            course_name = itemView.findViewById(R.id.course_name);
            materialCardView = itemView.findViewById(R.id.course_main);
            lyt_root = itemView.findViewById(R.id.lyt_root);
        }
    }

}
