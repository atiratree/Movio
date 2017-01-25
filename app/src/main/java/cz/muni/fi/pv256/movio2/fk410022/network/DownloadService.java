package cz.muni.fi.pv256.movio2.fk410022.network;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.muni.fi.pv256.movio2.fk410022.BuildConfig;
import cz.muni.fi.pv256.movio2.fk410022.DebugClass;
import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.FilmFacade;
import cz.muni.fi.pv256.movio2.fk410022.network.dto.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.dto.Films;
import cz.muni.fi.pv256.movio2.fk410022.network.exception.EmptyBodyException;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;
import cz.muni.fi.pv256.movio2.fk410022.util.NetworkUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.NotificationUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.PreferencesUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class DownloadService extends IntentService {

    private static final String TAG = DownloadService.class.getSimpleName();

    private final MovieDbClient movieDbClient = buildClient();
    private NotificationUtils notifUtils;
    private PreferencesUtils prefUtils;

    public DownloadService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notifUtils = new NotificationUtils(getApplicationContext(),
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE));
        prefUtils = new PreferencesUtils(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        notifUtils.cancelNotification(NotificationUtils.DOWNLOADING_NOTIFICATION_ID);
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notifUtils.cancelNotification(NotificationUtils.DOWNLOADING_NOTIFICATION_ID);
        super.onTaskRemoved(rootIntent);
    }

    public static void startFullDownload(Context context) {
        startDownload(context, null);
    }

    public static void startDownload(Context context, FilmListType type) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.FILM_LIST_TYPE, type);
        intent.putExtra(Constants.NOTIFY_USER, true);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean notify = intent.getBooleanExtra(Constants.NOTIFY_USER, false);
        if (!NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            if (notify) {
                makeTurnNetworkOnNotification();
            }
            return;
        }

        FilmListType type = (FilmListType) intent.getSerializableExtra(Constants.FILM_LIST_TYPE);
        boolean nextPage = intent.getBooleanExtra(Constants.NEXT_PAGE, false);
        boolean downloadAll = (type == null);

        try {
            if (notify) {
                makeDownloadingNotification();
            }

            Pair<Integer, Integer> updatedCount = null;
            if (downloadAll) {
                updatedCount = FilmFacade.update(downloadAllTypes());
            } else if (nextPage) {
                if (!prefUtils.isPageCapReached(type)) {
                    updatedCount = DownloadNewPage(type, prefUtils.getLastDownloadedPage(type) + 1);
                }
            } else {
                updatedCount = FilmFacade.update(downloadAllPages(type));
            }

            notifyDownloadFinished(type);

            if (notify && updatedCount != null) {
                makeDownloadedNotification(updatedCount, false);
            }
        } catch (Exception x) {
            String ex = x.getMessage() == null ? x.toString() : x.getMessage();
            String message = getString(R.string.error_message,
                    downloadAll ? getString(R.string.movies) : type.getReadableName(), ex);
            notifUtils.fireNotification(NotificationUtils.ERROR_NOTIFICATION_ID, message, true);
        }
    }

    @NonNull
    private Pair<Integer, Integer> DownloadNewPage(FilmListType type, int newPage) throws IOException, EmptyBodyException {
        Pair<Collection<Film>, Integer> result;
        Pair<Integer, Integer> updatedCount;

        do {
            result = downloadPage(type, newPage++);
            updatedCount = FilmFacade.update(result.first);
            if (result.second < newPage) {
                break;
            }
        }
        while (updatedCount.first == 0 && NetworkUtils.isNetworkAvailable(getApplicationContext()));

        return updatedCount;
    }

    private Collection<Film> downloadAllTypes() throws IOException, EmptyBodyException {
        HashMap<Film, Film> result = new HashMap<>();

        for (FilmListType type : FilmListType.values()) {
            for (Film film : downloadAllPages(type)) {
                result.put(film, film);
            }
        }

        return result.values();
    }

    private Collection<Film> downloadAllPages(FilmListType type) throws IOException, EmptyBodyException {
        Set<Film> result = new HashSet<>();
        prefUtils.setPageCapReached(type, false);

        for (int i = 1; i < type.getDefaultNumberOfPages() + 1 && i <= MovieDbClient.MAX_PAGES; i++) {
            Pair<Collection<Film>, Integer> films = downloadPage(type, i);
            result.addAll(films.first);

            if (films.second <= i) { // total Pages check
                break;
            }
        }

        return result;
    }

    private Pair<Collection<Film>, Integer> downloadPage(FilmListType type, int page) throws IOException, EmptyBodyException {
        if (MovieDbClient.MAX_PAGES < page) {
            return new Pair<>(Collections.emptyList(), MovieDbClient.MAX_PAGES);
        }

        retrofit2.Call<Films> requestCall = type.prepareRequestCall(movieDbClient, page);

        Films films = requestCall.execute().body();

        if (films == null) {
            throw new EmptyBodyException(getString(R.string.empty_body_message));
        }

        List<Film> result = Arrays.asList(films.getResults());
        type.processRequestResult(result);

        prefUtils.setLastDownloadedPage(type, page);
        Timber.i("Downloaded %s page %d", type.name(), page);

        if (films.getTotalPages() == page) {
            prefUtils.setPageCapReached(type, true);
        }

        return new Pair<>(result, films.getTotalPages());
    }

    private void notifyDownloadFinished(FilmListType type) {
        Intent finishIntent = new Intent(Constants.FILM_LIST_DOWNLOAD_FINISHED).putExtra(Constants.FILM_LIST_TYPE, type);
        finishIntent.putExtra(Constants.FILM_LIST_TYPE, type);
        LocalBroadcastManager.getInstance(this).sendBroadcast(finishIntent);
    }

    private void makeTurnNetworkOnNotification() {
        final NotificationCompat.Builder builder =
                notifUtils.getNetworkSettingsotificationBuilder(getString(R.string.turn_network_back_on_message));
        notifUtils.fireNotification(NotificationUtils.ERROR_NOTIFICATION_ID, builder, true);
    }

    private MovieDbClient buildClient() {
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        if (BuildConfig.DEBUG) {
            DebugClass.buildDebugClient(retrofitBuilder);
        }

        return retrofitBuilder.build().create(MovieDbClient.class);
    }

    private void makeDownloadedNotification(Pair<Integer, Integer> updatedCount, boolean strong) {
        int newMovieCount = updatedCount.first;
        int updatedMovieCount = updatedCount.second;

        if (newMovieCount == 0 && updatedMovieCount == 0) {
            return;
        }

        String message;

        if (newMovieCount == 0) {
            message = getResources().getString(R.string.updated_message, getUpdatedMoviesMessagePart(updatedMovieCount));
        } else if (updatedMovieCount == 0) {
            message = getDownloadedMessage(newMovieCount);
        } else {
            message = getResources().getString(R.string.and_updated_message, getDownloadedMessage(newMovieCount),
                    getUpdatedMoviesMessagePart(updatedMovieCount));
        }

        notifUtils.fireNotification(NotificationUtils.DOWNLOADED_NOTIFICATION_ID, message, strong);
    }

    @NonNull
    private String getDownloadedMessage(int newMovieCount) {
        return getResources().getQuantityString(R.plurals.downloaded_message, newMovieCount, newMovieCount);
    }

    @NonNull
    private String getUpdatedMoviesMessagePart(int updatedMovieCount) {
        return getResources().getQuantityString(R.plurals.movies, updatedMovieCount, updatedMovieCount);
    }

    private void makeDownloadingNotification() {
        String message = getString(R.string.updating_message);

        NotificationCompat.Builder builder = notifUtils.getMainActivityNotificationBuilder(message)
                .setAutoCancel(false)
                .setTicker(message)
                .setOngoing(true);

        notifUtils.fireNotification(NotificationUtils.DOWNLOADING_NOTIFICATION_ID, builder);
    }
}
