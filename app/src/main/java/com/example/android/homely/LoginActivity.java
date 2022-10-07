package com.example.android.homely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, pass;
    private MaterialButton login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.submit_button);

        Intent signupIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        TextView register = findViewById(R.id.signup);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(signupIntent);
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
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent user = new Intent(LoginActivity.this, MainActivity2.class);
                    user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(user);
                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}