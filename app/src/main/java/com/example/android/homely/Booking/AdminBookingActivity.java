package com.example.android.homely.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homely.Data.DealData;
import com.example.android.homely.Data.TourData;
import com.example.android.homely.R;
import com.example.android.homely.SendNotification.APIService;
import com.example.android.homely.SendNotification.Client;
import com.example.android.homely.SendNotification.Data;
import com.example.android.homely.SendNotification.MyResponse;
import com.example.android.homely.SendNotification.NotificationSender;
import com.example.android.homely.Tour.AcceptTourActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminBookingActivity extends AppCompatActivity {

    private DealData dealData;
    private TextView ptxt, ploc, dateTxt, timeTxt, statusTxt, uname, uemail, uphone, cname, cemail, cphone;
    private MaterialButton confirmButton, cancelButton;
    private TextInputEditText amountTxt;
    private String amount;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, ownerRef, customerRef, propertyRef;
    private DatabaseReference reference;
    private MaterialCardView materialCardView;
    private APIService apiService;
    private String userToken;
    private LinearLayout owner, customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        ptxt = findViewById(R.id.pName);
        ploc = findViewById(R.id.pLoc);
        dateTxt = findViewById(R.id.dateTxt);
        timeTxt = findViewById(R.id.timeTxt);
        statusTxt = findViewById(R.id.statusTxt);
        amountTxt = findViewById(R.id.dealAmount);
        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);
        materialCardView = findViewById(R.id.dealInfoCard);
        uname = findViewById(R.id.uname);
        uemail = findViewById(R.id.uemail);
        uphone = findViewById(R.id.uphone);
        cname = findViewById(R.id.cname);
        cemail = findViewById(R.id.cemail);
        cphone = findViewById(R.id.cphone);
        owner = findViewById(R.id.ownerinfo);
        customer = findViewById(R.id.customerinfo);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        dealData = (DealData) getIntent().getParcelableExtra("dealData");
        reference = firebaseDatabase.getReference("Token/"+dealData.getUserID());
        databaseReference = firebaseDatabase.getReference("Deals");
        ownerRef = firebaseDatabase.getReference("User/"+dealData.getUserID());
        propertyRef = firebaseDatabase.getReference("Property/"+dealData.getPropertyID()+"/userID");

        ownerRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                owner.setVisibility(View.GONE);
            }
        });

        propertyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    customerRef = firebaseDatabase.getReference("User/"+snapshot.getValue());
                    customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!=null){
                                cname.setText(snapshot.child("fname").getValue().toString());
                                cemail.setText(snapshot.child("email").getValue().toString());
                                cphone.setText(snapshot.child("phone").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            customer.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                customer.setVisibility(View.GONE);
            }
        });

        ptxt.setText(dealData.getPropertyName().toString());
        ploc.setText(dealData.getPropertyAddress().toString());
        timeTxt.setText(dealData.getDealTime().toString());
        dateTxt.setText(dealData.getDealDate().toString());
        statusTxt.setText(dealData.getDealStatus().toString());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    userToken =  snapshot.child("token").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = amountTxt.getText().toString();
                if(amount.isEmpty()){
                    amountTxt.setError("Enter Value");
                }else{
                    submit();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cnacel();
            }
        });

    }

    private void cnacel() {
        String title="Deal", message;
        message = "Your deal for "+dealData.getPropertyName()+" has been canceled!";
        dealData.setDealStatus("Cancel");
        databaseReference.child(dealData.getDealID()).setValue(dealData);
        sendNotification(title,message);
    }

    private void sendNotification(String title, String message) {
        Data data = new Data(title, message);
        NotificationSender notificationSender = new NotificationSender(data, userToken);
        apiService.sendNotification(notificationSender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.d("respo", "onResponse: "+response.body().success);
                if(response.code()==200 && response.body().success!=1){
                    Toast.makeText(AdminBookingActivity.this, "Notification send", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.d("gfg1", "onFailure: "+t);
                Toast.makeText(AdminBookingActivity.this, "Notification not send", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }

    private void submit() {
        String title="Deal", message;
        message = "Congratulations! Your deal for "+dealData.getPropertyName()+" has been accepted for "+amount+"₹";
        dealData.setDealStatus("Confirmed");
        dealData.setAmount(amount);
        databaseReference.child(dealData.getDealID()).setValue(dealData);
        sendNotification(title,message);
    }
}