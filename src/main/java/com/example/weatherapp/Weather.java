package com.example.weatherapp;

import com.example.weatherapp.dataClasses.CurrentWeather;
import com.example.weatherapp.dataClasses.DailyWeather;
import com.example.weatherapp.dataClasses.HourlyWeather;

public class Weather {
    private final CurrentWeather CW;
    private final DailyWeather DW;
    private final HourlyWeather HW;

    public Weather(CurrentWeather CW, DailyWeather DW, HourlyWeather HW) {
        this.CW = CW;
        this.DW = DW;
        this.HW = HW;
    }

    public int getCTemp() {
        return CW.CTemp;
    }

    public int getCAppTemp() {
        return CW.CAppTemp;
    }

    public int getCHumidity() {
        return CW.CHumidity;
    }

    public int getCIsDay() {
        return CW.CIsDay;
    }

    public int getCWeatherCode() {
        return CW.CWeatherCode;
    }

    public int getCCloudCover() {
        return CW.CCloudCover;
    }

    public double getCPressure() {
        return CW.CPressure;
    }

    public double getCWindSpeed() {
        return CW.CWindSpeed;
    }

    public String[] getDTimeArray() {
        return DW.DTimeArray;
    }

    public int[] getDWeatherCodeArray() {
        return DW.DWeatherCodeArray;
    }

    public double[] getDMaxTempArray() {
        return DW.DMaxTempArray;
    }

    public double[] getDMinTempArray() {
        return DW.DMinTempArray;
    }

    public String[] getDSunriseArray() {
        return DW.DSunriseArray;
    }

    public String[] getDSunsetArray() {
        return DW.DSunsetArray;
    }

    public double[] getDUVIndexArray() {
        return DW.DUVIndexArray;
    }

    public int[] getDPrecipitationProbabilityArray() {
        return DW.DPrecipitationProbabilityArray;
    }

    public double[] getDMaxWindSpeedArray() {
        return DW.DMaxWindSpeedArray;
    }

    public String[] getHTimeArray() {
        return HW.HTimeArray;
    }

    public double[] getHTempArray() {
        return HW.HTempArray;
    }

    public int[] getHWeatherCodeArray() {
        return HW.HWeatherCodeArray;
    }

    public double[] getHWindSpeedArray() {
        return HW.HWindSpeedArray;
    }
}
