package cz.muni.fi.pv256.movio2.fk410022.ui.film_activity;

import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.BasePresenter;

public interface FilmContract {

    interface View {

        void setTitle(String title);
    }

    interface Presenter extends BasePresenter {

        void onBackPressed();

        void onHomePressed();
    }
}
