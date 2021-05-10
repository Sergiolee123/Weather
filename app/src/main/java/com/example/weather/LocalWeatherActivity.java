package com.example.weather;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocalWeatherActivity extends AppCompatActivity {

    private TextView tv_maintemp, tv_time, tv_temp, tv_humid, tv_feel, tv_wind, tv_location
            , txt_mapTemp;
    private Button foreign;
    private FragmentA a;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    String TAG = "Local";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_weather);
        ContextData.getActivity().finish();
        tv_maintemp = (TextView) findViewById(R.id.tv_maintemp);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_humid = (TextView) findViewById(R.id.tv_humid);
        tv_feel = (TextView) findViewById(R.id.tv_feel);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_location = (TextView) findViewById(R.id.tv_location);
        foreign = (Button) findViewById(R.id.foreign);


        WeatherInfo weatherInfo = WeatherList.getWeather();

        Log.e(TAG, "date: " + Times.getFormatCurrentTime());
        Log.e(TAG, String.valueOf(weatherInfo));

        if (weatherInfo != null) {
            tv_location.setText(weatherInfo.getName());
            tv_maintemp.setText(weatherInfo.getTemp());
            tv_time.setText(weatherInfo.getDisplayDayTime());
            tv_temp.setText(weatherInfo.getTempMinMax());
            tv_humid.setText(weatherInfo.getHumidity());
            tv_feel.setText(weatherInfo.getFeelsLike());
            tv_wind.setText(weatherInfo.getWindSpeed());
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_local);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LocalWeatherAdapter(this, WeatherList.localWeatherList));

        a=new FragmentA();
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_1,a).commitAllowingStateLoss();




        foreign.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForeignWeatherActivity.class);
            this.startActivity(intent);
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Intent intent = new Intent(this, DesktopWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), DesktopWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
        Intent intent2 = new Intent(this, MainActivity.class);
        this.startActivity(intent2);
        finish();
    }






}
