package com.example.android.homely.Profile;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homely.Booking.MyBookingsActivity;
import com.example.android.homely.LoginActivity;
import com.example.android.homely.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private TextView name, email;
    private Button signout;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri ProfileUri;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Intent updateProfile, changePassword, favoritesI, tour, booking;
    private MaterialCardView uprofile, changepassword, favorites, tours, bookings;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        imageView = view.findViewById(R.id.imageView2);
        progressBar = view.findViewById(R.id.progressBar);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        signout = view.findViewById(R.id.signout_button);
        uprofile = view.findViewById(R.id.updateProfile);
        changepassword = view.findViewById(R.id.changePassword);
        favorites = view.findViewById(R.id.favorites);
        auth = FirebaseAuth.getInstance();
        tours = view.findViewById(R.id.myTours);
        bookings = view.findViewById(R.id.myDeal);

        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/"+user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    String full_name = snapshot.child("fname").getValue().toString();
                    String e_mail = snapshot.child("email").getValue().toString();
                    if(snapshot.hasChild("profile_pic")){
                        ProfileUri = Uri.parse(snapshot.child("profile_pic").getValue().toString());
                        Picasso.get().load(ProfileUri).into(imageView);
                    }
                    name.setText(full_name);
                    email.setText(e_mail);
                }catch (Exception e){
                    Log.e("DBError", String.valueOf(e));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DBError", String.valueOf(error));
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(getActivity(), "Signed Out!", Toast.LENGTH_SHORT).show();
                Intent login = new Intent(getActivity(), LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
            }
        });
        
        
        uprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile = new Intent(getContext(), UpdateProfile.class);
                startActivity(updateProfile);
            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword = new Intent(getContext(), ChangePassword.class);
                startActivity(changePassword);
            }
        });

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoritesI = new Intent(getContext(), Favorites.class);
                startActivity(favoritesI);
            }
        });

        tours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tour = new Intent(getContext(), MyTourActivity.class);
                startActivity(tour);
            }
        });

        bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booking = new Intent(getContext(), MyBookingsActivity.class);
                startActivity(booking);
            }
        });

        return view;
    }

}