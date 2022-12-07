package com.example.android.homely.Tour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.homely.R;
import com.example.android.homely.interfaces.PassDataInterface;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyPhoneNumber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyPhoneNumber extends Fragment {

    private PassDataInterface passDataInterface;
    private TextInputEditText[] otpEt;
    private MaterialButton button;

    private VerifyPhoneNumber(){}

    public static VerifyPhoneNumber newInstance(PassDataInterface passDataInterface){
        VerifyPhoneNumber f = new VerifyPhoneNumber();
        f.passDataInterface = passDataInterface;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_verify_phone_number, container, false);

        otpEt = new TextInputEditText[6];

        otpEt[0] = view.findViewById(R.id.otp1);
        otpEt[1] = view.findViewById(R.id.otp2);
        otpEt[2] = view.findViewById(R.id.otp3);
        otpEt[3] = view.findViewById(R.id.otp4);
        otpEt[4] = view.findViewById(R.id.otp5);
        otpEt[5] = view.findViewById(R.id.otp6);

        button = view.findViewById(R.id.confirmBtn);

        setOtpEditTextHandler();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataInterface.onDataReceivedVerifyPhone(true);
            }
        });

        return view;
    }

    private void setOtpEditTextHandler() {
        for (int i = 0;i < 6;i++) {
            final int iVal = i;
            otpEt[iVal].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(iVal == 5 && !otpEt[iVal].getText().toString().isEmpty()) {
                    } else if (!otpEt[iVal].getText().toString().isEmpty()) {
                        otpEt[iVal+1].requestFocus();
                    }
                }
            });

            otpEt[iVal].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return false;
                    }
                    if(keyCode == KeyEvent.KEYCODE_DEL && otpEt[iVal].getText().toString().isEmpty() && iVal != 0) {
                        otpEt[iVal-1].setText("");
                        otpEt[iVal-1].requestFocus();
                    }
                    return false;
                }
            });
        }
    }
}