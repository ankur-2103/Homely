package com.example.android.homely;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddProperty extends AppCompatActivity implements PassDataInterface {

    private int pro = 1;
    private ProgressBar progressBar;
    private TextView textView, textView1;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MaterialButton button_previous, button_next;
    private Fragment currentFragment;
    private add_property_step1 add_property_step1;
    private add_property_step2 add_property_step2;
    private add_property_step3 add_property_step3;
    private add_property_step4 add_property_step4;
    private Geocoder geocoder;
    private List<Address> addressList;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference userDatabaseReference;
    private Uri uri;
    private PropertyData propertyData;
    private String street_address, building_name, city_name, lat, lang, address, state, description, year_built, property, property_type, pincode, area_width, area_length, bedrooms, bathrooms, deposit, monthly_rent, furi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        textView = findViewById(R.id.steps_txt);
        textView1 = findViewById(R.id.txt1);
        button_previous = findViewById(R.id.previous);
        button_next = findViewById(R.id.next);
        progressBar = findViewById(R.id.progressbar);
        geocoder = new Geocoder(this);
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userDatabaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/my_property");
        add_property_step1 = add_property_step1.newInstance(AddProperty.this);
        add_property_step2 = add_property_step2.newInstance(AddProperty.this);
        add_property_step3 = add_property_step3.newInstance(AddProperty.this);
        add_property_step4 = add_property_step4.newInstance(AddProperty.this);

        progressBar.setProgress(pro);
        button_previous.setVisibility(View.GONE);

        button_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pro==1){
                    add_property_step1 fr = (add_property_step1) getSupportFragmentManager().findFragmentByTag("Step1");
                    fr.submitData();
                } else if(pro==2){
                    add_property_step2 fr = (add_property_step2) getSupportFragmentManager().findFragmentByTag("Step2");
                    fr.submitData();
                } else if(pro==3){
                    add_property_step3 fr = (add_property_step3) getSupportFragmentManager().findFragmentByTag("Step3");
                    fr.submitData();
                }else if(pro==4){
                    add_property_step4 fr = (add_property_step4) getSupportFragmentManager().findFragmentByTag("Step4");
                    fr.submitData();
                }
            }
        });

        loadFragment();
    }

    private void loadFragment() {

        if(pro==1){
            textView1.setText("Address");
            updateFragment(add_property_step1, "Step1");
        } else if(pro==2){
            textView1.setText("Facts");
            updateFragment(add_property_step2, "Step2");
        } else if(pro==3){
            textView1.setText("Description");
            updateFragment(add_property_step3, "Step3");
        }else if(pro==4){
            textView1.setText("Photo");
            updateFragment(add_property_step4, "Step4");
        }
    }

    private void updateFragment(Fragment fragment, String tag) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, tag);
        fragmentTransaction.commit();
        progressBar.setProgress(pro);
        textView.setText("0"+pro+" / 04");
    }

    @Override
    public void onDataReceivedStep1(String buildingname, String streetaddress, String cityname, String state, String pincode) {
        this.building_name = buildingname;
        this.street_address = streetaddress;
        this.city_name = cityname;
        this.state = state;
        this.pincode = pincode;
        this.address = buildingname+", "+streetaddress+", "+cityname+", "+state+", "+pincode;
        try {
            addressList = geocoder.getFromLocationName(this.address, 1);
            if (addressList != null){
                this.lat = String.valueOf(addressList.get(0).getLatitude());
                this.lang = String.valueOf(addressList.get(0).getLongitude());
//                Toast.makeText(this, lat+" "+lang, Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){

        }
//        Toast.makeText(this, building_name+" "+streetaddress+" "+city_name+" "+state+" "+pincode, Toast.LENGTH_SHORT).show();
        next();
    }

    @Override
    public void onDataReceivedStep2(String property, String property_type, String area_width, String area_length, String year_built, String bedroom, String bathroom, String deposit, String monthly_rent) {
        this.property = property;
        this.property_type = property_type;
        this.area_width = area_width;
        this.area_length = area_length;
        this.year_built = year_built;
        this.bedrooms = bedroom;
        this.bathrooms = bathroom;
        this.deposit = deposit;
        this.monthly_rent = monthly_rent;
        Toast.makeText(this, property+" "+property_type+" "+area_width+" "+area_length+" "+year_built+" "+bedroom+" "+bathroom+" "+deposit+" "+monthly_rent, Toast.LENGTH_SHORT).show();
        next();
    }

    @Override
    public void onDataReceivedStep3(String desc) {
        this.description = desc;
        Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
        next();
    }

    @Override
    public void onDataReceivedStep4(Uri uri) {
        this.uri = uri;
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        submit();
    }

    private void submit() {
        try {

        String propertyID = getUniqueID();
        databaseReference = firebaseDatabase.getReference("Property/"+propertyID);
        storageReference = storage.getReference("Property/"+propertyID+"/propertyPic.jpg");

        if(uri!=null){

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri1) {
                            propertyData = new PropertyData(street_address, building_name, city_name, lat, lang, state, description, year_built, property, property_type, pincode, area_width, area_length, bedrooms, bathrooms, deposit, monthly_rent, uri1.toString());
                            databaseReference.setValue(propertyData);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProperty.this, "Profile Image Upload Failed!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        userDatabaseReference.push().setValue(propertyID);

        }catch (Exception e){
            Log.e("PE", e.toString());
        }
        finish();
    }

    public String getUniqueID(){
        DateFormat dateFormat = new SimpleDateFormat("yyddmm");
        Date date = new Date();
        String dt=String.valueOf(dateFormat.format(date));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("HHmm");
        String tm= String.valueOf(time.format(new Date()));
        String id= dt+tm;
        System.out.println(id);
        return id;
    }

    private void previous() {
        if(pro-1>0){
            pro--;
            loadFragment();
            if(pro != 4){
                button_next.setText("Next");
            }
            if(pro==1){
                button_previous.setVisibility(View.GONE);
            }
        }
    }

    private void next() {
        if(pro+1<=4){
            pro++;
            loadFragment();
            if(pro==4){
                button_next.setText("Submit");
            }
            if(pro != 1){
                button_previous.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(pro==1){
            finish();
        }else{
            previous();
        }
    }
}