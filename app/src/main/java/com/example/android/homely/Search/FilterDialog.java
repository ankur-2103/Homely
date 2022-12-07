package com.example.android.homely.Search;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.homely.R;
import com.example.android.homely.Data.FilterData;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class FilterDialog extends DialogFragment {

    private MaterialButton cancel, apply;
    private TextView bedTxt, bathTxt;
    private TextInputEditText minA, maxA;
    private ImageButton minusBed, plusBed, minusBath, plusBath;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RangeSlider rangeSlider;
    private ChipGroup chipGroup;
    private Chip chip;
    private String property, priceMin, priceMax, beds, baths, minArea, maxArea, propertyType;
    private PassDataInterface passDataInterface;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_box, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        cancel = view.findViewById(R.id.cancel_button);
        apply = view.findViewById(R.id.apply_button);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
        rangeSlider = view.findViewById(R.id.priceRange);
        bedTxt = view.findViewById(R.id.bedTxt);
        bathTxt = view.findViewById(R.id.bathTxt);
        minusBed = view.findViewById(R.id.minusBed);
        plusBed = view.findViewById(R.id.plusBed);
        minusBath = view.findViewById(R.id.minusBath);
        plusBath = view.findViewById(R.id.plusBath);
        minA = view.findViewById(R.id.minArea);
        maxA = view.findViewById(R.id.maxArea);
        chipGroup = view.findViewById(R.id.chipGroup);

        try {
            Bundle bundle = new Bundle();
            property = bundle.getString("property");
            priceMin = bundle.getString("priceMin");
            priceMax = bundle.getString("priceMax");
            beds = bundle.getString("bed");
            baths = bundle.getString("bath");
            minArea = bundle.getString("areaMin");
            maxArea = bundle.getString("areaMax");
            propertyType = bundle.getString("propertyType");

//            if(property.equals("Sale")){
//                radioGroup.check(R.id.radio1);
//            }else{
//                radioGroup.check(R.id.radio2);
//            }

//            ArrayList<Float>list = new ArrayList<Float>();
//            list.add(Float.valueOf(priceMin));
//            list.add(Float.valueOf(priceMax));
//            rangeSlider.setValues(list);

            if (beds!=null){
                bedTxt.setText(beds);
            }
            if (baths!=null){
                bathTxt.setText(baths);
            }
            if (minArea!=null){
                minA.setText(minArea);
            }
            if(maxArea!=null){
                maxA.setText(maxArea);
            }

//            for (int i=0; i<chipGroup.getChildCount(); i++){
//                if (propertyType.contains(chip.getText().toString())) {
//                    chipGroup.check(i);
//                }
//            }
        }catch (Exception e){
            Log.w("filerDialog", e.toString());
        }

        minusBed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(bedTxt.getText().toString());
                if(value > 0){
                    value--;
                    bedTxt.setText(String.valueOf(value));
                }
            }
        });

        plusBed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(bedTxt.getText().toString());
                if(value < 9){
                    value++;
                    bedTxt.setText(String.valueOf(value));
                }
            }
        });

        minusBath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(bathTxt.getText().toString());
                if(value >= 1){
                    value--;
                    bathTxt.setText(String.valueOf(value));
                }
            }
        });

        plusBath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(bathTxt.getText().toString());
                if(value < 9){
                    value++;
                    bathTxt.setText(String.valueOf(value));
                }
            }
        });

        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                List<Float>value = slider.getValues();
                priceMin = String.valueOf(value.get(0));
                priceMax = String.valueOf(value.get(1));
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List<Float>value = slider.getValues();
                priceMin = String.valueOf(value.get(0));
                priceMax = String.valueOf(value.get(1));
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = view.findViewById(i);
                property = radioButton.getText().toString();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            passDataInterface = (PassDataInterface) context;
        }catch (Exception e){
            Log.e("passData", e.toString());
        }
    }

    private void submitData() {
        property = radioButton.getText().toString();
        beds = bedTxt.getText().toString();
        baths = bathTxt.getText().toString();
        minArea = minA.getText().toString();
        maxArea = maxA.getText().toString();
        propertyType = getChipValue();
        FilterData filterData = new FilterData(property, priceMin, priceMax, beds, baths, minArea, maxArea, propertyType);
        passDataInterface.onDataReceivedFilterBox(filterData);
    }

    public String getChipValue(){
        String res = "";
        for (int i=0; i<chipGroup.getChildCount(); i++){
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked() ) {
                res += chip.getText().toString() + " " ;
            }
        }
        return res.trim();
    }

}
