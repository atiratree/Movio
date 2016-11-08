package cz.muni.fi.pv256.movio2.fk410022.network;

import cz.muni.fi.pv256.movio2.fk410022.network.dto.Films;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface MovieDbClient {
    String DISCOVER_PART = "3/discover/movie";
    String API_KEY = "api_key";

    @GET(DISCOVER_PART + "?sort_by=popularity.desc")
    Call<Films> listRecentPopular(@Query(API_KEY) String apiKey, @Query("primary_release_date.gte")
            String from, @Query("primary_release_date.lte") String to);

    @GET(DISCOVER_PART + "?ort_by=popularity.desc&with_keywords=10183")
    Call<Films> listCurrentYearPpularIndependent(@Query(API_KEY) String apiKey, @Query("year") int year);

    @GET(DISCOVER_PART + "?sort_by=vote_average.desc&vote_count.gte=10&with_genres=878")
    Call<Films> listHighlyRatedScifi(@Query(API_KEY) String apiKey);
}
