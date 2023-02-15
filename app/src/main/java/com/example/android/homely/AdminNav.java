package com.example.android.homely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.android.homely.Home.HomeFragment;
import com.example.android.homely.MyHome.MyHomeFragment;
import com.example.android.homely.Profile.ProfileFragment;
import com.example.android.homely.Search.MapsFragment;
import com.example.android.homely.SendNotification.Token;
import com.example.android.homely.Tour.ToursFragment;
import com.example.android.homely.databinding.ActivityAdminnavBinding;
import com.example.android.homely.databinding.ActivityUsernavBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class AdminNav extends AppCompatActivity {

    private ActivityAdminnavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminnav);

        binding = ActivityAdminnavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView mBottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.search:
                    replaceFragment(new MapsFragment());
                    break;
                case R.id.myhome:
                    replaceFragment(new MyHomeFragment());
                    break;
                case R.id.tours:
                    replaceFragment(new ToursFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });

        UpdateToken();
    }

    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Token token = new Token(task.getResult());
                    FirebaseDatabase.getInstance().getReference("Token").child(firebaseUser.getUid()).setValue(token);
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(binding.bottomNavigationView.getSelectedItemId()==R.id.home){
            super.onBackPressed();
            finish();
        }else{
            binding.bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }
}