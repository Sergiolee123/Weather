package com.example.weather;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("DefaultLocale")
public class WeatherInfo{
    //data of the weather
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

    //return the weather icon for different weather condition
    public int getWeatherIcon() {
        if(weatherIcon.equals("01d")){
            return R.drawable.ic__1d;
        }else if(weatherIcon.equals("Rain")){
            return R.drawable.ic_rain;
        }else if(weatherIcon.equals("Clouds")){
            return R.drawable.ic_rain;
        }
            return R.drawable.ic__1d;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setTimeZoneOffset(int timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
    //return the speed of wind
    public String getWindSpeed() {
        return String.format("%.2f m/s WNW", (Double.parseDouble(windSpeed)));
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDayTimeByDay(){
        return convertUnixToDate(Long.parseLong(dayTime), "day", timeZoneOffset);
    }

    public String getDisplayDayTime(){
        return convertUnixToDate(Long.parseLong(dayTime), "display", timeZoneOffset);
    }

    public String getDisplayHour(){
        return convertUnixToDate(Long.parseLong(dayTime), "displayHour", timeZoneOffset);
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
        return String.format("%.0f", (Double.parseDouble(temp)-273));
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
        return String.format("%.1f", (Double.parseDouble(tempMin)-273));
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return String.format("%.1f", (Double.parseDouble(tempMax)-273));
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }
    //return the min temp and max temp
    public String getTempMinMax(){
        return getTempMin() + " - " + getTempMax() + " °C";
    }

    public String getHumidity() {
        return humidity + "%";
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }


    /*convert the unix time to different date format
    return the time when the data update*/
    @SuppressLint("SimpleDateFormat")
    public static String convertUnixToDate(long dt, String s, int offset) {
        SimpleDateFormat sdf;
        //multiply the daytime value from json by 1000 to convert to milliseconds
        Date date = new Date(dt*1000L);
        //set the format
        switch (s){
            case "day" :
                sdf = new SimpleDateFormat("dd/MM");
                break;
            case "display" :
                sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                break;
            case "displayHour" :
                sdf = new SimpleDateFormat("HH:mm");
                break;
            default :
                return "null";
        }
        /*multiply the offset value from json by 1000 to convert to milliseconds*
        get all the timezone ids of this offset value
         */
        String[] ids = TimeZone.getAvailableIDs(offset*1000);
        //set the timezone by the first returned id
        sdf.setTimeZone(TimeZone.getTimeZone(ids[0]));
        return sdf.format(date);
    }



}
