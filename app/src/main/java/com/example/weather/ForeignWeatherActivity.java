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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foreign_weather);
        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        WeatherList.cityName = new HashSet<>(sharedPreferences.getStringSet("CityName",null));
        Log.e("FWA","" + WeatherList.cityName.size());
        if(WeatherList.cityName != null) {
            for (String s : WeatherList.cityName) {
                WeatherList.updateWeather(s);
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Result");
            builder.setMessage("Please Add new city");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        RecyclerView recyclerView = findViewById(R.id.recycler_forecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ForeignWeatherAdapter(this, WeatherList.foreignWeatherList));
    }
    @Override
    protected void onStop() {
        super.onStop();
        StoreLocalData storeLocalData = new StoreLocalData();
        storeLocalData.write(WeatherList.cityName);
    }
}
