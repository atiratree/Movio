package cz.muni.fi.pv256.movio2.fk410022.network;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.model.store.FilmListStore;
import cz.muni.fi.pv256.movio2.fk410022.model.store.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.network.dto.Films;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnFilmListDownload;
import cz.muni.fi.pv256.movio2.fk410022.util.ApiUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadFilmListTask extends AsyncTask<Void, Void, List<Film>> {
    private static final String TAG = DownloadFilmListTask.class.getSimpleName();
    private static final FilmListStore filmListStore = FilmListStore.INSTANCE;

    private OnFilmListDownload caller;
    private FilmListType type;

    private Call requestCall;

    public DownloadFilmListTask(OnFilmListDownload caller, FilmListType type) {
        Utils.checkNotNull(caller);
        Utils.checkNotNull(type);
        this.caller = caller;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Request request = new Request.Builder()
                .url(ApiUtils.DISCOVER_URL + "?" + ApiUtils.API_KEY + type.getUrlParameters())
                .build();
        requestCall = new OkHttpClient().newCall(request);

        caller.onStartDownload(type, () -> {
            if (requestCall != null && !requestCall.isExecuted()) {
                requestCall.cancel();
            }
            DownloadFilmListTask.this.cancel(false);
        });
    }

    @Override
    protected List<Film> doInBackground(Void... voids) {
        Response response = null;
        try {
            response = requestCall.execute();
            if (isCancelled() || requestCall.isCanceled()) {
                return null;
            }
            Films films = new Gson().fromJson(response.body().string(), Films.class);
            Log.i(TAG, String.format("Downloaded %d films", films.getResultsCount()));
            return films.toEntityList();
        } catch (Exception x) {
            Log.e(TAG, x.getMessage() == null ? x.toString() : x.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Film> result) {
        filmListStore.putAll(type, result);
        caller.onFinishedDownload(type);
    }

    public interface Cancelable {
        void cancel();
    }
}
