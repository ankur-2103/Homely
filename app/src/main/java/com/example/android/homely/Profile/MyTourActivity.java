package com.example.android.homely.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Collections;

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
    private ImageView searchImg;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tour);

        recyclerView = findViewById(R.id.tours_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        textView = findViewById(R.id.textNo);
        searchImg = findViewById(R.id.tours_search_button);
        search = findViewById(R.id.tours_search_txt);

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

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myTourAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                    Collections.reverse(tourDataList);
                    myTourAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}