package com.example.android.homely.Tour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.android.homely.R;
import com.google.android.material.tabs.TabLayout;

public class TourActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        TourAdapter tourAdapter = new TourAdapter(getSupportFragmentManager());
        tourAdapter.addFragment(new TodayTourFragment(), "Today");
        tourAdapter.addFragment(new AcceptTourFragment(), "Requested");

        viewPager.setAdapter(tourAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}