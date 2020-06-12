package com.test.weather.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.test.weather.R;

public class MainActivity extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfoWifi;
    static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 0;
    LocationManager locationManager;
    AlertDialog.Builder alertDialogBuilder;
    boolean isConnected;
    int permissionResult;
    TextView versionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        instantiateVariables();
        checkPermissionsAndConnectivity(permissionResult, locationManager, isConnected);
    }

    private void instantiateVariables()
    {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfoWifi = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null; //connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        isConnected = networkInfoWifi != null && networkInfoWifi.isConnectedOrConnecting();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        permissionResult = this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
        alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        versionNumber = findViewById(R.id.textViewVersionNumber);
        versionNumber.setText("1.0.1");
    }

    private void checkPermissionsAndConnectivity(int permissionResult, LocationManager locationManager, boolean internetConnectivity) {
        if (permissionResult == 0)
        {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                if(internetConnectivity)
                {
                    stimulateSomeWork();
                }
                else
                {
                    alertDialogBuilder.setMessage(R.string.connectToNetwork).setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    finish();

                                }
                            });
                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();
                }
            }
            else
            {
                alertDialogBuilder.setMessage(R.string.enableGPS).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                finish();

                            }
                        });
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            }
        } else
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                instantiateVariables();
                checkPermissionsAndConnectivity(permissionResult, locationManager, isConnected);

            } else {
                alertDialogBuilder.setMessage(R.string.locationServicesPermissions).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        });
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            }
        }
    }

    private void stimulateSomeWork()
    {
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                finish();
            }
        }, 1000);
    }
}
