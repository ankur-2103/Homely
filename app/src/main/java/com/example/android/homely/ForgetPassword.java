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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email;
    private MaterialButton submit;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit_button);
        intent = new Intent(ForgetPassword.this, LoginActivity.class);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e_mail = email.getText().toString();

                if(TextUtils.isEmpty(e_mail)){
                    email.setError("Please enter E-mail");
                    email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(e_mail).matches()){
                    email.setError("Please enter re-enter email");
                    email.setText(null);
                    email.requestFocus();
                }else{
                    forgotPassword(e_mail);
                }
            }
        });

    }

    private void forgotPassword(String e_mail) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(e_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPassword.this, "Check your Email", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(ForgetPassword.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}