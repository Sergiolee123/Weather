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
    private int size;

    public LocalWeatherAdapter(Context context, List<WeatherInfo> weatherList, int size) {
        this.LocalWeatherList = weatherList;
        this.context = context;
        this.size = size;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LocalWeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.local_cardview, viewGroup, false);

        return new LocalWeatherAdapter.ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(LocalWeatherAdapter.ViewHolder viewHolder, final int position) {
        WeatherInfo weatherInfo;
        if(size == 33) {
            weatherInfo = LocalWeatherList.get(position + 1);
            viewHolder.localImage.setImageResource(R.drawable.ic__1d);
            viewHolder.txt_localTime.setText(weatherInfo.getDayTimeByDay() + "     " + weatherInfo.getDisplayHour());
            viewHolder.txt_localTemperature.setText(weatherInfo.getTempMinMax());
        }
        else{
            weatherInfo = LocalWeatherList.get(position);
            viewHolder.localImage.setImageResource(R.drawable.ic__1d);
            viewHolder.txt_localTime.setText(weatherInfo.getDayTimeByDay());
            viewHolder.txt_localTemperature.setText(new StringBuilder()
                    .append(weatherInfo.getTemp()).append("°C").toString());
        }



    }


    @Override
    public int getItemCount() {
        return LocalWeatherList.size() - size;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView localImage;
        private TextView txt_localTime, txt_localTemperature;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            localImage = (ImageView) view.findViewById(R.id.localImage);
            txt_localTime = (TextView) view.findViewById(R.id.txt_localTime);
            txt_localTemperature = (TextView) view.findViewById(R.id.txt_localTemperature);
        }

    }
}
