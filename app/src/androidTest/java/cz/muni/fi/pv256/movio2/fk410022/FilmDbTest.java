package cz.muni.fi.pv256.movio2.fk410022;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.annimon.stream.Stream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbHelper;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilmDbTest {
    private Context context = InstrumentationRegistry.getTargetContext();
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    private Film fightClub;

    @Before
    public void setUp() {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();

        fightClub = createFightClub();
    }

    @After
    public void tearDown() {
        context.deleteDatabase(DbHelper.DB_NAME);
    }

    @Test
    public void testInsert() {
        Assert.assertTrue(insertToDb(fightClub) > 0);
    }

    @Test
    public void testRetrieve() {
        List<Film> retrieved = retrieveAllFilms();
        Assert.assertEquals(0, retrieved.size());

        insertToDb(fightClub);
        retrieved = retrieveAllFilms();
        Assert.assertEquals(1, retrieved.size());

        Film first = Stream.of(retrieved).findFirst().get();
        Assert.assertNotNull(first.getId());
        Assert.assertTrue(first.getId() > 0);
        first.setId(null);
        Assert.assertEquals(fightClub, first);

        insertToDb(fightClub);
        retrieved = retrieveAllFilms();
        Assert.assertEquals(2, retrieved.size());
    }

    @Test
    public void testRemove() {
        insertToDb(fightClub);
        Assert.assertEquals(1, db.delete(DbContract.Film.TABLE, null, null));

        insertToDb(fightClub);
        insertToDb(fightClub);
        insertToDb(fightClub);

        Film film = retrieveFirstFilm();
        String selection = DbContract.BaseEntity.ID + " = ?";
        String[] selectionArgs = {film.getId().toString()};

        Assert.assertEquals(1, db.delete(DbContract.Film.TABLE, selection, selectionArgs));

        Assert.assertEquals(2, retrieveAllFilms().size());
    }

    @Test
    public void testUpdate() {
        insertToDb(fightClub);
        insertToDb(fightClub);

        Film film = retrieveFirstFilm();
        film.setTitle("Arrival");

        String selection = DbContract.BaseEntity.ID + " = ?";
        String[] selectionArgs = {film.getId().toString()};

        int count = db.update(
                DbContract.Film.TABLE,
                film.toValues(),
                selection,
                selectionArgs);

        Assert.assertEquals(1, count);
        Assert.assertEquals(film, retrieveFirstFilm());
    }

    private Film retrieveFirstFilm() {
        Film result = null;
        Cursor c = db.query(DbContract.Film.TABLE, null, null, null, null, null, "1");
        try {
            if (c.moveToFirst()) {
                result = new Film(c);
            }
        } finally {
            c.close();
        }

        return result;
    }

    private List<Film> retrieveAllFilms() {
        List<Film> result = null;
        Cursor c = db.query(DbContract.Film.TABLE, null, null, null, null, null, null);

        try {
            result = new ArrayList<>(c.getCount());
            while (c.moveToNext()) {
                result.add(new Film(c));
            }
        } finally {
            c.close();
        }

        return result;
    }

    private long insertToDb(Film film) {
        return db.insert(DbContract.Film.TABLE, null, film.toValues());
    }

    private Film createFightClub() {
        Film film = new Film();
        film.setDescription("A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy.");
        film.setRating(8.1);
        film.setReleaseDate(DateUtils.convertToDate("1999-10-14"));
        film.setBackdropPathId("/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg");
        film.setPopularity(5.253096);
        film.setPosterPathId("/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg");
        film.setTitle("Fight Club");

        return film;
    }
}
