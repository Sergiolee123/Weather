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

public class UserLocation implements OnSuccessListener<Location> {
    //set the permission request code to 1
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private double lat, lon;
    String TAG = "location";
    //set up the thread pool to handle json, the pool size is 1
    protected static final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(1,
            1, 20, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            new ThreadPoolExecutor.AbortPolicy());


    //get user location
    protected void getCurrentLocation() {
        Log.e(TAG, "start location");
        //check whether the location access permission is granted
        if (ContextCompat.checkSelfPermission(
                ContextData.getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            //if the permission is not granted, request the permission
            ActivityCompat.requestPermissions(ContextData.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            Log.e(TAG, "fused location");
            //force the google map service API to update user's location
            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(60000);
            mLocationRequest.setFastestInterval(5000);
            //get the high accuracy location
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            //only request the location update for one time
            mLocationRequest.setNumUpdates(1);
            //get the requested user location result
            Boolean b;
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        //If cannot get the result, show error message
                        Toast toast = Toast.makeText(ContextData.getActivity(),
                                ContextData.getActivity().getText(R.string.no_location)
                                , Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            };
            //request update start
            LocationServices.getFusedLocationProviderClient(ContextData.getActivity()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            //use fusedLocationClient to get user last location
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(ContextData.getActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(this);
        }

    }

    @Override
    public void onSuccess(Location location) {
        Log.e(TAG, "fused location success" + location);
        //if the getLastLocation success
        if (location != null) {
            Log.e(TAG, "get location");
            lon = location.getLongitude();
            lat = location.getLatitude();
            //store the value of lon, lat to local data
            StoreLocalData storeLocalData = new StoreLocalData();
            storeLocalData.write(String.valueOf(lat), String.valueOf(lon));
            //start the Thread to handle json
            Future<Boolean> future = executorPool.submit(new LocalWeatherJson(String.valueOf(lat),
                    String.valueOf(lon)), true);
            //use future.get() to let the UI thread to wait for the data update finished
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //start the local weather activity from the main activity
            Intent intent = new Intent(ContextData.getActivity(), LocalWeatherActivity.class);
            ContextData.getActivity().startActivity(intent);

        } else if (location == null) {
            //if the location is null, retry to get the location
            Intent intent = new Intent(ContextData.getActivity(), MainActivity.class);
            ContextData.getActivity().startActivity(intent);
            ContextData.getActivity().finish();
        }

    }


}
