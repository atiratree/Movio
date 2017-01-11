package cz.muni.fi.pv256.movio2.fk410022;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class TestUtils {
    public static final String TEST_DB_NAME = "test.db";

    public static Film getNewFilm() {
        Film film = new Film();
        film.setDescription("A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy.");
        film.setRating(8.1);
        film.setMovieDbId(540L);
        film.setReleaseDate(DateUtils.convertToDate("1999-10-14"));
        film.setBackdropPathId("/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg");
        film.setPopularity(5.253096);
        film.setPosterPathId("/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg");
        film.setTitle("Fight Club");

        return film;
    }

    public static Favorite getNewFavorite() {
        Favorite favorite = new Favorite();
        favorite.setFavorite(true);

        return favorite;
    }
}
