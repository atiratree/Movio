package cz.muni.fi.pv256.movio2.fk410022.ui.film_detail;

import android.os.Bundle;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.ui.BaseMenuActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnSwipeListener;

public class FilmDetailActivity extends BaseMenuActivity {

    private static final String TAG = FilmDetailActivity.class.getSimpleName();

    public static final String FILM_ID_PARAM = "FILM_ID_PARAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        if (findViewById(R.id.detail_fragment_container) != null) {
            FilmDetailFragment detailFragment = FilmDetailFragment.newInstance(getIntent().getLongExtra(FILM_ID_PARAM, -1), new OnSwipeListener() {
                @Override
                public void onSwipeLeft() {
                    finish();
                }

                @Override
                public void onSwipeRight() {
                    finish();
                }
            });
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, detailFragment).commit();
        }
    }
}
