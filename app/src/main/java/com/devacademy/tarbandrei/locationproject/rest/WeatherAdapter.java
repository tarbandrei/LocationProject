package com.devacademy.tarbandrei.locationproject.rest;

import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by a on 8/4/2016.
 *
 */
public class WeatherAdapter {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private WeatherApi weatherApi;

    public WeatherAdapter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        weatherApi = retrofit.create(WeatherApi.class);
    }

    public WeatherApi getWeatherApi() {
        return weatherApi;
    }
}
