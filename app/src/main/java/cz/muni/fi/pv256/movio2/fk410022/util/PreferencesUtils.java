package cz.muni.fi.pv256.movio2.fk410022.util;

import android.content.Context;
import android.content.SharedPreferences;

import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.ui.Theme;

public class PreferencesUtils {
    private static final String THEME_PREF_KEY = "THEME_PREF_KEY";
    private static final String LAST_DOWNLOADED_PAGE = "LAST_DOWNLOADED_PAGE";
    private static final String PAGE_CAP_REACHED = "PAGE_CAP_REACHED";
    private static final String MOVIO_SHARED_PREFERENCES = "MOVIO_SHARED_PREFERENCES";

    private final Context context;

    public PreferencesUtils(Context context) {
        this.context = context.getApplicationContext();
    }

    public Theme getPrefTheme() {
        return Theme.fromValue(getPreferences().getInt(THEME_PREF_KEY, Theme.APP_THEME.getValue()));
    }

    public boolean setPrefTheme(Theme theme) {
        return getPreferences().edit().putInt(THEME_PREF_KEY, theme.getValue()).commit();
    }

    public int getLastDownloadedPage(FilmListType type) {
        return getPreferences().getInt(LAST_DOWNLOADED_PAGE + type.name(), type.getDefaultNumberOfPages());
    }

    public boolean setLastDownloadedPage(FilmListType type, int page) {
        return getPreferences().edit().putInt(LAST_DOWNLOADED_PAGE + type.name(), page).commit();
    }

    public boolean isPageCapReached(FilmListType type) {
        return getPreferences().getBoolean(PAGE_CAP_REACHED + type.name(), false);
    }

    public boolean setPageCapReached(FilmListType type, boolean pageCapReached) {
        return getPreferences().edit().putBoolean(PAGE_CAP_REACHED + type.name(), pageCapReached).commit();
    }

    public SharedPreferences getPreferences() {
        return context.getSharedPreferences(MOVIO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }
}
