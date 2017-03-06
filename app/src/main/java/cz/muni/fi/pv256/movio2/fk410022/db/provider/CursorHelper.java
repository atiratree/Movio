package cz.muni.fi.pv256.movio2.fk410022.db.provider;

import android.database.Cursor;

import java.sql.Timestamp;

public class CursorHelper {
    private final Cursor cursor;

    public CursorHelper(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getString(String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public byte[] getByteArray(String columnName) {
        return cursor.getBlob(cursor.getColumnIndexOrThrow(columnName));
    }

    public int getInt(String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public long getLong(String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public double getDouble(String columnName) {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(columnName));
    }

    public boolean getBoolean(String columnName) {
        return getInt(columnName) > 0;
    }

    public boolean getBoolean(int columnIndex) {
        return cursor.getInt(columnIndex) > 0;
    }

    public <E extends Enum<E>> E getEnum(String columnName, Class<E> clazz) {
        return E.valueOf(clazz, getString(columnName));
    }

    public Timestamp getTimestamp(String columnName) {
        return Timestamp.valueOf(getString(columnName));
    }
}
