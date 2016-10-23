package cz.muni.fi.pv256.movio2.fk410022.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;
import cz.muni.fi.pv256.movio2.fk410022.adapter.MessageAdapter;
import cz.muni.fi.pv256.movio2.fk410022.adapter.MovieAdapter;
import cz.muni.fi.pv256.movio2.fk410022.model.Film;

public class MainActivity extends MainMenuActivity implements OnItemClickListener {
    private boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTablet = getResources().getBoolean(R.bool.isTablet);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeRecyclerView((RecyclerView) findViewById(R.id.recycler_view_popular_movies), getMovies());
        initializeRecyclerView((RecyclerView) findViewById(R.id.recycler_view_popular_shows), getShows());
    }

    @Override
    public void onItemClick(Film film) {
        if (!isTablet) {
            Intent intent = new Intent(this, FilmDetailActivity.class);
            intent.putExtra(FilmDetailActivity.FILM_PARAM, film);
            startActivity(intent);
        } else {
            FilmDetailFragment fragment = FilmDetailFragment.newInstance(film);
            if (findViewById(R.id.fragment_container) == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
            } else {
                android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        }
    }

    private void initializeRecyclerView(RecyclerView recyclerView, Film[] films) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.Adapter adapter;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (Utils.isNetworkAvailable(getApplicationContext())) {
            //download films
            adapter = (films.length == 0) ? new MessageAdapter("Žádná data") : new MovieAdapter(films, this);

        } else {
            adapter = new MessageAdapter("Žádné připojení");
        }

        recyclerView.setAdapter(adapter);
    }

    private Film[] getMovies() {
        return new Film[]{
                new Film("The Shawshank Redemption",
                        getString(R.string.the_shawshank_redemption_description),
                        1994, 4.858781,
                        R.drawable.the_shawshank_redemption,
                        R.drawable.the_shawshank_redemption_backdrop),
                new Film("The Godfather",
                        getString(R.string.the_godfather__description),
                        2014, 4.117833,
                        R.drawable.the_godfather,
                        R.drawable.the_godfather_backdrop),
                new Film("Whiplash",
                        getString(R.string.whiplash_description),
                        2014, 4.117833,
                        R.drawable.whiplash,
                        R.drawable.whiplash_backdrop),
                new Film("Hunt for the Wilderpeople",
                        getString(R.string.hunt_for_the_wilderpeople_description),
                        2016, 3.776794,
                        R.drawable.hunt_for_the_wilderpeople,
                        R.drawable.hunt_for_the_wilderpeople_backdrop),
                new Film("Spirited Away",
                        getString(R.string.spirited_away_description),
                        2001, 2.596589,
                        R.drawable.spirited_away,
                        R.drawable.spirited_away_backdrop),
                new Film("Interstellar",
                        getString(R.string.hunt_for_the_wilderpeople_description),
                        2014, 11.710438,
                        R.drawable.interstellar,
                        R.drawable.interstellar_backdrop)};
    }

    private Film[] getShows() {
        return new Film[]{};
    }
}
