package com.devacademy.tarbandrei.locationproject.rest;

import com.devacademy.tarbandrei.locationproject.pojos.WeatherData;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by a on 8/4/2016.
 *
 */
public interface WeatherApi {
    public static final String API_KEY = "a22034ca876e81d654015b3c39c01e09";

    @GET("weather?appid=" + API_KEY)
    Call<WeatherData> getWeatherData(@Query("lon") String longitude, @Query("lat") String latitude);
}
