package com.professionalandroid.apps.whereami;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

public class MyLocationUpdateReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(LocationResult.hasResult(intent)){
            LocationResult locationResult = LocationResult.extractResult(intent);
            for(Location location: locationResult.getLocations()){
                Toast.makeText(context, "longitude: = "+location.getLongitude()
                        +"latitude: = "+location.getLatitude(), Toast.LENGTH_SHORT).show();
                Log.d("DSK_OPER","backgroud");
            }
        }
    }
}
