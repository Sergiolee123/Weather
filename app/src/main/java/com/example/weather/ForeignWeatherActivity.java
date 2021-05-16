package com.example.weather;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;

public class ForeignWeatherActivity extends AppCompatActivity {
    //This is the layout of foreign weather
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foreign_weather);
        //get all city name requested by user from local store
        SharedPreferences sharedPreferences = getSharedPreferences("CityData", Context.MODE_PRIVATE);
        //make sure the String set from local store is not null
        if (sharedPreferences.getStringSet("CityName", null) != null) {
            if (!sharedPreferences.getStringSet("CityName", null).isEmpty()) {
                //get the String set
                WeatherList.cityName = new HashSet<>(sharedPreferences.getStringSet("CityName", null));
            } else {
                //if the string set from local storage is created but empty, set the cityName String set to null
                WeatherList.cityName = null;
            }
        } else {
            //if the string set from local storage is null, set the cityName String set to null
            WeatherList.cityName = null;
        }
        //if the cityName String set is not null, update each city weather.
        if (WeatherList.cityName != null) {
            for (String s : WeatherList.cityName) {
                Log.e("FWK", s);
                WeatherList.updateWeather(s);
            }
            //set up the recyclerview for display all the city weather information that user requested
            RecyclerView recyclerView = findViewById(R.id.recycler_forecast);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ForeignWeatherAdapter(this, WeatherList.foreignWeatherList));
        } else {
            //if no city is requested, ask the user the add new city
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getText(R.string.no_result_alertdialog_title));
            builder.setMessage(getText(R.string.cityname_message_alertdialog));
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //store all the city name to local storage when the activity is closing
        StoreLocalData storeLocalData = new StoreLocalData();
        storeLocalData.write(WeatherList.cityName);
    }
}
