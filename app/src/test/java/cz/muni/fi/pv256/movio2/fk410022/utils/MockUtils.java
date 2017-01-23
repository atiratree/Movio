package cz.muni.fi.pv256.movio2.fk410022.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MockUtils {
    public static void setFinalStatic(Class<?> clazz, String fieldName, Object newValue) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }
}
