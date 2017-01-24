package cz.muni.fi.pv256.movio2.fk410022.pojo;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.DataUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.LastMethodCall;
import cz.muni.fi.pv256.movio2.fk410022.utils.MockUtils;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {Film.class, Genre.class})
public class FilmDiff {

    @Mock
    private Film filmOne;

    @Mock
    private FilmGenre filmGenre;

    @Before
    public void setUp() throws Exception {
        DataUtils.setFilmOneValues(filmOne);
        Mockito.when(filmGenre.getGenre()).thenReturn(Genre.SCIENCE_FICTION);

        PowerMockito.spy(Genre.class);
        Mockito.when(Genre.fromId(Genre.SCIENCE_FICTION.getId())).thenReturn(Genre.SCIENCE_FICTION);
    }

    @Test
    public void testEmpty() {
        cz.muni.fi.pv256.movio2.fk410022.network.dto.Film dto = new cz.muni.fi.pv256.movio2.fk410022.network.dto.Film();
        Mockito.when(filmOne.getGenres()).thenReturn(Collections.emptyList());

        dto.updateValuesOfDbFilm(filmOne);
        verify(filmOne, new LastMethodCall()).setToUpdate(true);
        verify(filmOne, never()).setGenresToPersist(anyObject());
        verify(filmOne, never()).setGenresToRemove(anyObject());
    }

    @Test
    public void testSame() throws Exception {
        cz.muni.fi.pv256.movio2.fk410022.network.dto.Film dto = new cz.muni.fi.pv256.movio2.fk410022.network.dto.Film();

        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "id", dto, filmOne.getMovieDbId());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "original_title", dto, filmOne.getTitle());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "overview", dto, filmOne.getDescription());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "release_date", dto,
                DateUtils.convertToString(filmOne.getReleaseDate()));
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "backdrop_path", dto, filmOne.getBackdropPathId());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "poster_path", dto, filmOne.getPosterPathId());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "popularity", dto, filmOne.getPopularity());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "vote_average", dto, filmOne.getRating());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "vote_count", dto, filmOne.getRatingVoteCount());
        Mockito.when(filmOne.getGenres()).thenReturn(Collections.emptyList());

        Assert.assertFalse(dto.updateValuesOfDbFilm(filmOne));
        verify(filmOne, never()).setGenresToPersist(anyObject());
        verify(filmOne, never()).setGenresToRemove(anyObject());
    }

    @Test
    public void testDifferentTitle() throws Exception {
        String differentTitle = "DIFFERENT TITLE";
        cz.muni.fi.pv256.movio2.fk410022.network.dto.Film dto = new cz.muni.fi.pv256.movio2.fk410022.network.dto.Film();

        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "id", dto, filmOne.getMovieDbId());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "original_title",
                dto, differentTitle);
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "overview", dto, filmOne.getDescription());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "release_date", dto,
                DateUtils.convertToString(filmOne.getReleaseDate()));
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "backdrop_path", dto, filmOne.getBackdropPathId());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "poster_path", dto, filmOne.getPosterPathId());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "popularity", dto, filmOne.getPopularity());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "vote_average", dto, filmOne.getRating());
        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "vote_count", dto, filmOne.getRatingVoteCount());
        Mockito.when(filmOne.getGenres()).thenReturn(Collections.emptyList());

        dto.updateValuesOfDbFilm(filmOne);
        verify(filmOne).setToUpdate(true);
        verify(filmOne).setTitle(differentTitle);
    }

    @Test
    public void testNewGenre() throws Exception {
        cz.muni.fi.pv256.movio2.fk410022.network.dto.Film dto = new cz.muni.fi.pv256.movio2.fk410022.network.dto.Film();
        Mockito.when(filmOne.getGenres()).thenReturn(Collections.emptyList());

        MockUtils.setPrivate(cz.muni.fi.pv256.movio2.fk410022.network.dto.Film.class, "genre_ids", dto,
                new Integer[]{Genre.SCIENCE_FICTION.getId()});

        dto.updateValuesOfDbFilm(filmOne);
        verify(filmOne, new LastMethodCall()).setToUpdate(true);

        EnumSet<Genre> expecteResult = EnumSet.of(Genre.SCIENCE_FICTION);
        verify(filmOne).setGenresToPersist(expecteResult);
        verify(filmOne, never()).setGenresToRemove(anyObject());
    }

    @Test
    public void testDeleteGenre() throws Exception {
        cz.muni.fi.pv256.movio2.fk410022.network.dto.Film dto = new cz.muni.fi.pv256.movio2.fk410022.network.dto.Film();
        Mockito.when(filmOne.getGenres()).thenReturn(Collections.singletonList(filmGenre));

        dto.updateValuesOfDbFilm(filmOne);
        verify(filmOne, new LastMethodCall()).setToUpdate(true);

        Set<FilmGenre> expecteResult = new HashSet<>();
        expecteResult.add(filmGenre);

        verify(filmOne, never()).setGenresToPersist(anyObject());
        verify(filmOne).setGenresToRemove(expecteResult);
    }
}
