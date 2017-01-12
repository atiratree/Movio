package cz.muni.fi.pv256.movio2.fk410022.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;

public class Facade {

    public static void update(FilmListType type, cz.muni.fi.pv256.movio2.fk410022.network.dto.Film[] films) {
        Date currentYear = null;
        if (type == FilmListType.CURRENT_YEAR_POPULAR_ANIMATED_MOVIES) {
            currentYear = DateUtils.getCurrentYear();
        }

        List<Film> dbFilms = new Select().from(Film.class).execute();

        final HashMap<Long, Film> filmMap = Stream.of(dbFilms)
                .collect(HashMap<Long, Film>::new, (map, film) -> map.put(film.getMovieDbId(), film));

        List<Film> toUpdate = new ArrayList<>();

        for (cz.muni.fi.pv256.movio2.fk410022.network.dto.Film film : films) {
            if (film.getId() == null) {
                continue;
            }

            Film dbFilm = filmMap.get(film.getId());
            if (dbFilm == null) {
                dbFilm = new Film();
            }

            boolean updateLateReleaseDate = updateLateReleaseDate(type, currentYear, dbFilm);
            if (film.updateDbFilm(dbFilm) || updateLateReleaseDate) {
                toUpdate.add(dbFilm);
            }
        }

        ActiveAndroid.beginTransaction();
        try {
            Stream.of(toUpdate).forEach(Model::save);

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        ActiveAndroid.beginTransaction();
        try {
            Stream.of(toUpdate).flatMap(film -> Stream.of(film.getGenresToPersist())
                    .filter(value -> value != null)
                    .map(genre -> new FilmGenre(film, genre)))
                    .forEach(Model::save);

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    private static boolean updateLateReleaseDate(FilmListType type, Date currentYear, Film film) {
        if (type != FilmListType.CURRENT_YEAR_POPULAR_ANIMATED_MOVIES) {
            return false;
        }

        if (currentYear != null ? !currentYear.equals(film.getLateReleaseDate()) : film.getLateReleaseDate() != null) {
            film.setLateReleaseDate(currentYear);
            return true;
        }
        return false;
    }
}
