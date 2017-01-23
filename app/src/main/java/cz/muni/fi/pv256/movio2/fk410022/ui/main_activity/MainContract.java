package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity;

import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.BasePresenter;

public interface MainContract {

    interface View {
        void replaceFilmLists(boolean showDiscover);

        void toggleFilmDetail(boolean showDetail);

        void startDetailActivity();

        void setTitle(String title);

        void setFavoritesTitle();

        void setDiscoverTitle();

        void refreshMenu(boolean showDiscoverButton, boolean showFavoritesButton, boolean showCloseDetailButton);
    }

    interface Presenter extends BasePresenter {
        void onRefreshClicked();

        void onShowFavoritesClicked();

        void onShowDiscoverClicked();

        void onCloseDetailClicked();
    }
}
