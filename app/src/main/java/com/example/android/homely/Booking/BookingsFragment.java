package com.example.android.homely.Booking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.homely.R;
import com.example.android.homely.Tour.AcceptTourFragment;
import com.example.android.homely.Tour.TodayTourFragment;
import com.example.android.homely.Tour.TourAdapter;
import com.google.android.material.tabs.TabLayout;


public class BookingsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public BookingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        tabLayout = view.findViewById(R.id.bookTabLayout);
        viewPager = view.findViewById(R.id.bookViewPager);

        tabLayout.setupWithViewPager(viewPager);

        BookingAdapter bookingAdapter = new BookingAdapter(getChildFragmentManager());
        bookingAdapter.addFragment(new TodayBookingsFragment(), "Today");
        bookingAdapter.addFragment(new AllBookingsFragment(), "All");
        viewPager.setAdapter(bookingAdapter);

        return view;
    }
}