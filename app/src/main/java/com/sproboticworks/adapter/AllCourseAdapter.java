package com.sproboticworks.adapter;

import static com.sproboticworks.network.util.Constant.ADD_CART;
import static com.sproboticworks.network.util.Constant.GET_CART;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.cartrespone.CartResponse;
import com.sproboticworks.model.courseresponse.DataItem;
import com.sproboticworks.network.interfaces.OnCallBackListner;
import com.sproboticworks.network.util.ApiRequest;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.preferences.SessionManager;
import com.sproboticworks.util.MethodClass;
import com.sproboticworks.view.activity.CartActivity;
import com.sproboticworks.view.activity.CourseDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AllCourseAdapter extends RecyclerView.Adapter<AllCourseAdapter.CourseViewHolder> implements OnCallBackListner {

    List<DataItem> list;
    Context context;
    private ApiRequest apiRequest;
    private int selectedPosition;
    Activity activity;

    public AllCourseAdapter(List<DataItem> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        apiRequest = new ApiRequest(context, this);
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
            getCartData();
            selectedPosition = position;
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

    @Override
    public void OnCallBackSuccess(String tag, String response) {

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
