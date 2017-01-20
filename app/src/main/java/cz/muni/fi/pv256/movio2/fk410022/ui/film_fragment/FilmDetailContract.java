package cz.muni.fi.pv256.movio2.fk410022.ui.film_fragment;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.BasePresenter;

public interface FilmDetailContract {

    interface View {
        void showMovie(Film film);

        void showFavorite(boolean show);
    }

    interface Presenter extends BasePresenter {
        void toggleFavorite();

        void onSwipeRight();
    }
}
