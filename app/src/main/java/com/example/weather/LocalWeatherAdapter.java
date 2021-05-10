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

    public LocalWeatherAdapter(Context context, List<WeatherInfo> weatherList) {
        this.LocalWeatherList = weatherList;
        this.context = context;
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

        WeatherInfo weatherInfo = LocalWeatherList.get(position+1);
        viewHolder.localImage.setImageResource(R.drawable.ic__1d);
        viewHolder.txt_localTime.setText(weatherInfo.getDisplayHour());
        viewHolder.txt_localTemperature.setText(weatherInfo.getTempMinMax());

    }


    @Override
    public int getItemCount() {
        return LocalWeatherList.size()-33;
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
