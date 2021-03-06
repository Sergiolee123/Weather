package com.example.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mgoogleMap;
    protected static Double lat = null;
    protected static Double lng = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map_fragment, container, false);
        //the mapView used in the fragment that requires View object to retrieve the mapView
        mMapView = (MapView) view.findViewById(R.id.google_map);

        initGoogleMap(savedInstanceState);

        return view;
    }

    //set up for the GoogleMap initialization
    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            //"MapViewBundleKey" is the Google API key for google map stored in values subfolder
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey");
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        mgoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //set the Marker title to show its Location
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                DecimalFormat df = new DecimalFormat("#.##");

                lat = latLng.latitude;
                lng = latLng.longitude;

                //Display the marker location when clicked the marker
                markerOptions.title(df.format(latLng.latitude) + "," + df.format(latLng.longitude));

                mgoogleMap.clear();
                //Add the Marker on the Map after clicking on the map once
                mgoogleMap.addMarker(markerOptions);
                //send data
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}