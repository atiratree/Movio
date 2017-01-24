package cz.muni.fi.pv256.movio2.fk410022.presenters;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Locale;

import cz.muni.fi.pv256.movio2.fk410022.App;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_activity.FilmContract;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_activity.FilmPresenter;
import cz.muni.fi.pv256.movio2.fk410022.utils.DataUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.LastMethodCall;
import cz.muni.fi.pv256.movio2.fk410022.utils.MockUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.ParamUtils;
import de.schauderhaft.rules.parameterized.Generator;
import de.schauderhaft.rules.parameterized.ListGenerator;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {App.class, RxStore.class, RxDbObservables.class, Film.class})
public class FilmPresenterTest {

    @Rule
    public TestName name = new TestName();

    private Boolean isTablet;
    private Boolean selectedFilm;

    @Rule
    @SuppressWarnings("unchecked")
    public Generator<Boolean[]> parameters = new ListGenerator(ParamUtils.getTruthTable(2));

    private void initializeParams() {
        final Boolean[] params = parameters.value();
        isTablet = params[0];
        selectedFilm = params[1];
        System.out.println(String.format(Locale.ENGLISH, "%s: run %d isTablet=%b selectedFilm=%b",
                name.getMethodName(), parameters.runIndex(), isTablet, selectedFilm));
    }

    @Mock
    private FilmContract.View view;

    @Mock
    private Film filmOne;

    @Mock
    private Film filmTwo;

    private FilmPresenter presenter;

    @Before
    public void setUp() throws Exception {
        initializeParams();

        DataUtils.setFilmOneValues(filmOne);
        DataUtils.setFilmTwoValues(filmTwo);

        PowerMockito.mockStatic(RxStore.class);
        MockUtils.setFinalStatic(RxStore.class, "SELECTED_FILM", new SerializedSubject<>(
                BehaviorSubject.create(selectedFilm ? new SelectedFilm(filmOne.getId()) : SelectedFilm.EMPTY)));

        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.isTablet()).thenReturn(isTablet);

        PowerMockito.mockStatic(RxDbObservables.class);
        PowerMockito.when(RxDbObservables.createFilmObservable(filmOne.getId())).thenReturn(rx.Observable.just(filmOne));
        PowerMockito.when(RxDbObservables.createFilmObservable(filmTwo.getId())).thenReturn(rx.Observable.just(filmTwo));
        PowerMockito.when(RxDbObservables.createFilmObservable(null)).thenReturn(rx.Observable.just(null));

        initializeTestAndReset();
    }

    private void initializeTestAndReset() {
        presenter = new FilmPresenter(view).initialize();

        if (selectedFilm) {
            verify(view).setTitle(filmOne.getTitle());
        } else {
            verify(view, new LastMethodCall()).finish();
        }

        Mockito.reset(view);
        Mockito.reset(filmOne);
        Mockito.reset(filmTwo);
    }

    @Test
    public void testOnBackPressed() {
        if (selectedFilm) {
            presenter.onBackPressed();
            verify(view).finish();
        }
    }

    @Test
    public void testOnHomePressed() {
        if (selectedFilm) {
            presenter.onHomePressed();
            verify(view).finish();
        }
    }

    @Test
    public void testOnNewFilmChange() throws Exception {
        if (selectedFilm) {
            RxStore.SELECTED_FILM.onNext(new SelectedFilm(filmTwo.getId()));
            verify(view).setTitle(filmTwo.getTitle());
        }
    }

    @Test
    public void testOnEmptyFilmChange() throws Exception {
        if (selectedFilm) {
            RxStore.SELECTED_FILM.onNext(SelectedFilm.EMPTY);// invert
            verify(view).finish();
        }
    }
}
