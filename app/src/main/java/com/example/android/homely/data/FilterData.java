package com.example.android.homely.data;

public class FilterData {
    private String property, priceMin, priceMax, bed, bath, areaMin, areaMax, propertyType;

    public FilterData(String property, String priceMin, String priceMax, String bed, String bath, String areaMin, String areaMax, String propertyType){
        this.property = property;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.bed = bed;
        this.bath = bath;
        this.areaMin = areaMin;
        this.areaMax = areaMax;
        this.propertyType = propertyType;
    }

    public String getProperty() {
        return property;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public String getBed() {
        return bed;
    }

    public String getBath() {
        return bath;
    }

    public String getAreaMin() {
        return areaMin;
    }

    public String getAreaMax() {
        return areaMax;
    }

    public String getPropertyType() {
        return propertyType;
    }
}
