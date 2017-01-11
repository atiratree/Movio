package cz.muni.fi.pv256.movio2.fk410022.util;

import android.content.Context;

import cz.muni.fi.pv256.movio2.fk410022.ui.Theme;

public class ThemeHelper {

    public static Theme getActiveTheme(Context context) {
        PreferencesUtils myPrefs = new PreferencesUtils(context);
        Theme theme = myPrefs.getPrefTheme();
        return theme == null ? Theme.APP_THEME : theme;
    }
}
