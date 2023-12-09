package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

class Location {
    @SerializedName("display_name")
    protected String name;
    @SerializedName("lat")
    protected double latitude;
    @SerializedName("lon")
    protected double longitude;

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void minimizeName() {
        if (name.indexOf(',') != -1) {
            String[] nameParts = name.split(",");
            name = nameParts[0] + ", " + nameParts[nameParts.length - 1];
        }
    }
}
