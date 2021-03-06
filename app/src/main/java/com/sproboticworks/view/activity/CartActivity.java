package com.sproboticworks.view.activity;


import static com.sproboticworks.network.util.Constant.APPLY_COUPON;
import static com.sproboticworks.network.util.Constant.DELETE_ITEM_FROM_CART;
import static com.sproboticworks.network.util.Constant.DISCOUNT_COUPON;
import static com.sproboticworks.network.util.Constant.GET_CART;
import static com.sproboticworks.network.util.Constant.SET_GET_ADDRESS;
import static com.sproboticworks.network.util.Constant.UPDATE_CART_ITEM;
import static com.sproboticworks.network.zubaer.Global.API_PLACE_HOLDER;
import static com.sproboticworks.network.zubaer.Global.SHOW_ERROR_TOAST;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;


import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.adapter.CartAdapter;
import com.sproboticworks.model.StateCityModel;
import com.sproboticworks.model.address.AddressResponse;
import com.sproboticworks.model.cartrespone.CartResponse;
import com.sproboticworks.model.couponresponse.CouponResponse;
import com.sproboticworks.network.util.Constant;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.network.util.ToastUtils;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.NetworkCallActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends NetworkCallActivity {


    TextView cart_delivery_pincode, cart_delivery_address;
    RecyclerView recyclerViewCart;


    MaterialButton cart_apply;

    MaterialCardView course_details_buy_now, course_details_enquire_now;

    EditText edt_cart_coupon;

    TextView tv_cart_value_of_products, tv_cart_discount, tv_cart_estimated_gst, tv_cart_shipping, tv_cart_total, tv_label_gst;

    ImageButton imageButton;
    ImageButton cart_pincode_edit;


    private CartAdapter adapter;


    public double totalPrice = 0;
    public String cartId;
    public String coupon_id = "";
    public String coupon_amount = "0";

    double igstCalculation;

    private ArrayList<StateCityModel.Datum> stateArrayList = new ArrayList<>();
    private ArrayList<StateCityModel.Datum> cityArrayList = new ArrayList<>();
    private ArrayList<String> stateArrayListString = new ArrayList<>();
    private ArrayList<String> cityArrayListString = new ArrayList<>();
    private String stateId;
    private String cityId;

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        adapter = new CartAdapter(new ArrayList<>(), CartActivity.this);
        activity = this;
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
        course_details_enquire_now = findViewById(R.id.course_details_enquire_now);

        imageButton = findViewById(R.id.back_button);
        cart_pincode_edit = findViewById(R.id.cart_pincode_edit);

        imageButton.setOnClickListener(v -> {
            onBackPressed();
        });
        course_details_enquire_now.setOnClickListener(v->{
            /*this.startActivity(new Intent(this, MainActivity.class).putExtra("bottomTag", "page_1"));
            this.overridePendingTransition(0, 0);
            finish();*/
            startActivity(new Intent(this, AllCoursesActivity.class));
        });

        cart_pincode_edit.setOnClickListener(v->{
            startActivity(new Intent(this, BillingAddressEditActivity.class));
            finish();
            //this.overridePendingTransition(0, 0);
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
        Logger.d("Customer Id in Cart: "+SessionManager.getLoginResponse().getData().getCustomerId());
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
        map.put("action", "plus");
        apiRequest.postRequest(UPDATE_CART_ITEM, map, UPDATE_CART_ITEM);
    }

    public void updateQuantityMinus(String cart_item_id, String quantity) {
        HashMap<String, String> map = new HashMap<>();
        map.put("cart_item_id", cart_item_id);
        map.put("qty", quantity);
        map.put("action", "minus");
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
        //String total = String.format("%.2f", totalAmount);
        String total = String.format("%.2f", totalPrice);
        tv_cart_total.setText(total);
    }

    private void getStatesIfNoAddressSaved() {
        stateArrayList.clear();
        stateArrayListString.clear();
        Call<StateCityModel> call = API_PLACE_HOLDER.getStates();
        call.enqueue(new Callback<StateCityModel>() {
            @Override
            public void onResponse(Call<StateCityModel> call, Response<StateCityModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        StateCityModel stateCityModel = response.body();
                        stateArrayList = new ArrayList<>(stateCityModel.getData());
                        for (StateCityModel.Datum data : stateArrayList) {
                            stateArrayListString.add(data.getName());
                        }
                        if (stateArrayListString.contains(Constant.STATE)){
                            int index = stateArrayListString.indexOf(Constant.STATE);
                            stateId = stateArrayList.get(index).getId().toString();
                            Constant.STATE_ID = stateId;
                            getCity(stateId);
                            //Toast.makeText(activity, "State ID: "+stateId, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Toast.makeText(activity, "No state found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SHOW_ERROR_TOAST(activity, "Could not get states");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Could not get states");
                }
            }

            @Override
            public void onFailure(Call<StateCityModel> call, Throwable t) {
                SHOW_ERROR_TOAST(activity, "Server error");
                Logger.e(t.getMessage());
            }
        });
    }

    private void getCity(String stateId) {
        cityArrayList.clear();
        cityArrayListString.clear();
        //autoCompleteTextViewCity.setText("");
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Cities");
        progressDialog.show();
        Call<StateCityModel> call = API_PLACE_HOLDER.getCity(stateId);
        call.enqueue(new Callback<StateCityModel>() {
            @Override
            public void onResponse(Call<StateCityModel> call, Response<StateCityModel> response) {
                progressDialog.dismiss();
                String json = new Gson().toJson(response.body());
                Logger.d(json);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        StateCityModel stateCityModel = response.body();
                        cityArrayList = new ArrayList<>(stateCityModel.getData());
                        for (StateCityModel.Datum data : cityArrayList) {
                            cityArrayListString.add(data.getName());
                        }
                        if (cityArrayListString.contains(Constant.DEMOCITY)){
                            int index = cityArrayListString.indexOf(Constant.DEMOCITY);
                            cityId = cityArrayList.get(index).getId().toString();
                            Constant.COUNTRY_ID = cityId;
                            //Toast.makeText(activity, "City ID: "+cityId, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Toast.makeText(activity, "No city id found. City Geo is: "+Constant.CITY, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(activity, "No city id found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        SHOW_ERROR_TOAST(activity, "Could not load city");
                    }
                } else {
                    SHOW_ERROR_TOAST(activity, "Could not load city");
                }
            }

            @Override
            public void onFailure(Call<StateCityModel> call, Throwable t) {
                SHOW_ERROR_TOAST(activity, "Server error");
                progressDialog.dismiss();
                Logger.e(t.getMessage());
            }
        });
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
                    getStatesIfNoAddressSaved();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
                    map.put("address",  Constant.ADDRESS);
                    map.put("city", cityId);
                    map.put("state",  stateId);
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

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }
}