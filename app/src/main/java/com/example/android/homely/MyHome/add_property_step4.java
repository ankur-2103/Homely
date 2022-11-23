package com.example.android.homely.MyHome;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.homely.R;
import com.example.android.homely.data.PropertyData;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class add_property_step4 extends Fragment {

    private PassDataInterface passDataInterface;
    private MaterialButton btnSelect;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private PropertyData propertyData;

    public add_property_step4() {
        // Required empty public constructor
    }

    public static add_property_step4 newInstance(PassDataInterface passDataInterface) {
        add_property_step4 fragment = new add_property_step4();
        fragment.passDataInterface = passDataInterface;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_property_step4, container, false);

        btnSelect = view.findViewById(R.id.materialButton);
        imageView = view.findViewById(R.id.imageView);

        if(filePath != null){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        try {
            propertyData = getArguments().getParcelable("propertyData");
            Picasso.get().load(propertyData.getFuri()).into(imageView);
        }catch (Exception e){

        }

        return view;
    }

    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void submitData(){
        if(filePath == null && propertyData == null){
            Toast.makeText(getContext(), "Please Select a Image", Toast.LENGTH_SHORT).show();
        }else{
            passDataInterface.onDataReceivedStep4(filePath);
        }
    }

}