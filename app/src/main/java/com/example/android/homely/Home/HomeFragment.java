package com.example.android.homely.Home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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

import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView currLoc;
    private EditText searchTxt;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference propertyFirebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<PropertyData> propertyDataList;
    private ArrayList<String> mypropIDList;
    private ArrayList<String>currPropIDList;
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private ImageView searchIcon;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.homeRecyclerView);
        searchIcon = view.findViewById(R.id.search_button);
        searchTxt = view.findViewById(R.id.search_txt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/my_property");
        propertyFirebaseDatabase = firebaseDatabase.getReference("Property");

        propertyDataList = new ArrayList<PropertyData>();
        mypropIDList = new ArrayList<>();
        currPropIDList = new ArrayList<>();


        homeAdapter = new HomeAdapter(getContext(), propertyDataList, new HomeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(PropertyData propertyData, int pos) {
                Intent property = new Intent(getContext(), PropertyProfile.class);
                property.putExtra("propertyData", propertyData);
                property.putExtra("propertyID", currPropIDList.get(pos));
                startActivity(property);
                Log.w("onclick", "yes");
            }
        });
        recyclerView.setAdapter(homeAdapter);

        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                homeAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        propertyFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("home123", "onDataChange: "+snapshot.getValue());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.d("home12345", "onDataChange: "+!user.getUid().equals(dataSnapshot.child("userID").getValue().toString()));
                    Log.d("home1234", "onDataChange: "+!currPropIDList.contains(dataSnapshot.getKey()));
                    if(!user.getUid().equals(dataSnapshot.child("userID").getValue().toString()) && !currPropIDList.contains(dataSnapshot.getKey())){
                        PropertyData propertyData = dataSnapshot.getValue(PropertyData.class);
                        Log.d("home123", "onDataChange: "+!user.getUid().equals(dataSnapshot.child("userID").getValue().toString()));
                        propertyDataList.add(dataSnapshot.getValue(PropertyData.class));
                        currPropIDList.add(dataSnapshot.getKey());

                    }
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}