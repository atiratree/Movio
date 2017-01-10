package cz.muni.fi.pv256.movio2.fk410022.db.provider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;

public class FilmManager {
    private static final String WHERE_ID = DbContract.Film._ID + " = ?";
    private Context mContext;

    public FilmManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void create(Film film) {
        if (film == null) {
            throw new NullPointerException("film == null");
        }
        if (film.getId() != null) {
            throw new IllegalStateException("film id shouldn't be set");
        }

        film.setId(ContentUris.parseId(mContext.getContentResolver().insert(DbContract.Film.CONTENT_URI, film.toValues())));
    }

    public List<Film> findAll() {

        Cursor cursor = mContext.getContentResolver().query(DbContract.Film.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            List<Film> films = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    films.add(new Film(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return films;
        }

        return Collections.emptyList();
    }

    public int update(Film film) {
        if (film == null) {
            throw new NullPointerException("film == null");
        }
        if (film.getId() == null) {
            throw new IllegalStateException("film id cannot be null");
        }

        return mContext.getContentResolver().update(DbContract.Film.CONTENT_URI, film.toValues(), WHERE_ID, new String[]{String.valueOf(film.getId())});
    }

    public int delete(Film film) {
        if (film == null) {
            return mContext.getContentResolver().delete(DbContract.Film.CONTENT_URI, null, null);
        }

        if (film.getId() == null) {
            throw new IllegalStateException("film id cannot be null");
        }

        return mContext.getContentResolver().delete(DbContract.Film.CONTENT_URI, WHERE_ID, new String[]{String.valueOf(film.getId())});
    }
}
