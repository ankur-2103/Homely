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

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
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

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    try {
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        String cityName = addresses.get(0).getAddressLine(0);
                        String stateName = addresses.get(0).getAddressLine(1);
                        String countryName = addresses.get(0).getAddressLine(2);
                        currLoc.setText(addresses.get(0).getLocality()+", "+addresses.get(0).getAdminArea());
                    } catch (IOException e) {
                        Log.w("locate", e.toString());
                    }

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        currLoc = view.findViewById(R.id.currLoc);
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fetchLocation();

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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        mypropIDList.add(dataSnapshot.getValue().toString());
                        Log.d("dr", dataSnapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        propertyFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!mypropIDList.contains(dataSnapshot.getKey()) && !currPropIDList.contains(dataSnapshot.getKey())){
                        PropertyData propertyData = dataSnapshot.getValue(PropertyData.class);
                        Log.d("pr", dataSnapshot.getKey().toString());
                        if(propertyData.getFaddress().toLowerCase().contains(currLoc.getText().toString().toLowerCase())){
                            propertyDataList.add(dataSnapshot.getValue(PropertyData.class));
                            currPropIDList.add(dataSnapshot.getKey());
                        }
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