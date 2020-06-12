package com.test.weather.ui;

import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.test.weather.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeatherActivityTest {
    @Rule
    ActivityTestRule<WeatherActivity> rule = new ActivityTestRule<>(WeatherActivity.class);

    WeatherActivity weatherActivity = null;

    @Before
    public void setUp() throws Exception {
        weatherActivity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        weatherActivity = null;
    }

    @Test
    public void onCreate() {
        View tvCity = weatherActivity.findViewById(R.id.LocationTextView);
        assertNotNull(tvCity);
    }

    @Test
    public void initializeVariables() {
        View minimumTemperature = weatherActivity.findViewById(R.id.txtMinimumValue);
        View maximumTemperature = weatherActivity.findViewById(R.id.txtMaximumValue);
        View weatherDescription = weatherActivity.findViewById(R.id.textViewWeatherDescription);
        View humidity = weatherActivity.findViewById(R.id.textViewHumidityValue);
        View pressure = weatherActivity.findViewById(R.id.textViewPressureValue);
        View currentDate = weatherActivity.findViewById(R.id.textViewDate);

        assertNotNull(minimumTemperature);
        assertNotNull(maximumTemperature);
        assertNotNull(weatherDescription);
        assertNotNull(humidity);
        assertNotNull(pressure);
        assertNotNull(currentDate);
    }

    @Test
    public void getWeather() {
    }
}