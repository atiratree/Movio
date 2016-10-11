package cz.muni.fi.pv256.movio2.fk410022;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        super.onCreate(savedInstanceState);
        PreferencesUtils myPrefs = new PreferencesUtils(this);
        setTheme(myPrefs.getPrefTheme().getValue());
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Film[] movies = new Film[]{
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
        mAdapter = new MyAdapter(movies, this);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.recycler_view);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeTheme:
                changeTheme();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeTheme() {
        PreferencesUtils myPrefs = new PreferencesUtils(this);
        Theme theme = myPrefs.getPrefTheme();
        theme = (theme == Theme.APP_THEME) ? Theme.MY_THEME : Theme.APP_THEME;
        myPrefs.savePrefTheme(theme);
        recreate();
    }
}
