package com.example.android.homely.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.homely.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UpdateProfile extends AppCompatActivity {

    private ImageView imageView;
    private EditText Nfname, Nphone, Nemail;
    private Button save;
    private Uri filePath;
    private Uri ProfileUri;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String full_name, phone_no, e_mail;
    private boolean success = true;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Nfname = findViewById(R.id.Nfname);
        Nphone = findViewById(R.id.Nphone);
        Nemail = findViewById(R.id.Nemail);
        imageView = findViewById(R.id.img);
        save = findViewById(R.id.save_button);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Users/"+user.getUid()+"/profilepic.jpg");
        progressBar.setVisibility(View.VISIBLE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid());
        ProfileUri = user.getPhotoUrl();

        if (ProfileUri != null){
            Picasso.get().load(ProfileUri).into(imageView);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    Log.d("DBError", snapshot.toString());
                    full_name = snapshot.child("fname").getValue().toString();
                    phone_no = snapshot.child("phone").getValue().toString();
                    e_mail = snapshot.child("email").getValue().toString();
                    Nfname.setText(full_name);
                    Nphone.setText(phone_no);
                    Nemail.setText(e_mail);
                }catch (Exception e){
                    Log.e("DBError", String.valueOf(e));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DBError", String.valueOf(error));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = Nfname.getText().toString();
                String phone_number = Nphone.getText().toString();
                String email = Nemail.getText().toString();

                if(TextUtils.isEmpty(fullname)){
                    Nfname.setError("Please enter Full Name");
                    Nfname.requestFocus();
                }else if(TextUtils.isEmpty(phone_number)){
                    Nphone.setError("Please enter phone number");
                    Nphone.requestFocus();
                }else if(TextUtils.isEmpty(email)){
                    Nemail.setError("Please enter E-mail");
                    Nemail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Nemail.setError("Please enter re-enter email");
                    Nemail.setText(null);
                    Nemail.requestFocus();
                }else{
                    saveChanges(fullname, phone_number, email);
                }
            }
        });
    }

    private void SelectImage()
    {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                imageView.setImageURI(filePath);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveChanges(String fullname, String phone_number, String email) {
        if(!fullname.equals(full_name)){
            databaseReference.child("fname").setValue(fullname);
        }
        if(!phone_number.equals(phone_no)){
            databaseReference.child("phone").setValue(phone_number);
        }
        if(!e_mail.equals(email)){
            databaseReference.child("email").setValue(email);
            changeEmail(email);
        }
        if(filePath != null){
            uploadPic();
        }
        if (success){
            Toast.makeText(this, "Updated Profile!!!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void changeEmail(String email) {
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()){
                    success = false;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(UpdateProfile.this, "Email Update Failed!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void uploadPic() {
        storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                        user.updateProfile(profileChangeRequest);
                        databaseReference.child("profile_pic").setValue(uri.toString());
                        ProfileUri = uri;
                        imageView.setImageURI(ProfileUri);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                success = false;
                Toast.makeText(UpdateProfile.this, "Profile Image Upload Failed!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}