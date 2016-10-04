package cz.muni.fi.pv256.movio2.fk410022;

import android.app.Activity;
import android.content.Context;

/**
 * Created by suomiy on 10/3/16.
 */

class PreferencesUtils {
    private static final String THEME_PREF_KEY = "THEME_PREF_KEY";

    private Activity activity;

    PreferencesUtils(Activity activity) {
        this.activity = activity;
    }

    Theme getPrefTheme() {
        return Theme.fromValue(activity.getPreferences(Context.MODE_PRIVATE)
                .getInt(THEME_PREF_KEY, Theme.APP_THEME.getValue()));
    }

    boolean savePrefTheme(Theme theme) {
        return activity.getPreferences(Context.MODE_PRIVATE).edit().putInt(THEME_PREF_KEY, theme.getValue()).commit();
    }
}
