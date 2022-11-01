package com.example.android.homely;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyHomeFragment extends Fragment {

    private ImageView imageView;
    private MaterialButton addProperty_button;
    private FloatingActionButton floating_button;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static MyHomeFragment newInstance(){
        return new MyHomeFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_home, container, false);

        imageView = view.findViewById(R.id.noData);
        floating_button = view.findViewById(R.id.addProperty_fbutton);
        addProperty_button = view.findViewById(R.id.addProperty);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid());
        Intent addProperty = new Intent(getContext(), AddProperty.class);
        DataSnapshot data;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("my_property").exists()){
                    imageView.setVisibility(View.VISIBLE);
                    addProperty_button.setVisibility(View.VISIBLE);
                    floating_button.setVisibility(View.GONE);
                }else{
                    imageView.setVisibility(View.GONE);
                    addProperty_button.setVisibility(View.GONE);
                    floating_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                imageView.setVisibility(View.VISIBLE);
                addProperty_button.setVisibility(View.VISIBLE);
                floating_button.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getCode(), Toast.LENGTH_SHORT).show();
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
}