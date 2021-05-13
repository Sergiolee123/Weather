package com.example.weather;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

public class MapWeatherJson extends WeatherJson implements Callable<WeatherInfo> {

    final String TAG = "Map";
    private  String lat, lon;

    public MapWeatherJson(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    protected String[] makeRequest() {
        StringBuilder weatherUrl = new StringBuilder();
        String apiKey = "6edd96d498ffab01ff671f39d92df7c1";

        if(lat != null & lon != null){
            weatherUrl.append("http://api.openweathermap.org/data/2.5/weather?lat=")
                    .append(lat).append("&lon=").append(lon)
                    .append("&appid=").append(apiKey);
        }


        return new String[]{getRequest(weatherUrl.toString())};
    }

    @Override
    public WeatherInfo call(){

        String[] response = makeRequest();


        Log.e(TAG, "Response from url: " + response[0]);

        if (response[0] != null) {
            try {
                JSONObject jsonObj = new JSONObject(response[0]);
                String name = jsonObj.getString("name");
                String dayTime = jsonObj.getString("dt");
                String timeZoneOffset = jsonObj.getString("timezone");

                // Getting JSON
                JSONObject main = jsonObj.getJSONObject("main");
                String temp = main.getString("temp");
                String feelsLike = main.getString("feels_like");
                String tempMin = main.getString("temp_min");
                String tempMax = main.getString("temp_max");
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                // JSON Object
                JSONObject wind = jsonObj.getJSONObject("wind");
                String windSpeed = wind.getString("speed");

                // Create a new WeatherInfo object
                WeatherInfo w = new WeatherInfo();
                w.setName(name);
                w.setTemp(temp);
                w.setFeelsLike(feelsLike);
                w.setTempMin(tempMin);
                w.setTempMax(tempMax);
                w.setPressure(pressure);
                w.setHumidity(humidity);
                w.setDayTime(dayTime);
                w.setWindSpeed(windSpeed);
                w.setTimeZoneOffset(Integer.parseInt(timeZoneOffset));

                return w;
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error1: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }
}