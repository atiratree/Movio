package cz.muni.fi.pv256.movio2.fk410022.db;

import android.support.v4.util.Pair;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.manager.FilmGenreManager;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;

public class FilmFacade {

    /**
     * @param films films to be checked and potentially persisted
     * @return Pair, pair.first == number of new movies, pair.second == number of changed movies
     */
    public static Pair<Integer, Integer> update(Collection<cz.muni.fi.pv256.movio2.fk410022.network.dto.Film> films) {
        Integer newCount = 0;

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

            if (film.updateValuesOfDbFilm(dbFilm)) {
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
}
