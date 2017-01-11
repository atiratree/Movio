package cz.muni.fi.pv256.movio2.fk410022.store;

import android.support.annotation.NonNull;

public enum FilmListType {
    RECENT_POPULAR_MOVIES,
    CURRENT_YEAR_POPULAR_ANIMATED_MOVIES,
    HIGHLY_RATED_SCIFI_MOVIES;

    @NonNull
    public String getReadableName() {
        return name().replace('_', ' ').toLowerCase();
    }
}
