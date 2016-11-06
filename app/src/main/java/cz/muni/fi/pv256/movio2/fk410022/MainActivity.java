package cz.muni.fi.pv256.movio2.fk410022;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        Movie[] movies = new Movie[]{new Movie("The Shawshank Redemption", R.drawable.the_shawshank_redemption),
                new Movie("The Godfather", R.drawable.the_godfather),
                new Movie("Whiplash", R.drawable.whiplash),
                new Movie("Hunt for the Wilderpeople", R.drawable.hunt_for_the_wilderpeople),
                new Movie("Spirited Away", R.drawable.spirited_away),
                new Movie("Interstellar", R.drawable.interstellar)};
        mAdapter = new MyAdapter(movies);
        mRecyclerView.setAdapter(mAdapter);
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

        // changeTheme activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
