package cz.muni.fi.pv256.movio2.fk410022.ui.loaders;

import android.content.Context;
import android.os.Bundle;

import com.activeandroid.query.From;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbSyntax;

public class FavoriteFilmsLoader extends EntitiesLoader<Film> {

    public FavoriteFilmsLoader(FavoriteFilmsListener listener, Context context) {
        super(Film.class, listener, context);
    }

    @Override
    protected void buildOnQuery(Bundle args, From from) {
        from.innerJoin(Favorite.class)
                .on(DbSyntax.columnEquality(DbContract.Film.TABLE, DbContract.Film.ID,
                        DbContract.Favorites.TABLE, DbContract.Favorites.FILM))
                .orderBy(DbSyntax.desc(
                        DbSyntax.makeColumn(DbContract.Favorites.TABLE, DbContract.Favorites.ID)));
    }

    public interface FavoriteFilmsListener extends EntitiesLoader.EntitiesListener<Film> {
        @Override
        void onLoadFinished(List<Film> favorites);
    }
}
