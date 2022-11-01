package com.example.android.homely;


public class PropertyData {

    public String street_address, building_name, city_name, lat, lang, state, description, year_built, property, property_type, pincode, area_width, area_length, bedrooms, bathrooms, deposit, monthly_rent, furi;

    public PropertyData(String street_address,String building_name,String city_name,String lat,String lang,String state,String description,String year_built,String property,String property_type,String pincode,String area_width,String area_length,String bedrooms,String bathrooms,String deposit,String monthly_rent,String furi){
        this.building_name = building_name;
        this.street_address = street_address;
        this.city_name = city_name;
        this.state = state;
        this.pincode = pincode;
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
}
