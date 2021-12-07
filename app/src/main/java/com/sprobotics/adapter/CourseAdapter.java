package com.sprobotics.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sprobotics.R;

public class CourseAdapter {

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewYear;
        ImageView imageView;
        MaterialButton materialButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.course_name);
            textViewYear = itemView.findViewById(R.id.course_year);
            imageView = itemView.findViewById(R.id.course_image);
            materialButton = itemView.findViewById(R.id.course_view);
        }
    }
}
