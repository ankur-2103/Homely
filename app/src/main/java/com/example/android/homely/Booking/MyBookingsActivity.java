package com.example.android.homely.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.homely.Data.DealData;
import com.example.android.homely.Data.TourData;
import com.example.android.homely.Profile.MyTourActivity;
import com.example.android.homely.Profile.MyTourAdapter;
import com.example.android.homely.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MyBookingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dealFirebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<DealData> dealDataList;
    private ArrayList<String> dealIDList;
    private ArrayList<String> currDealIDList;
    private RecyclerView recyclerView;
    private MyBookingsAdapter myBookingsAdapter;
    private TextView textView;
    private ImageView searchImg;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        recyclerView = findViewById(R.id.bookings_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        textView = findViewById(R.id.textNo);
        searchImg = findViewById(R.id.bookings_search_button);
        search = findViewById(R.id.bookings_search_txt);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/my_deals");
        dealFirebaseDatabase = firebaseDatabase.getReference("Deals");

        dealDataList = new ArrayList<DealData>();
        dealIDList = new ArrayList<>();
        currDealIDList = new ArrayList<>();

        myBookingsAdapter = new MyBookingsAdapter(MyBookingsActivity.this, dealDataList);
        recyclerView.setAdapter(myBookingsAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dealDataList.clear();
                dealIDList.clear();
                currDealIDList.clear();
                if (snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        dealIDList.add(dataSnapshot.getValue().toString());
                    }
                    getProperties();
                }

                if (dealIDList.isEmpty()){
                    textView.setVisibility(View.VISIBLE);
                }
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
                myBookingsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getProperties() {
        dealFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(dealIDList.contains(dataSnapshot.getKey())){
                            dealDataList.add(dataSnapshot.getValue(DealData.class));
                            currDealIDList.add(dataSnapshot.getKey());
                            Log.d("bk123", "onDataChange: "+dataSnapshot.getValue());
                        }
                    }
                    Collections.reverse(dealDataList);
                    myBookingsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}