package com.sproboticworks.view.activity;

<<<<<<< HEAD:app/src/main/java/com/sprobotics/view/activity/CartActivity.java
import static com.sprobotics.network.util.Constant.ADD_CART;
import static com.sprobotics.network.util.Constant.APPLY_COUPON;
import static com.sprobotics.network.util.Constant.DELETE_ITEM_FROM_CART;
import static com.sprobotics.network.util.Constant.DISCOUNT_COUPON;
import static com.sprobotics.network.util.Constant.GET_CART;
import static com.sprobotics.network.util.Constant.SET_GET_ADDRESS;
import static com.sprobotics.network.util.Constant.UPDATE_CART_ITEM;
=======
import static com.sproboticworks.network.util.Constant.APPLY_COUPON;
import static com.sproboticworks.network.util.Constant.DELETE_ITEM_FROM_CART;
import static com.sproboticworks.network.util.Constant.DISCOUNT_COUPON;
import static com.sproboticworks.network.util.Constant.GET_CART;
import static com.sproboticworks.network.util.Constant.UPDATE_CART_ITEM;
>>>>>>> 124ff37bd4f67e52d3c2ad443521fe7ecfbee989:app/src/main/java/com/sproboticworks/view/activity/CartActivity.java

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
<<<<<<< HEAD:app/src/main/java/com/sprobotics/view/activity/CartActivity.java
import com.sprobotics.R;
import com.sprobotics.adapter.CartAdapter;
import com.sprobotics.model.address.AddressResponse;
import com.sprobotics.model.cartrespone.CartResponse;
import com.sprobotics.model.couponresponse.CouponResponse;
import com.sprobotics.network.util.Constant;
import com.sprobotics.network.util.GsonUtil;
import com.sprobotics.network.util.ToastUtils;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.util.MethodClass;
import com.sprobotics.util.NetworkCallActivity;
=======
import com.sproboticworks.R;
import com.sproboticworks.adapter.CartAdapter;
import com.sproboticworks.model.cartrespone.CartResponse;
import com.sproboticworks.model.couponresponse.CouponResponse;
import com.sproboticworks.network.util.Constant;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.network.util.ToastUtils;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.NetworkCallActivity;
>>>>>>> 124ff37bd4f67e52d3c2ad443521fe7ecfbee989:app/src/main/java/com/sproboticworks/view/activity/CartActivity.java

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

        imageButton.setOnClickListener(v -> {
            onBackPressed();
        });


        recyclerViewCart.setAdapter(adapter);
        recyclerViewCart.setFocusable(false);

        adapter = new CartAdapter(new ArrayList<>(), CartActivity.this);
        recyclerViewCart.setAdapter(adapter);
        recyclerViewCart.setFocusable(false);


        onClick();

        getCartData();
        getAddress();
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

    public void getAddress() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        map.put("address", "");
        map.put("city", "");
        map.put("state", "");
        map.put("postal_code", "");
        map.put("contact_no", "");
        map.put("landmark", "");
        apiRequest.postRequest(SET_GET_ADDRESS, map, "Fetch_ADDRESS");

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

            if (response1.getData().size() > 0) {
                cartId = response1.getData1().getCartId();

                totalPrice = Double.parseDouble(response1.getData1().getProductTotalPrice());
                adapter.addItem(response1.getData());

                tv_cart_value_of_products.setText(String.format("%.2f", totalPrice));

                igstCalculation = totalPrice * 18 / 100;
                tv_cart_estimated_gst.setText(igstCalculation + "");


                setTotalPrice();
            } else onBackPressed();


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
        if (tag.equalsIgnoreCase("Fetch_ADDRESS")) {
            AddressResponse response1 = (AddressResponse) GsonUtil.toObject(response, AddressResponse.class);

            if (response1.isResponse()) {
                // this is the okay.......
                if (response1.getData().size() > 0) {

                    Constant.ADDRESS=response1.getData().get(0).getAddress();
                    Constant.STATE=response1.getData().get(0).getStateName().get(0);
                    Constant.PINCODE=response1.getData().get(0).getPostalCode();
                    Constant.CITY=response1.getData().get(0).getCityName().get(0);



                    Constant.STATE_ID=response1.getData().get(0).getStateId();
                    Constant.COUNTRY_ID=response1.getData().get(0).getCountryId();

                    cart_delivery_pincode.setText(response1.getData().get(0).getPostalCode());
                    cart_delivery_address.setText(
                            Constant.ADDRESS+" "+
                            Constant.STATE+" "+
                            Constant.CITY+" "+
                            Constant.PINCODE);
                } else {

                    cart_delivery_pincode.setText(Constant.PINCODE);
                    cart_delivery_address.setText(Constant.ADDRESS);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
                    map.put("address",  Constant.ADDRESS);
                    map.put("city", Constant.CITY);
                    map.put("state",  Constant.STATE);
                    map.put("postal_code", Constant.PINCODE);
                    map.put("contact_no", SessionManager.getLoginResponse().getData().getCustomerContactNo());
                    map.put("landmark", "");
                    apiRequest.postRequest(SET_GET_ADDRESS, map, "SAVE_ADDRESS");

                }


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