package com.example.weather;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WeatherList {
    /*Use CopyOnWriteArrayList to avoid java.util.concurrentModificationException
     foreignWeatherList to store Weather information of foreign weather
     localWeatherList to store Weather information of local weather*/
    static CopyOnWriteArrayList<WeatherInfo> foreignWeatherList = new CopyOnWriteArrayList<>();
    static CopyOnWriteArrayList<WeatherInfo> localWeatherList = new CopyOnWriteArrayList<>();
    //Contain the foreign city name that set by user
    static Set<String> cityName = new HashSet<>();
    static String TAG = "WeatherList";
    //Thread pool to handle json request, open 3 thread, max 5 thread
    protected static final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(3,
            5, 20, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            new ThreadPoolExecutor.AbortPolicy());

    //get the current local data
    public static WeatherInfo getWeather() {
        if (!localWeatherList.isEmpty())
            //the current local data always stores at index 0
            return localWeatherList.get(0);
        else
            return null;
    }

    //get a weatherInfo object by specify date
    public static WeatherInfo getWeather(String dayTime) {

        for (WeatherInfo w : localWeatherList) {
            if (w.getDayTimeByDay().equals(dayTime))
                return w;

        }

        return null;
    }


    //Update the location data
    public static void updateWeather() {
        //update the user location
        UserLocation userLocation = new UserLocation();
        userLocation.getCurrentLocation();


    }

    //Update the weather data by city name, return the weatherInfo object
    public static WeatherInfo updateWeather(String city) {

        WeatherInfo s;
        //use future.get() to let the UI thread to wait for the data update finished
        try {
            Future<WeatherInfo> future = executorPool.submit(new ForeignWeatherJson(city));
            s = future.get();
            Log.e(TAG, "future: " + s);
        } catch (Exception e) {
            s = null;
            e.printStackTrace();
        }

        return s;
    }

    //update weather data by latitude and longitude
    public static void updateWeather(String lat, String lon) {

        //use future.get() to let the UI thread to wait for the data update finished
        try {
            Future<Boolean> future = executorPool.submit(new LocalWeatherJson(lat, lon), true);
            future.get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update weather data by latitude and longitude, return a WeatherInfo object
    public static WeatherInfo updateMapWeather(String lat, String lon) {

        WeatherInfo s;
        //use future.get() to let the UI thread to wait for the data update finished
        try {
            Future<WeatherInfo> future = executorPool.submit(new MapWeatherJson(lat, lon));
            s = future.get();
            Log.e(TAG, "future: " + s);
        } catch (Exception e) {
            s = null;
            e.printStackTrace();
        }
        return s;
    }


    public static void removeOutdatedData() {

        Future<Boolean> future = executorPool.submit(() -> {
            boolean b = false;

            //store the reference of the object that will be deleted
            ArrayList<WeatherInfo> delete = new ArrayList<>();

            delete.addAll(foreignWeatherList);

            if (!delete.isEmpty()) {
                Log.e(TAG, "removing weatherList");
                foreignWeatherList.removeAll(delete);
            }


            delete.addAll(localWeatherList);

            if (!delete.isEmpty()) {
                Log.e(TAG, "removing LocalWeatherList");
                b = localWeatherList.removeAll(delete);
            }
            return b;
        });
        //use future.get() to let the UI thread to wait for the data delete finished
        try {
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
