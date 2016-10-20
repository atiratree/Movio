package cz.muni.fi.pv256.movio2.fk410022;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cz.muni.fi.pv256.movio2.fk410022.model.Film;

import static cz.muni.fi.pv256.movio2.fk410022.FilmDetailActivity.FILM_PARAM;

public class FilmDetailFragment extends Fragment {
    private static final String TAG = FilmDetailFragment.class.getSimpleName();

    public static FilmDetailFragment newInstance(Film film) {
        Bundle args = new Bundle();

        FilmDetailFragment fragment = new FilmDetailFragment();
        args.putParcelable(FILM_PARAM, film);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_film_detail, container, false);
        Bundle bundle = this.getArguments();
        Film film = null;
        if (bundle != null) {
            film = bundle.getParcelable(FILM_PARAM);
        }

        if (film != null) {
            ImageView backdrop = (ImageView) view.findViewById(R.id.backdrop);
            backdrop.setImageResource(film.getBackdrop());

            ImageView cover = (ImageView) view.findViewById(R.id.cover);
            cover.setImageResource(film.getCoverPath());

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(film.getTitle());

            TextView releaseDate = (TextView) view.findViewById(R.id.year);
            releaseDate.setText(String.valueOf(film.getReleaseDate()));

            TextView description = (TextView) view.findViewById(R.id.description);
            description.setText(String.valueOf(film.getDescription()));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }
}
