package cz.muni.fi.pv256.movio2.fk410022.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.R;

public class ColorUtils {
    @ColorInt
    public static int addAlphaToColor(int color, float opacity) {
        if (opacity > 1 || opacity < 0) {
            throw new IllegalArgumentException("opacity should be in interval 0-1");
        }

        return ((int) (opacity * 255.0f) << 24) | (color & 0x00ffffff);
    }

    @ColorInt
    public static int getDominantColor(Palette palette) {
        List<Palette.Swatch> swatches = palette.getSwatches();
        if (swatches.isEmpty()) {
            return 0xff000000; // black
        }
        return Collections.max(swatches, (sw1, sw2) -> sw1.getPopulation() - sw2.getPopulation()).getRgb();
    }

    @NonNull
    public static int[] getAlphaGradientColors(int color, float minAlpha, float maxAlpha) {
        return new int[]{addAlphaToColor(color, minAlpha), addAlphaToColor(color, maxAlpha)};
    }

    @NonNull
    public static Palette generatePalette(Bitmap bitmap) {
        return Palette
                .from(bitmap)
                .generate();
    }

    @NonNull
    public static Palette generateBottomPalette(Bitmap bitmap) {
        return Palette.from(bitmap)
                .setRegion(0, (int) (bitmap.getHeight() * 0.9), bitmap.getWidth(), bitmap.getHeight())
                .generate();
    }

    @ColorRes
    public static int getThemeAccentResourceId(final Context context) {
        switch (ThemeHelper.getActiveTheme(context)) {
            case MY_THEME:
                return R.color.my_theme_accent;
            case APP_THEME:
            default:
                return R.color.accent;
        }
    }
}
