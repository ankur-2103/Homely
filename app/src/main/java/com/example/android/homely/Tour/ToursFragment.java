package com.example.android.homely.Tour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.homely.R;
import com.google.android.material.tabs.TabLayout;

public class ToursFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public ToursFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tours, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        TourAdapter tourAdapter = new TourAdapter(getChildFragmentManager());
        tourAdapter.addFragment(new TodayTourFragment(), "Today");
        tourAdapter.addFragment(new AcceptTourFragment(), "Requested");
        viewPager.setAdapter(tourAdapter);

        return view;
    }
}