package com.sproboticworks.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.orhanobut.logger.Logger;
import com.sproboticworks.R;
import com.sproboticworks.model.courseresponse.DataItem;
import com.sproboticworks.view.activity.CourseDetailsActivity;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    List<com.sproboticworks.model.courseresponse.DataItem> list;
    Context context;
    Boolean isSenior;

    public CourseAdapter(List<DataItem> list, Context context, Boolean isSenior) {
        this.list = list;
        this.context = context;
        this.isSenior = isSenior;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        DataItem item = list.get(position);
        holder.course_name.setText(item.getName());

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("MyClass", item);
            intent.putExtra("comingFromCourseAdapter", true);
            context.startActivity(intent);

        });
        if (position == list.size() - 1) {
            setMargins(holder.materialCardView, 30);
        }

        String url = item.getMobile_app_image();
        Logger.d(item.getName());

        if (isSenior)
            holder.textViewYear.setText("10+ years");
        else
            holder.textViewYear.setText("7+ years");

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
