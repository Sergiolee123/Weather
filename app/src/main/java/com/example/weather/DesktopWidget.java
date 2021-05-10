package com.example.weather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;



public class DesktopWidget extends AppWidgetProvider {

    private static final String UpDate = "update";
    protected static String lat, lon;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.desktop_widget);
        new Thread(() ->{
            WeatherInfo weatherInfo;
            int i = 0;
            while (lat == null && lon == null){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                if(i>10){
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    return;
                }
            }
            i = 0;
            do{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                weatherInfo = WeatherList.getWeather();
                if(weatherInfo != null)
                    break;
                WeatherList.updateWeather(lat,lon);
                i++;
                if(i>10){
                    views.setTextViewText(R.id.appwidget_Location, "Error, please restart the app");
                    views.setTextViewText(R.id.appwidget_temp, "");
                    views.setTextViewText(R.id.appwidget_updateTime, "");
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                    return;
                }


            }while (true);
            Log.e("DW","NMSL");
            Log.e("DW",weatherInfo.getDisplayDayTime());
            views.setTextViewText(R.id.appwidget_Location, weatherInfo.getName() + " ");
            views.setTextViewText(R.id.appwidget_temp, weatherInfo.getTemp() + "°C ");
            views.setTextViewText(R.id.appwidget_updateTime
                    , "Last update: " + weatherInfo.getDisplayDayTime());
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }).start();


        // button

        Intent intent = new Intent(context, DesktopWidget.class);
        intent.setAction(UpDate);
        intent.putExtra("id", appWidgetId);
        Log.e("DWID", String.valueOf(intent.getIntExtra("id",0)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        super.onReceive(context,intent);

        if (UpDate.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra("id",0);
            Log.e("DW", String.valueOf(appWidgetId));
            if (appWidgetId != 0) {
                Log.e("DW","Button2");
                updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);

            }
        }
    }


}