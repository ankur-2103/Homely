package com.example.android.homely;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.textfield.TextInputEditText;

public class add_property_step3 extends Fragment {

    private TextInputEditText desc;
    private String description;
    private PassDataInterface passDataInterface;

    public add_property_step3() {
    }

    public static add_property_step3 newInstance(PassDataInterface passDataInterface) {
        add_property_step3 fragment = new add_property_step3();
        fragment.passDataInterface = passDataInterface;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_property_step3, container, false);

        desc = view.findViewById(R.id.description);

        return view;
    }

    public void submitData(){
        description = desc.getText().toString();
        passDataInterface.onDataReceivedStep3(description);
    }

}