package com.example.weather;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;


public class FragmentA extends Fragment implements OnMapReadyCallback {


    private MapView mMapView;
    private GoogleMap mgoogleMap;
    private Button btn, addBtn;
    private static Double lat=null;
    private static Double lng=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_a, container, false);

        mMapView=(MapView)view.findViewById(R.id.google_map);
        btn=(Button)view.findViewById(R.id.btn);
        addBtn=(Button)view.findViewById(R.id.addBtn);



        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(lat!=null&&lng!=null){
                    WeatherInfo weatherInfo = WeatherList.updateMapWeather(String.valueOf(lat), String.valueOf(lng));
                    if(weatherInfo != null){
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FragmentA.super.getContext());
                            builder.setTitle(weatherInfo.getName());
                            builder.setMessage("Temp: " + weatherInfo.getTemp());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }catch (Exception e){
                                e.printStackTrace();
                        }
                    }

                }
            }
        });

        addBtn.setOnClickListener( v -> {
            WeatherInfo weatherInfo = WeatherList.updateMapWeather(String.valueOf(lat), String.valueOf(lng));
            if(weatherInfo != null){
                if(weatherInfo.getName().length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentA.super.getContext());
                    builder.setTitle("");
                    builder.setMessage("This location is not supported");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }

                WeatherList.cityName.add(weatherInfo.getName());
                StoreLocalData storeLocalData = new StoreLocalData();
                storeLocalData.write(WeatherList.cityName);
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentA.super.getContext());
                    builder.setTitle("");
                    builder.setMessage(weatherInfo.getName() + "is added to foreign weather");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        initGoogleMap(savedInstanceState);

        return view;
    }

    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if(savedInstanceState  !=null){
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey");
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap=googleMap;
        mgoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                DecimalFormat df = new DecimalFormat("#.##");

                lat= latLng.latitude;
                lng=latLng.longitude;


                 markerOptions.title(df.format(latLng.latitude)+ "," + df.format(latLng.longitude));

                mgoogleMap.clear();
                mgoogleMap.addMarker(markerOptions);
                //send data
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}