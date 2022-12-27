package com.example.android.homely.Tour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.Data.TourData;
import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
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
    private MaterialCardView materialCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_tour);

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
        tourId = (String) getIntent().getStringExtra("tourId");

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
                }
            }

            private void submit() {
                if(tourStatus.equals("Accept")){
                    tourData.status = "Accepted";
                    tourData.agentName = agentName;
                    tourData.agentPhoneNumber = agentPhoneNumber;
                    tourData.agentEmail = agentEmail;
                }

                if(!description.isEmpty()){
                    tourData.description = description;
                }
                databaseReference = firebaseDatabase.getReference("Tour/"+tourId);
                databaseReference.setValue(tourData);
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