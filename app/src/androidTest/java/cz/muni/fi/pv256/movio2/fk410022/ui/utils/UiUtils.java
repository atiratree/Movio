package cz.muni.fi.pv256.movio2.fk410022.ui.utils;

import java.lang.reflect.Field;
import java.util.Arrays;

import cz.muni.fi.pv256.movio2.fk410022.App;
import cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class UiUtils {

    public static Film getAnimatedFilm() {
        Film film = new Film();
        film.setDescription("In Ancient Polynesia, when a terrible curse incurred by Maui reaches an impetuous Chieftain's daughter's island, she answers the Ocean's call to seek out the demigod to set things right.");
        film.setRating(6.5);
        film.setRatingVoteCount(870);
        film.setMovieDbId(277834L);
        film.setReleaseDate(DateUtils.convertToDate("2016-11-23"));
        film.setLateReleaseDate(DateUtils.getCurrentYear());
        film.setBackdropPathId("/1qGzqGUd1pa05aqYXGSbLkiBlLB.jpg");
        film.setPopularity(302.26918);
        film.setPosterPathId("/z4x0Bp48ar3Mda8KiPD1vwSY3D8.jpg");
        film.setTitle("Moana");
        film.setGenresToPersist(Arrays.asList(Genre.ANIMATION));

        return film;
    }

    public static void setIsTablet(boolean isTablet) {
        try {
            Field field = App.class.getDeclaredField("isTablet");
            field.setAccessible(true);
            field.set(null, isTablet);
            field.setAccessible(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
