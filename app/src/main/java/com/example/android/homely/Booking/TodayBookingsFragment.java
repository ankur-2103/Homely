package com.example.android.homely.Booking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.homely.Data.DealData;
import com.example.android.homely.Data.TourData;
import com.example.android.homely.R;
import com.example.android.homely.Tour.PendingTourAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class TodayBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<DealData> dealDataArrayList;
    private ArrayList<String> dealIdArrayList;
    private PendingTourAdapter pendingDealAdapter;
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("EEE, MMM dd yyyy");
    private SimpleDateFormat timeformate = new SimpleDateFormat("hh:mm aa");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm aa");
    private TextView textView;

    public TodayBookingsFragment() {}

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_bookings, container, false);

       dealDataArrayList = new ArrayList<DealData>();
       dealIdArrayList = new ArrayList<String>();
       firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference("Deals");
       recyclerView = view.findViewById(R.id.tbrecyclerView);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       textView = view.findViewById(R.id.textNo);

       AdminBookingsAdapter adminBookingsAdapter = new AdminBookingsAdapter(getContext(), dealDataArrayList);
       recyclerView.setAdapter(adminBookingsAdapter);

       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.getValue() != null){
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                       if(!dealIdArrayList.contains(dataSnapshot.getKey())){
                           try {
                               calendar2.setTime(simpleDateFormat.parse(dataSnapshot.child("tourDate").getValue().toString()+" "+dataSnapshot.child("tourTime").getValue().toString()));
                               if(calendar.compareTo(calendar2) == -1 && dateformat.format(calendar.getTime()).equals(dataSnapshot.child("tourDate").getValue().toString())){
                                   dealIdArrayList.add(dataSnapshot.getKey());
                                   dealDataArrayList.add(dataSnapshot.getValue(DealData.class));
                               }
                           } catch (Exception e) {
                               Log.d("dateTimeE", "onDataChange: "+e);
                           }
                       } else{
                           try {
                               calendar2.setTime(simpleDateFormat.parse(dataSnapshot.child("tourDate").getValue().toString()+" "+dataSnapshot.child("tourTime").getValue().toString()));
                               if(calendar.compareTo(calendar2) == 1){
                                   int pos = dealIdArrayList.indexOf(dataSnapshot.getKey());
                                   dealIdArrayList.remove(pos);
                                   dealDataArrayList.remove(pos);
                               }
                           } catch (Exception e) {
                               Log.d("dateTimeE", "onDataChange: "+e);
                           }
                       }
                   }
               }else{
                   dealIdArrayList.clear();
                   dealDataArrayList.clear();
               }

               if(dealDataArrayList.isEmpty()){
                   textView.setVisibility(View.VISIBLE);
               }else{
                   textView.setVisibility(View.GONE);
               }

               adminBookingsAdapter.notifyDataSetChanged();

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        return view;
    }
}