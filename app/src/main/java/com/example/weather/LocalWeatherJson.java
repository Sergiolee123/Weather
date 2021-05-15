package com.example.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocalWeatherJson extends WeatherJson implements Runnable {

    private static final String TAG = "LocalWeatherJson";
    private String lat, lon;

    //for normal weather update
    public LocalWeatherJson(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;

    }

    protected String[] makeRequest() {
        String[] response;
        String apiKey = "6edd96d498ffab01ff671f39d92df7c1";

        //for app data update, get both current and 5 day 3 hourly forecast update, set up the API URL
        response = new String[2];
        StringBuilder weatherUrl = new StringBuilder();
        StringBuilder forecastUrl = new StringBuilder();
        weatherUrl.append("https://api.openweathermap.org/data/2.5/weather?lat=")
                .append(lat).append("&lon=").append(lon)
                .append("&appid=").append(apiKey);
        forecastUrl.append("https://api.openweathermap.org/data/2.5/forecast?lat=")
                .append(lat).append("&lon=").append(lon)
                .append("&appid=").append(apiKey);
        response[0] = getRequest(weatherUrl.toString());
        response[1] = getRequest(forecastUrl.toString());


        return response;
    }

    @Override
    public void run() {
        String[] response = makeRequest();
        String weatherStr = null;
        String forecastStr = null;
        //for widget data update, only get the current local time response
        if (response != null) {
            //for app data update, only get the current and forecast local time response
            weatherStr = response[0];
            forecastStr = response[1];
        }

        Log.e(TAG, "Response from url: " + weatherStr);

        if (forecastStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(forecastStr);

                JSONObject city = jsonObj.getJSONObject("city");
                String name = city.getString("name");
                String timeZoneOffset = city.getString("timezone");

                // Getting JSON Array node
                JSONArray list = jsonObj.getJSONArray("list");

                for (int i = 0; i < list.length(); i++) {
                    JSONObject w = list.getJSONObject(i);
                    String dayTime = w.getString("dt");

                    JSONObject main = w.getJSONObject("main");
                    String temp = main.getString("temp");
                    String feelsLike = main.getString("feels_like");
                    String tempMin = main.getString("temp_min");
                    String tempMax = main.getString("temp_max");
                    String humidity = main.getString("humidity");

                    JSONArray weather = w.getJSONArray("weather");
                    JSONObject w2 = weather.getJSONObject(0);
                    String weatherIcon = w2.getString("icon");

                    JSONObject wind = w.getJSONObject("wind");
                    String windSpeed = wind.getString("speed");

                    //Set the WeatherInfo object
                    WeatherInfo w1 = new WeatherInfo();
                    w1.setName(name);
                    w1.setTemp(temp);
                    w1.setFeelsLike(feelsLike);
                    w1.setTempMin(tempMin);
                    w1.setTempMax(tempMax);
                    w1.setWeatherIcon(weatherIcon);
                    w1.setHumidity(humidity);
                    w1.setDayTime(dayTime);
                    w1.setWindSpeed(windSpeed);
                    w1.setTimeZoneOffset(Integer.parseInt(timeZoneOffset));
                    // Add the object to the localWeatherList
                    WeatherList.localWeatherList.add(w1);

                }


            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error2: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }

        if (weatherStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(weatherStr);
                String name = jsonObj.getString("name");
                String dayTime = jsonObj.getString("dt");
                String timeZoneOffset = jsonObj.getString("timezone");
                Log.e(TAG, "timeoff" + timeZoneOffset);

                JSONArray weather = jsonObj.getJSONArray("weather");
                JSONObject ww = weather.getJSONObject(0);
                String weatherIcon = ww.getString("icon");


                JSONObject main = jsonObj.getJSONObject("main");
                String temp = main.getString("temp");
                String feelsLike = main.getString("feels_like");
                String tempMin = main.getString("temp_min");
                String tempMax = main.getString("temp_max");
                String humidity = main.getString("humidity");


                JSONObject wind = jsonObj.getJSONObject("wind");
                String windSpeed = wind.getString("speed");

                // Create a new WeatherInfo object
                WeatherInfo w = new WeatherInfo();
                w.setName(name);
                w.setTemp(temp);
                w.setFeelsLike(feelsLike);
                w.setTempMin(tempMin);
                w.setTempMax(tempMax);
                w.setWeatherIcon(weatherIcon);
                w.setHumidity(humidity);
                w.setDayTime(dayTime);
                w.setWindSpeed(windSpeed);
                w.setTimeZoneOffset(Integer.parseInt(timeZoneOffset));

                // Add the current weatherInfo object to the index 0 of localWeatherList
                WeatherList.localWeatherList.add(0, w);

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error1: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }
}
