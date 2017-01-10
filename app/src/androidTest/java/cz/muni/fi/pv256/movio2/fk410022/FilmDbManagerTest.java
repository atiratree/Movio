package cz.muni.fi.pv256.movio2.fk410022;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.annimon.stream.Stream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.provider.FilmManager;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilmDbManagerTest {
    private Context context = InstrumentationRegistry.getTargetContext();
    private FilmManager manager;

    private Film fightClub;

    @Before
    public void setUp() {
        manager = new FilmManager(context);

        fightClub = createFightClub();
    }

    @After
    public void tearDown() {
        context.getContentResolver().delete(
                DbContract.Film.CONTENT_URI,
                null,
                null
        );
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
        Assert.assertEquals(fightClub, first);

        fightClub.setId(null);
        insertToDb(fightClub);
        retrieved = retrieveAllFilms();
        Assert.assertEquals(2, retrieved.size());
    }

    @Test
    public void testRemove() {
        insertToDbAndNullId(fightClub);
        Assert.assertEquals(1, manager.delete(null));

        insertToDbAndNullId(fightClub);
        insertToDbAndNullId(fightClub);
        insertToDb(fightClub);

        Assert.assertEquals(1, manager.delete(retrieveFirstFilm()));

        Assert.assertEquals(2, retrieveAllFilms().size());
    }

    @Test
    public void testUpdate() {
        insertToDbAndNullId(fightClub);
        insertToDb(fightClub);

        Film film = retrieveFirstFilm();
        film.setTitle("Arrival");

        int count = manager.update(film);

        Assert.assertEquals(1, count);
        Assert.assertEquals(film, retrieveFirstFilm());
    }

    private Film retrieveFirstFilm() {
        return Stream.of(retrieveAllFilms()).findFirst().orElse(null);
    }

    private List<Film> retrieveAllFilms() {

        return manager.findAll();
    }

    private long insertToDbAndNullId(Film film) {
        long id = insertToDb(film);
        film.setId(null);
        return id;
    }

    private long insertToDb(Film film) {
        manager.create(fightClub);
        return film.getId();
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
