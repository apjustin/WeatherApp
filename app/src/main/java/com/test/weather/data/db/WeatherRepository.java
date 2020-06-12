package com.test.weather.data.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.test.weather.data.model.Weather;
import com.test.weather.data.model.WeatherResponse;

import java.util.List;

import retrofit2.Response;

public class WeatherRepository {

    private String DB_NAME = "db_weatheer";
    private WeatherDatabase weatherDatabase;

    public WeatherRepository(Context context) {
        weatherDatabase = Room.databaseBuilder(context, WeatherDatabase.class, DB_NAME).build();
    }

    public void insertWeatherInfo(Response<WeatherResponse> response) {
        WeatherEntity entity = new WeatherEntity();
        assert response.body() != null;
        List<Weather> list = response.body().getWeather();
        entity.setLat(response.body().getCoord().getLat());
        entity.setLon(response.body().getCoord().getLon());
        entity.setWeather_main(list.get(0).getMain());
        entity.setWeather_desc(list.get(0).getDescription());
        entity.setTemp(response.body().getMain().getTemp());
        entity.setTemp_max(response.body().getMain().getTempMax());
        entity.setTemp_min(response.body().getMain().getTempMin());
        entity.setPressure(response.body().getMain().getPressure());
        entity.setHumidity(response.body().getMain().getHumidity());
        entity.setWind_speed(response.body().getWind().getSpeed());
        entity.setWind_degree(response.body().getWind().getDeg());
        entity.setCity_name(response.body().getName());
        entity.setTime_stamp(""+System.currentTimeMillis());
        insertTask(entity);
    }

    private void insertTask(final WeatherEntity entity) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                weatherDatabase.weatherDAO().insertWeatherInfo(entity);
                return null;
            }
        }.execute();
    }

    public LiveData<WeatherEntity> getLatestWeather() {
        return weatherDatabase.weatherDAO().getLatestWeather();
    }
}
