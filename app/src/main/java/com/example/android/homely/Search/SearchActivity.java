package com.example.android.homely.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
import com.example.android.homely.Data.FilterData;
import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements PassDataInterface {

    private EditText search;
    private ImageView searchIcon, filterIcon;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference propertyFirebaseDatabase;
    private DatabaseReference databaseReference;
    private SearchAdapter searchAdapter;
    private ArrayList<PropertyData>propertyDataList;
    private ArrayList<String>mypropIDList;
    private ArrayList<String>currPropIDList;
    private String property, priceMin, priceMax, bed, bath, areaMin, areaMax, propertyType;

    @Override
    public void onDataReceivedFilterBox(FilterData filterData) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.search_recyclerView);
        searchIcon = findViewById(R.id.search_button);
        filterIcon = findViewById(R.id.filter_button);
        search = findViewById(R.id.search_txt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        propertyDataList = new ArrayList<PropertyData>();
        mypropIDList = new ArrayList<String>();
        currPropIDList = new ArrayList<String>();

        searchAdapter = new SearchAdapter(SearchActivity.this, propertyDataList);
        recyclerView.setAdapter(searchAdapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/my_property");
        propertyFirebaseDatabase = firebaseDatabase.getReference("Property");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        mypropIDList.add(dataSnapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        propertyFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!mypropIDList.contains(dataSnapshot.getKey()) && !currPropIDList.contains(dataSnapshot.getKey())){
                        propertyDataList.add(dataSnapshot.getValue(PropertyData.class));
                        currPropIDList.add(dataSnapshot.getKey());
                    }
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialog filterDialog = new FilterDialog();
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("property", property);
                    bundle.putString("priceMin",priceMin);
                    bundle.putString("priceMax",priceMax);
                    bundle.putString("bed",bed);
                    bundle.putString("bath",bath);
                    bundle.putString("areaMin",areaMin);
                    bundle.putString("areaMax",areaMax);
                    bundle.putString("propertyType",propertyType);
                    filterDialog.setArguments(bundle);
                }catch (Exception e){
                    Log.e("err", "Error : "+e);
                }
                filterDialog.show(getFragmentManager(),"filterDialog");
            }
        });
        
    }

}