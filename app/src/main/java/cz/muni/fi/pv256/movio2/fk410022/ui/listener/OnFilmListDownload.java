package cz.muni.fi.pv256.movio2.fk410022.ui.listener;

import cz.muni.fi.pv256.movio2.fk410022.model.store.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadFilmListTask;

public interface OnFilmListDownload {
    void onFinishedDownload(FilmListType type);

    void onStartDownload(FilmListType type, DownloadFilmListTask.Cancelable cancelable);
}
