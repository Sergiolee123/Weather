package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ForeignWeatherAdapter extends RecyclerView.Adapter<ForeignWeatherAdapter.ViewHolder> {
    //WeatherInformation list for RecyclerView
    private List<WeatherInfo> foreignWeatherList;
    //context reference to foreign weather activity
    private Context context;

    public ForeignWeatherAdapter(Context context, List<WeatherInfo> weatherList) {
        this.foreignWeatherList = weatherList;
        this.context = context;
    }

    // Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines a CardView UI of the list item
        View view = LayoutInflater.from(context)
                .inflate(R.layout.foreign_cardview, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //set the weather data
        WeatherInfo weatherInfo = foreignWeatherList.get(position);
        viewHolder.img_weather.setImageResource(weatherInfo.getWeatherIcon());
        viewHolder.txt_city_name.setText(weatherInfo.getName());
        viewHolder.txt_temperature.setText(weatherInfo.getTemp() + "°C");
        viewHolder.txt_date_time.setText(weatherInfo.getDisplayDayTime());
        viewHolder.txt_maxmintemperature.setText(weatherInfo.getTempMinMax());
        viewHolder.txt_feelslike.setText(weatherInfo.getFeelsLike());
        viewHolder.txt_speed.setText(weatherInfo.getWindSpeed());
        viewHolder.txt_humidity.setText(weatherInfo.getHumidity());
        //if user click on one of the item, the city weather data of that item will be remove
        viewHolder.itemView.setOnClickListener(v -> {
            //remove the city name from the String set
            WeatherList.cityName.remove(weatherInfo.getName());
            //update local storage
            StoreLocalData storeLocalData = new StoreLocalData();
            storeLocalData.write(WeatherList.cityName);
            //remove the item from the recycle view
            foreignWeatherList.remove(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());
            //update the recycle view
            notifyItemRangeChanged(viewHolder.getAdapterPosition(), foreignWeatherList.size());
            //use toast to inform the user that he deleted a weather data
            Toast toast = Toast.makeText(context, context.getText(R.string.foreignweather_Toast)
                    + weatherInfo.getName(), Toast.LENGTH_LONG);
            toast.show();
        });

    }

    //get the total number of items
    @Override
    public int getItemCount() {
        return foreignWeatherList.size();
    }
    //create a ViewHolder for the RecycleView
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
