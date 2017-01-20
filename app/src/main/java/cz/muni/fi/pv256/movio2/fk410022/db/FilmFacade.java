package cz.muni.fi.pv256.movio2.fk410022.db;

import android.support.v4.util.Pair;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.activeandroid.sqlbrite.BriteDatabase;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;

public class FilmFacade {

    private static final String TAG = FilmFacade.class.getSimpleName();

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

        BriteDatabase.Transaction transaction = ActiveAndroid.beginTransaction();
        try {
            Stream.of(toUpdate).filter(Film::isToSave).forEach(Model::save);

            Stream.of(toUpdate).flatMap(film -> Stream.of(film.getGenresToPersist())
                    .filter(value -> value != null)
                    .map(genre -> new FilmGenre(film, genre)))
                    .forEach(Model::save);

            Stream.of(toUpdate).flatMap(film -> Stream.of(film.getGenresToRemove()))
                    .filter(value -> value != null)
                    .forEach(Model::delete);

            ActiveAndroid.setTransactionSuccessful(transaction);
        } finally {
            ActiveAndroid.endTransaction(transaction);
        }

        Log.i(TAG, String.format("Film Db count: %d", dbFilms.size() + newCount));

        return new Pair<>(newCount, toUpdate.size() - newCount);
    }
}
