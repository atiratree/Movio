package cz.muni.fi.pv256.movio2.fk410022.ui.loaders;

import android.content.Context;
import android.os.Bundle;

import com.activeandroid.query.From;

import cz.muni.fi.pv256.movio2.fk410022.db.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.DbSyntax;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.MovieDbClient;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class PopularFilmsLoader extends EntitiesLoader<Film> {

    public PopularFilmsLoader(EntitiesListener<Film> listener, Context context) {
        super(Film.class, listener, context);
    }

    @Override
    protected void buildOnQuery(Bundle args, From from) {
        from.where(DbSyntax.fromToBoth(DbContract.Film.RELEASE_DATE),
                DateUtils.getNewMoviesMonthsBack().getTime(), DateUtils.getToday().getTime())
                .where(DbSyntax.largerThanOrEqual(DbContract.Film.RATING_VOTE_COUNT),
                        MovieDbClient.POPULAR_MIN_VOTE_COUNT)
                .orderBy(DbSyntax.desc(DbContract.Film.POPULARITY));
    }
}
