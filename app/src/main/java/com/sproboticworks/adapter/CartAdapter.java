package com.sproboticworks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sproboticworks.R;
import com.sproboticworks.model.cartrespone.DataItem;
import com.sproboticworks.view.activity.CartActivity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CourseViewHolder> {

    List<DataItem> list;
    Context context;

    public CartAdapter(List<DataItem> list, Context context) {
        this.list = list;
        this.context = context;
    }


    public void addItem(List<DataItem> items) {
        this.list = items;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        DataItem item = list.get(position);
        holder.course_name.setText(item.getProductName().get(0));
        holder.tvCoursePrice.setText(item.getProductLocalPrice());
        holder.tvQuantity.setText(item.getProductQuantity());

        String url = item.getImage();
        Glide.with(context.getApplicationContext()).load(url).placeholder(context.getResources().getDrawable(R.drawable.sprobotics_recyclerview)).into(holder.cart_image);



        holder.imgDeleteItem.setOnClickListener(v -> {

            ((CartActivity) context).deleteItem(item.getItemId());

        });
        holder.imgPlus.setOnClickListener(v -> {

            int updatedQty= Integer.valueOf(holder.tvQuantity.getText().toString())+1;
            ((CartActivity) context).updateQuantity(item.getItemId(),""+updatedQty);

        });
        holder.imgMinus.setOnClickListener(v -> {

            if (!holder.tvQuantity.getText().toString().equalsIgnoreCase("1")) {
                int updatedQty = Integer.valueOf(holder.tvQuantity.getText().toString()) - 1;
                ((CartActivity) context).updateQuantity(item.getItemId(), "" + updatedQty);
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuantity, tvCoursePrice, course_name;
        ImageButton imgDeleteItem, imgPlus, imgMinus;
        ImageView cart_image;


        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvCoursePrice = itemView.findViewById(R.id.tvCoursePrice);
            course_name = itemView.findViewById(R.id.course_name);

            imgDeleteItem = itemView.findViewById(R.id.imgDeleteItem);
            imgPlus = itemView.findViewById(R.id.imgPlus);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            cart_image = itemView.findViewById(R.id.cart_image);
        }
    }
}
