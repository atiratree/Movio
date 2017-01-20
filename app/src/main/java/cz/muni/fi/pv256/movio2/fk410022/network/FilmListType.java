package cz.muni.fi.pv256.movio2.fk410022.network;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import java.util.Collection;
import java.util.Date;

import cz.muni.fi.pv256.movio2.fk410022.network.dto.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.dto.Films;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;
import retrofit2.Call;

/**
 * Use helper methods of this enum to make requests because of {@link FilmListType#CURRENT_YEAR_POPULAR_ANIMATED_MOVIES}
 */
public enum FilmListType {
    RECENT_POPULAR_MOVIES(5),
    /**
     * This type is a bit hacky, because it is not viable to download release dates of each film
     */
    CURRENT_YEAR_POPULAR_ANIMATED_MOVIES(5),
    HIGHLY_RATED_SCIFI_MOVIES(5);

    private final int defaultNumberOfPages;

    FilmListType(int defaultNumberOfPages) {
        if(defaultNumberOfPages < 2){
            throw new IllegalArgumentException("Must be at least 2 because of ContinuousFilmAdapterPresenter");
        }
        this.defaultNumberOfPages = defaultNumberOfPages;
    }

    @NonNull
    public String getReadableName() {
        return name().replace('_', ' ').toLowerCase();
    }

    public int getDefaultNumberOfPages() {
        return defaultNumberOfPages;
    }

    /**
     * Caller must also call {@link FilmListType#processRequestResult(Collection)} after the request
     */
    @NonNull
    public Call<Films> prepareRequestCall(MovieDbClient movieDbClient, int page) {
        Utils.checkNotNull(movieDbClient);

        Call<Films> requestCall;
        switch (this) {
            case CURRENT_YEAR_POPULAR_ANIMATED_MOVIES:
                requestCall = movieDbClient.listCurrentYearPopularAnimation(Constants.API_KEY, page, DateUtils.getCurrentYearAsInt());
                break;
            case HIGHLY_RATED_SCIFI_MOVIES:
                requestCall = movieDbClient.listHighlyRatedScifi(Constants.API_KEY, page);
                break;
            case RECENT_POPULAR_MOVIES:
            default:
                requestCall = movieDbClient.listRecentPopular(Constants.API_KEY, page,
                        DateUtils.convertToString(DateUtils.getNewMoviesMonthsBack()),
                        DateUtils.convertToString(DateUtils.getToday()));
                break;
        }
        return requestCall;
    }

    /**
     * this method must be called after the request
     */
    public void processRequestResult(Collection<Film> films) {
        Utils.checkNotNull(films);
        switch (this) {
            case CURRENT_YEAR_POPULAR_ANIMATED_MOVIES:
                // Movies have multiple release dates and this REST query is about current year
                // and the query takes highest release date into account
                Date currentYear = DateUtils.getCurrentYear();
                Stream.of(films).forEach(film -> film.setLateReleaseDate(currentYear));
                break;
            default:
                break;
        }
    }
}
