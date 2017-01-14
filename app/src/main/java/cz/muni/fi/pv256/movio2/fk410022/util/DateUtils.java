package cz.muni.fi.pv256.movio2.fk410022.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.muni.fi.pv256.movio2.fk410022.network.MovieDbClient;

public class DateUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static String convertToReadableString(Date date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return df.format(date);
    }

    @Nullable
    public static Date convertToDate(String date) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String convertToString(Date date) {
        return format.format(date.getTime());
    }

    public static int getCurrentYearAsInt() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static Date getCurrentYear() {
        return getFirstTimeOfYear().getTime();
    }

    public static Date getToday() {
        Calendar today = Calendar.getInstance();
        nullTime(today);
        return today.getTime();
    }

    public static Date getNewMoviesMonthsBack() {
        Calendar twoMonthsBack = Calendar.getInstance();
        twoMonthsBack.add(Calendar.MONTH, - MovieDbClient.NEW_MOVIES_MONTHS_BACK);
        nullTime(twoMonthsBack);

        return twoMonthsBack.getTime();
    }

    public static Pair<Long, Long> getCurrentYearInterval() {
        Long from;
        Long to;

        Calendar calendar = getFirstTimeOfYear();
        from = calendar.getTime().getTime();

        calendar.add(Calendar.YEAR, 1);
        to = calendar.getTime().getTime();

        return new Pair<>(from, to);
    }

    @NonNull
    private static Calendar getFirstTimeOfYear() {
        Calendar calendar;
        calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        nullTime(calendar);
        return calendar;
    }

    private static void nullTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}
