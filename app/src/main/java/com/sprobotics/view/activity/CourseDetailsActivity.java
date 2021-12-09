package com.sprobotics.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.sprobotics.R;
import com.sprobotics.adapter.CourseDetailsPageAdapter;
import com.sprobotics.model.courseresponse.DataItem;
import com.sprobotics.preferences.SessionManager;
import com.sprobotics.view.fragment.CourseDetailsFragment;
import com.sprobotics.view.fragment.CourseLearnFragment;
import com.sprobotics.view.fragment.HomeFragment;

public class CourseDetailsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private ImageButton imageButtonBack;
    private TextView course_details_title, course_details_age, course_details_money;
    private MaterialCardView materialCardViewEnquire, materialCardViewBuy;

    private DataItem courseDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        courseDetails = (DataItem) getIntent().getSerializableExtra("MyClass");

        findViewById();
        setButtonCallBacks();
        setTabLayout(savedInstanceState);
    }

    private void setButtonCallBacks() {
        imageButtonBack.setOnClickListener(v -> {
            onBackPressed();
        });
        materialCardViewEnquire.setOnClickListener(v -> {

        });
        materialCardViewBuy.setOnClickListener(v -> {

        });
    }

    private void findViewById() {
        tabLayout = findViewById(R.id.simpleTabLayout);
        frameLayout = findViewById(R.id.simpleFrameLayout);
        imageButtonBack = findViewById(R.id.back_button);
        materialCardViewEnquire = findViewById(R.id.course_details_enquire_now);
        materialCardViewBuy = findViewById(R.id.course_details_buy_now);


        course_details_title = findViewById(R.id.course_details_title);
        course_details_age = findViewById(R.id.course_details_age);
        course_details_money = findViewById(R.id.course_details_money);
        setData();
        onClick();
    }

    private void onClick() {
        materialCardViewBuy.setOnClickListener(v -> {

            if (!SessionManager.isLoggedIn()) {
                // appear bottom sheet for login.
            } else {
                // go to cart page

            }

        });


    }

    private void setData() {

        course_details_title.setText(courseDetails.getName());

        course_details_age.setText(courseDetails.getAgeCategory().get(0));
        course_details_money.setText(courseDetails.getPrice().get(0));


    }

    private void setTabLayout(Bundle savedInstanceState) {
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Course Details");
        tabLayout.addTab(firstTab);

        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("What will you learn");
        tabLayout.addTab(secondTab);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new CourseDetailsFragment();
                        break;
                    case 1:
                        fragment = new CourseLearnFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.getTabAt(0).select();
        tabLayout.getTabAt(1).select();
    }
}