package com.example.weather;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
@SuppressLint("ClickableViewAccessibility")
public class LocalWeatherActivity extends AppCompatActivity {

    private TextView tv_maintemp, tv_time, tv_temp, tv_humid, tv_feel, tv_wind, tv_location;
    private EditText cityInput;
    private Button foreign, addWeather, btn, addBtn;;
    private MapFragment a;
    private ScrollView scrollView;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    String TAG = "Local";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_weather);
        //finish the main activity
        ContextData.getActivity().finish();
        //set the activity reference to this class
        ContextData.setActivity(this);
        //views to show weather data
        tv_maintemp = (TextView) findViewById(R.id.tv_maintemp);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_humid = (TextView) findViewById(R.id.tv_humid);
        tv_feel = (TextView) findViewById(R.id.tv_feel);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        tv_location = (TextView) findViewById(R.id.tv_location);
        //button to open foreign weather activity
        foreign = (Button) findViewById(R.id.foreign);
        //button to add new foreign weather by edit text
        addWeather = (Button) findViewById(R.id.addWeather);
        //user input
        cityInput = (EditText) findViewById(R.id.cityInput);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        //button for google map
        btn = (Button) findViewById(R.id.btn);
        addBtn = (Button) findViewById(R.id.addBtn);
        ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

        /*override the motion control of the scroll view by a image view for the google map
        , allow the user move the google map vertically in the scroll view.
         */
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                    case MotionEvent.ACTION_MOVE:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    default:
                        return true;
                }
            }
        });

        //get the weatherInfo reference of current location
        WeatherInfo weatherInfo = WeatherList.getWeather();

        Log.e(TAG, String.valueOf(weatherInfo));
        //set the data if weatherInfo is not null
        if (weatherInfo != null) {
            tv_location.setText(weatherInfo.getName());
            tv_maintemp.setText(weatherInfo.getTemp());
            tv_time.setText(weatherInfo.getDisplayDayTime());
            tv_temp.setText(weatherInfo.getTempMinMax());
            tv_humid.setText(weatherInfo.getHumidity());
            tv_feel.setText(weatherInfo.getFeelsLike());
            tv_wind.setText(weatherInfo.getWindSpeed());
        }
        //create a array list to store the weatherInfo object for next five days
        ArrayList<WeatherInfo> w = new ArrayList<>();
        for(int i = 1; i<=5; i++){
            Calendar cal = Calendar.getInstance();
            //use to get day of next five day, add the weatherInfo object from current day +1 to +5
            cal.add(Calendar.DATE, + i);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            //set the timezone to user default timezone
            sdf.setTimeZone(TimeZone.getDefault());
            String Date = sdf.format(cal.getTime());
            //use the date to get the weatherInfo object and store it to the array list
            WeatherInfo fiveDayInfo = WeatherList.getWeather(Date);
            Log.e(TAG,fiveDayInfo + "");
            if(fiveDayInfo != null){
                w.add(fiveDayInfo);
            }else{
                /*if the Weather info of the API is not updated to the newest vision,
                add current date data*/
                java.util.Date d = new Date();
                sdf.setTimeZone(TimeZone.getDefault());
                Date = sdf.format(d);
                w.add(0,WeatherList.getWeather(Date));
            }

        }
        //set the recycler view for 3 hourly weather
        RecyclerView recyclerView = findViewById(R.id.recycler_local);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LocalWeatherAdapter(this, WeatherList.localWeatherList,33));
        //set the recycler view for 5 day weather
        RecyclerView fiveDayRecyclerView = findViewById(R.id.recycler_local5day);
        fiveDayRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fiveDayRecyclerView.setAdapter(new LocalWeatherAdapter(this, w,0));
        //set the google app fragment view
        a=new MapFragment();
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().add(R.id.mapView,a).commitAllowingStateLoss();



        //open the foreign weather activity after click
        foreign.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForeignWeatherActivity.class);
            this.startActivity(intent);
        });

        addWeather.setOnClickListener(v -> {
            //get the user input
            String input = cityInput.getText().toString();
            //show error message if the user input is empty
            if(input.length() == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getText(R.string.no_result_alertdialog_title));
                builder.setMessage(getText(R.string.empty_city_input_message));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return;
            }
            //check whether the API supports the user inputted city
            WeatherInfo weatherInfo1 = WeatherList.updateWeather(input);
            //if the api support, add the city name to String set
            if(weatherInfo1 != null) {
                Log.e("TAG", input);
                WeatherList.cityName.add(weatherInfo1.getName());
                StoreLocalData sl = new StoreLocalData();
                //store the new city name to local data
                sl.write(WeatherList.cityName);
                //clear the edit text
                cityInput.setText("");
                //remove all object of the foreign weather list for next clear update
                ArrayList<WeatherInfo> www = new ArrayList<>();
                www.addAll(WeatherList.foreignWeatherList);
                WeatherList.foreignWeatherList.removeAll(www);

            }else{
                //if the city is not supported by the API, show an error message
                cityInput.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getText(R.string.no_result_alertdialog_title));
                builder.setMessage(input + getText(R.string.not_supported));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        //shows the weather information from the location which the user clicked on google map
        btn.setOnClickListener( v ->{
                //check whether user click a location on the google map, if true, update the location
                if(MapFragment.lat!=null&& MapFragment.lng!=null){
                    WeatherInfo weatherInfo1 =
                            WeatherList.updateMapWeather(String.valueOf(MapFragment.lat), String.valueOf(MapFragment.lng));
                    if(weatherInfo1 != null){
                        //if the weather information from API is null, show it to the user
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(weatherInfo1.getName());
                            builder.setMessage( getText(R.string.temperature) + ": " + weatherInfo1.getTemp());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }

        });
        //add the city name of the user clicked google map location to the city name String set
        addBtn.setOnClickListener( v -> {
            WeatherInfo mapWeatherInfo =
                    WeatherList.updateMapWeather(String.valueOf(MapFragment.lat), String.valueOf(MapFragment.lng));
            //check whether the API supports the user inputted city
            if(mapWeatherInfo != null){
                //if there is no city name return, than this city is not supported
                if(mapWeatherInfo.getName().length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("");
                    builder.setMessage(getText(R.string.location_not_support));
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
                //if supported, add it to the String set and update the local storage
                WeatherList.cityName.add(mapWeatherInfo.getName());
                StoreLocalData storeLocalData = new StoreLocalData();
                storeLocalData.write(WeatherList.cityName);
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("");
                    builder.setMessage(mapWeatherInfo.getName() + getText(R.string.is_added));
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                //if the API don't response anything, show error message
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("");
                builder.setMessage(getText(R.string.no_data_return));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //update all the widgets if the user open this activity again
        Intent intent = new Intent(this, DesktopWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        //get all the widget IDs
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), DesktopWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
        //open the main activity again to perform update
        Intent intent2 = new Intent(this, MainActivity.class);
        this.startActivity(intent2);
        //close this activity
        finish();
    }

    //same as main activity, use to handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    CharSequence msg = getText(R.string.permission_message);
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
        //update the local data while activity closing
        StoreLocalData storeLocalData = new StoreLocalData();
        storeLocalData.write(WeatherList.cityName);
    }
}
