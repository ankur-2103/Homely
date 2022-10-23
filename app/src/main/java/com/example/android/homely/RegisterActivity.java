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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText fname, email, phoneno, pass, cpass;
    private TextView signin;
    private MaterialButton register;
    private MaterialTextView txt1, txt2, txt3, txt4, txt5;
    private FirebaseAuth auth;
    private Intent loginIntent;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = findViewById(R.id.fname);
        email = findViewById(R.id.email);
        phoneno = findViewById(R.id.pno);
        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        signin = findViewById(R.id.signin);
        register = findViewById(R.id.submit_button);

        loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(loginIntent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = fname.getText().toString();
                String e_mail = email.getText().toString();
                String phone_number = phoneno.getText().toString();
                String password = pass.getText().toString();
                String cpassword = cpass.getText().toString();

                if(TextUtils.isEmpty(fullname)){
                    fname.setError("Please enter Full Name");
                    fname.requestFocus();
                }else if(TextUtils.isEmpty(e_mail)){
                    email.setError("Please enter E-mail");
                    email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(e_mail).matches()){
                    email.setError("Please enter re-enter email");
                    email.setText(null);
                    email.requestFocus();
                }else if(TextUtils.isEmpty(phone_number)) {
                    phoneno.setError("Please enter enter phone number");
                    phoneno.requestFocus();
                }else if(TextUtils.isEmpty(password)){
                    pass.setError("Please enter password");
                    pass.requestFocus();
                }else if(password.length() < 6){
                    pass.setError("Atleast 6 characters are required");
                    pass.requestFocus();
                }else if(!cpassword.equals(password)){
                    cpass.setError("Please enter same password");
                    cpass.setText(null);
                    cpass.requestFocus();
                }else{
                    registerUser(fullname, e_mail, phone_number, password);
                }
            }
        });
    }

    private void registerUser(String fullname, String e_mail, String phone_number, String password) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(e_mail,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = auth.getCurrentUser();
                    UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(fullname).build();
                    if(user!=null){
                        user.updateProfile(profile);

                        User user1 = new User(fullname, phone_number, e_mail);
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("User");
                        databaseReference.child(user.getUid()).setValue(user1);
                    }
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }else{
                    Toast.makeText(RegisterActivity.this, "Registration Failed Try again later", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}