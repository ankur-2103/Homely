package com.example.android.homely.SendNotification;

import androidx.annotation.NonNull;

import com.example.android.homely.Profile.UpdateProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FirebaseStorage;

public class MyFirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Token newToken = new Token(token);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Token").child(firebaseUser.getUid()).setValue(newToken);
    }

}
