package com.example.weather;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class ContextData extends Application {
    private static Activity mActivity;

    public static Activity getActivity() {
        return mActivity;
    }

    public static void setActivity(Activity activity) {
        mActivity = activity;
    }

}

