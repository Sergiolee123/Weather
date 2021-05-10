package com.example.weather;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ForeignWeatherAdapter extends RecyclerView.Adapter<ForeignWeatherAdapter.ViewHolder> {

    private List<WeatherInfo> foreignWeatherList;
    private Context context;

    public ForeignWeatherAdapter(Context context, List<WeatherInfo> weatherList) {
        this.foreignWeatherList = weatherList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.foreign_cardview, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        WeatherInfo weatherInfo = foreignWeatherList.get(position);
        viewHolder.img_weather.setImageResource(R.drawable.ic__1d);
        viewHolder.txt_city_name.setText(weatherInfo.getName());
        viewHolder.txt_temperature.setText(weatherInfo.getTemp());
        viewHolder.txt_date_time.setText(weatherInfo.getDisplayDayTime());
        viewHolder.txt_maxmintemperature.setText(weatherInfo.getTempMinMax());
        viewHolder.txt_feelslike.setText(weatherInfo.getFeelsLike());
        viewHolder.txt_speed.setText(weatherInfo.getWindSpeed());
        viewHolder.txt_humidity.setText(weatherInfo.getHumidity());

    }


    @Override
    public int getItemCount() {
        return foreignWeatherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_city_name, txt_temperature, txt_date_time, txt_maxmintemperature
                , txt_feelslike, txt_speed, txt_humidity;
        private ImageView img_weather;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            img_weather = (ImageView) view.findViewById(R.id.img_weather);
            txt_city_name = (TextView) view.findViewById(R.id.txt_city_name);
            txt_temperature = (TextView) view.findViewById(R.id.txt_temperature);
            txt_date_time = (TextView) view.findViewById(R.id.txt_date_time);
            txt_maxmintemperature = (TextView) view.findViewById(R.id.txt_maxmintemperature);
            txt_feelslike = (TextView) view.findViewById(R.id.txt_feelslike);
            txt_speed = (TextView) view.findViewById(R.id.txt_speed);
            txt_humidity = (TextView) view.findViewById(R.id.txt_humidity);
        }

    }

}
