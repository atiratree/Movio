package cz.muni.fi.pv256.movio2.fk410022.ui.loaders;

import android.content.Context;
import android.os.Bundle;

import com.activeandroid.query.From;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbSyntax;

public class FilmLoader extends EntityLoader<Film> {

    public static final String MOVIE_DB_ID_PARAM = "MOVIE_DB_ID_PARAM";

    public FilmLoader(FilmListener listener, Context context) {
        super(Film.class, listener, context);
    }

    @Override
    protected void buildOnQuery(Bundle args, From from) {
        from.where(DbSyntax.equalsTo(DbContract.Film.MOVIE_DB_ID), args.getLong(MOVIE_DB_ID_PARAM));
    }

    public interface FilmListener extends EntityListener<Film> {
        void onLoadFinished(Film film);
    }
}

