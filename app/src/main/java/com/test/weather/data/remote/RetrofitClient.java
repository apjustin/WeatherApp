package com.test.weather.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {
    private static Retrofit client = null;
    static Retrofit getClient(String baseURL)
    {
        if (client == null)
        {
            client = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return client;
    }
}
