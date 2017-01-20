package cz.muni.fi.pv256.movio2.fk410022.ui.film_fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.custom.OnSwipeListener;
import cz.muni.fi.pv256.movio2.fk410022.ui.custom.OnSwipeTouchListener;
import cz.muni.fi.pv256.movio2.fk410022.util.ColorUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.image.ImageHelper;

public class FilmDetailFragment extends Fragment implements FilmDetailContract.View {
    private static final String TAG = FilmDetailFragment.class.getSimpleName();

    private View view;

    private FloatingActionButton fab;

    private OnSwipeTouchListener onSwipeTouchListener;

    FilmDetailContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_film_detail, container, false);
        onSwipeTouchListener = new OnSwipeTouchListener(getContext());
        view.setOnTouchListener(onSwipeTouchListener);
        fab = (FloatingActionButton) view.findViewById(R.id.add_to_favorites);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));

        return view;
    }

    @Override
    public void onDestroyView() {
        removeImages();
        presenter.destroy();
        view = null;
        fab = null;
        onSwipeTouchListener = null;
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter = new FilmDetailPresenter(this).initialize();
        onSwipeTouchListener.setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onSwipeRight() {
                presenter.onSwipeRight();
            }
        });
    }

    @Override
    public void showFavorite(boolean show) {
        if (fab != null) {
            fab.setImageResource(show ? R.drawable.ic_clear_white_24dp : R.drawable.ic_add_white_24dp);

            int backgroundColor = ContextCompat.getColor(getContext(),
                    show ? R.color.fab_warning : ColorUtils.getThemeAccentResourceId(getContext()));
            fab.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
            fab.setOnClickListener(e -> presenter.toggleFavorite());
        }
    }

    private void removeImages() {
        removeImage((ImageView) view.findViewById(R.id.poster));
        removeImage((ImageView) view.findViewById(R.id.backdrop));
    }

    private void removeImage(ImageView image) {
        ImageHelper.cancelDisplay(image);
        image.setImageDrawable(null);
    }

    @Override
    public void showMovie(Film film) {
        if (film != null) {
            ImageView poster = (ImageView) view.findViewById(R.id.poster);
            ImageView backdrop = (ImageView) view.findViewById(R.id.backdrop);

            poster.setContentDescription(getString(R.string.accessibility_poster_of, film.getTitle()));
            backdrop.setContentDescription(getString(R.string.accessibility_backdrop_of, film.getTitle()));

            removeImage(poster);
            removeImage(backdrop);
            ImageHelper.displayPoster(film, poster);
            ImageHelper.displayBackdrop(film, backdrop);

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(film.getTitle());

            TextView releaseDate = (TextView) view.findViewById(R.id.release_date);
            releaseDate.setText(DateUtils.convertToReadableString(film.getReleaseDate()));

            TextView description = (TextView) view.findViewById(R.id.description);
            description.setText(String.valueOf(film.getDescription()));
            description.setOnTouchListener(onSwipeTouchListener);

            NestedScrollView nestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
            nestedScrollView.setOnTouchListener(onSwipeTouchListener);
        }
    }
}
