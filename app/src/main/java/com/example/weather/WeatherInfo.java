package com.example.weather;

import android.annotation.SuppressLint;
@SuppressLint("DefaultLocale")
public class WeatherInfo{

    private String name;
    private String temp;
    private String feelsLike;
    private String tempMin;
    private String tempMax;
    private String weatherIcon;
    private String humidity;
    private String dayTime;
    private String windSpeed;
    private int timeZoneOffset;
    private String TAG = "WeatherInfo";

    public int getWeatherIcon() {
        if(weatherIcon.equals("Clear")){
            return R.drawable.ic__1d;
        }else if(weatherIcon.equals("Rain")){
            return R.drawable.ic_rain;
        }else if(weatherIcon.equals("Clouds")){

        }

            return R.drawable.ic__1d;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setTimeZoneOffset(int timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public String getWindSpeed() {
        return String.format("%.2f m/s WNW", (Double.parseDouble(windSpeed)));
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }


    public String getDayTimeByHour() {
        return Times.convertUnixToDate(Long.parseLong(dayTime), "hour", timeZoneOffset);
    }

    public String getDayTimeByDay(){
        return Times.convertUnixToDate(Long.parseLong(dayTime), "day", timeZoneOffset);
    }

    public String getDisplayDayTime(){
        return Times.convertUnixToDate(Long.parseLong(dayTime), "display", timeZoneOffset);
    }

    public String getDisplayHour(){
        return Times.convertUnixToDate(Long.parseLong(dayTime), "displayHour", timeZoneOffset);
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return String.format("%.1f", (Double.parseDouble(temp)-273));
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFeelsLike() {
        return String.format("%.0f °C", (Double.parseDouble(feelsLike)-273));
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getTempMin() {
        return String.format("%.0f", (Double.parseDouble(tempMin)-273));
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return String.format("%.0f", (Double.parseDouble(tempMax)-273));
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMinMax(){
        return getTempMin() + " °C- " + getTempMax() + " °C";
    }

    public String getHumidity() {
        return humidity + "%";
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }



}
