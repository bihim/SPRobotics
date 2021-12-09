package com.sprobotics.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sprobotics.R;
import com.sprobotics.model.cartrespone.DataItem;
import com.sprobotics.view.activity.CourseDetailsActivity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CourseViewHolder> {

    List<DataItem> list;
    Context context;

    public CartAdapter(List<DataItem> list, Context context) {
        this.list = list;
        this.context = context;
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





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuantity, tvCoursePrice,course_name;


        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvCoursePrice = itemView.findViewById(R.id.tvCoursePrice);
            course_name = itemView.findViewById(R.id.course_name);
        }
    }
}
