package com.test.weather.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert
    long insertWeatherInfo(WeatherEntity entity);

    @Query("SELECT * FROM WeatherInfo WHERE id = (SELECT MAX(id) FROM WeatherInfo)")
    LiveData<WeatherEntity> getLatestWeather();

    @Update
    void updateWeatherInfo(WeatherEntity entity);
}
