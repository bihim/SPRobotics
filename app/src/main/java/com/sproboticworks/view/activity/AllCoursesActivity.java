package com.sproboticworks.view.activity;

import static com.sproboticworks.network.util.Constant.GET_ALL_COURSES;
import static com.sproboticworks.network.util.Constant.PLACE_ORDER;
import static com.sproboticworks.network.util.Constant.PRODUCT_LIST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;

import com.sproboticworks.R;
import com.sproboticworks.adapter.AllCourseAdapter;
import com.sproboticworks.adapter.CourseAdapter;
import com.sproboticworks.model.courseresponse.CourseListResponse;
import com.sproboticworks.network.util.GsonUtil;
import com.sproboticworks.util.NetworkCallActivity;

import java.util.HashMap;

public class AllCoursesActivity extends NetworkCallActivity {

    RecyclerView course_recyclerview;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        course_recyclerview = findViewById(R.id.course_recyclerview);
        imageButton = findViewById(R.id.back_button);

        apiRequest.postRequest(GET_ALL_COURSES, new HashMap<>(), GET_ALL_COURSES);
        imageButton.setOnClickListener(v->{
            onBackPressed();
        });

    }

    @Override
    public void OnCallBackSuccess(String tag, String response) {
        super.OnCallBackSuccess(tag, response);

        if (tag.equalsIgnoreCase(GET_ALL_COURSES)) {
            CourseListResponse response1 = (CourseListResponse) GsonUtil.toObject(response, CourseListResponse.class);
            course_recyclerview.setAdapter(new AllCourseAdapter(response1.getData(), this, this));
        }

    }


    @Override
    public void OnCallBackError(String tag, String error, int i) {
        super.OnCallBackError(tag, error, i);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}