package com.example.weather;



import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class WeatherList{
    static CopyOnWriteArrayList<WeatherInfo> foreignWeatherList = new CopyOnWriteArrayList<>();
    static CopyOnWriteArrayList<WeatherInfo> localWeatherList = new CopyOnWriteArrayList<>();
    static String TAG = "WeatherList";
    protected static final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(3,
            5, 20, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            new ThreadPoolExecutor.AbortPolicy());

    public static WeatherInfo getWeather(){
        //Use synchronized to avoid ConcurrentModificationException while looping a vector
            return localWeatherList.get(0);
    }

    public static WeatherInfo getWeather(String dayTime){
        //Use synchronized to avoid ConcurrentModificationException while looping a vector

            for (WeatherInfo w : localWeatherList) {
                if (w.getDayTimeByHour().equals(dayTime))
                    return w;

            }

        return null;
    }

    public static WeatherInfo getWeather(String name, String dayTime){
        //Use synchronized to avoid ConcurrentModificationException while looping a vector

            for (WeatherInfo w : foreignWeatherList) {
                if (w.getName().equals(name) && w.getDayTimeByHour().equals(dayTime))
                    return w;
            }

        return null;
    }
    //Update the localWeather
    public static void updateWeather(){

        executorPool.execute(() -> {
            UserLocation userLocation = new UserLocation();
            userLocation.getCurrentLocation();
        });

    }

    public static void updateWeather(String city){

        Boolean s;
        try {
            Future<Boolean> future = executorPool.submit(new ForeignWeatherJson(city), true);
            s = future.get();
            Log.e(TAG, "future: " + s);
        } catch (Exception e) {
            s = false;
            e.printStackTrace();
        }
    }

    public static void updateWeather(String lat, String lon) {


        try {
            Future<Boolean> future = executorPool.submit(new LocalWeatherJson(lat, lon,true),true);
            future.get();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WeatherInfo updateMapWeather(String lat, String lon) {

        WeatherInfo s;
        try {
            Future<WeatherInfo> future = executorPool.submit(new MapWeatherJson(lat, lon));
            s = future.get();
            Log.e(TAG, "future: " + s);
        } catch (Exception e) {
            s = null;
            e.printStackTrace();
        }
        return  s;
    }


    public static void removeOutdatedData(){

         Future<Boolean> future = executorPool.submit(() -> {
            Boolean b = false;
           /* Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
            sdf.setTimeZone(TimeZone.getDefault());
            String outDate = sdf.format(cal.getTime());
            Log.e(TAG,"out day is " + outDate);*/

            //Use a array to avoid ConcurrentModificationException for deleting
            ArrayList<WeatherInfo> delete = new ArrayList<>();

                for (WeatherInfo w : foreignWeatherList) {
                    delete.add(w);
                }

             if(!delete.isEmpty()) {
                 Log.e(TAG,"removing weatherList");
                 foreignWeatherList.removeAll(delete);
             }


                for (WeatherInfo w : localWeatherList) {
                    Log.e(TAG, "All outDate " + w.getDayTimeByDay());
                    delete.add(w);
                }

            if(!delete.isEmpty()){
                Log.e(TAG,"removing LocalWeatherList");
                b = localWeatherList.removeAll(delete);
            }
            return b;
            });

         try {
             Log.e(TAG,"OutDate " + future.get().toString());
         }catch (Exception e){
             e.printStackTrace();
         }

    }



}
