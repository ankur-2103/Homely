package com.example.android.homely.Tour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.Data.TourData;
import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
import com.example.android.homely.SendNotification.APIService;
import com.example.android.homely.SendNotification.Client;
import com.example.android.homely.SendNotification.Data;
import com.example.android.homely.SendNotification.MyResponse;
import com.example.android.homely.SendNotification.NotificationSender;
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

public class AcceptTourActivity extends AppCompatActivity {

    private TourData tourData;
    private TextView ptxt, ploc, typeTxt, dateTxt, timeTxt;
    private MaterialButton submitButton;
    private TextInputEditText agName, agEmail, agPhoneNumber, desc;
    private RadioGroup tourStatusTypeRadioGrp;
    private MaterialRadioButton tourStatusTypeButton;
    private String tourStatus, agentName, agentEmail, agentPhoneNumber, description, tourId;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private MaterialCardView materialCardView;
    private APIService apiService;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_tour);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        ptxt = findViewById(R.id.pName);
        ploc = findViewById(R.id.pLoc);
        typeTxt = findViewById(R.id.typeTxt);
        dateTxt = findViewById(R.id.dateTxt);
        timeTxt = findViewById(R.id.timeTxt);
        submitButton = findViewById(R.id.sbtn);
        agName = findViewById(R.id.agentName);
        agEmail = findViewById(R.id.agentEmail);
        agPhoneNumber = findViewById(R.id.agentPhoneNumber);
        desc = findViewById(R.id.description);
        tourStatusTypeRadioGrp = findViewById(R.id.toutStatusTypeRadioGrp);
        materialCardView = findViewById(R.id.tourInfoCard);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        tourData = (TourData) getIntent().getParcelableExtra("tourData");
        tourId = tourData.getTourID();
        reference = firebaseDatabase.getReference("Token/"+tourData.getUserID());

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

        if (!tourData.getAgentName().equals("null")){
            agName.setText(tourData.getAgentName());
        }
        if (!tourData.getAgentEmail().equals("null")){
            agEmail.setText(tourData.getAgentEmail());
        }
        if (!tourData.getAgentPhoneNumber().equals("null")){
            agPhoneNumber.setText(tourData.getAgentPhoneNumber());
        }
        if (!tourData.getDescription().equals("null")){
            desc.setText(tourData.getDescription());
        }

        ptxt.setText(tourData.getPropertyName().toString());
        ploc.setText(tourData.getPropertyLoc().toString());
        if(tourData.getTourType().toString().equals("Virtual")){
            typeTxt.setText(tourData.getTourType().toString()+" ("+tourData.getVirtualType().toString()+")");
        }else{
            typeTxt.setText(tourData.getTourType().toString());
        }
        dateTxt.setText(tourData.getTourDate().toString());
        timeTxt.setText(tourData.getTourTime().toString());

        tourStatusTypeRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                tourStatusTypeButton = findViewById(i);
            }
        });

        tourStatusTypeButton = findViewById(tourStatusTypeRadioGrp.getCheckedRadioButtonId());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourStatus = tourStatusTypeButton.getText().toString();
                agentName = agName.getText().toString();
                agentEmail = agEmail.getText().toString();
                agentPhoneNumber = agPhoneNumber.getText().toString();
                description = desc.getText().toString();

                if(tourStatus.equals("Accept")){
                    if(agentName.isEmpty()){
                        agName.setError("Required");
                    }else if(agentEmail.isEmpty()){
                        agEmail.setError("Required");
                    }else if(agentPhoneNumber.isEmpty()){
                        agPhoneNumber.setError("Required");
                    }else{
                        submit();
                    }
                }else{
                    submit();
                }
            }

            private void submit() {
                String title="Tour", message;
                if(tourStatus.equals("Accept")){
                    tourData.status = "Accepted";
                    tourData.agentName = agentName;
                    tourData.agentPhoneNumber = agentPhoneNumber;
                    tourData.agentEmail = agentEmail;
                    message = "Your tour for "+tourData.getPropertyName()+" has been scheduled for "+tourData.getTourDate()+ " at "+tourData.getTourTime();
                }else{
                    tourData.status = "Rejected";
                    message = "Your tour for "+tourData.getPropertyName()+" has been rejected";
                }

                if(!description.isEmpty()){
                    tourData.description = description;
                }
                databaseReference = firebaseDatabase.getReference("Tour/"+tourId);
                databaseReference.setValue(tourData);

                Data data = new Data(title, message);
                NotificationSender notificationSender = new NotificationSender(data, userToken);
                apiService.sendNotification(notificationSender).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        Log.d("respo", "onResponse: "+response.body().success);
                        if(response.code()==200 && response.body().success!=1){
                            Toast.makeText(AcceptTourActivity.this, "Notification send", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.d("gfg1", "onFailure: "+t);
                        Toast.makeText(AcceptTourActivity.this, "Notification not send", Toast.LENGTH_SHORT).show();
                    }
                });

                finish();
            }
        });

        materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = firebaseDatabase.getReference("Property/"+tourData.getPropertyID());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null){
                            Intent property = new Intent(AcceptTourActivity.this, PropertyProfile.class);
                            property.putExtra("propertyData", snapshot.getValue(PropertyData.class));
                            property.putExtra("propertyID", tourData.getPropertyID());
                            startActivity(property);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}