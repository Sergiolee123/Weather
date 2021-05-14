package com.example.weather;




import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    String TAG = "main";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the loading screen
        setContentView(R.layout.activity_main);
        //set the reference of this activity for classes that do not extents AppCompatActivity
        //e.g. UserLocation class
        ContextData.setActivity(this);
        //remove all old weather information data
        WeatherList.removeOutdatedData();
        Log.e(TAG, "try to update weather");
        //update weather information data by user current location
        WeatherList.updateWeather();

    }


    //use to receive permissions request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //use to receive permissions request result for getting user location in UserLocation class
            case MY_PERMISSIONS_REQUEST_LOCATION:
                //if user didn't give the permission, tell the user to give it and close the app
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    String msg = "Please run the app again and grant the required permission.";
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }else{
                    //if user gives permission, restart the mainActivity class again
                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);
                }
                finish();
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}