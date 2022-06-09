package com.kw.gdx.log;

import java.util.Locale;

public class StringUtils {

    public static String format(String fmt, Object... args) {
        return String.format(Locale.getDefault(), fmt, args);
    }

    public static String formatRaceTime(float time) {
        int minutes = (int) (time / 60);
        int seconds = (int) (time) % 60;
        int millis = (int) (time * 1000) % 1000;
        return String.format(Locale.US, "%02d.%03d",  seconds, millis);
    }
}
