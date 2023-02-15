package com.example.android.homely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.example.android.homely.Tour.TourActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static int SPLASH_TIMER = 4000;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        Intent intent1 = new Intent(MainActivity.this, UserNav.class);
        Intent intent2 = new Intent(MainActivity.this, AdminNav.class);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if(firebaseUser!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference("User/"+firebaseUser.getUid()+"/isAdmin");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    isAdmin = (boolean) snapshot.getValue();
                    sharedPreferences  = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAdmin", isAdmin);
                    editor.apply();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        new Handler().postDelayed(() -> {
            if(firebaseUser==null){
                startActivity(intent);
            }else if(isAdmin){
                startActivity(intent2);
            } else {
                startActivity(intent1);
            }
            finish();
        }, SPLASH_TIMER);
    }
}