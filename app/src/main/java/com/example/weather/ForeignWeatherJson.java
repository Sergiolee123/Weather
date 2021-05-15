package com.example.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

//use Callable to return a weatherInfo object
public class ForeignWeatherJson extends WeatherJson implements Callable<WeatherInfo> {

    private String TAG = "WeatherInfo";
    //store the city name that need to perform information update
    private String city;
    private WeatherInfo w;

    //set up the city name
    public ForeignWeatherJson(String city) {
        this.city = city;
    }

    //override the abstract method to set up the request for update
    @Override
    protected String[] makeRequest() {
        StringBuilder weatherUrl = new StringBuilder();
        String apiKey = "6edd96d498ffab01ff671f39d92df7c1";
        //if the city name is not null, use String Builder to set the URL for getting data
        if (city != null) {
            weatherUrl.append("https://api.openweathermap.org/data/2.5/weather?q=")
                    .append(city).append("&appid=").append(apiKey);
        }


        //return the json
        return new String[]{getRequest(weatherUrl.toString())};
    }

    //use call() to return a weatherInfo object
    @Override
    public WeatherInfo call() {
        //get the responded json
        String[] response = makeRequest();

        Log.e(TAG, "Response from url: " + response[0]);
        //check whether the response is null
        if (response[0] != null) {
            try {
                //get the city name, update time, timezone of the city
                JSONObject jsonObj = new JSONObject(response[0]);
                String name = jsonObj.getString("name");
                String dayTime = jsonObj.getString("dt");
                String timeZoneOffset = jsonObj.getString("timezone");

                //get temperature, humidity
                JSONObject main = jsonObj.getJSONObject("main");
                String temp = main.getString("temp");
                String feelsLike = main.getString("feels_like");
                String tempMin = main.getString("temp_min");
                String tempMax = main.getString("temp_max");
                String humidity = main.getString("humidity");
                //get the Weather conditions e.g. rain, clear
                JSONArray weather = jsonObj.getJSONArray("weather");
                JSONObject ww = weather.getJSONObject(0);
                String weatherIcon = ww.getString("icon");

                //get the speed of wind
                JSONObject wind = jsonObj.getJSONObject("wind");
                String windSpeed = wind.getString("speed");

                // Create a new WeatherInfo object
                w = new WeatherInfo();
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

                // Add the object to the foreignWeatherList
                WeatherList.foreignWeatherList.add(w);

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error1: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            return null;
        }
        return w;


    }
}
