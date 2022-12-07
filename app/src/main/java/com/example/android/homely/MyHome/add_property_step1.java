package com.example.android.homely.MyHome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class add_property_step1 extends Fragment {

    private TextInputEditText bname, saddress, cname, tstate, tpincode;
    private String building_name, street_address, city_name, state, pincode;
    private MaterialButton button;
    private PassDataInterface passDataInterface;
    private PropertyData propertyData;

    public add_property_step1() {
    }

    public static add_property_step1 newInstance(PassDataInterface passDataInterface){
        add_property_step1 f = new add_property_step1();
        f.passDataInterface = passDataInterface;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_property_step1, container, false);

        bname = view.findViewById(R.id.building_name);
        saddress = view.findViewById(R.id.street_address);
        cname = view.findViewById(R.id.city_name);
        tstate = view.findViewById(R.id.state);
        tpincode = view.findViewById(R.id.pincode);

        try {
            propertyData = getArguments().getParcelable("propertyData");
            Log.d("editProp", String.valueOf(propertyData));
            if(propertyData!=null){
                bname.setText(propertyData.getBuilding_name());
                saddress.setText(propertyData.getStreet_address());
                cname.setText(propertyData.getCity_name());
                tstate.setText(propertyData.getState());
                tpincode.setText(propertyData.getPincode());
            }else{
                Log.d("editProp", "no data");
            }
        }catch (Exception e){
            Log.d("editProp", e.toString());
        }

        return view;
    }

    public void submitData(){
        building_name = bname.getText().toString();
        street_address = saddress.getText().toString();
        city_name = cname.getText().toString();
        state = tstate.getText().toString();
        pincode = tpincode.getText().toString();

        if(TextUtils.isEmpty(building_name)){
            bname.setError("Please Enter Building Name");
            bname.requestFocus();
        }else if(TextUtils.isEmpty(street_address)){
            saddress.setError("Please Enter Street Address");
            saddress.requestFocus();
        }else if(TextUtils.isEmpty(city_name)){
            cname.setError("Please Enter City Name");
            cname.requestFocus();
        }else if(TextUtils.isEmpty(state)){
            tstate.setError("Please Enter State");
            tstate.requestFocus();
        }else if(TextUtils.isEmpty(pincode)){
            tpincode.setError("Please Enter Pincode");
            tpincode.requestFocus();
        }else{
            passDataInterface.onDataReceivedStep1(building_name, street_address, city_name, state, pincode);
        }
    }
}