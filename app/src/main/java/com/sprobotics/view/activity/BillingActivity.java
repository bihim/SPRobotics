package com.sprobotics.view.activity;

import static com.sprobotics.network.util.Constant.APPLY_COUPON;
import static com.sprobotics.network.util.Constant.PLACE_ORDER;
import static com.sprobotics.network.util.Constant.UPDATE_CART_ITEM;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.sprobotics.R;
import com.sprobotics.model.couponresponse.CouponResponse;
import com.sprobotics.model.orderresponse.OrderResponse;
import com.sprobotics.network.util.Constant;
import com.sprobotics.network.util.GsonUtil;
import com.sprobotics.network.util.ToastUtils;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.util.MethodClass;
import com.sprobotics.util.NetworkCallActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class BillingActivity extends NetworkCallActivity implements PaymentResultWithDataListener {
    private TextView billingName, billingNumber, billingAddress, billingEdit, billingDelete;
    private MaterialCardView materialCardViewPayNow;
    private ImageButton imageButton;

    TextView tv_cart_value_of_products, tv_cart_discount, tv_cart_estimated_gst, tv_cart_shipping, tv_cart_total, tv_label_gst;


    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        bundle = getIntent().getBundleExtra("get_bundle");
        findViewById();
        setButtonCallBacks();
    }

    private void setButtonCallBacks() {
        billingEdit.setOnClickListener(v -> {

        });

        billingDelete.setOnClickListener(v -> {

        });

        materialCardViewPayNow.setOnClickListener(v -> {
            startPayment(tv_cart_total.getText().toString());
        });

        imageButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void findViewById() {
        billingName = findViewById(R.id.billing_name);
        billingNumber = findViewById(R.id.billing_phone);
        billingAddress = findViewById(R.id.billing_address);
        billingEdit = findViewById(R.id.billing_edit);
        billingDelete = findViewById(R.id.billing_delete);


        tv_cart_value_of_products = findViewById(R.id.tv_cart_value_of_products);
        tv_cart_discount = findViewById(R.id.tv_cart_discount);
        tv_cart_estimated_gst = findViewById(R.id.tv_cart_estimated_gst);
        tv_cart_shipping = findViewById(R.id.tv_cart_shipping);
        tv_cart_total = findViewById(R.id.tv_cart_total);
        tv_label_gst = findViewById(R.id.tv_label_gst);


        materialCardViewPayNow = findViewById(R.id.billing_pay_now);
        imageButton = findViewById(R.id.back_button);


        setData();

    }

    private void setData() {
        billingName.setText(SessionManager.getValue(SessionManager.CHILD_NAME));
        billingAddress.setText(Constant.ADDRESS);


        //    bundle.putString("edt_cart_coupon", edt_cart_coupon.getText().toString());
        //  bundle.putString("tv_cart_value_of_products", tv_cart_value_of_products.getText().toString());
        //bundle.putString("tv_cart_discount", tv_cart_discount.getText().toString());
        //  bundle.putString("tv_cart_estimated_gst", tv_cart_estimated_gst.getText().toString());
        //  bundle.putString("tv_cart_shipping", tv_cart_shipping.getText().toString());
        //   bundle.putString("tv_cart_total", tv_cart_total.getText().toString());
        // bundle.putString("cart_delivery_pincode", cart_delivery_pincode.getText().toString());
        // bundle.putString("cart_delivery_address", cart_delivery_address.getText().toString());
        //  bundle.putString("cart_id", cartId.toString());

/*        bundle.putString("coupon_id", coupon_id.toString());
        intent.putExtra("get_bundle", bundle);*/


        tv_cart_value_of_products.setText(bundle.getString("tv_cart_value_of_products"));
        tv_cart_discount.setText(bundle.getString("tv_cart_discount"));
        tv_cart_estimated_gst.setText(bundle.getString("tv_cart_estimated_gst"));
        tv_cart_shipping.setText(bundle.getString("tv_cart_shipping"));
        tv_cart_total.setText(bundle.getString("tv_cart_total"));
        //  cart_delivery_pincode.setText(bundle.getString("cart_delivery_pincode"));


    }


    public void orderPlace() {


        HashMap<String, String> map = new HashMap<>();
        map.put("cart_id", bundle.getString("cart_id"));
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        map.put("coupon_id", bundle.getString("coupon_id"));
        map.put("coupon_amount", bundle.getString("coupon_amount"));
        map.put("total_tax", bundle.getString("tv_cart_estimated_gst"));
        map.put("customer_address", bundle.getString("cart_delivery_address"));
        map.put("customer_postal_code", bundle.getString("cart_delivery_pincode"));
        map.put("customer_state_id", "1");
        map.put("customer_city_id", "1");
        map.put("customer_state", MethodClass.defaultWhenNull(Constant.ADDRESS_DETAILS.getAdminArea(),""));
        map.put("customer_city",MethodClass.defaultWhenNull(Constant.ADDRESS_DETAILS.getLocality(),""));


        apiRequest.postRequest(PLACE_ORDER, map, PLACE_ORDER);

    }


    public void startPayment(String amount) {


        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.icon);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Sensea");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //    options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            // calculate the amount
            Double total = Double.parseDouble(amount);
            total = total * 100;
            options.put("amount", total);

           /* JSONObject preFill = new JSONObject();
            preFill.put("email",SessionManager.getEMail());
            preFill.put("contact",SessionManager.getUserMobile());
            options.put("prefill",preFill);*/
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("paymentgateway", "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        orderPlace();

        /*Bundle bundle = new Bundle();
        Intent intent = new Intent(BillingActivity.this, SuccessPaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putString("tv_cart_total", tv_cart_total.getText().toString());
        intent.putExtra("get_bundle", bundle);
        startActivity(intent);*/


    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);
        if (tag.equalsIgnoreCase(PLACE_ORDER)) {
            OrderResponse response1 = (OrderResponse) GsonUtil.toObject(response, OrderResponse.class);

            if (response1.isResponse()) {

                ToastUtils.showLong(BillingActivity.this, response1.getMessage());

                Bundle bundle = new Bundle();
                Intent intent = new Intent(BillingActivity.this, SuccessPaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putString("tv_cart_total", tv_cart_total.getText().toString());
                intent.putExtra("get_bundle", bundle);
                startActivity(intent);


            }


            //  tv_cart_value_of_products.setText(totalPrice+"");

        }
    }
}