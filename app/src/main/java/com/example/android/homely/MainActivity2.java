package com.example.android.homely;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.android.homely.Home.HomeFragment;
import com.example.android.homely.MyHome.MyHomeFragment;
import com.example.android.homely.Notification.NotificationsFragment;
import com.example.android.homely.Profile.ProfileFragment;
import com.example.android.homely.Search.MapsFragment;
import com.example.android.homely.databinding.ActivityMain2Binding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView mBottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.getMenu().findItem(R.id.search).setChecked(true);
        replaceFragment(new MapsFragment());

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
                case R.id.notification:
                    replaceFragment(new NotificationsFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
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