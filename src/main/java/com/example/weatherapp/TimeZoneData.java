package com.example.weatherapp;

public class TimeZoneData {
    String timeZone;
    String currentLocalTime;

    public String getTimeZone() {
        return timeZone;
    }

    public String getCurrentLocalTime() {
        return currentLocalTime;
    }


    TimeZoneData(String timeZone, String currentLocalTime) {
        this.timeZone = timeZone;
        this.currentLocalTime = currentLocalTime;
    }
}
