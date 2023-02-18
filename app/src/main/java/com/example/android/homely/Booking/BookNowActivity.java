package com.example.android.homely.Booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.android.homely.Data.DealData;
import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookNowActivity extends AppCompatActivity {

    private RadioGroup  dealDateRadioGrp, dealTimeRadioGrp;
    private MaterialButton button;
    private MaterialRadioButton dealDateButton, dealTimeButton;
    private String dealDate, dealTime, propertyID, propertyName, propertyLoc, dealID;
    private DealData dealData;
    private PropertyData propertyData;
    private FirebaseUser user;
    private DatabaseReference databaseReference, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);

        dealDateRadioGrp = findViewById(R.id.dealDateRadioGrp);
        dealTimeRadioGrp = findViewById(R.id.dealTimeRadioGrp);
        button = findViewById(R.id.submitDealBtn);

        propertyID = getIntent().getStringExtra("propertyID");
        propertyLoc = getIntent().getStringExtra("propertyLoc");
        propertyName = getIntent().getStringExtra("propertyName");

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Deals");
        userRef = FirebaseDatabase.getInstance().getReference("User/"+user.getUid()+"/my_deals");

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
        finish();
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
}