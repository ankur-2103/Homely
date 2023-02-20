package com.example.android.homely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.homely.Booking.BookNowActivity;
import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.Tour.AddTourActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class PropertyProfile extends AppCompatActivity {

    private PropertyData propertyData;
    private String propertyID;
    private TextView bname, saddress, bath, bed, area, desc, pTag1, pTag2, price1, price2, uname, uemail, uphone, uinfo;
    private LinearLayout ownerinfo;
    private ImageView imageView;
    private ToggleButton like;
    private MaterialButton tour, deal;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference propertyFirebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_profile);

        propertyData = (PropertyData) getIntent().getParcelableExtra("propertyData");
        propertyID = (String) getIntent().getStringExtra("propertyID");

        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        bname = findViewById(R.id.bname);
        saddress = findViewById(R.id.streetAddress);
        bath = findViewById(R.id.bathroom);
        bed = findViewById(R.id.bedroom);
        area = findViewById(R.id.area);
        desc = findViewById(R.id.desc);
        pTag1 = findViewById(R.id.priceTag1);
        pTag2 = findViewById(R.id.priceTag2);
        price1 = findViewById(R.id.price1);
        price2 = findViewById(R.id.price2);
        imageView = findViewById(R.id.imageView);
        tour = findViewById(R.id.tourButton);
        deal = findViewById(R.id.dealButton);
        like = findViewById(R.id.likeToggle);
        ownerinfo = findViewById(R.id.ownerinfo);
        uname = findViewById(R.id.uname);
        uemail = findViewById(R.id.uemail);
        uphone = findViewById(R.id.uphone);
        uinfo = findViewById(R.id.ownerinfo1);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/favourites");
        propertyFirebaseDatabase = firebaseDatabase.getReference("Property");
        storage = FirebaseStorage.getInstance();

        if(isAdmin){
            ownerinfo.setVisibility(View.VISIBLE);
            uinfo.setVisibility(View.VISIBLE);
            tour.setVisibility(View.GONE);
            deal.setVisibility(View.GONE);
            DatabaseReference reference = firebaseDatabase.getReference("User/"+propertyData.getUserID());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        uname.setText(snapshot.child("fname").getValue().toString());
                        uemail.setText(snapshot.child("email").getValue().toString());
                        uphone.setText(snapshot.child("phone").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildren()!=null){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(dataSnapshot.getValue().toString().equals(propertyID)){
                            like.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (like.isChecked()){
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean b = true;
                            if (snapshot.getChildren()!=null){
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    if(dataSnapshot.getValue().toString().equals(propertyID)){
                                        b = false;
                                    }
                                }
                                if (b){
                                    databaseReference.push().setValue(propertyID);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildren()!=null){
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    if(dataSnapshot.getValue().toString().equals(propertyID)){
                                        databaseReference.child(dataSnapshot.getKey()).removeValue();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        Uri uri = Uri.parse(propertyData.getFuri());
        float areal = Float.parseFloat(propertyData.getArea_length());
        float areaw = Float.parseFloat(propertyData.getArea_width());
        float farea = areal * areaw;
        String sarea = String.valueOf(farea);

        bname.setText(propertyData.getBuilding_name());
        saddress.setText(propertyData.getFaddress());
        bath.setText(propertyData.getBathrooms()+" Rooms");
        bed.setText(propertyData.getBedrooms()+" Rooms");
        area.setText(sarea+" ft");
        desc.setText(propertyData.getDescription());
        if(propertyData.getProperty().equals("Sell")){
            pTag1.setText("Advance Price");
            pTag2.setText("Estmiated Price");
        }
        price1.setText("₹ "+propertyData.getDeposit());
        price2.setText("₹ "+propertyData.getMonthly_rent());
        try {
            Picasso.get().load(uri).into(imageView);
        }catch (Exception e){
            Log.w("pice", "onCreate: "+e.toString() );
        }

        tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Tour = new Intent(PropertyProfile.this, AddTourActivity.class);
                Tour.putExtra("propertyID", propertyID);
                Tour.putExtra("propertyName", propertyData.getBuilding_name());
                Tour.putExtra("propertyLoc", propertyData.getFaddress());
                startActivity(Tour);
            }
        });

        deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dealIntent = new Intent(PropertyProfile.this, BookNowActivity.class);
                dealIntent.putExtra("propertyID", propertyID);
                dealIntent.putExtra("propertyName", propertyData.getBuilding_name());
                dealIntent.putExtra("propertyLoc", propertyData.getFaddress());
                startActivity(dealIntent);
            }
        });
    }
}