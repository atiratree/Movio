package cz.muni.fi.pv256.movio2.fk410022.pojo;

import android.graphics.Color;
import android.support.v7.graphics.Palette;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;

import cz.muni.fi.pv256.movio2.fk410022.util.ColorUtils;

public class ColorUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void addAlphaToColorException() {
        ColorUtils.addAlphaToColor(Color.RED, -0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAlphaToColorException2() {
        ColorUtils.addAlphaToColor(Color.RED, 1.1f);
    }

    @Test
    public void addAlphaToColor() {
        Assert.assertEquals(ColorUtils.addAlphaToColor(Color.BLACK, 0), 0x00000000);
        Assert.assertEquals(ColorUtils.addAlphaToColor(Color.BLACK, 0.5f), 0x7F000000);
        Assert.assertEquals(ColorUtils.addAlphaToColor(Color.BLACK, 1), Color.BLACK);

        Assert.assertEquals(ColorUtils.addAlphaToColor(Color.RED, 0), 0x00FF0000);
        Assert.assertEquals(ColorUtils.addAlphaToColor(Color.RED, 0.5f), 0x7FFF0000);
        Assert.assertEquals(ColorUtils.addAlphaToColor(Color.RED, 1), Color.RED);
    }

    @Test
    public void getDominantColor() {
        Assert.assertEquals(ColorUtils.getDominantColor(Palette.from(Arrays.asList(
                new Palette.Swatch(Color.BLACK, 4),
                new Palette.Swatch(Color.RED, 5),
                new Palette.Swatch(Color.BLUE, 6)
        ))), Color.BLUE);

        Assert.assertEquals(ColorUtils.getDominantColor(null), Color.BLACK);
    }

    @Test
    public void getAlphaGradientColors() {
        final int[] colors = ColorUtils.getAlphaGradientColors(Color.RED, 0, 0.5f);
        Assert.assertEquals(colors[0], 0x00FF0000);
        Assert.assertEquals(colors[1], 0x7FFF0000);
    }
}
