package com.example.android.homely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homely.Profile.ForgetPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText email, pass;
    private MaterialButton login;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.submit_button);

        Intent signupIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        Intent forgotPassword = new Intent(LoginActivity.this, ForgetPassword.class);
        TextView register = findViewById(R.id.signup);
        TextView fpass = findViewById(R.id.forgotpassword);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(signupIntent);
            }
        });

        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(forgotPassword);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_mail = email.getText().toString();
                String password = pass.getText().toString();

                if(TextUtils.isEmpty(e_mail)){
                    email.setError("Please enter E-mail");
                    email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(e_mail).matches()){
                    email.setError("Please enter re-enter email");
                    email.setText(null);
                    email.requestFocus();
                }else if(TextUtils.isEmpty(password)){
                    pass.setError("Please enter password");
                    pass.requestFocus();
                }else if(password.length() < 6){
                    pass.setError("Wrong Passowrd");
                    pass.requestFocus();
                }else{
                    loginUser(e_mail, password);
                }
            }
        });
    }

    private void loginUser(String e_mail, String password) {
        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(e_mail,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    initUser();
                }else{
                    Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUser() {
        Intent intent1 = new Intent(LoginActivity.this, UserNav.class);
        Intent intent2 = new Intent(LoginActivity.this, AdminNav.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User/"+firebaseUser.getUid()+"/isAdmin");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                isAdmin = (boolean) snapshot.getValue();
                sharedPreferences  = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isAdmin", isAdmin);
                editor.apply();
                if(isAdmin){
                    startActivity(intent2);
                } else {
                    startActivity(intent1);
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}