package com.test.weather.ui;

import android.app.Instrumentation;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.test.weather.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    MainActivity mainActivity = null;


    @Before
    public void setUp() throws Exception {

        mainActivity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }

    @Test
    public void onCreate() {
        View tvVersion = mainActivity.findViewById(R.id.textViewVersionNumber);
        assertNotNull(tvVersion);
    }

    @Test
    public void onRequestPermissionsResult() {

    }
}