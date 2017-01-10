package cz.muni.fi.pv256.movio2.fk410022.db.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import cz.muni.fi.pv256.movio2.fk410022.util.Constants;

import static cz.muni.fi.pv256.movio2.fk410022.db.provider.DbSyntax.*;

public interface DbContract {

    String CONTENT_AUTHORITY =  Constants.APP_PACKAGE_DOT + "provider";
    Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    interface BaseEntity extends BaseColumns {
        String ID = _ID;
    }



    interface Film extends BaseEntity {
        String PATH = "film";
        String PATH_ID = "film/#";

        Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        String TABLE = "film";

        String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH;
        String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH;


        String TITLE = "title";
        String DESCRIPTION = "description";
        String RELEASE_DATE = "release_date";
        String POSTER_PATH_ID = "poster_path_id";
        String BACKDROP_PATH_ID = "backdrop_path_id";
        String POPULARITY = "popularity";
        String RATING = "rating";
        String FAVORITE = "favorite";

        String CREATE_TABLE_SQL =
                CREATE_TABLE + TABLE  + " (" +
                        Film._ID + Type.INTEGER + Type.PRIMARY_KEY+ COMMA +
                        Film.TITLE + Type.TEXT + COMMA +
                        Film.DESCRIPTION + Type.TEXT + COMMA +
                        Film.RELEASE_DATE + Type.TEXT + COMMA +
                        Film.POSTER_PATH_ID + Type.TEXT + COMMA +
                        Film.BACKDROP_PATH_ID + Type.TEXT + COMMA +
                        Film.POPULARITY + Type.REAL + COMMA +
                        Film.RATING + Type.REAL + COMMA +
                        Film.FAVORITE + Type.INTEGER +
                        ")";
    }
}
