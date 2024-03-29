package com.example.android.homely.Booking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.homely.Data.DealData;
import com.example.android.homely.R;
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
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("EEE, MMM dd yyyy");
    private SimpleDateFormat timeformate = new SimpleDateFormat("hh:mm aa");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm aa");
    private TextView textView;
    private ImageView searchImg;
    private EditText search;

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
       searchImg = view.findViewById(R.id.tbsearch_button);
       search = view.findViewById(R.id.tbsearch_txt);

       AdminBookingsAdapter adminBookingsAdapter = new AdminBookingsAdapter(getContext(), dealDataArrayList);
       recyclerView.setAdapter(adminBookingsAdapter);

       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.getValue() != null){
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                       if(!dealIdArrayList.contains(dataSnapshot.getKey())){
                           try {
                               calendar2.setTime(simpleDateFormat.parse(dataSnapshot.child("dealDate").getValue().toString()+" "+dataSnapshot.child("dealTime").getValue().toString()));
                               if(calendar.compareTo(calendar2) == -1 && dateformat.format(calendar.getTime()).equals(dataSnapshot.child("dealDate").getValue().toString())){
                                   dealIdArrayList.add(dataSnapshot.getKey());
                                   dealDataArrayList.add(dataSnapshot.getValue(DealData.class));
                               }
                           } catch (Exception e) {
                               Log.e("err", "Error : "+e);
                           }
                       } else{
                           try {
                               calendar2.setTime(simpleDateFormat.parse(dataSnapshot.child("dealDate").getValue().toString()+" "+dataSnapshot.child("dealTime").getValue().toString()));
                               if(calendar.compareTo(calendar2) == 1){
                                   int pos = dealIdArrayList.indexOf(dataSnapshot.getKey());
                                   dealIdArrayList.remove(pos);
                                   dealDataArrayList.remove(pos);
                               }
                           } catch (Exception e) {
                               Log.e("err", "Error : "+e);
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

       search.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adminBookingsAdapter.getFilter().filter(charSequence);
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });

        return view;
    }
}