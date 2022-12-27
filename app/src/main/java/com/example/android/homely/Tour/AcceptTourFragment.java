package com.example.android.homely.Tour;

import android.content.Intent;
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

import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.Data.TourData;
import com.example.android.homely.Home.HomeAdapter;
import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AcceptTourFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<TourData> tourDataArrayList;
    private ArrayList<String> tourIdArrayList;
    private PendingTourAdapter pendingTourAdapter;
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("EEE, MMM dd yyyy");
    private SimpleDateFormat timeformate = new SimpleDateFormat("hh:mm aa");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm aa");
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accept_tour, container, false);

        tourDataArrayList = new ArrayList<TourData>();
        tourIdArrayList = new ArrayList<String>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tour");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textView = view.findViewById(R.id.textNo);

        pendingTourAdapter = new PendingTourAdapter(getContext(), tourDataArrayList, new PendingTourAdapter.ItemClickListener() {
            @Override
            public void onItemClick(TourData tourData, int position) {
                Intent tourAccept = new Intent(getContext(), AcceptTourActivity.class);
                tourAccept.putExtra("tourData", tourData);
                tourAccept.putExtra("tourId", tourIdArrayList.get(position));
                startActivity(tourAccept);
            }
        });
        recyclerView.setAdapter(pendingTourAdapter);

        databaseReference.orderByChild("status").equalTo("Pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("data", "onDataChange: "+snapshot);
                if (snapshot.getValue() != null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(!tourIdArrayList.contains(dataSnapshot.getKey())){
                            try {
                                calendar2.setTime(simpleDateFormat.parse(dataSnapshot.child("tourDate").getValue().toString()+" "+dataSnapshot.child("tourTime").getValue().toString()));
                                if(calendar.compareTo(calendar2) == -1){
                                    Log.d("dateTime", "onDataChange: smaller");
                                    tourIdArrayList.add(dataSnapshot.getKey());
                                    tourDataArrayList.add(dataSnapshot.getValue(TourData.class));
                                }
                            } catch (Exception e) {
                                Log.d("dateTimeE", "onDataChange: "+e);
                            }
                        } else{
                            try {
                                calendar2.setTime(simpleDateFormat.parse(dataSnapshot.child("tourDate").getValue().toString()+" "+dataSnapshot.child("tourTime").getValue().toString()));
                                if(calendar.compareTo(calendar2) == 1){
                                    Log.d("dateTime", "onDataChange: greater");
                                    int pos = tourIdArrayList.indexOf(dataSnapshot.getKey());
                                    tourIdArrayList.remove(pos);
                                    tourDataArrayList.remove(pos);
                                }
                            } catch (Exception e) {
                                Log.d("dateTimeE", "onDataChange: "+e);
                            }
                        }
                    }
                }else{
                    tourIdArrayList.clear();
                    tourDataArrayList.clear();
                }

                if(tourDataArrayList.isEmpty()){
                    textView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                }

                pendingTourAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}