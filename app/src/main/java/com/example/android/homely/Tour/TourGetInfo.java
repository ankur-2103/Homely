package com.example.android.homely.Tour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.android.homely.R;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourGetInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourGetInfo extends Fragment {

    private PassDataInterface passDataInterface;
    private RadioGroup tourTypeRadioGrp, tourDateRadioGrp, tourTimeRadioGrp;
    private MaterialButton button;
    private MaterialRadioButton tourTypeButton, tourDateButton, tourTimeButton;
    private String tourType, tourDate, tourTime;

    private TourGetInfo(){}

    public static TourGetInfo newInstance(PassDataInterface passDataInterface){
        TourGetInfo f = new TourGetInfo();
        f.passDataInterface = passDataInterface;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tour_get_info, container, false);

        tourTypeRadioGrp = view.findViewById(R.id.toutTypeRadioGrp);
        tourDateRadioGrp = view.findViewById(R.id.dateRadioGrp);
        tourTimeRadioGrp = view.findViewById(R.id.timeRadioGrp);
        button = view.findViewById(R.id.submitBtn);

        tourTypeRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                tourTypeButton = view.findViewById(i);
            }
        });

        tourDateRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                tourDateButton = view.findViewById(i);
            }
        });

        tourTimeRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                tourTimeButton = view.findViewById(i);
            }
        });

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar2.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR, 9);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        int a = calendar.getActualMaximum(Calendar.DATE)-calendar.get(Calendar.DATE);
        a += calendar2.getActualMaximum(Calendar.DATE);

        SimpleDateFormat dateformat = new SimpleDateFormat("EEE, MMM dd yyyy");
        SimpleDateFormat timeformate = new SimpleDateFormat("hh:mm aa");

        for(int i=1; i<=a; i++){
            calendar.add(Calendar.DATE,1);
            MaterialRadioButton radioButton = new MaterialRadioButton(getContext());
            radioButton.setId(View.generateViewId());
            radioButton.setText(dateformat.format(calendar.getTime()));
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(10, 10, 10, 10);
            params.weight = 1;
            radioButton.setLayoutParams(params);
            radioButton.setPadding(20, 20, 20, 20);
            radioButton.setBackgroundResource(R.drawable.date_radio_selector);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setTextSize(20);
            radioButton.setGravity(Gravity.CENTER);
            if(i==1 && tourDate==null){radioButton.setChecked(true);}
            if(tourDate!=null && tourDate.equals(radioButton.getText().toString())){radioButton.setChecked(true);}
            tourDateRadioGrp.addView(radioButton);
        }

        for(int i=1; i<=12; i++){
            if (i!=1){calendar.add(Calendar.HOUR,1);}
            MaterialRadioButton radioButton = new MaterialRadioButton(getContext());
            radioButton.setId(View.generateViewId());
            radioButton.setText(timeformate.format(calendar.getTime()));
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(10, 10, 10, 10);
            params.weight = 1;
            radioButton.setLayoutParams(params);
            radioButton.setPadding(20, 20, 20, 20);
            radioButton.setBackgroundResource(R.drawable.date_radio_selector);
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setTextSize(20);
            radioButton.setGravity(Gravity.CENTER);
            if(i==1 && tourTime==null){radioButton.setChecked(true);}
            if(tourTime!=null && tourTime.equals(radioButton.getText().toString())){radioButton.setChecked(true);}
            tourTimeRadioGrp.addView(radioButton);
        }

        tourTypeButton = view.findViewById(tourTypeRadioGrp.getCheckedRadioButtonId());
        tourDateButton = view.findViewById(tourDateRadioGrp.getCheckedRadioButtonId());
        tourTimeButton = view.findViewById(tourTimeRadioGrp.getCheckedRadioButtonId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourType = tourTypeButton.getText().toString();
                tourDate = tourDateButton.getText().toString();
                tourTime = tourTimeButton.getText().toString();
                passDataInterface.onDataReceivedTourGetInfo(tourType, tourDate, tourTime);
            }
        });

        return view;
    }
}