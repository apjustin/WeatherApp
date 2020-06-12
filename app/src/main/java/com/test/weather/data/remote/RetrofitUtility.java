package com.test.weather.data.remote;

public class RetrofitUtility {
    public static final String BASE_URL="http://api.openweathermap.org/data/2.5/";
    public static RetrofitService getRetrofitService()
    {
        return RetrofitClient.getClient(BASE_URL).create(RetrofitService.class);
    }
}
