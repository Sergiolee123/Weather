package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class StoreLocalData {

    public boolean write(Set<String> s) {
        //get a shared preferences object with name CityData, if there is no such object, create a new one
        SharedPreferences sharedPreferences = ContextData.getActivity().getSharedPreferences("CityData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //write the String set value into the shared preferences
        editor.putStringSet("CityName", s);
        //commit the change
        return editor.commit();
    }


    public boolean write(String lat, String lon) {
        //get a shared preferences object with name LocationData, if there is no such object, create a new one
        SharedPreferences sharedPreferences = ContextData.getActivity().getSharedPreferences("LocationData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //write the lat and lon value into the shared preferences
        editor.putString("lat", lat);
        editor.putString("lon", lon);
        //commit the change
        return editor.commit();
    }
}
