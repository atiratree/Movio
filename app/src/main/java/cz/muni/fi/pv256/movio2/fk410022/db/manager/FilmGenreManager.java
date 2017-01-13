package cz.muni.fi.pv256.movio2.fk410022.db.manager;

import com.activeandroid.Cache;
import com.activeandroid.content.ContentProvider;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;

public class FilmGenreManager extends Manager<FilmGenre> {

    @Override
    public void notifyDependentTables() {
        Cache.getContext().getContentResolver().notifyChange(ContentProvider.createUri(Film.class, null), null);
    }
}
