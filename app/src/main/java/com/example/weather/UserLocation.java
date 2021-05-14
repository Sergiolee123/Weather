package com.example.weather;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserLocation implements OnSuccessListener<Location>{
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private double lat, lon;
    String TAG = "location";
    protected static final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(1,
            1, 20, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            new ThreadPoolExecutor.AbortPolicy());




    protected void getCurrentLocation() {
        Log.e(TAG, "start location");
        if (ContextCompat.checkSelfPermission(
                ContextData.getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContextData.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            Log.e(TAG, "fused location");
            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setNumUpdates(1);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        Toast toast = Toast.makeText(ContextData.getActivity(),
                                "Cannot get your location"
                                , Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            Log.e(TAG, "request location success" + location);
                        }
                    }
                }
            };
            LocationServices.getFusedLocationProviderClient(ContextData.getActivity()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(ContextData.getActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(this);
        }

    }

    @Override
    public void onSuccess(Location location) {
        Log.e(TAG, "fused location success" + location);
        if(location != null){
            Log.e(TAG, "get location");
            lon = location.getLongitude();
            lat = location.getLatitude();
            StoreLocalData storeLocalData = new StoreLocalData();
            storeLocalData.write(String.valueOf(lat),String.valueOf(lon));
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
        }else if (location == null){
            Intent intent = new Intent(ContextData.getActivity(), MainActivity.class);
            ContextData.getActivity().startActivity(intent);
            ContextData.getActivity().finish();
        }

    }


}
