package cz.muni.fi.pv256.movio2.fk410022.ui.film_activity;

import android.os.Bundle;
import android.view.MenuItem;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.ui.MovioActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_fragment.FilmDetailFragment;

public class FilmActivity extends MovioActivity implements FilmContract.View {

    private static final String TAG = FilmActivity.class.getSimpleName();

    FilmContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);
        if (findViewById(R.id.detail_fragment_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, new FilmDetailFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onHomePressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        presenter = new FilmPresenter(this).initialize();
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        presenter.destroy();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
