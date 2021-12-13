package com.sprobotics.view.activity;

import static com.sprobotics.network.util.Constant.ADD_CART;
import static com.sprobotics.network.util.Constant.APPLY_COUPON;
import static com.sprobotics.network.util.Constant.DELETE_ITEM_FROM_CART;
import static com.sprobotics.network.util.Constant.DISCOUNT_COUPON;
import static com.sprobotics.network.util.Constant.GET_CART;
import static com.sprobotics.network.util.Constant.UPDATE_CART_ITEM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.sprobotics.R;
import com.sprobotics.adapter.CartAdapter;
import com.sprobotics.model.cartrespone.CartResponse;
import com.sprobotics.model.couponresponse.CouponResponse;
import com.sprobotics.network.util.Constant;
import com.sprobotics.network.util.GsonUtil;
import com.sprobotics.network.util.ToastUtils;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.util.MethodClass;
import com.sprobotics.util.NetworkCallActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends NetworkCallActivity {


    TextView cart_delivery_pincode, cart_delivery_address;
    RecyclerView recyclerViewCart;


    MaterialButton cart_apply;

    MaterialCardView course_details_buy_now;

    EditText edt_cart_coupon;

    TextView tv_cart_value_of_products, tv_cart_discount, tv_cart_estimated_gst, tv_cart_shipping, tv_cart_total, tv_label_gst;

    ImageButton imageButton;


    private CartAdapter adapter;


    public double totalPrice = 0;
    public String cartId;
    public String coupon_id = "";
    public String coupon_amount = "0";

    double igstCalculation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        adapter = new CartAdapter(new ArrayList<>(), CartActivity.this);


        cart_delivery_pincode = findViewById(R.id.cart_delivery_pincode);
        cart_delivery_address = findViewById(R.id.cart_delivery_address);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        cart_apply = findViewById(R.id.cart_apply);
        edt_cart_coupon = findViewById(R.id.edt_cart_coupon);
        tv_cart_value_of_products = findViewById(R.id.tv_cart_value_of_products);
        tv_cart_discount = findViewById(R.id.tv_cart_discount);
        tv_cart_estimated_gst = findViewById(R.id.tv_cart_estimated_gst);
        tv_cart_shipping = findViewById(R.id.tv_cart_shipping);
        tv_cart_total = findViewById(R.id.tv_cart_total);
        tv_label_gst = findViewById(R.id.tv_label_gst);
        course_details_buy_now = findViewById(R.id.course_details_buy_now);

        imageButton = findViewById(R.id.back_button);

        imageButton.setOnClickListener(v->{
            onBackPressed();
        });


        recyclerViewCart.setAdapter(adapter);
        recyclerViewCart.setFocusable(false);

        adapter = new CartAdapter(new ArrayList<>(), CartActivity.this);
        recyclerViewCart.setAdapter(adapter);
        recyclerViewCart.setFocusable(false);

        cart_delivery_pincode.setText(Constant.PINCODE);
        cart_delivery_address.setText(Constant.ADDRESS);

        onClick();

        getCartData();
    }

    private void onClick() {

        cart_apply.setOnClickListener(v -> {
            if (edt_cart_coupon.getText().toString().length() > 0) {
                applyCoupon(edt_cart_coupon.getText().toString());
            }

        });
        course_details_buy_now.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            Intent intent = new Intent(CartActivity.this, BillingActivity.class);

            bundle.putString("edt_cart_coupon", edt_cart_coupon.getText().toString());
            bundle.putString("tv_cart_value_of_products", tv_cart_value_of_products.getText().toString());
            bundle.putString("tv_cart_discount", tv_cart_discount.getText().toString());
            bundle.putString("tv_cart_estimated_gst", tv_cart_estimated_gst.getText().toString());
            bundle.putString("tv_cart_shipping", tv_cart_shipping.getText().toString());
            bundle.putString("tv_cart_total", tv_cart_total.getText().toString());
            bundle.putString("cart_delivery_pincode", cart_delivery_pincode.getText().toString());
            bundle.putString("cart_delivery_address", cart_delivery_address.getText().toString());
            bundle.putString("cart_id", cartId.toString());
            bundle.putString("coupon_amount", coupon_amount.toString());
            bundle.putString("coupon_id", coupon_id.toString());
            intent.putExtra("get_bundle", bundle);

            startActivity(intent);


        });


    }

    public void getCartData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);


    }


    public void updateQuantity(String cart_item_id, String quantity) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cart_item_id", cart_item_id);
        map.put("qty", quantity);
        apiRequest.postRequest(UPDATE_CART_ITEM, map, UPDATE_CART_ITEM);

    }

    public void deleteItem(String cart_item_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cart_item_id", cart_item_id);
        apiRequest.postRequest(DELETE_ITEM_FROM_CART, map, DELETE_ITEM_FROM_CART);

    }


    public void applyCoupon(String code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        apiRequest.postRequest(APPLY_COUPON, map, APPLY_COUPON);


    }

    public void getDiscountedPrice(String code, String cart_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("cart", cart_id);
        apiRequest.postRequest(DISCOUNT_COUPON, map, DISCOUNT_COUPON);


    }

    public void setTotalPrice() {
        double totalAmount = totalPrice + igstCalculation - (Double.valueOf(tv_cart_discount.getText().toString()));
        String total = String.format("%.2f", totalAmount);
        tv_cart_total.setText(total);
    }


    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);

        if (tag.equalsIgnoreCase(GET_CART)) {
            CartResponse response1 = (CartResponse) GsonUtil.toObject(response, CartResponse.class);

            if (response1.getData().size()>0) {
                cartId = response1.getData1().getCartId();

                totalPrice = Double.parseDouble(response1.getData1().getProductTotalPrice());
                adapter.addItem(response1.getData());

                tv_cart_value_of_products.setText(String.format("%.2f", totalPrice));

                igstCalculation = totalPrice * 18 / 100;
                tv_cart_estimated_gst.setText(igstCalculation + "");


                setTotalPrice();
            }else onBackPressed();


            //  tv_cart_value_of_products.setText(totalPrice+"");

        }


        if (tag.equalsIgnoreCase(APPLY_COUPON)) {
            CouponResponse response1 = (CouponResponse) GsonUtil.toObject(response, CouponResponse.class);

            if (response1.isResponse()) {

                coupon_id = "" + response1.getData().get(0).getCouponId();
                coupon_amount = "" + response1.getData().get(0).getLocalValue();

                getDiscountedPrice(response1.getData().get(0).getCode(), cartId);
            }


            //  tv_cart_value_of_products.setText(totalPrice+"");

        }


        if (tag.equalsIgnoreCase(DISCOUNT_COUPON)) {

            try {
                JSONObject object = new JSONObject(response);

                boolean status = object.getBoolean("response");

                if (status) {
                    tv_cart_discount.setText(object.getJSONObject("data").getString("discount"));
                    setTotalPrice();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            //  tv_cart_value_of_products.setText(totalPrice+"");

        }
        if (tag.equalsIgnoreCase(DELETE_ITEM_FROM_CART)) {

            try {
                JSONObject object = new JSONObject(response);

                boolean status = object.getBoolean("response");

                if (status) {
                    ToastUtils.showLong(CartActivity.this, object.getString("message"));
                    getCartData();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            //  tv_cart_value_of_products.setText(totalPrice+"");

        }
        if (tag.equalsIgnoreCase(UPDATE_CART_ITEM)) {

            try {
                JSONObject object = new JSONObject(response);

                boolean status = object.getBoolean("response");

                if (status) {
                    ToastUtils.showLong(CartActivity.this, object.getString("message"));
                    getCartData();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            //  tv_cart_value_of_products.setText(totalPrice+"");

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}