package com.test.weather.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.weather.R;
import com.test.weather.data.DateFormatUtil;
import com.test.weather.data.db.WeatherEntity;
import com.test.weather.data.db.WeatherRepository;
import com.test.weather.data.model.Weather;
import com.test.weather.data.model.WeatherResponse;
import com.test.weather.data.remote.RetrofitService;
import com.test.weather.data.remote.RetrofitUtility;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private ProgressBar progressBarRetrieveData;
    private Location location;
    private double latitude, longitude;
    private TextView currentLocation, minimumTemperature, maximumTemperature, weatherDescription, currentDate, progressBarText, humidity, pressure;
    private TextView tvLatitude, tvLongitude, tvWindSpeed, tvSunRise, tvSunSet;
    private ImageView weatherIcon;
    private Geocoder geocoder;
    private RetrofitService retrofitService;
    private String APIKEY= "5ad7218f2e11df834b0eaf3a33a39d2a";
    private StringBuilder addressStringBuilder;
    private Calendar calendar;
    private DateFormatUtil dateFormatUtil;
    private AlertDialog.Builder alertDialogBuilder;
    private WeatherRepository weatherRepositoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);

        InitializeVariables();

        LocationListener locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
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
        };

        int permissionCheck = ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == 0)
        {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }

        if(location != null )
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        try
        {
            geocoder  = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
            addressStringBuilder = new StringBuilder();
            if(addressList.size() > 0)
            {
                Address locationAddress = addressList.get(0);
                for(int i =0 ; i <= locationAddress.getMaxAddressLineIndex(); i++)
                {
                    locationAddress.getAddressLine(i);
//                    addressStringBuilder.append(locationAddress.getSubLocality()).append(",");
                    addressStringBuilder.append(locationAddress.getLocality());
                }
                currentLocation.setText(addressStringBuilder);
            }
        }
        catch (IOException e)
        {
            Log.d("Exception: ", Objects.requireNonNull(e.getMessage()));
        }
        if(isTimeExceeded())
            getWeather();
    }

    public void InitializeVariables()
    {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        retrofitService = RetrofitUtility.getRetrofitService();
        currentLocation = findViewById(R.id.LocationTextView);
        minimumTemperature = findViewById(R.id.txtMinimumValue);
        maximumTemperature = findViewById(R.id.txtMaximumValue);
        weatherDescription = findViewById(R.id.textViewWeatherDescription);
        humidity = findViewById(R.id.textViewHumidityValue);
        pressure = findViewById(R.id.textViewPressureValue);
        currentDate = findViewById(R.id.textViewDate);

        tvLatitude = findViewById(R.id.textViewLatitudeValue);
        tvLongitude = findViewById(R.id.textViewLongitudeValue);
        tvWindSpeed = findViewById(R.id.textVieWindValue);
        tvSunRise = findViewById(R.id.textViewSunriseValue);
        tvSunSet = findViewById(R.id.textViewSunsetValue);

        progressBarText = findViewById((R.id.textViewProgressBarText));
        progressBarRetrieveData = findViewById(R.id.progressBarRetrieveData);
        weatherIcon = findViewById(R.id.imageViewIcon);
        alertDialogBuilder = new AlertDialog.Builder(WeatherActivity.this);
        calendar = Calendar.getInstance();
        dateFormatUtil = new DateFormatUtil();
        currentDate.setText(dateFormatUtil.getSimpleDateFormat().format(calendar.getTime()));
        weatherRepositoy = new WeatherRepository(this);
    }

    public void getWeather()
    {
        retrofitService.getTemp(addressStringBuilder, APIKEY,"metric").enqueue(new Callback<WeatherResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response)
            {
                if(response.isSuccessful())
                {
                    Log.i("getWeather()","response.isSuccessful()");
                    assert response.body() != null;
//                  JSONArray arr = new JSONArray(response.body().getWeather());
                    List<Weather> list;
                    list = response.body().getWeather();

                    weatherDescription.setText(String.format("%s (%s)", list.get(0).getMain().toUpperCase(), list.get(0).getDescription().toUpperCase()));
                    maximumTemperature.setText(String.format("%s ℃", response.body().getMain().getTempMax().toString()));
                    minimumTemperature.setText(String.format("%s ℃", response.body().getMain().getTempMin().toString()));
                    humidity.setText(String.format("%s %%", (response.body().getMain().getHumidity()).toString()));
                    pressure.setText(String.format("%s hPa", response.body().getMain().getPressure().toString()));

                    tvLatitude.setText(response.body().getCoord().getLat().toString());
                    tvLongitude.setText(response.body().getCoord().getLon().toString());
                    tvWindSpeed.setText(response.body().getWind().getSpeed().toString());
                    tvSunRise.setText(response.body().getSys().getSunrise().toString());
                    tvSunSet.setText(response.body().getSys().getSunset().toString());

                    switch (list.get(0).getIcon()){
                        case "01d": weatherIcon.setImageResource(R.drawable.sunny);
                        case "01n": weatherIcon.setImageResource(R.drawable.sunny); break;
                        case "02d": weatherIcon.setImageResource(R.drawable.sun_and_cloud);
                        case "02n": weatherIcon.setImageResource(R.drawable.sun_and_cloud); break;
                        case "03d": weatherIcon.setImageResource(R.drawable.partly_cloudy);
                        case "04d": weatherIcon.setImageResource(R.drawable.partly_cloudy);
                        case "03n": weatherIcon.setImageResource(R.drawable.partly_cloudy);
                        case "04n": weatherIcon.setImageResource(R.drawable.partly_cloudy); break;
                        case "09d": weatherIcon.setImageResource(R.drawable.rain);
                        case "10d": weatherIcon.setImageResource(R.drawable.rain);
                        case "09n": weatherIcon.setImageResource(R.drawable.rain);
                        case "10n": weatherIcon.setImageResource(R.drawable.rain);
                        case "11d": weatherIcon.setImageResource(R.drawable.rain);
                        case "11n": weatherIcon.setImageResource(R.drawable.rain); break;
                        case "13d": weatherIcon.setImageResource(R.drawable.snow);
                        case "13n": weatherIcon.setImageResource(R.drawable.snow);
                        case "50d": weatherIcon.setImageResource(R.drawable.snow);
                        case "50n": weatherIcon.setImageResource(R.drawable.snow); break;
                    }
                    Log.i("getWeather() ", "Before Calling insertWeatherInfo(response)");
                    weatherRepositoy.insertWeatherInfo(response);
                    Log.i("getWeather() ", "After Calling insertWeatherInfo(response)");
                    progressBarRetrieveData.setVisibility(View.INVISIBLE);
                    displayElements();
                } else {
                    Log.i("getWeather()","response.isNotSuccessful()");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {

                Log.d("weatherDisplayActivity","Error retrieving information from API");
            }
        });
    }

    private boolean isTimeExceeded() {
        LiveData<WeatherEntity> latestWeather = weatherRepositoy.getLatestWeather();
        WeatherEntity weatherEntity = latestWeather.getValue();
        String lastTimeStamp = weatherEntity != null ? weatherEntity.getTime_stamp() : null;
        Log.i("isTimeExceeded() ", "lastTimeStamp = " + lastTimeStamp);
        if(lastTimeStamp == null || lastTimeStamp.equals("null") || System.currentTimeMillis() - Long.valueOf(lastTimeStamp) > 72000000)
            return true;
        weatherDescription.setText(String.format("%s (%s)", weatherEntity.getWeather_main().toUpperCase(), weatherEntity.getWeather_desc().toUpperCase()));
        maximumTemperature.setText(String.format("%s ℃", weatherEntity.getTemp_max()));
        minimumTemperature.setText(String.format("%s ℃", weatherEntity.getTemp_min()));
        humidity.setText(String.format("%s %%", (weatherEntity.getHumidity())));
        pressure.setText(String.format("%s hPa", weatherEntity.getPressure()));
        return false;
    }

    private void displayElements()
    {
        currentLocation.setVisibility(View.VISIBLE);
        weatherDescription.setVisibility(View.VISIBLE);
        maximumTemperature.setVisibility(View.VISIBLE);
        minimumTemperature.setVisibility(View.VISIBLE);
        humidity.setVisibility(View.VISIBLE);
        pressure.setVisibility(View.VISIBLE);
        tvLatitude.setVisibility(View.VISIBLE);
        tvLongitude.setVisibility(View.VISIBLE);
        tvWindSpeed.setVisibility(View.VISIBLE);
        tvSunRise.setVisibility(View.VISIBLE);
        tvSunSet.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.INVISIBLE);
        weatherIcon.setVisibility(View.VISIBLE);
    }

}
