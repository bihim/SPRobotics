package com.sprobotics.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sprobotics.view.fragment.CourseDetailsFragment;
import com.sprobotics.view.fragment.CourseLearnFragment;

public class CourseDetailsPageAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public CourseDetailsPageAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CourseDetailsFragment courseDetailsFragment = new CourseDetailsFragment();

                return courseDetailsFragment;
            case 1:
                CourseLearnFragment courseLearnFragment = new CourseLearnFragment();
                return courseLearnFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
