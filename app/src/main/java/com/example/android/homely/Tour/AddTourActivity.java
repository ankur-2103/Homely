package com.example.android.homely.Tour;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.homely.Data.TourData;
import com.example.android.homely.R;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTourActivity extends AppCompatActivity implements PassDataInterface {

    private int i = 1;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TourGetInfo tourGetInfo;
    private VerifyPhoneNumber verifyEmailPhoneNumber;
    private VirtualTourType virtualTourType;
    private ScheduleTour scheduleTour;
    private Boolean isVerified = false;
    private TourData tourData;
    private Bundle bundle;
    private String propertyID, propertyName, propertyLoc;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);

        tourGetInfo = TourGetInfo.newInstance(AddTourActivity.this);
        verifyEmailPhoneNumber = VerifyPhoneNumber.newInstance(AddTourActivity.this);
        virtualTourType = VirtualTourType.newInstance(AddTourActivity.this);
        scheduleTour = ScheduleTour.newInstance(AddTourActivity.this);

        propertyID = getIntent().getStringExtra("propertyID");
        propertyLoc = getIntent().getStringExtra("propertyLoc");
        propertyName = getIntent().getStringExtra("propertyName");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tour");

        tourData = new TourData(propertyID, user.getUid(), propertyName, propertyLoc,"null","null","null","null","Pending","null","null","null","null","null");

        loadFragment();
    }

    @Override
    public void onDataReceivedTourGetInfo(String tourType, String tourDate, String tourTime) {
        tourData.tourType = tourType;
        tourData.tourDate = tourDate;
        tourData.tourTime = tourTime;
        if (tourType.equals("Virtual")){
            tourData.virtualType = "null";
        }
        Toast.makeText(this, tourType+" "+tourDate+" "+tourTime, Toast.LENGTH_SHORT).show();
        i = isVerified ? tourType.equals("Virtual") ? 3 : 4 : 2;
        loadFragment();
    }

    @Override
    public void onDataReceivedVerifyPhone(Boolean isVerified) {
        if (isVerified){
            this.isVerified = isVerified;
            i = tourData.getTourType().toString().equals("Virtual") ? 3 : 4;
            loadFragment();
        }else{
            finish();
        }
    }

    @Override
    public void onDataReceivedVirtualTourType(String virtualType) {
        tourData.virtualType = virtualType;
        i++;
        loadFragment();
    }

    @Override
    public void onDataReceivedScheduleTour(Boolean isConfirmed) {
        if(isConfirmed){
            submitData();
        }else{
            i=1;
            loadFragment();
        }
    }

    public void loadFragment(){
        if(i==1){
            updateFragment(tourGetInfo, "tourGetInfo");
        }else if(i==2 && !isVerified){
            updateFragment(verifyEmailPhoneNumber, "verifyEmailPhoneNumber");
        }else if (i==3 && tourData.getTourType().toString().equals("Virtual")){
            updateFragment(virtualTourType, "virtualTourType");
        }else if (i==4){
            bundle = new Bundle();
            bundle.putParcelable("tourData", tourData);
            scheduleTour.setArguments(bundle);
            updateFragment(scheduleTour, "scheduleTour");
        }else{
            finish();
        }
    }

    private void submitData() {
        String tourId = getTourID();
        databaseReference.child(tourId).setValue(tourData);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = firebaseDatabase.getReference("User/"+firebaseUser.getUid()+"/my_tour");
        reference.push().setValue(tourId);
        Toast.makeText(this, "Tour Added Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getTourID() {
        DateFormat dateFormat = new SimpleDateFormat("yyddmm");
        Date date = new Date();
        String dt=String.valueOf(dateFormat.format(date));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("HHmmss");
        String tm= String.valueOf(time.format(new Date()));
        String id= dt+tm;
        System.out.println(id);
        return id;
    }

    private void updateFragment(Fragment fragment, String tag) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment, tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(i==1){
            finish();
        }else if(i==3){
            i = 1;
            loadFragment();
        }else if(i==4){
            i = tourData.getTourType().toString().equals("Virtual") ? 3 : 1;
            loadFragment();
        }
    }
}