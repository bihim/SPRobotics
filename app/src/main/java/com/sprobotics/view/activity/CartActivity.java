package com.sprobotics.view.activity;

import static com.sprobotics.network.util.Constant.ADD_CART;
import static com.sprobotics.network.util.Constant.GET_CART;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;

public class CartActivity extends NetworkCallActivity {


    TextView cart_delivery_pincode;
    RecyclerView recyclerViewCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cart_delivery_pincode = findViewById(R.id.cart_delivery_pincode);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);


        cart_delivery_pincode.setText(Constant.PINCODE);

        getCartData();
    }

    public void getCartData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("customer_id", SessionManager.getLoginResponse().getData().getCustomerId());
        apiRequest.postRequest(GET_CART, map, GET_CART);


    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);

        if (tag.equalsIgnoreCase(GET_CART)) {
            CartResponse response1 = (CartResponse) GsonUtil.toObject(response, CartResponse.class);
            recyclerViewCart.setAdapter(new CartAdapter(response1.getData(), CartActivity.this));


        }
      /*  if (tag.equalsIgnoreCase(ADD_CART)) {

            try {
                JSONObject object = new JSONObject(response);
                Toast.makeText(CourseDetailsActivity.this, object.getString("data"), Toast.LENGTH_SHORT).show();
                MethodClass.go_to_next_activity(CourseDetailsActivity.this, CartActivity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }*/


    }
}