package com.sproboticworks.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public CourseAdapter(List<DataItem> list, Context context) {
        this.list = list;
        this.context = context;
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

        holder.course_view.setOnClickListener(v -> {

            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("MyClass", item);
            context.startActivity(intent);

        });
        if (position == list.size() - 1) {
            setMargins(holder.materialCardView, 30);
        }

        String url = item.getMobile_app_image();
        Logger.d(item.getName());

     /*   switch (item.getSlug()) {
            case "scratch-programming-online-course":
                holder.imageView.setImageResource(R.drawable.scratch);
                break;

            case "android-kit":
                holder.imageView.setImageResource(R.drawable.android);
                break;

            case "codey-inventor-kit":
                holder.imageView.setImageResource(R.drawable.codey);
                break;

            case "electro-blocks":
                holder.imageView.setImageResource(R.drawable.electro);
                break;

            case "drone-kit":
                holder.imageView.setImageResource(R.drawable.drone);
                break;

        }*/

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
        MaterialButton course_view;
        MaterialCardView materialCardView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewYear = itemView.findViewById(R.id.course_year);
            imageView = itemView.findViewById(R.id.course_image);
            course_view = itemView.findViewById(R.id.course_view);
            course_name = itemView.findViewById(R.id.course_name);
            materialCardView = itemView.findViewById(R.id.course_main);
        }
    }

}
