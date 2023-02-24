package com.example.android.homely.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.homely.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private EditText pass, cpass;
    private MaterialButton save;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        pass = findViewById(R.id.pass);
        cpass = findViewById(R.id.cpass);
        save = findViewById(R.id.submit_button);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = pass.getText().toString();
                String cpassword = cpass.getText().toString();

                if(TextUtils.isEmpty(password)){
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
                    changePassword(password);
                }
                finish();
            }
        });

    }

    private void changePassword(String password) {
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ChangePassword.this, "Password Updated Successfully!!!", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(ChangePassword.this, e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("err", e.toString());
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}