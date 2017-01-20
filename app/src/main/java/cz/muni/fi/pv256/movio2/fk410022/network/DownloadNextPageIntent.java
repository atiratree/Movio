package cz.muni.fi.pv256.movio2.fk410022.network;

import android.content.Context;
import android.content.Intent;

import cz.muni.fi.pv256.movio2.fk410022.util.Constants;

public class DownloadNextPageIntent {

    private Context context;
    private FilmListType type;

    public DownloadNextPageIntent(Context context, FilmListType type) {
        this.context = context.getApplicationContext();
        this.type = type;
    }

    public void downloadNewMovies() {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.FILM_LIST_TYPE, type);
        intent.putExtra(Constants.NEXT_PAGE, true);
        intent.putExtra(Constants.NOTIFY_USER, false);
        context.startService(intent);
    }
}
