package cz.muni.fi.pv256.movio2.fk410022.network;

import cz.muni.fi.pv256.movio2.fk410022.network.dto.Films;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDbClient {
    int MAX_PAGES = 1000; // api limitation

    int SCIFI_MIN_VOTE_COUNT = 10;
    int POPULAR_MIN_VOTE_COUNT = 30;
    int NEW_MOVIES_MONTHS_BACK = 3;

    String DISCOVER_PART = "3/discover/movie";
    String API_KEY = "api_key";
    String PAGE = "page";

    String PRIMARY_RELEASE_DATE_LTE = "primary_release_date.lte";
    String PRIMARY_RELEASE_DATE_GTE = "primary_release_date.gte";
    String YEAR = "year";

    String ANIMATION_ID = "16";
    String SCIENCE_FICTION_ID = "878";

    @GET(DISCOVER_PART + "?sort_by=popularity.desc&vote_count.gte=" + POPULAR_MIN_VOTE_COUNT)
    Call<Films> listRecentPopular(@Query(API_KEY) String apiKey, @Query(PAGE) int page, @Query(PRIMARY_RELEASE_DATE_GTE)
            String from, @Query(PRIMARY_RELEASE_DATE_LTE) String to);

    @GET(DISCOVER_PART + "?sort_by=popularity.desc&with_genres=" + ANIMATION_ID)
    Call<Films> listCurrentYearPopularAnimation(@Query(API_KEY) String apiKey, @Query(PAGE) int page, @Query(YEAR) int year);

    @GET(DISCOVER_PART + "?sort_by=vote_average.desc&vote_count.gte=" + SCIFI_MIN_VOTE_COUNT + "&with_genres=" + SCIENCE_FICTION_ID)
    Call<Films> listHighlyRatedScifi(@Query(API_KEY) String apiKey, @Query(PAGE) int page);
}
