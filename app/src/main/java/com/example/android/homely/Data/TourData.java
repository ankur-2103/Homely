package com.example.android.homely.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class TourData implements Parcelable {

    public String propertyID;
    public String userID;
    public String propertyName;
    public String propertyLoc;
    public String tourType;
    public String tourDate;
    public String tourTime;
    public String virtualType;
    public String status;
    public String agentName;
    public String agentPhoneNumber;
    public String agentEmail;
    public String description;
    public String feedback;
    public String tourID;

    public TourData() {}

    public TourData(String tourID, String propertyID, String userID, String propertyName, String propertyLoc, String tourType, String tourDate, String tourTime, String virtualType, String status, String agentName, String agentPhoneNumber, String agentEmail, String description, String feedback) {
        this.propertyID = propertyID;
        this.userID = userID;
        this.propertyName = propertyName;
        this.propertyLoc = propertyLoc;
        this.tourType = tourType;
        this.tourDate = tourDate;
        this.tourTime = tourTime;
        this.virtualType = virtualType;
        this.status = status;
        this.agentName = agentName;
        this.agentPhoneNumber = agentPhoneNumber;
        this.agentEmail = agentEmail;
        this.description = description;
        this.feedback = feedback;
        this.tourID = tourID;
    }

    protected TourData(Parcel in) {
        propertyID = in.readString();
        userID = in.readString();
        propertyName = in.readString();
        propertyLoc = in.readString();
        tourType = in.readString();
        tourDate = in.readString();
        tourTime = in.readString();
        virtualType = in.readString();
        status = in.readString();
        agentName = in.readString();
        agentPhoneNumber = in.readString();
        agentEmail = in.readString();
        description = in.readString();
        feedback = in.readString();
        tourID = in.readString();
    }

    public static final Creator<TourData> CREATOR = new Creator<TourData>() {
        @Override
        public TourData createFromParcel(Parcel in) {
            return new TourData(in);
        }

        @Override
        public TourData[] newArray(int size) {
            return new TourData[size];
        }
    };

    public String getPropertyID() {
        return propertyID;
    }

    public String getUserID() {
        return userID;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyLoc() {
        return propertyLoc;
    }

    public String getTourType() {
        return tourType;
    }

    public String getTourDate() {
        return tourDate;
    }

    public String getTourTime() {
        return tourTime;
    }

    public String getVirtualType() {
        return virtualType;
    }

    public String getStatus() {
        return status;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAgentPhoneNumber() {
        return agentPhoneNumber;
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public String getDescription() {
        return description;
    }

    public String getFeedback() {
        return feedback;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(propertyID);
        parcel.writeString(userID);
        parcel.writeString(propertyName);
        parcel.writeString(propertyLoc);
        parcel.writeString(tourType);
        parcel.writeString(tourDate);
        parcel.writeString(tourTime);
        parcel.writeString(virtualType);
        parcel.writeString(status);
        parcel.writeString(agentName);
        parcel.writeString(agentPhoneNumber);
        parcel.writeString(agentEmail);
        parcel.writeString(description);
        parcel.writeString(feedback);
        parcel.writeString(tourID);
    }
}
