package com.example.newapp;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import android.os.Handler;
import android.widget.Toast;

public class ServiceLocation extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String tag="DSK_OPER";

    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_location";
    private static final long TIME_INTERVAL_GET_LOCATION = 1000 * 2; // 1 Minute
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // meters

    private Handler handlerSendLocation;
    private Context mContext;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 5000;

    Location locationData;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(TIME_INTERVAL_GET_LOCATION)    // 3 seconds, in milliseconds
                .setFastestInterval(TIME_INTERVAL_GET_LOCATION); // 1 second, in milliseconds

        mContext = this;


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setColor(getResources().getColor(R.color.fontColorDarkGray))
                .setPriority(Notification.PRIORITY_MIN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_ID);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
            startForeground(1, builder.build());
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(tag, "BGS > Started");

        if (handlerSendLocation == null) {
            handlerSendLocation = new Handler();
            handlerSendLocation.post(runnableSendLocation);
            Log.d(tag, "BGS > handlerSendLocation Initialized");
        } else {
            Log.d(tag, "BGS > handlerSendLocation Already Initialized");
        }
        return START_STICKY;
    }

    private Runnable runnableSendLocation = new Runnable() {

        @Override
        public void run() {
            // You can get Location
            //locationData and Send Location X Minutes
            if (locationData != null) {
                Intent intent = new Intent("GPSLocationUpdates");
                intent.putExtra("Latitude", "" + locationData.getLatitude());
                intent.putExtra("Longitude", "" + locationData.getLongitude());

                Toast.makeText(mContext, "Latitude: "+locationData.getLatitude()+"Longitude: "+locationData
                        .getLongitude(), Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                Log.d(tag, "" + String.format("%.6f", locationData.getLatitude()) + "," +
                        String.format("%.6f", locationData.getLongitude()));

                Log.d(tag, "BGS >> Location Updated");
            }
            if (handlerSendLocation != null && runnableSendLocation != null)
                handlerSendLocation.postDelayed(runnableSendLocation, TIME_INTERVAL_GET_LOCATION);


        }


    };




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                locationData = locationResult.getLastLocation();
            }
        }, null);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution() && mContext instanceof Activity) {
            try {
                Activity activity = (Activity) mContext;
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(tag, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(tag, "" + String.format("%.6f", location.getLatitude()) + "," + String.format("%.6f", location.getLongitude()));
        locationData = location;

    }

    @Override
    public void onDestroy() {

        if (handlerSendLocation != null)
            handlerSendLocation.removeCallbacks(runnableSendLocation);


        Log.d(tag, "on Stopped");

        stopSelf();
        super.onDestroy();
    }


}