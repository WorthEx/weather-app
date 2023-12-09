package com.example.weatherapp.dataClasses;

import com.google.gson.annotations.SerializedName;

public class CurrentWeather {
    @SerializedName("temperature_2m")
    public int CTemp;
    @SerializedName("relative_humidity_2m")
    public int CHumidity;
    @SerializedName("apparent_temperature")
    public int CAppTemp;
    @SerializedName("weather_code")
    public int CWeatherCode;
    @SerializedName("cloud_cover")
    public int CCloudCover;
    @SerializedName("surface_pressure")
    public double CPressure;
    @SerializedName("wind_speed_10m")
    public double CWindSpeed;
    @SerializedName("is_day")
    public int CIsDay;
}
