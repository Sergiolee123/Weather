package com.example.weather;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ForeignWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foreign_weather);
        WeatherList.updateWeather("London");
        RecyclerView recyclerView = findViewById(R.id.recycler_forecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ForeignWeatherAdapter(this, WeatherList.foreignWeatherList));
    }

}
