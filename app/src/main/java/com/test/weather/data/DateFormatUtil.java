package com.test.weather.data;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatUtil {

    public DateFormatUtil() {
    }

    public SimpleDateFormat getSimpleDateFormat(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E,LLLL d y", Locale.getDefault());
        return simpleDateFormat;
    }
}
