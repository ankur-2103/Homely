package com.example.android.homely.Search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference propertyFirebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayMap<String, PropertyData> list;
    private ArrayList<String>mypropIDList;
    private ArrayList<String>currPropIDList;
    private CardView cardView;
    private TextView bname, bath, bed, area, price, searchTxt;
    private ImageView imageView, searchIcon, filterIcon;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        public void onMapReady(@NonNull GoogleMap googleMap) {
            LatLng latLng;
            if(currentLocation!=null){
                latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
            if(!list.isEmpty()){
                for (ArrayMap.Entry<String, PropertyData> arrayMap: list.entrySet()){
                    latLng = new LatLng(Double.parseDouble(arrayMap.getValue().getLat()), Double.parseDouble(arrayMap.getValue().getLang()));
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(arrayMap.getValue().getBuilding_name())).setTag(arrayMap.getKey());
                }
            }

            googleMap.setOnMarkerClickListener(MapsFragment.this);
        }
    };

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        if(marker.getTitle().equals("Current Location")){
            cardView.setVisibility(View.GONE);
            return false;
        }

        String propertyId = marker.getTag().toString();
        PropertyData propertyData = list.get(propertyId);

        if(cardView.getVisibility()==View.GONE || cardView.getVisibility()==View.INVISIBLE){
            cardView.setVisibility(View.VISIBLE);
        }

        Log.w("faddress", String.valueOf(list.get(propertyId)));

        bname.setText(propertyData.getBuilding_name());
        bath.setText(propertyData.getBathrooms());
        bed.setText(propertyData.getBedrooms());
        area.setText(propertyData.getArea_length()+" x "+propertyData.getArea_width());
        price.setText("₹ "+propertyData.getMonthly_rent());
        Uri uri = Uri.parse(propertyData.getFuri());
        try {
            Picasso.get().load(uri).into(imageView);
        }catch (Exception e){
            Log.e("pic", "onBindViewHolder: "+e.toString());
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent property = new Intent(getContext(), PropertyProfile.class);
                property.putExtra("propertyData", propertyData);
                property.putExtra("propertyID", propertyId);
                startActivity(property);
            }
        });

        return false;
    }

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
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    if (supportMapFragment != null){
                        supportMapFragment.getMapAsync(callback);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        cardView = view.findViewById(R.id.propertyCard);
        searchIcon = view.findViewById(R.id.search_button);
        searchTxt = view.findViewById(R.id.search_txt);
        filterIcon = view.findViewById(R.id.filter_button);
        imageView = view.findViewById(R.id.image);
        bname = view.findViewById(R.id.bname);
        bed = view.findViewById(R.id.bed);
        bath = view.findViewById(R.id.bath);
        area = view.findViewById(R.id.area);
        price = view.findViewById(R.id.price);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/my_property");
        propertyFirebaseDatabase = firebaseDatabase.getReference("Property");

        list = new ArrayMap<String,PropertyData>();
        mypropIDList = new ArrayList<>();
        currPropIDList = new ArrayList<>();

        if (mapFragment != null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fetchLocation();
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        mypropIDList.add(dataSnapshot.getValue().toString());
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
                        Log.w("faddress", dataSnapshot.getValue().toString());
                        PropertyData propertyData = dataSnapshot.getValue(PropertyData.class);
                        list.put(dataSnapshot.getKey(), propertyData);
                        currPropIDList.add(dataSnapshot.getKey());
                    }
                }
                mapFragment.getMapAsync(callback);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIntent();
            }
        });

        searchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIntent();
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIntent();
            }
        });

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIntent();
            }
        });
    }

    private void filterIntent() {
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.show(getActivity().getFragmentManager(), "filterDialog");
    }

    public void searchIntent(){
        Intent search = new Intent(getContext(), SearchActivity.class);
        search.putStringArrayListExtra("propertyIDList", currPropIDList);
        startActivity(search);
    }
}