package com.example.android.homely.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class DealData implements Parcelable {
    public String amount;
    public String dealDate;
    public String dealTime;
    public String userID;
    public String propertyID;
    public String propertyAddress;
    public String propertyName;
    public String dealStatus;
    public String dealID;

    public DealData(){}


    public static final Creator<DealData> CREATOR = new Creator<DealData>() {
        @Override
        public DealData createFromParcel(Parcel in) {
            return new DealData(in);
        }

        @Override
        public DealData[] newArray(int size) {
            return new DealData[size];
        }
    };

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getDealID() {
        return dealID;
    }

    public void setDealID(String dealID) {
        this.dealID = dealID;
    }



    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public DealData(String dealID, String dealDate, String dealTime, String userID, String propertyID, String propertyAddress, String propertyName, String dealStatus) {
        this.dealDate = dealDate;
        this.dealTime = dealTime;
        this.userID = userID;
        this.propertyID = propertyID;
        this.propertyAddress = propertyAddress;
        this.propertyName = propertyName;
        this.dealStatus = dealStatus;
        this.dealID = dealID;
        this.amount = "null";
    }

    protected DealData(Parcel in) {
        dealDate = in.readString();
        dealTime = in.readString();
        userID = in.readString();
        propertyID = in.readString();
        propertyAddress = in.readString();
        propertyName = in.readString();
        dealStatus = in.readString();
        dealID = in.readString();
        amount = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dealDate);
        parcel.writeString(dealTime);
        parcel.writeString(userID);
        parcel.writeString(propertyID);
        parcel.writeString(propertyAddress);
        parcel.writeString(propertyName);
        parcel.writeString(dealStatus);
        parcel.writeString(dealID);
        parcel.writeString(amount);
    }
}
