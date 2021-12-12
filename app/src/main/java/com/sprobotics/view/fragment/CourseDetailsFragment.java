package com.sprobotics.view.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sprobotics.R;

public class CourseDetailsFragment extends Fragment {
    public CourseDetailsFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_course_details, container, false);
        TextView textView = view.findViewById(R.id.course_details_text);
        textView.setText(Html.fromHtml("<p>Learn to Code (C-Language) with Codey Robot. Fire-fighting Robot, Robot Vacuum Cleaner &amp; 100+ fun Projects. No Prior coding knowledge needed.</p>\n" +
                "<div class=\"feature-title\">Includes:</div>\n" +
                "<ul>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Programmable Robotic Maker Kit</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Lifetime Access with 1:1 Guidance</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\">Upto 100+ Projects, no limitation..</div>\n" +
                "    </li>\n" +
                "    <li>\n" +
                "        <div class=\"feature\" id=\"isPasted\">Certification upon completion</div>\n" +
                "    </li>\n" +
                "</ul>"));
        return view;
    }
}
