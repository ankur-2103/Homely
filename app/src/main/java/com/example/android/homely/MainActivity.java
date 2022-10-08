package com.example.android.homely;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static int SPLASH_TIMER = 2000;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        Intent intent1 = new Intent(MainActivity.this, MainActivity2.class);
        auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
            if(auth.getCurrentUser()==null){
                startActivity(intent);
            }else {
                startActivity(intent1);
            }
            finish();
        }, SPLASH_TIMER);


    }


}