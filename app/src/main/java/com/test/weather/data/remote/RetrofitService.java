package com.test.weather.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.test.weather.data.model.WeatherResponse;

public interface RetrofitService {
    @GET("weather")
    Call<WeatherResponse> getTemp(@Query("q") StringBuilder cityName, @Query("appid")String APIKEY, @Query("units")String Metric);
}
