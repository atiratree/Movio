package cz.muni.fi.pv256.movio2.fk410022.pojo;

import org.junit.Test;

import cz.muni.fi.pv256.movio2.fk410022.util.Utils;

public class UtilsTest {
    @Test
    public void checkNotNull() {
        Utils.checkNotNull(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNotNullException() {
        Utils.checkNotNull(null);
    }
}
