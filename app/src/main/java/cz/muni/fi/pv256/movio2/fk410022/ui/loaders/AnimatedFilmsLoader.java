package cz.muni.fi.pv256.movio2.fk410022.ui.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;

import com.activeandroid.query.From;

import cz.muni.fi.pv256.movio2.fk410022.db.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.DbSyntax;
import cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class AnimatedFilmsLoader extends EntitiesLoader<Film> {

    public AnimatedFilmsLoader(EntitiesListener<Film> listener, Context context) {
        super(Film.class, listener, context);
    }

    @Override
    protected void buildOnQuery(Bundle args, From from) {
        Pair<Long, Long> interval = DateUtils.getCurrentYearInterval();
        from.innerJoin(FilmGenre.class)
                .on(DbSyntax.columnEquality(DbContract.Film.TABLE, DbContract.Film.ID,
                        DbContract.FilmGenre.TABLE, DbContract.FilmGenre.FILM))
                .where(DbSyntax.equalsTo(
                        DbSyntax.makeColumn(DbContract.FilmGenre.TABLE, DbContract.FilmGenre.GENRE)),
                        Genre.ANIMATION.name())
                .where(DbSyntax.fromToLeft(
                        DbSyntax.makeColumn(DbContract.Film.TABLE, DbContract.Film.LATE_RELEASE_DATE)),
                        interval.first, interval.second)
                .orderBy(DbSyntax.desc(
                        DbSyntax.makeColumn(DbContract.Film.TABLE, DbContract.Film.POPULARITY)));
    }
}
