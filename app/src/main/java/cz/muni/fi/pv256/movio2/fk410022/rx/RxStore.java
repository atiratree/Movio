package cz.muni.fi.pv256.movio2.fk410022.rx;

import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxStore {
    public static final Subject<Boolean, Boolean> SHOW_DISCOVER = new SerializedSubject<>(BehaviorSubject.create(true));
    public static final Subject<SelectedFilm, SelectedFilm> SELECTED_FILM = new SerializedSubject<>(BehaviorSubject.create(SelectedFilm.EMPTY));
}
