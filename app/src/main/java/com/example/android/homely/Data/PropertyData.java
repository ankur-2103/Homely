package com.example.android.homely.Data;


import android.os.Parcel;
import android.os.Parcelable;

public class PropertyData implements Parcelable {

    public String street_address, building_name, city_name, faddress, lat, lang, state, description, year_built, property, property_type, pincode, area_width, area_length, bedrooms, bathrooms, deposit, monthly_rent, furi;

    public PropertyData(String street_address,String building_name,String city_name, String faddress,String lat,String lang,String state,String description,String year_built,String property,String property_type,String pincode,String area_width,String area_length,String bedrooms,String bathrooms,String deposit,String monthly_rent,String furi){
        this.building_name = building_name;
        this.street_address = street_address;
        this.city_name = city_name;
        this.state = state;
        this.pincode = pincode;
        this.faddress = faddress;
        this.property = property;
        this.property_type = property_type;
        this.area_width = area_width;
        this.area_length = area_length;
        this.year_built = year_built;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.deposit = deposit;
        this.monthly_rent = monthly_rent;
        this.description = description;
        this.furi = furi;
        this.lang = lang;
        this.lat = lat;
    }

    public PropertyData() {
    }

    protected PropertyData(Parcel in) {
        street_address = in.readString();
        building_name = in.readString();
        city_name = in.readString();
        faddress = in.readString();
        lat = in.readString();
        lang = in.readString();
        state = in.readString();
        description = in.readString();
        year_built = in.readString();
        property = in.readString();
        property_type = in.readString();
        pincode = in.readString();
        area_width = in.readString();
        area_length = in.readString();
        bedrooms = in.readString();
        bathrooms = in.readString();
        deposit = in.readString();
        monthly_rent = in.readString();
        furi = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(street_address);
        dest.writeString(building_name);
        dest.writeString(city_name);
        dest.writeString(faddress);
        dest.writeString(lat);
        dest.writeString(lang);
        dest.writeString(state);
        dest.writeString(description);
        dest.writeString(year_built);
        dest.writeString(property);
        dest.writeString(property_type);
        dest.writeString(pincode);
        dest.writeString(area_width);
        dest.writeString(area_length);
        dest.writeString(bedrooms);
        dest.writeString(bathrooms);
        dest.writeString(deposit);
        dest.writeString(monthly_rent);
        dest.writeString(furi);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PropertyData> CREATOR = new Creator<PropertyData>() {
        @Override
        public PropertyData createFromParcel(Parcel in) {
            return new PropertyData(in);
        }

        @Override
        public PropertyData[] newArray(int size) {
            return new PropertyData[size];
        }
    };

    public String getStreet_address() {
        return street_address;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getLat() {
        return lat;
    }

    public String getLang() {
        return lang;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public String getYear_built() {
        return year_built;
    }

    public String getProperty() {
        return property;
    }

    public String getProperty_type() {
        return property_type;
    }

    public String getPincode() {
        return pincode;
    }

    public String getArea_width() {
        return area_width;
    }

    public String getArea_length() {
        return area_length;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public String getDeposit() {
        return deposit;
    }

    public String getMonthly_rent() {
        return monthly_rent;
    }

    public String getFuri() {
        return furi;
    }

    public String getFaddress() { return faddress; }
}
