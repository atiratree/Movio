package cz.muni.fi.pv256.movio2.fk410022.ui.loaders;

import android.content.Context;
import android.os.Bundle;

import com.activeandroid.query.From;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbSyntax;

public class FavoritesLoader extends EntityLoader<Favorite> {

    public static final String FILM_ID_PARAM = "FILM_ID_PARAM";

    public FavoritesLoader(FavoriteListener listener, Context context) {
        super(Favorite.class, listener, context);
    }

    @Override
    protected void buildOnQuery(Bundle args, From from) {
        from.where(DbSyntax.equalsTo(DbContract.Favorites.FILM), args.getLong(FILM_ID_PARAM));
    }

    public interface FavoriteListener extends EntityListener<Favorite> {
        void onLoadFinished(Favorite favorite);
    }
}
