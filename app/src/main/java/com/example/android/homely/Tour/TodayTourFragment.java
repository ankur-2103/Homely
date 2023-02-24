package com.example.android.homely.Tour;

import android.content.Intent;
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

import com.example.android.homely.Data.TourData;
import com.example.android.homely.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TodayTourFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<TourData> tourDataArrayList;
    private ArrayList<String> tourIdArrayList;
    private AdminTourAdapter adminTourAdapter;
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();
    private SimpleDateFormat dateformat = new SimpleDateFormat("EEE, MMM dd yyyy");
    private SimpleDateFormat timeformate = new SimpleDateFormat("hh:mm aa");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm aa");
    private TextView textView;
    private ImageView searchImg;
    private EditText search;

    public TodayTourFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_tour, container, false);

        tourDataArrayList = new ArrayList<TourData>();
        tourIdArrayList = new ArrayList<String>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tour");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        textView = view.findViewById(R.id.textNo);
        searchImg = view.findViewById(R.id.ttsearch_button);
        search = view.findViewById(R.id.ttsearch_txt);

        adminTourAdapter = new AdminTourAdapter(getContext(), tourDataArrayList, new AdminTourAdapter.ItemClickListener() {
            @Override
            public void onItemClick(TourData tourData, int position) {
                Intent tourAccept = new Intent(getContext(), AcceptTourActivity.class);
                tourAccept.putExtra("tourData", tourData);
                startActivity(tourAccept);
            }
        });
        recyclerView.setAdapter(adminTourAdapter);

        databaseReference.orderByChild("status").equalTo("Accepted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(!tourIdArrayList.contains(dataSnapshot.getKey())){
                            try {
                                calendar2.setTime(simpleDateFormat.parse(dataSnapshot.child("tourDate").getValue().toString()+" "+dataSnapshot.child("tourTime").getValue().toString()));
                                if(calendar.compareTo(calendar2) == -1 && dateformat.format(calendar.getTime()).equals(dataSnapshot.child("tourDate").getValue().toString())){
                                    tourIdArrayList.add(dataSnapshot.getKey());
                                    tourDataArrayList.add(dataSnapshot.getValue(TourData.class));
                                }
                            } catch (Exception e) {
                                Log.e("err", "Error : "+e);
                            }
                        } else{
                            try {
                                calendar2.setTime(simpleDateFormat.parse(dataSnapshot.child("tourDate").getValue().toString()+" "+dataSnapshot.child("tourTime").getValue().toString()));
                                if(calendar.compareTo(calendar2) == 1){
                                    int pos = tourIdArrayList.indexOf(dataSnapshot.getKey());
                                    tourIdArrayList.remove(pos);
                                    tourDataArrayList.remove(pos);
                                }
                            } catch (Exception e) {
                                Log.e("err", "Error : "+e);
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

                adminTourAdapter.notifyDataSetChanged();
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
                adminTourAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
}