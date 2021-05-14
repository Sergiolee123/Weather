package com.example.weather;

import android.app.Activity;
import android.app.Application;

public class ContextData extends Application {
    //To hold a reference of an activity
    private static Activity mActivity;
    //get the reference of an activity
    public static Activity getActivity() {
        return mActivity;
    }
    //set the reference of an activity
    public static void setActivity(Activity activity) {
        mActivity = activity;
    }

}

