package cz.muni.fi.pv256.movio2.fk410022.db;

import android.support.v4.util.Pair;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.manager.FilmGenreManager;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class FilmFacade {

    /**
     * @param type  to be saved
     * @param films films to be checked and potentially persisted
     * @return Pair, pair.first == number of new movies, pair.second == number of changed movies
     */
    public static Pair<Integer, Integer> update(FilmListType type, List<cz.muni.fi.pv256.movio2.fk410022.network.dto.Film> films) {
        Integer newCount = 0;

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
                newCount++;
                dbFilm = new Film();
            }

            boolean updateLateReleaseDate = updateLateReleaseDate(type, currentYear, dbFilm);
            if (film.updateValuesOfDbFilm(dbFilm) || updateLateReleaseDate) {
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
            FilmGenreManager filmGenreManager = new FilmGenreManager();
            Stream.of(toUpdate).flatMap(film -> Stream.of(film.getGenresToPersist())
                    .filter(value -> value != null)
                    .map(genre -> new FilmGenre(film, genre)))
                    .forEach(filmGenreManager::save);

            Stream.of(toUpdate).flatMap(film -> Stream.of(film.getGenresToRemove()))
                    .filter(value -> value != null)
                    .forEach(filmGenreManager::delete);

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return new Pair<>(newCount, toUpdate.size() - newCount);
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
