package cz.muni.fi.pv256.movio2.fk410022.db.provider;

import android.provider.BaseColumns;

public interface DbContract {

    interface BaseEntity extends BaseColumns {
        String ID = _ID;
    }

    interface Film extends BaseEntity {
        String TABLE = "film";

        String MOVIE_DB_ID = "movie_db_id";
        String TITLE = "title";
        String DESCRIPTION = "description";
        String RELEASE_DATE = "release_date";
        String POSTER_PATH_ID = "poster_path_id";
        String BACKDROP_PATH_ID = "backdrop_path_id";
        String POPULARITY = "popularity";
        String RATING = "rating";
        String RATING_VOTE_COUNT = "rating_vote_count";
    }

    interface Favorites extends BaseEntity {
        String TABLE = "favorites";

        String FILM = "film";
        String FAVORITE = "favorite";
    }

    interface FilmGenre extends BaseEntity {
        String TABLE = "film_genre";

        String FILM = "film";
        String GENRE = "genre";
    }
}
