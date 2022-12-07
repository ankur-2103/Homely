package com.example.android.homely.Tour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.homely.R;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VirtualTourType#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VirtualTourType extends Fragment {

    private PassDataInterface passDataInterface;
    private MaterialCardView whatsappCard, skypeCard, gmeetCard;
    private MaterialRadioButton radioButtonW, radioButtonS, radioButtonM;
    private MaterialButton button;
    private String tourType;

    private VirtualTourType(){}

    public static VirtualTourType newInstance(PassDataInterface passDataInterface){
        VirtualTourType f = new VirtualTourType();
        f.passDataInterface = passDataInterface;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_virtual_tour_type, container, false);

        whatsappCard = view.findViewById(R.id.card1);
        skypeCard = view.findViewById(R.id.card2);
        gmeetCard = view.findViewById(R.id.card3);
        radioButtonW = view.findViewById(R.id.whatsappRadio);
        radioButtonS = view.findViewById(R.id.skypeRadio);
        radioButtonM = view.findViewById(R.id.gmeetRadio);
        button = view.findViewById(R.id.submitBtn);

        tourType = "Whatsapp";
        radioButtonW.setChecked(true);

        whatsappCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourType = "Whatsapp";
                radioButtonW.setChecked(true);
                radioButtonS.setChecked(false);
                radioButtonM.setChecked(false);
            }
        });

        skypeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourType = "Skype";
                radioButtonW.setChecked(false);
                radioButtonS.setChecked(true);
                radioButtonM.setChecked(false);
            }
        });

        gmeetCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourType = "Google Meet";
                radioButtonW.setChecked(false);
                radioButtonS.setChecked(false);
                radioButtonM.setChecked(true);
            }
        });

        radioButtonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourType = "Whatsapp";
                radioButtonW.setChecked(true);
                radioButtonS.setChecked(false);
                radioButtonM.setChecked(false);
            }
        });

        radioButtonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourType = "Skype";
                radioButtonW.setChecked(false);
                radioButtonS.setChecked(true);
                radioButtonM.setChecked(false);
            }
        });

        radioButtonM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tourType = "Google Meet";
                radioButtonW.setChecked(false);
                radioButtonS.setChecked(false);
                radioButtonM.setChecked(true);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataInterface.onDataReceivedVirtualTourType(tourType);
            }
        });

        return view;
    }
}