package cz.muni.fi.pv256.movio2.fk410022.utils;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class DataUtils {

    public static void setFilmOneValues(Film film) {
        Mockito.when(film.getId()).thenReturn(5L);
        Mockito.when(film.getDescription()).thenReturn("A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy.");
        Mockito.when(film.getRating()).thenReturn(8.1);
        Mockito.when(film.getMovieDbId()).thenReturn(540L);
        Mockito.when(film.getReleaseDate()).thenReturn(DateUtils.convertToDate("1999-10-14"));
        Mockito.when(film.getBackdropPathId()).thenReturn("/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg");
        Mockito.when(film.getPosterPathId()).thenReturn("/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg");
        Mockito.when(film.getTitle()).thenReturn("Fight Club");
        Mockito.when(film.getPopularity()).thenReturn(6.829822);
    }

    public static void setFavoriteOneValues(Favorite favorite, Film film) {
        Mockito.when(favorite.getId()).thenReturn(1L);
        Mockito.when(favorite.getFilm()).thenReturn(film);
        Mockito.when(favorite.isFavorite()).thenReturn(true);
    }

    public static void setFavoriteTwoValues(Favorite favorite) {
        Mockito.when(favorite.getId()).thenReturn(2L);
    }

    public static void setFilmTwoValues(Film film) {
        Mockito.when(film.getId()).thenReturn(6L);
        Mockito.when(film.getDescription()).thenReturn("Set in the 22nd century, The Matrix tells the story of a computer hacker who joins a group of underground insurgents fighting the vast and powerful computers who now rule the earth.");
        Mockito.when(film.getRating()).thenReturn(7.8);
        Mockito.when(film.getMovieDbId()).thenReturn(603L);
        Mockito.when(film.getReleaseDate()).thenReturn(DateUtils.convertToDate("1999-03-30"));
        Mockito.when(film.getBackdropPathId()).thenReturn("/RhUxjzNojIJsdZSYTn0CQvdKsn.jpg");
        Mockito.when(film.getPosterPathId()).thenReturn("/lh4aGpd3U9rm9B8Oqr6CUgQLtZL.jpg");
        Mockito.when(film.getTitle()).thenReturn("The Matrix");
        Mockito.when(film.getPopularity()).thenReturn(5.525683);
    }

    public static List<Film> createFakeList(int size, Film... films) {
        List<Film> result = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            result.add(films[i % films.length]);
        }

        return result;
    }
}
