package com.sprobotics.view.activity;

import static com.sprobotics.network.util.Constant.ADD_CART;
import static com.sprobotics.network.util.Constant.APPLY_COUPON;
import static com.sprobotics.network.util.Constant.GET_CART;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sprobotics.R;
import com.sprobotics.adapter.CartAdapter;
import com.sprobotics.model.cartrespone.CartResponse;
import com.sprobotics.network.util.Constant;
import com.sprobotics.network.util.GsonUtil;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.util.MethodClass;
import com.sprobotics.util.NetworkCallActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends NetworkCallActivity {


    TextView cart_delivery_pincode;
    RecyclerView recyclerViewCart;


    MaterialButton cart_apply;
    EditText edt_cart_coupon;

    TextView tv_cart_value_of_products, tv_cart_discount, tv_cart_estimated_gst, tv_cart_shipping, tv_cart_total;


    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        adapter = new CartAdapter(new ArrayList<>(), CartActivity.this);
        recyclerViewCart.setAdapter(adapter);
        recyclerViewCart.setFocusable(false);

        cart_delivery_pincode = findViewById(R.id.cart_delivery_pincode);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        cart_apply = findViewById(R.id.cart_apply);
        edt_cart_coupon = findViewById(R.id.edt_cart_coupon);
        tv_cart_value_of_products = findViewById(R.id.tv_cart_value_of_products);
        tv_cart_discount = findViewById(R.id.tv_cart_discount);
        tv_cart_estimated_gst = findViewById(R.id.tv_cart_estimated_gst);
        tv_cart_shipping = findViewById(R.id.tv_cart_shipping);
        tv_cart_total = findViewById(R.id.tv_cart_total);


        cart_delivery_pincode.setText(Constant.PINCODE);

        onClick();

        getCartData();
    }

    private void onClick() {

        cart_apply.setOnClickListener(v -> {
            if (edt_cart_coupon.getText().toString().length() > 0) {
                applyCoupon(edt_cart_coupon.getText().toString());
            }

        });


    }

    public void getCartData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);


    }


    public void updateQuantity() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);

    }

    public void deleteItem() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);

    }


    public void applyCoupon(String code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        apiRequest.postRequest(APPLY_COUPON, map, APPLY_COUPON);


    }


    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);

        if (tag.equalsIgnoreCase(GET_CART)) {
            CartResponse response1 = (CartResponse) GsonUtil.toObject(response, CartResponse.class);
            adapter.addItem(response1.getData());

        }


    }
}