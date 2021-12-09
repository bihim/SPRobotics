package com.sprobotics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.google.android.material.button.MaterialButton;
import com.sprobotics.R;
import com.sprobotics.model.courseresponse.DataItem;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    List<com.sprobotics.model.courseresponse.DataItem> list;
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewYear,course_name;
        ImageView imageView;
        MaterialButton materialButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.course_name);
            textViewYear = itemView.findViewById(R.id.course_year);
            imageView = itemView.findViewById(R.id.course_image);
            materialButton = itemView.findViewById(R.id.course_view);
            course_name = itemView.findViewById(R.id.course_name);
        }
    }
}
