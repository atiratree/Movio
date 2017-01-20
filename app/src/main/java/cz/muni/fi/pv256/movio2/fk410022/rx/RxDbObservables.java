package cz.muni.fi.pv256.movio2.fk410022.rx;

import android.support.v4.util.Pair;

import com.activeandroid.Cache;
import com.activeandroid.query.Select;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.DbSyntax;
import cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
import cz.muni.fi.pv256.movio2.fk410022.network.MovieDbClient;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import rx.Observable;

public class RxDbObservables {

    public static Observable<List<Film>> createPopularFilmsObservable() {
        return RxDbHelper.execute(Film.class, new Select().from(Film.class).where(DbSyntax.fromToBoth(DbContract.Film.RELEASE_DATE),
                DateUtils.getNewMoviesMonthsBack().getTime(), DateUtils.getToday().getTime())
                .where(DbSyntax.largerThanOrEqual(DbContract.Film.RATING_VOTE_COUNT),
                        MovieDbClient.POPULAR_MIN_VOTE_COUNT)
                .orderBy(DbSyntax.desc(DbContract.Film.POPULARITY)));
    }

    public static Observable<List<Film>> createAnimatedFilmsObservable() {
        Pair<Long, Long> interval = DateUtils.getCurrentYearInterval();
        Collection<String> tables = Arrays.asList(
                Cache.getTableName(Film.class),
                Cache.getTableName(FilmGenre.class));

        return RxDbHelper.execute(Film.class, tables, new Select().from(Film.class).innerJoin(FilmGenre.class)
                .on(DbSyntax.columnEquality(DbContract.Film.TABLE, DbContract.Film.ID,
                        DbContract.FilmGenre.TABLE, DbContract.FilmGenre.FILM))
                .where(DbSyntax.equalsTo(
                        DbSyntax.makeColumn(DbContract.FilmGenre.TABLE, DbContract.FilmGenre.GENRE)),
                        Genre.ANIMATION.name())
                .where(DbSyntax.fromToLeft(
                        DbSyntax.makeColumn(DbContract.Film.TABLE, DbContract.Film.LATE_RELEASE_DATE)),
                        interval.first, interval.second)
                .orderBy(DbSyntax.desc(
                        DbSyntax.makeColumn(DbContract.Film.TABLE, DbContract.Film.POPULARITY))));
    }

    public static Observable<List<Film>> createScifiFilmsObservable() {
        Collection<String> tables = Arrays.asList(
                Cache.getTableName(Film.class),
                Cache.getTableName(FilmGenre.class));

        return RxDbHelper.execute(Film.class, tables, new Select().from(Film.class).innerJoin(FilmGenre.class)
                .on(DbSyntax.columnEquality(DbContract.Film.TABLE, DbContract.Film.ID,
                        DbContract.FilmGenre.TABLE, DbContract.FilmGenre.FILM))
                .where(DbSyntax.equalsTo(
                        DbSyntax.makeColumn(DbContract.FilmGenre.TABLE, DbContract.FilmGenre.GENRE)),
                        Genre.SCIENCE_FICTION.name())
                .where(DbSyntax.largerThanOrEqual(
                        DbSyntax.makeColumn(DbContract.Film.TABLE, DbContract.Film.RATING_VOTE_COUNT)),
                        MovieDbClient.SCIFI_MIN_VOTE_COUNT)
                .orderBy(DbSyntax.desc(
                        DbSyntax.makeColumn(DbContract.Film.TABLE, DbContract.Film.RATING))));
    }

    public static Observable<List<Film>> createFavoriteFilmsObservable() {
        Collection<String> tables = Arrays.asList(
                Cache.getTableName(Film.class),
                Cache.getTableName(Favorite.class));

        return RxDbHelper.execute(Film.class, tables, new Select().from(Film.class).innerJoin(Favorite.class)
                .on(DbSyntax.columnEquality(DbContract.Film.TABLE, DbContract.Film.ID,
                        DbContract.Favorites.TABLE, DbContract.Favorites.FILM))
                .orderBy(DbSyntax.desc(
                        DbSyntax.makeColumn(DbContract.Favorites.TABLE, DbContract.Favorites.ID))));
    }

    public static Observable<Film> createFilmObservable(Long id) {
        return RxDbHelper.executeSingle(Film.class, new Select().from(Film.class)
                .where(DbSyntax.equalsTo(DbContract.Film.ID), id));
    }

    public static Observable<Favorite> createFavoriteObservable(Long filmId) {
        return RxDbHelper.executeSingle(Favorite.class, new Select().from(Favorite.class)
                .where(DbSyntax.equalsTo(DbContract.Favorites.FILM), filmId));
    }
}
