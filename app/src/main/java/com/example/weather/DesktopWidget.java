package com.example.weather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;


//This class is to set up the desktop widget
public class DesktopWidget extends AppWidgetProvider {
    //String UpDate is for setAction of the intent to update the information of widget
    private static final String UpDate = "update";
    //String lat, lon is to get the lat and lon for update information
    protected static String lat, lon;

    //update an app widget
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.desktop_widget);
        //use thread to handle update
        new Thread(() -> {
            WeatherInfo weatherInfo;
            lat = null;
            lon = null;
            int i = 0;
            while (lat == null && lon == null) {
                //the widget won't update the location, only the application will update it.
                //sleep for one second to wait the application get the location
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //get the location from local storage that stored by the application
                SharedPreferences sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
                lat = sharedPreferences.getString("lat", null);
                lon = sharedPreferences.getString("lon", null);
                i++;
                //if widget can't get location after trying 10 times, open the app to update location
                if (i > 10) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    break;
                }
            }
            i = 0;
            do {
                //get the reference of the local weather information
                weatherInfo = WeatherList.updateMapWeather(lat, lon);
                //if the reference is not null, exit the while loop
                if (weatherInfo != null)
                    break;
                //update the weather by last saved latitude and longitude
                i++;
                //if the weather is still null after trying 10 times to update, show error message
                if (i > 10) {
                    views.setTextViewText(R.id.appwidget_Location, context.getText(R.string.widget_error));
                    views.setTextViewText(R.id.appwidget_temp, "");
                    views.setTextViewText(R.id.appwidget_updateTime, "");
                    views.setImageViewResource(R.id.widgetLocalImage, android.R.color.transparent);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                    return;
                }
                //wait for 1 second before next update
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } while (true);
            //set the new information to the widget
            views.setTextViewText(R.id.appwidget_Location, weatherInfo.getName() + " ");
            views.setTextViewText(R.id.appwidget_temp, weatherInfo.getTemp() + "°C ");
            views.setTextViewText(R.id.appwidget_updateTime
                    , context.getText(R.string.widget_last_update) + weatherInfo.getDisplayDayTime());
            views.setImageViewResource(R.id.widgetLocalImage, weatherInfo.getWeatherIcon());
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }).start();


        //set a intent for update information
        Intent intent = new Intent(context, DesktopWidget.class);
        intent.setAction(UpDate);
        //put the appWidgetId that needs to be update to the intent extra date.
        intent.putExtra("id", appWidgetId);
        Log.e("DWID", String.valueOf(intent.getIntExtra("id", 0)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //if the user click the time on the widget, the widget will update
        views.setOnClickPendingIntent(R.id.appwidget_updateTime, pendingIntent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //if the user click the time on widget, call updateAppWidget to perform update
        if (UpDate.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra("id", 0);
            Log.e("DW", String.valueOf(appWidgetId));
            if (appWidgetId != 0) {
                Log.e("DW", "Button2");
                updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);

            }
        }
    }


}