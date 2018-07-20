package org.manriquecms.phonebill.util;

import com.github.javafaker.Faker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class StaticUtils {
    public static final Faker faker = new Faker();
    public static final Random random = new Random();
    private static final SimpleDateFormat datePatternHHmmss = new SimpleDateFormat("HH:mm:ss");

    static {
        datePatternHHmmss.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    //Time in hh-mm-ss
    public static long getSecondsFromTime(String time) throws ParseException{
            return TimeUnit.SECONDS.convert(datePatternHHmmss.parse(time).getTime(), TimeUnit.MILLISECONDS);
    }

    public static long getSecondsToMinutes(long seconds) {
        return Math.round(Math.ceil(seconds / 60D));
    }
}
