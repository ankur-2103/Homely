package com.example.android.homely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    private TextView name, email;
    private Button signout;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signout = findViewById(R.id.signout_button);
        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        if(user != null){
            name.setText("Hello " + user.getDisplayName());
            email.setText(user.getEmail());
        }else{
            Toast.makeText(UserActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(UserActivity.this, "Signed Out!", Toast.LENGTH_SHORT).show();
                Intent login = new Intent(UserActivity.this, LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
        });

    }
}