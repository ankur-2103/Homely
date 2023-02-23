package com.example.android.homely.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.homely.Data.DealData;
import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.R;
import com.example.android.homely.SendNotification.APIService;
import com.example.android.homely.SendNotification.Client;
import com.example.android.homely.SendNotification.Data;
import com.example.android.homely.SendNotification.MyResponse;
import com.example.android.homely.SendNotification.NotificationSender;
import com.example.android.homely.Tour.AddTourActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookNowActivity extends AppCompatActivity {

    private RadioGroup  dealDateRadioGrp, dealTimeRadioGrp;
    private MaterialButton button;
    private MaterialRadioButton dealDateButton, dealTimeButton;
    private String dealDate, dealTime, propertyID, propertyName, propertyLoc, dealID, adminToken;
    private DealData dealData;
    private PropertyData propertyData;
    private FirebaseUser user;
    private DatabaseReference databaseReference, userRef, reference;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        dealDateRadioGrp = findViewById(R.id.dealDateRadioGrp);
        dealTimeRadioGrp = findViewById(R.id.dealTimeRadioGrp);
        button = findViewById(R.id.submitDealBtn);

        propertyID = getIntent().getStringExtra("propertyID");
        propertyLoc = getIntent().getStringExtra("propertyLoc");
        propertyName = getIntent().getStringExtra("propertyName");

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Deals");
        userRef = FirebaseDatabase.getInstance().getReference("User/"+user.getUid()+"/my_deals");
        reference = FirebaseDatabase.getInstance().getReference("Token/NzlFcIpQ1ra6QEaHS4BNscAGYNN2");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    adminToken =  snapshot.child("token").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dealDateRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                dealDateButton = findViewById(i);
            }
        });

        dealTimeRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                dealTimeButton = findViewById(i);
            }
        });

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar2.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR, 9);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        int a = calendar.getActualMaximum(Calendar.DATE)-calendar.get(Calendar.DATE);
        a += calendar2.getActualMaximum(Calendar.DATE);

        SimpleDateFormat dateformat = new SimpleDateFormat("EEE, MMM dd yyyy");
        SimpleDateFormat timeformate = new SimpleDateFormat("hh:mm aa");

        for(int i=1; i<=a; i++){
            calendar.add(Calendar.DATE,1);
            MaterialRadioButton radioButton = new MaterialRadioButton(BookNowActivity.this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(dateformat.format(calendar.getTime()));
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(10, 10, 10, 10);
            params.weight = 1;
            radioButton.setLayoutParams(params);
            radioButton.setPadding(20, 20, 20, 20);
            radioButton.setBackgroundResource(R.drawable.date_radio_selector);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setTextSize(20);
            radioButton.setGravity(Gravity.CENTER);
            if(i==1){radioButton.setChecked(true);}
            dealDateRadioGrp.addView(radioButton);
        }

        for(int i=1; i<=12; i++){
            if (i!=1){calendar.add(Calendar.HOUR,1);}
            MaterialRadioButton radioButton = new MaterialRadioButton(BookNowActivity.this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(timeformate.format(calendar.getTime()));
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(10, 10, 10, 10);
            params.weight = 1;
            radioButton.setLayoutParams(params);
            radioButton.setPadding(20, 20, 20, 20);
            radioButton.setBackgroundResource(R.drawable.date_radio_selector);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setTextSize(20);
            radioButton.setGravity(Gravity.CENTER);
            if(i==1){radioButton.setChecked(true);}
            dealTimeRadioGrp.addView(radioButton);
        }

        dealDateButton = findViewById(dealDateRadioGrp.getCheckedRadioButtonId());
        dealTimeButton = findViewById(dealTimeRadioGrp.getCheckedRadioButtonId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

    }

    private void submitData() {
        dealID = getDealID();
        dealDate = dealDateButton.getText().toString();
        dealTime = dealTimeButton.getText().toString();
        dealData = new DealData(dealID, dealDate, dealTime, user.getUid(), propertyID, propertyLoc, propertyName, "Pending");
        databaseReference.child(dealID).setValue(dealData);
        userRef.push().setValue(dealID);
        Toast.makeText(this, "Deal Added Successfully", Toast.LENGTH_SHORT).show();
        sendNotification("New Deal", "New deal arrived has for "+dealData.getPropertyName()+"!");
    }

    private String getDealID() {
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

    private void sendNotification(String title, String message) {
        Data data = new Data(title, message);
        NotificationSender notificationSender = new NotificationSender(data, adminToken);
        apiService.sendNotification(notificationSender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.d("respo", "onResponse: "+response.body().success);
                if(response.code()==200 && response.body().success!=1){
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.d("gfg1", "onFailure: "+t);
                Toast.makeText(BookNowActivity.this, "Deal Request Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }
}