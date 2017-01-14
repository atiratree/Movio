package cz.muni.fi.pv256.movio2.fk410022.network;

import android.support.annotation.NonNull;

import cz.muni.fi.pv256.movio2.fk410022.network.dto.Films;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;
import retrofit2.Call;

public enum FilmListType {
    RECENT_POPULAR_MOVIES(7),
    CURRENT_YEAR_POPULAR_ANIMATED_MOVIES(7),
    HIGHLY_RATED_SCIFI_MOVIES(7);

    private int defaultNumberOfPages;

    FilmListType(int defaultNumberOfPages) {
        this.defaultNumberOfPages = defaultNumberOfPages;
    }

    @NonNull
    public String getReadableName() {
        return name().replace('_', ' ').toLowerCase();
    }

    public int getDefaultNumberOfPages() {
        return defaultNumberOfPages;
    }

    @NonNull
    public Call<Films> makeRequest(MovieDbClient movieDbClient, int page) {
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
            default: // for NonNull annotation
                requestCall = movieDbClient.listRecentPopular(Constants.API_KEY, page,
                        DateUtils.convertToString(DateUtils.getNewMoviesMonthsBack()),
                        DateUtils.convertToString(DateUtils.getToday()));
                break;
        }
        return requestCall;
    }
}
