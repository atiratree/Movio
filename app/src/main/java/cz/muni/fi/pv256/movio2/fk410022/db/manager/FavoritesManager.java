package cz.muni.fi.pv256.movio2.fk410022.db.manager;

import com.activeandroid.Cache;
import com.activeandroid.content.ContentProvider;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;

public class FavoritesManager {

    public static void delete(Favorite favorite) {
        favorite.delete();
        notifyDependentTables();
    }

    public static Long save(Favorite favorite) {
        Long result = favorite.save();
        notifyDependentTables();
        return result;
    }

    public static void notifyDependentTables() {
        Cache.getContext().getContentResolver().notifyChange(ContentProvider.createUri(Film.class, null), null);
    }
}
