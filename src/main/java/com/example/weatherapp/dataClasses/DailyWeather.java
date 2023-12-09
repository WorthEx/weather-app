package com.example.weatherapp.dataClasses;

import com.google.gson.annotations.SerializedName;

public class DailyWeather {
    @SerializedName("time")
    public String[] DTimeArray;
    @SerializedName("weather_code")
    public int[] DWeatherCodeArray;
    @SerializedName("temperature_2m_max")
    public double[] DMaxTempArray;
    @SerializedName("temperature_2m_min")
    public double[] DMinTempArray;
    @SerializedName("sunrise")
    public String[] DSunriseArray;
    @SerializedName("sunset")
    public String[] DSunsetArray;
    @SerializedName("uv_index_max")
    public double[] DUVIndexArray;
    @SerializedName("precipitation_probability_max")
    public int[] DPrecipitationProbabilityArray;
    @SerializedName("wind_speed_10m_max")
    public double[] DMaxWindSpeedArray;
}
