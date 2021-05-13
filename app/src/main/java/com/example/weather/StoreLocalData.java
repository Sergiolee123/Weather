package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class StoreLocalData {

    public boolean write(Set<String> s){


        SharedPreferences sharedPreferences= ContextData.getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putStringSet("CityName",s);

        return editor.commit();
    }


    public boolean write(String lat, String lon) {

        SharedPreferences sharedPreferences= ContextData.getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("lat",lat);

        editor.putString("lon",lon);

        return editor.commit();
    }
}
