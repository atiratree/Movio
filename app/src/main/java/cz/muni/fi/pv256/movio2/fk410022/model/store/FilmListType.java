package cz.muni.fi.pv256.movio2.fk410022.model.store;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static cz.muni.fi.pv256.movio2.fk410022.util.Utils.getCurrentYear;

public enum FilmListType {
    RECENT_POPULAR_MOVIES("&sort_by=popularity.desc"),
    HIGHLY_RATED_SCIFI_MOVIES("&sort_by=vote_average.desc&vote_count.gte=10&with_genres=878"),
    CURRENT_YEAR_POPULAR_INDEPENDENT_MOVIES("&sort_by=popularity.desc&with_keywords=10183");

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final String url;

    FilmListType(String url) {
        this.url = url;
    }

    public String getUrlParameters() {
        String finalUrl;

        switch (this) {
            case RECENT_POPULAR_MOVIES:
                Calendar today = new GregorianCalendar();
                Calendar twoMonthsBack = new GregorianCalendar();
                twoMonthsBack.add(Calendar.MONTH, -2);
                finalUrl = url + String.format("&primary_release_date.gte=%s&primary_release_date.lte=%s",
                        format.format(twoMonthsBack.getTime()), format.format(today.getTime()));
                break;
            case HIGHLY_RATED_SCIFI_MOVIES:
                finalUrl = url;
                break;
            case CURRENT_YEAR_POPULAR_INDEPENDENT_MOVIES:
                finalUrl = url + String.format(Locale.ENGLISH, "&year=%d", getCurrentYear());
                break;
            default:
                finalUrl = url;
                break;
        }

        return finalUrl;
    }
}
