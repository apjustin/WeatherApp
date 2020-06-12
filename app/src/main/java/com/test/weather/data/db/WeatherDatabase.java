package com.test.weather.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherEntity.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    public abstract WeatherDAO weatherDAO();

}
