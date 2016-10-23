package cz.muni.fi.pv256.movio2.fk410022.ui;

import android.os.Bundle;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.model.Film;

public class FilmDetailActivity extends MainMenuActivity {
    private static final String TAG = FilmDetailActivity.class.getSimpleName();
    public static final String FILM_PARAM = "FILM_PARAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            FilmDetailFragment firstFragment = FilmDetailFragment.newInstance((Film) getIntent().getParcelableExtra(FILM_PARAM));
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }
}
