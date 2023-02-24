package com.example.android.homely.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference propertyFirebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<PropertyData> propertyDataList;
    private ArrayList<String> favoritesIDList;
    private ArrayList<String>currPropIDList;
    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favorites_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        textView = findViewById(R.id.textNo);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/favourites");
        propertyFirebaseDatabase = firebaseDatabase.getReference("Property");

        propertyDataList = new ArrayList<PropertyData>();
        favoritesIDList = new ArrayList<>();
        currPropIDList = new ArrayList<>();

        favoritesAdapter = new FavoritesAdapter(Favorites.this, propertyDataList, new FavoritesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(PropertyData propertyData, int position) {
                Intent property = new Intent(Favorites.this, PropertyProfile.class);
                property.putExtra("propertyData", propertyData);
                startActivity(property);
            }
        });
        recyclerView.setAdapter(favoritesAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoritesIDList.clear();
                propertyDataList.clear();
                currPropIDList.clear();
                if(snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        favoritesIDList.add(dataSnapshot.getValue().toString());
                    }
                    getProperties();
                }
                if(favoritesIDList.isEmpty()){
                    textView.setVisibility(View.VISIBLE);
                    favoritesAdapter.notifyDataSetChanged();
                }else{
                    textView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getProperties() {
        propertyFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(favoritesIDList.contains(dataSnapshot.getKey())){
                            propertyDataList.add(dataSnapshot.getValue(PropertyData.class));
                            currPropIDList.add(dataSnapshot.getKey());
                        }
                    }
                    favoritesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}