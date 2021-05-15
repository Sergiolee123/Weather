package com.example.weather;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public abstract class WeatherJson {
    private static final String TAG = "WeatherJson";

    //get the json data from the URL
    protected String getRequest(String jsonUrl) {
        String response = null;
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection weatherConn = (HttpURLConnection) url.openConnection();
            weatherConn.setRequestMethod("GET");

            // Read the response
            InputStream weatherInput = new BufferedInputStream(weatherConn.getInputStream());
            response = inputStreamToString(weatherInput);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;

    }

    //read the response and convert to String
    protected String inputStreamToString(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder result = new StringBuilder();

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                result.append(line).append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
        }
        return result.toString();
    }

    //subClass must override makeRequest method
    protected abstract String[] makeRequest();


}

