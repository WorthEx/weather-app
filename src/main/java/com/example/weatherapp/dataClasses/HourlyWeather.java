package com.example.weatherapp.dataClasses;

import com.google.gson.annotations.SerializedName;

public class HourlyWeather {
    @SerializedName("time")
    public String[] HTimeArray;
    @SerializedName("temperature_2m")
    public double[] HTempArray;
    @SerializedName("weather_code")
    public int[] HWeatherCodeArray;
    @SerializedName("wind_speed_10m")
    public double[] HWindSpeedArray;
}
