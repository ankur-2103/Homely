package com.example.android.homely.interfaces;

import android.net.Uri;

import com.example.android.homely.Data.FilterData;

public interface PassDataInterface {
    public default void onDataReceivedStep1(String buildingname, String streetaddress, String cityname, String state, String pincode){
    }

    public default void onDataReceivedStep2(String property, String property_type, String area_width, String area_length, String year_built, String bedroom, String bathroom, String deposit, String monthly_rent){
    }

    public default void onDataReceivedStep3(String desc){
    }

    public default void onDataReceivedStep4(Uri uri){
    }

    public default void onDataReceivedFilterBox(FilterData filterData){
    }

    public default void onDataReceivedTourGetInfo(String tourType, String tourDate, String tourTime){
    }

    public default void onDataReceivedVerifyPhone(Boolean isVerified){
    }

    public default void onDataReceivedVirtualTourType(String virtualType){
    }

    public default void onDataReceivedScheduleTour(Boolean isConfirmed){
    }
}