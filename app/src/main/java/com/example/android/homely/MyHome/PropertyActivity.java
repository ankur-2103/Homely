package com.example.android.homely.MyHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

public class PropertyActivity extends AppCompatActivity {

    private PropertyData propertyData;
    private String propertyID;
    private TextView bname, saddress, bath, bed, area, desc, pTag1, pTag2, price1, price2;
    private ImageView imageView;
    private MaterialButton edit, delete;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference propertyFirebaseDatabase, databaseReference, tourReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        propertyData = (PropertyData) getIntent().getParcelableExtra("propertyData");
        propertyID = propertyData.getPropertyID();

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
        edit = findViewById(R.id.editButton);
        delete = findViewById(R.id.deleteButton);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/my_property");
        propertyFirebaseDatabase = firebaseDatabase.getReference("Property");
        tourReference = firebaseDatabase.getReference("Tour");
        storage = FirebaseStorage.getInstance();

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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PropertyActivity.this, EditProperty.class);
                intent.putExtra("propertyData", propertyData);
                intent.putExtra("propertyID", propertyID);
                startActivity(intent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if(propertyID.equals(dataSnapshot.getValue())){
                                    databaseReference.child(dataSnapshot.getKey()).removeValue();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                propertyFirebaseDatabase.child(propertyID).removeValue();
                storageReference = storage.getReference("Property/"+propertyID+"/propertyPic.jpg");
                storageReference.delete();

                tourReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("propertyID").equals(propertyID)){
                                databaseReference.child(dataSnapshot.getKey()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                finish();
            }
        });
    }
}