package com.example.android.homely.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.Data.TourData;
import com.example.android.homely.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyTourActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tourFirebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<TourData> tourDataList;
    private ArrayList<String> tourIDList;
    private ArrayList<String> currTourIDList;
    private RecyclerView recyclerView;
    private MyTourAdapter myTourAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tour);

        recyclerView = findViewById(R.id.tours_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        textView = findViewById(R.id.textNo);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/my_tour");
        tourFirebaseDatabase = firebaseDatabase.getReference("Tour");

        tourDataList = new ArrayList<TourData>();
        tourIDList = new ArrayList<>();
        currTourIDList = new ArrayList<>();

        myTourAdapter = new MyTourAdapter(MyTourActivity.this, tourDataList);
        recyclerView.setAdapter(myTourAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tourDataList.clear();
                tourIDList.clear();
                currTourIDList.clear();
                if (snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        tourIDList.add(dataSnapshot.getValue().toString());
                    }
                    getProperties();
                }

                if (tourIDList.isEmpty()){
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getProperties() {
        tourFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(tourIDList.contains(dataSnapshot.getKey())){
                            tourDataList.add(dataSnapshot.getValue(TourData.class));
                            currTourIDList.add(dataSnapshot.getKey());
                        }
                    }
                    myTourAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}