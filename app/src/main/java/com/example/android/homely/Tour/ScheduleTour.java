package com.example.android.homely.Tour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.homely.Data.TourData;
import com.example.android.homely.R;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.button.MaterialButton;

public class ScheduleTour extends Fragment {

    private PassDataInterface passDataInterface;
    private TourData tourData;
    private TextView ptxt, ploc, typeTxt, dateTxt, timeTxt;
    private MaterialButton editButton, confirmButton;

    private ScheduleTour(){}

    public static ScheduleTour newInstance(PassDataInterface passDataInterface){
        ScheduleTour f = new ScheduleTour();
        f.passDataInterface = passDataInterface;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_tour, container, false);

        ptxt = view.findViewById(R.id.pName);
        ploc = view.findViewById(R.id.pLoc);
        typeTxt = view.findViewById(R.id.typeTxt);
        dateTxt = view.findViewById(R.id.dateTxt);
        timeTxt = view.findViewById(R.id.timeTxt);
        editButton = view.findViewById(R.id.ebtn);
        confirmButton = view.findViewById(R.id.cbtn);

        tourData = getArguments().getParcelable("tourData");

        ptxt.setText(tourData.getPropertyName().toString());
        ploc.setText(tourData.getPropertyLoc().toString());
        if(tourData.getTourType().toString().equals("Virtual")){
            typeTxt.setText(tourData.getTourType().toString()+" ("+tourData.getVirtualType().toString()+")");
        }else{
            typeTxt.setText(tourData.getTourType().toString());
        }
        dateTxt.setText(tourData.getTourDate().toString());
        timeTxt.setText(tourData.getTourTime().toString());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataInterface.onDataReceivedScheduleTour(true);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataInterface.onDataReceivedScheduleTour(false);
            }
        });

        return view;
    }
}