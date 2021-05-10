package com.example.weather;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.renderscript.RenderScript;
import android.util.Log;




import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

public class UserLocation implements OnSuccessListener<Location>{
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private double lat, lon;
    protected static final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(3,
            5, 20, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            new ThreadPoolExecutor.AbortPolicy());




    protected void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(
                ContextData.getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContextData.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(ContextData.getActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(this);
        }

    }

    @Override
    public void onSuccess(Location location) {
        if(location != null){
            lon = location.getLongitude();
            lat = location.getLatitude();
            DesktopWidget.lon = String.valueOf(lon);
            DesktopWidget.lat = String.valueOf(lat);
            Future<Boolean> future = executorPool.submit(new LocalWeatherJson(String.valueOf(lat),
                    String.valueOf(lon)),true);
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                Intent intent = new Intent(ContextData.getActivity(), LocalWeatherActivity.class);
                ContextData.getActivity().startActivity(intent);
            }

            Log.e("Location", lat+" "+lon);
        }

    }


}
