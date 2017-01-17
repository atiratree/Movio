package cz.muni.fi.pv256.movio2.fk410022.util;

import android.content.Context;
import android.content.SharedPreferences;

import cz.muni.fi.pv256.movio2.fk410022.ui.Theme;

public class PreferencesUtils {
    private static final String THEME_PREF_KEY = "THEME_PREF_KEY";
    private static final String MOVIO_SHARED_PREFERENCES = "MOVIO_SHARED_PREFERENCES";

    private Context context;

    public PreferencesUtils(Context context) {
        this.context = context;
    }

    public Theme getPrefTheme() {
        return Theme.fromValue(getPreferences().getInt(THEME_PREF_KEY, Theme.APP_THEME.getValue()));
    }

    public boolean savePrefTheme(Theme theme) {
        return getPreferences().edit().putInt(THEME_PREF_KEY, theme.getValue()).commit();
    }

    public SharedPreferences getPreferences() {
        return context.getSharedPreferences(MOVIO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }
}
