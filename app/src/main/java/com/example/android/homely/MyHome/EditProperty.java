package com.example.android.homely.MyHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class EditProperty extends AppCompatActivity implements PassDataInterface {

    private int pro = 1;
    private ProgressBar progressBar;
    private TextView textView, textView1;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MaterialButton button_previous, button_next;
    private Fragment currentFragment;
    private com.example.android.homely.MyHome.add_property_step1 add_property_step1;
    private com.example.android.homely.MyHome.add_property_step2 add_property_step2;
    private com.example.android.homely.MyHome.add_property_step3 add_property_step3;
    private com.example.android.homely.MyHome.add_property_step4 add_property_step4;
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
    private Bundle bundle;
    private String propertyID, street_address, building_name, city_name, faddress,lat, lang, state, description, year_built, property, property_type, pincode, area_width, area_length, bedrooms, bathrooms, deposit, monthly_rent, furi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);

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
        add_property_step1 = add_property_step1.newInstance(EditProperty.this);
        add_property_step2 = add_property_step2.newInstance(EditProperty.this);
        add_property_step3 = add_property_step3.newInstance(EditProperty.this);
        add_property_step4 = add_property_step4.newInstance(EditProperty.this);

        propertyData = (PropertyData) getIntent().getParcelableExtra("propertyData");
        propertyID = (String) getIntent().getStringExtra("propertyID");

        bundle = new Bundle();
        bundle.putParcelable("propertyData", propertyData);
        add_property_step1.setArguments(bundle);
        add_property_step2.setArguments(bundle);
        add_property_step3.setArguments(bundle);
        add_property_step4.setArguments(bundle);

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
        propertyData.building_name = buildingname;
        propertyData.street_address = streetaddress;
        propertyData.city_name = cityname;
        propertyData.state = state;
        propertyData.pincode = pincode;
        this.faddress = buildingname+", "+streetaddress+", "+cityname+", "+state+", "+pincode;
        try {
            addressList = geocoder.getFromLocationName(this.faddress, 1);
            if (addressList != null){
                propertyData.lat = String.valueOf(addressList.get(0).getLatitude());
                propertyData.lang = String.valueOf(addressList.get(0).getLongitude());
            }
        }catch (Exception e){

        }
//        Toast.makeText(this, building_name+" "+streetaddress+" "+city_name+" "+state+" "+pincode, Toast.LENGTH_SHORT).show();
        next();
    }

    @Override
    public void onDataReceivedStep2(String property, String property_type, String area_width, String area_length, String year_built, String bedroom, String bathroom, String deposit, String monthly_rent) {
        propertyData.property = property;
        propertyData.property_type = property_type;
        propertyData.area_width = area_width;
        propertyData.area_length = area_length;
        propertyData.year_built = year_built;
        propertyData.bedrooms = bedroom;
        propertyData.bathrooms = bathroom;
        propertyData.deposit = deposit;
        propertyData.monthly_rent = monthly_rent;
        next();
    }

    @Override
    public void onDataReceivedStep3(String desc) {
        propertyData.description = desc;
        Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
        next();
    }

    @Override
    public void onDataReceivedStep4(Uri uri) {
        if(uri != null){
            this.uri = uri;
        }
//        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        submit();
    }

    private void submit() {
        databaseReference = firebaseDatabase.getReference("Property/"+propertyID);
        if(uri != null){
            storageReference = storage.getReference("Property/"+propertyID+"/propertyPic.jpg");
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri != null){
                                propertyData.furi = uri.toString();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProperty.this, "Profile Image Upload Failed!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        databaseReference.setValue(propertyData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(EditProperty.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditProperty.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        finish();
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

    public PropertyData getPropertyData(){
        return this.propertyData;
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