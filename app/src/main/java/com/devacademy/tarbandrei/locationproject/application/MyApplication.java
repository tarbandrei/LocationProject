package com.devacademy.tarbandrei.locationproject.application;

import android.app.Application;

import com.devacademy.tarbandrei.locationproject.rest.WeatherAdapter;
import com.devacademy.tarbandrei.locationproject.rest.WeatherApi;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by a on 8/4/2016.
 *
 */
public class MyApplication extends Application {
    private WeatherAdapter weatherAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        weatherAdapter = new WeatherAdapter();
    }

    public WeatherApi getWeatherApi() {
        return weatherAdapter.getWeatherApi();
    }
}
