package cz.muni.fi.pv256.movio2.fk410022.pojo;

import org.junit.Test;

import java.util.Collections;

import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.network.dto.Film;

import static org.junit.Assert.fail;

public class FilmListTypeTest {

    @Test
    public void getDefaultNumberOfPages() throws Exception {
        for (FilmListType type : FilmListType.values()) {
            if (type.getDefaultNumberOfPages() < 2) {
                fail("Must be at least 2 because of ContinuousFilmAdapterPresenter");
            }
        }
    }

    @Test
    public void processRequestResult() throws Exception {
        for (FilmListType type : FilmListType.values()) {
            Film film = new Film();
            type.processRequestResult(Collections.singletonList(film));

            if (type == FilmListType.CURRENT_YEAR_POPULAR_ANIMATED_MOVIES) {
                if (film.getLateReleaseDate() == null) {
                    fail();
                }
            } else if (film.getLateReleaseDate() != null) {
                fail();
            }
        }
    }
}
