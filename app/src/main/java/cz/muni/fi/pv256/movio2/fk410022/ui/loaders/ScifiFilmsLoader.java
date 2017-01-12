package cz.muni.fi.pv256.movio2.fk410022.ui.loaders;

import android.content.Context;
import android.os.Bundle;

import com.activeandroid.query.From;

import cz.muni.fi.pv256.movio2.fk410022.db.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.DbSyntax;
import cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
import cz.muni.fi.pv256.movio2.fk410022.network.MovieDbClient;

public class ScifiFilmsLoader extends EntitiesLoader<Film> {

    public ScifiFilmsLoader(EntitiesListener<Film> listener, Context context) {
        super(Film.class, listener, context);
    }

    @Override
    protected void buildOnQuery(Bundle args, From from) {
        from.innerJoin(FilmGenre.class)
                .on(DbSyntax.columnEquality(DbContract.Film.TABLE, DbContract.Film.ID,
                        DbContract.FilmGenre.TABLE, DbContract.FilmGenre.FILM))
                .where(DbSyntax.equalsTo(
                        DbSyntax.makeColumn(DbContract.FilmGenre.TABLE, DbContract.FilmGenre.GENRE)),
                        Genre.SCIENCE_FICTION.name())
                .where(DbSyntax.largerThanOrEqual(
                        DbSyntax.makeColumn(DbContract.Film.TABLE, DbContract.Film.RATING_VOTE_COUNT)),
                        MovieDbClient.SCIFI_MIN_VOTE_COUNT)
                .orderBy(DbSyntax.desc(
                        DbSyntax.makeColumn(DbContract.Film.TABLE, DbContract.Film.RATING)));
    }
}
