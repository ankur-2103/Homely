package com.example.android.homely.MyHome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class add_property_step2 extends Fragment  {

    private MaterialAutoCompleteTextView autoCompleteTextView;
    private TextInputLayout price1, price2, dropdown;
    private RadioGroup radioGroup;
    private RadioButton radioButton, radioButton1, radioButton2;
    private MaterialButton button;
    private TextInputEditText areaw, areal, ybuilt, bedr, bathr, dep, mrent;
    private String property, property_type, area_width, area_length, year_built, bedroom, bathroom, deposit, monthly_rent;
    private PassDataInterface passDataInterface;
    private PropertyData propertyData;

    public add_property_step2() {
    }

    public static add_property_step2 newInstance(PassDataInterface passDataInterface){
        add_property_step2 f = new add_property_step2();
        f.passDataInterface = passDataInterface;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_property_step2, container, false);

        areaw = view.findViewById(R.id.area_width);
        areal = view.findViewById(R.id.area_length);
        ybuilt = view.findViewById(R.id.year_built);
        bedr = view.findViewById(R.id.bedroom);
        bathr = view.findViewById(R.id.bathroom);
        dep = view.findViewById(R.id.deposit);
        mrent = view.findViewById(R.id.monthly_rent);
        price1 = view.findViewById(R.id.textlayout6);
        price2 = view.findViewById(R.id.textlayout7);
        dropdown = view.findViewById(R.id.inputLayout);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
        radioButton1 = view.findViewById(R.id.radio1);
        radioButton2 = view.findViewById(R.id.radio2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = view.findViewById(i);
                property = radioButton.getText().toString();
                if (property.equals("Sell")){
                    price1.setHint("Advance ₹");
                    price2.setHint("Estimated Price ₹");
                }else if(property.equals("Rent")){
                    price1.setHint("Deposit ₹");
                    price2.setHint("Monthly Rent ₹");
                }
            }
        });

        List<String> dropdown = new ArrayList<String>();
        dropdown = Arrays.asList(getResources().getStringArray(R.array.drop_down_list));
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item, dropdown);
        autoCompleteTextView = view.findViewById(R.id.autoComplete);
        autoCompleteTextView.setAdapter(adapter);

        try {
            propertyData = getArguments().getParcelable("propertyData");
            areal.setText(propertyData.getArea_length());
            areaw.setText(propertyData.getArea_width());
            ybuilt.setText(propertyData.getYear_built());
            bedr.setText(propertyData.getBedrooms());
            bathr.setText(propertyData.getBathrooms());
            dep.setText(propertyData.getDeposit());
            mrent.setText(propertyData.getMonthly_rent());
            if(propertyData.getProperty().equals("Sell")){
                radioButton2.setChecked(true);
            }
        }catch (Exception e){
            Log.e("err", "Error : "+e);
        }

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                property_type = adapterView.getItemAtPosition(i).toString();
                autoCompleteTextView.setError(null);
            }
        });

        return view;
    }

    public void submitData(){
        property = radioButton.getText().toString();
        area_width = areaw.getText().toString();
        area_length = areal.getText().toString();
        year_built = ybuilt.getText().toString();
        bedroom = bedr.getText().toString();
        bathroom = bathr.getText().toString();
        deposit = dep.getText().toString();
        monthly_rent = mrent.getText().toString();

        if(TextUtils.isEmpty(property_type)) {
            autoCompleteTextView.setError("Please Select Property Type");
        }else if(TextUtils.isEmpty(area_width)) {
            areaw.setError("Please Enter Area Width");
            areaw.requestFocus();
        }else if(TextUtils.isEmpty(area_length)) {
            areal.setError("Please Enter Area Length");
            areal.requestFocus();
        }else if(TextUtils.isEmpty(year_built)) {
            ybuilt.setError("Please Enter Year Built");
            ybuilt.requestFocus();
        }else if(TextUtils.isEmpty(bedroom)) {
            bedr.setError("Please Enter No. of Bedrooms");
            bedr.requestFocus();
        }else if(TextUtils.isEmpty(bathroom)) {
            bathr.setError("Please Enter No. of Bathrooms");
            bathr.requestFocus();
        }else if(TextUtils.isEmpty(deposit) && property.equals("Rent")) {
            dep.setError("Please Enter Deposit");
            dep.requestFocus();
        }else if(TextUtils.isEmpty(deposit) && property.equals("Sell")) {
            deposit = "0";
        }else if(TextUtils.isEmpty(monthly_rent)) {
            mrent.setError("Please Enter Price");
            mrent.requestFocus();
        }else{
            passDataInterface.onDataReceivedStep2(property, property_type, area_width, area_length, year_built, bedroom, bathroom, deposit, monthly_rent);
        }
    }
}