package com.devacademy.tarbandrei.locationproject.activities;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.devacademy.tarbandrei.locationproject.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = AppCompatActivity.class.getName();

    private static Handler uiHandler;
    private static Handler handler;

    private final int DOWNLOAD_REQUEST = 1;
    private final int DOWNLOAD_RESPONSE = 2;

    GoogleApiClient mGoogleApiClient;
    private String xCoord;
    private String yCoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        HandlerThread handlerThread = new HandlerThread("DownloadImageThread");
        handlerThread.start();

        uiHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOAD_RESPONSE:
                        ImageView imageView = (ImageView) findViewById(R.id.google_map_imageview);
                        imageView.setImageBitmap((Bitmap) msg.obj);
                }
            }
        };

        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOAD_REQUEST:
                        try {
                            Bitmap bitmap = downloadUrl((String) msg.obj);
                            Message responseMessage = Message.obtain();
                            responseMessage.what = DOWNLOAD_RESPONSE;
                            responseMessage.obj = bitmap;
                            uiHandler.sendMessage(responseMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        };

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            xCoord = String.valueOf(lastLocation.getLatitude());
            yCoord = String.valueOf(lastLocation.getLongitude());
            Log.d(TAG, xCoord + " " + yCoord);
        }

        //px = dp * (dpi / 160)

        int zoom = 16;
        int scale = 2;
        int dpi = getResources().getConfiguration().densityDpi;
        int width = Math.min((getResources().getConfiguration().screenWidthDp * (dpi / 160)) / scale, 640);
        int height;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            height = Math.min((getResources().getConfiguration().screenHeightDp * (dpi / 160)) / scale, 640);
        } else {
            height = 320;
        }
        String stringUrl = "http://maps.googleapis.com/maps/api/staticmap?center=" + xCoord + "," + yCoord + "&zoom=" + zoom + "&size=" + width + "x" + height + "&scale=" + scale;
        Message message = Message.obtain();
        message.what = DOWNLOAD_REQUEST;
        message.obj = stringUrl;

        Log.d(TAG, stringUrl);
        handler.sendMessage(message);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private Bitmap downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string

            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);

            return BitmapFactory.decodeStream(bufferedInputStream);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
