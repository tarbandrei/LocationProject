package com.devacademy.tarbandrei.locationproject.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.devacademy.tarbandrei.locationproject.R;
import com.devacademy.tarbandrei.locationproject.application.MyApplication;
import com.devacademy.tarbandrei.locationproject.pojos.WeatherData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = WeatherActivity.class.getName();

    private GoogleApiClient googleApiClient;
    private String longitudeCoordinate;
    private String latitudeCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            longitudeCoordinate = String.valueOf(lastLocation.getLongitude());
            latitudeCoordinate = String.valueOf(lastLocation.getLatitude());
            Log.d(TAG, longitudeCoordinate + " " + latitudeCoordinate);
        }

        Call<WeatherData> result = ((MyApplication) getApplication()).getWeatherApi().getWeatherData(longitudeCoordinate, latitudeCoordinate);
        result.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Response<WeatherData> response, Retrofit retrofit) {
                Log.d(TAG, response.body().toString());
                if (response.body() != null) {
                    TextView name = (TextView) findViewById(R.id.activity_weather_name_text_view);
                    TextView country = (TextView) findViewById(R.id.activity_weather_country_text_view);
                    TextView temperature = (TextView) findViewById(R.id.activity_weather_temperature_text_view);
                    TextView main = (TextView) findViewById(R.id.activity_weather_main_text_view);
                    TextView windSpeed = (TextView) findViewById(R.id.activity_weather_wind_speed_text_view);
                    TextView windDirection = (TextView) findViewById(R.id.activity_weather_wind_deg_text_view);
                    TextView cloudiness = (TextView) findViewById(R.id.activity_weather_description_text_view);
                    TextView pressure = (TextView) findViewById(R.id.activity_weather_pressure_text_view);
                    TextView humidity = (TextView) findViewById(R.id.activity_weather_humidity_text_view);
                    TextView sunrise = (TextView) findViewById(R.id.activity_weather_sunrise_text_view);
                    TextView sunset = (TextView) findViewById(R.id.activity_weather_sunset_text_view);
                    TextView longitude = (TextView) findViewById(R.id.activity_weather_coordinate_longitude_text_view);
                    TextView latitude = (TextView) findViewById(R.id.activity_weather_coordinate_latitude_text_view);
                    name.setText(response.body().getName() + ", ");
                    if (country != null) {
                        country.setText(response.body().getSys().getCountry());
                    }
                    temperature.setText((double) Math.round(response.body().getMain().getTemp() - 273.15) + "°C, ");
                    main.setText(response.body().getWeather()[0].getMain());
                    windSpeed.setText(response.body().getWind().getSpeed() + " m/s");
                    windDirection.setText(response.body().getWind().getDeg() + " °");
                    cloudiness.setText(response.body().getWeather()[0].getDescription());
                    pressure.setText(response.body().getMain().getPressure() + " hpa");
                    humidity.setText(response.body().getMain().getHumidity() + " %");
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(response.body().getSys().getSunrise() * 1000);
                    sunrise.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                    calendar.setTimeInMillis(response.body().getSys().getSunset() * 1000);
                    sunset.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                    longitude.setText(response.body().getCoord().getLon().toString());
                    latitude.setText(response.body().getCoord().getLat().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
