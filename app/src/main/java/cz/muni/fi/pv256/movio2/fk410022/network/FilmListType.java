package cz.muni.fi.pv256.movio2.fk410022.network;

import android.support.annotation.NonNull;

import cz.muni.fi.pv256.movio2.fk410022.network.MovieDbClient;
import cz.muni.fi.pv256.movio2.fk410022.network.dto.Films;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;
import retrofit2.Call;

public enum FilmListType {
    RECENT_POPULAR_MOVIES,
    CURRENT_YEAR_POPULAR_ANIMATED_MOVIES,
    HIGHLY_RATED_SCIFI_MOVIES;

    @NonNull
    public String getReadableName() {
        return name().replace('_', ' ').toLowerCase();
    }

    @NonNull
    public Call<Films> makeRequest(MovieDbClient movieDbClient) {
        Utils.checkNotNull(movieDbClient);

        Call<Films> requestCall;
        switch (this) {
            case CURRENT_YEAR_POPULAR_ANIMATED_MOVIES:
                requestCall = movieDbClient.listCurrentYearPopularAnimation(Constants.API_KEY, DateUtils.getCurrentYearAsInt());
                break;
            case HIGHLY_RATED_SCIFI_MOVIES:
                requestCall = movieDbClient.listHighlyRatedScifi(Constants.API_KEY);
                break;
            case RECENT_POPULAR_MOVIES:
            default: // for NonNull annotation
                requestCall = movieDbClient.listRecentPopular(Constants.API_KEY,
                        DateUtils.convertToString(DateUtils.getTwoMonthsBack()),
                        DateUtils.convertToString(DateUtils.getToday()));
                break;
        }
        return requestCall;
    }
}
