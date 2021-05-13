package com.example.weather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class LocalWeatherActivity extends AppCompatActivity {

    private TextView tv_maintemp, tv_time, tv_temp, tv_humid, tv_feel, tv_wind, tv_location;
    private EditText cityInput;
    private Button foreign, addWeather;
    private FragmentA a;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    String TAG = "Local";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_weather);
        ContextData.getActivity().finish();
        ContextData.setActivity(this);
        tv_maintemp = (TextView) findViewById(R.id.tv_maintemp);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_humid = (TextView) findViewById(R.id.tv_humid);
        tv_feel = (TextView) findViewById(R.id.tv_feel);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_location = (TextView) findViewById(R.id.tv_location);
        foreign = (Button) findViewById(R.id.foreign);
        addWeather = (Button) findViewById(R.id.addWeather);
        cityInput = (EditText) findViewById(R.id.cityInput);


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
        ArrayList<WeatherInfo> w = new ArrayList<>();
        for(int i = 1; i<=5; i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, + i);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            sdf.setTimeZone(TimeZone.getDefault());
            String Date = sdf.format(cal.getTime());
            w.add(WeatherList.getWeather(Date));
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_local);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LocalWeatherAdapter(this, WeatherList.localWeatherList,33));

        RecyclerView fiveDayRecyclerView = findViewById(R.id.recycler_local5day);
        fiveDayRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fiveDayRecyclerView.setAdapter(new LocalWeatherAdapter(this, w,0));

        a=new FragmentA();
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_1,a).commitAllowingStateLoss();




        foreign.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForeignWeatherActivity.class);
            this.startActivity(intent);
        });

        addWeather.setOnClickListener(v -> {
            String input = cityInput.getText().toString().replace(" ","");
            if(input.length() == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No Result");
                builder.setMessage("Please input a city name.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return;
            }
            if(WeatherList.updateWeather(input)) {
                Log.e("TAG", input);
                WeatherList.cityName.add(input);
                StoreLocalData sl = new StoreLocalData();
                sl.write(WeatherList.cityName);
                cityInput.setText("");
                for(WeatherInfo wi: WeatherList.foreignWeatherList){
                    WeatherList.foreignWeatherList.remove(wi);
                }

            }else{
                cityInput.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No Result");
                builder.setMessage(input + "is not supported");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    String msg = "Please run the app again and grant the required permission.";
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        StoreLocalData storeLocalData = new StoreLocalData();
        storeLocalData.write(WeatherList.cityName);
    }
}
