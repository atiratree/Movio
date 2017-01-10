package cz.muni.fi.pv256.movio2.fk410022.db.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbContract.Film;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    public static final String DB_NAME = "movio.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        executeAndLog(db, Film.CREATE_TABLE_SQL);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        executeAndLog(db, DbSyntax.DROP_TABLE_IF_EXISTS + Film.TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void executeAndLog(SQLiteDatabase db, String sql) {
        Log.i(TAG, sql);
        db.execSQL(sql);
    }
}

