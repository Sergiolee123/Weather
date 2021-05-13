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

public class WeatherList{
    static CopyOnWriteArrayList<WeatherInfo> foreignWeatherList = new CopyOnWriteArrayList<>();
    static CopyOnWriteArrayList<WeatherInfo> localWeatherList = new CopyOnWriteArrayList<>();
    static Set<String> cityName = new HashSet<>();
    static String TAG = "WeatherList";
    protected static final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(3,
            5, 20, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            new ThreadPoolExecutor.AbortPolicy());

    public static WeatherInfo getWeather(){
            return localWeatherList.get(0);
    }

    public static WeatherInfo getWeather(String dayTime){

            for (WeatherInfo w : localWeatherList) {
                if (w.getDayTimeByDay().equals(dayTime))
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

    public static Boolean updateWeather(String city){

        Boolean s;
        try {
            Future<Boolean> future = executorPool.submit(new ForeignWeatherJson(city));
            s = future.get();
            Log.e(TAG, "future: " + s);
        } catch (Exception e) {
            s = false;
            e.printStackTrace();
        }

        return s;
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


            ArrayList<WeatherInfo> delete = new ArrayList<>();

             delete.addAll(foreignWeatherList);

             if(!delete.isEmpty()) {
                 Log.e(TAG,"removing weatherList");
                 foreignWeatherList.removeAll(delete);
             }


             delete.addAll(localWeatherList);

            if(!delete.isEmpty()){
                Log.e(TAG,"removing LocalWeatherList");
                b = localWeatherList.removeAll(delete);
            }
            return b;
            });

         try {
             future.get();
         }catch (Exception e){
             e.printStackTrace();
         }

    }



}
