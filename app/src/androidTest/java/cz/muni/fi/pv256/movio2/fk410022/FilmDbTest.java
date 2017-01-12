package cz.muni.fi.pv256.movio2.fk410022;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.annimon.stream.Stream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.DbContract;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilmDbTest {

    private static Context context = InstrumentationRegistry.getTargetContext();

    @BeforeClass
    public static void setUp() {
        Configuration dbConfiguration = new Configuration.Builder(context).setDatabaseName(TestUtils.TEST_DB_NAME).create();
        ActiveAndroid.initialize(dbConfiguration);
    }

    @AfterClass
    public static void afterClass() {
        ActiveAndroid.dispose();
        context.deleteDatabase(TestUtils.TEST_DB_NAME);
    }

    @After
    public void tearDown() {
        new Delete().from(Film.class).execute();
    }

    @Test
    public void testInsert() {
        Film result = TestUtils.getNewFilm();
        Assert.assertTrue(result.save() > 0);

        Assert.assertTrue(result.equals(retrieveFirstFilm()));
    }

    @Test
    public void testRetrieve() {
        List<Film> retrieved = retrieveAllFilms();
        Assert.assertEquals(0, retrieved.size());

        insertToDb(TestUtils.getNewFilm());
        retrieved = retrieveAllFilms();
        Assert.assertEquals(1, retrieved.size());

        Film first = Stream.of(retrieved).findFirst().get();
        Assert.assertNotNull(first.getId());
        Assert.assertTrue(first.getId() > 0);

        insertToDb(TestUtils.getNewFilm());
        retrieved = retrieveAllFilms();
        Assert.assertEquals(2, retrieved.size());
    }

    @Test
    public void testRemove() {

        insertToDb(TestUtils.getNewFilm());
        insertToDb(TestUtils.getNewFilm());
        insertToDb(TestUtils.getNewFilm());
        Film.delete(Film.class, retrieveFirstFilm().getId());

        Assert.assertEquals(2, retrieveAllFilms().size());
    }

    @Test
    public void testUpdate() {
        insertToDb(TestUtils.getNewFilm());
        insertToDb(TestUtils.getNewFilm());

        Film film = retrieveFirstFilm();
        film.setTitle("Arrival");

        film.save();

        Assert.assertTrue(film.equals(retrieveFirstFilm()));
    }

    private Film retrieveFirstFilm() {
        return new Select().from(Film.class).orderBy(DbContract.BaseEntity.ID).limit(1).executeSingle();
    }

    private List<Film> retrieveAllFilms() {
        return new Select().from(Film.class).execute();
    }

    private long insertToDb(Film film) {
        return film.save();
    }
}
