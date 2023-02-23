package com.example.android.homely.MyHome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.homely.Data.DealData;
import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MyHomeFragment extends Fragment {

    private ImageView imageView;
    private TextView textView;
    private MaterialButton addProperty_button;
    private RecyclerView recyclerView;
    private MyPropertyAdapter myPropertyAdapter;
    private ArrayList<PropertyData>list;
    private ArrayList<String>propIDList;
    private ArrayList<String>currPropIDList;
    private FloatingActionButton floating_button;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference propertyFirebaseDatabase;
    private DatabaseReference databaseReference;

    public static MyHomeFragment newInstance(){
        return new MyHomeFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_home, container, false);

        imageView = view.findViewById(R.id.noData);
        textView = view.findViewById(R.id.myHomeTxt);
        floating_button = view.findViewById(R.id.addProperty_fbutton);
        recyclerView = view.findViewById(R.id.recyclerView);
        addProperty_button = view.findViewById(R.id.addProperty);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid()+"/my_property");
        propertyFirebaseDatabase = firebaseDatabase.getReference("Property");
        Intent addProperty = new Intent(getContext(), AddProperty.class);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        propIDList = new ArrayList<>();
        currPropIDList = new ArrayList<>();
        myPropertyAdapter = new MyPropertyAdapter(getContext(), list);
        recyclerView.setAdapter(myPropertyAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.w("DBsnap", "onDataChange: "+snapshot.toString());
                if(snapshot.getValue()==null){
                    imageView.setVisibility(View.VISIBLE);
                    addProperty_button.setVisibility(View.VISIBLE);
                    floating_button.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }else{
                    imageView.setVisibility(View.GONE);
                    addProperty_button.setVisibility(View.GONE);
                    floating_button.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    propIDList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        propIDList.add(dataSnapshot.getValue().toString());
                    }
                    getProperties();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("dberr", error.toString());
                imageView.setVisibility(View.VISIBLE);
                addProperty_button.setVisibility(View.VISIBLE);
                floating_button.setVisibility(View.GONE);
            }
        });

        addProperty_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addProperty);
            }
        });

        floating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addProperty);
            }
        });

        return view;
    }

    private void getProperties() {
        propertyFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(propIDList.contains(dataSnapshot.getKey())){
                            list.add(dataSnapshot.getValue(PropertyData.class));
                            currPropIDList.add(dataSnapshot.getKey());
                            Log.d("bk123", "onDataChange: "+dataSnapshot.getValue());
                        }
                    }
                    Collections.reverse(list);
                    myPropertyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}