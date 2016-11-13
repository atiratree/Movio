package cz.muni.fi.pv256.movio2.fk410022.util;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
