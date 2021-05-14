package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocalWeatherAdapter extends RecyclerView.Adapter<LocalWeatherAdapter.ViewHolder>{
    private List<WeatherInfo> LocalWeatherList;
    private Context context;
    //use to get different weatherInfo from the local weather list
    private int size;

    public LocalWeatherAdapter(Context context, List<WeatherInfo> weatherList, int size) {
        this.LocalWeatherList = weatherList;
        this.context = context;
        this.size = size;
    }

    // Create new views
    @Override
    public LocalWeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the local CardView UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.local_cardview, viewGroup, false);

        return new LocalWeatherAdapter.ViewHolder(view);
    }


    // Replace the contents of a view
    @Override
    public void onBindViewHolder(LocalWeatherAdapter.ViewHolder viewHolder, final int position) {
        WeatherInfo weatherInfo;
        //if size is 33, it will get 8 recent weatherInfo object that used for hourly weather
        if(size == 33) {
            weatherInfo = LocalWeatherList.get(position + 1);
            viewHolder.localImage.setImageResource(weatherInfo.getWeatherIcon());
            viewHolder.txt_localTime.setText(weatherInfo.getDayTimeByDay() + "     " + weatherInfo.getDisplayHour());
            viewHolder.txt_localTemperature.setText(new StringBuilder()
                    .append(weatherInfo.getTemp()).append("°C").toString());
        }
        else{
            //show 5 day weather
            weatherInfo = LocalWeatherList.get(position);
            viewHolder.localImage.setImageResource(weatherInfo.getWeatherIcon());
            viewHolder.txt_localTime.setText(weatherInfo.getDayTimeByDay());
            viewHolder.txt_localTemperature.setText(weatherInfo.getTempMinMax());
        }



    }

    //get the number of item
    @Override
    public int getItemCount() {
        return LocalWeatherList.size() - size;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView localImage;
        private TextView txt_localTime, txt_localTemperature;

        public ViewHolder(View view) {
            super(view);

            localImage = (ImageView) view.findViewById(R.id.localImage);
            txt_localTime = (TextView) view.findViewById(R.id.txt_localTime);
            txt_localTemperature = (TextView) view.findViewById(R.id.txt_localTemperature);
        }

    }
}
