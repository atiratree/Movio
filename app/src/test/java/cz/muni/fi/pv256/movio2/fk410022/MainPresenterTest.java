package cz.muni.fi.pv256.movio2.fk410022;

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

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.sync.SyncIntent;
import cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.MainContract;
import cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.MainPresenter;
import cz.muni.fi.pv256.movio2.fk410022.utils.DataUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.LastMethodCall;
import cz.muni.fi.pv256.movio2.fk410022.utils.MockUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.ParamUtils;
import de.schauderhaft.rules.parameterized.Generator;
import de.schauderhaft.rules.parameterized.ListGenerator;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {App.class, RxStore.class, RxDbObservables.class, Film.class})
public class MainPresenterTest {
    @Rule
    public TestName name = new TestName();

    private Boolean isTablet;
    private Boolean selectedFilm;
    private Boolean showDiscover;

    @Rule
    @SuppressWarnings("unchecked")
    public Generator<Boolean[]> parameters = new ListGenerator(ParamUtils.getTruthTable(3));

    private void initializeParams() {
        final Boolean[] params = parameters.value();
        isTablet = params[0];
        selectedFilm = params[1];
        showDiscover = params[2];
        System.out.println(String.format(Locale.ENGLISH, "%s: run %d isTablet=%b selectedFilm=%b showDiscover=%b",
                name.getMethodName(), parameters.runIndex(), isTablet, selectedFilm, showDiscover));
    }

    @Mock
    private MainContract.View view;

    @Mock
    private Film filmOne;

    @Mock
    private Film filmTwo;

    @Mock
    private SyncIntent syncIntent;

    private MainPresenter presenter;

    @Before
    public void setUp() throws Exception {
        initializeParams();

        DataUtils.setFilmOneValues(filmOne);
        DataUtils.setFilmTwoValues(filmTwo);

        PowerMockito.mockStatic(RxStore.class);
        MockUtils.setFinalStatic(RxStore.class, "SHOW_DISCOVER", new SerializedSubject<>(BehaviorSubject.create(showDiscover)));
        MockUtils.setFinalStatic(RxStore.class, "SELECTED_FILM", new SerializedSubject<>(
                BehaviorSubject.create(selectedFilm ? new SelectedFilm(filmOne.getId()) : SelectedFilm.EMPTY)));

        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.isTablet()).thenReturn(isTablet);

        PowerMockito.mockStatic(RxDbObservables.class);
        PowerMockito.when(RxDbObservables.createFilmObservable(filmOne.getId())).thenReturn(rx.Observable.just(filmOne));
        PowerMockito.when(RxDbObservables.createFilmObservable(filmTwo.getId())).thenReturn(rx.Observable.just(filmTwo));

        initializeTestAndReset();
    }

    private void initializeTestAndReset() {
        presenter = new MainPresenter(view, syncIntent).initialize();

        Mockito.reset(view);
        Mockito.reset(filmOne);
        Mockito.reset(filmTwo);
        Mockito.reset(syncIntent);
    }

    @Test
    public void testOnRefreshClicked() {
        presenter.onRefreshClicked();

        verify(syncIntent).sync();
    }

    @Test
    public void testOnShowFavoritesClicked() throws Exception {
        if (showDiscover) {
            presenter.onShowFavoritesClicked();

            if (isTablet && selectedFilm) {
                verify(view).toggleFilmDetail(false);
            }

            verify(view, new LastMethodCall()).refreshMenu(true, false, false);
            verify(view).setFavoritesTitle();
            verify(view).replaceFilmLists(false);
        }
    }

    @Test
    public void testOnShowDiscoverClicked() throws Exception {
        if (!showDiscover) {
            presenter.onShowDiscoverClicked();

            if (isTablet && selectedFilm) {
                verify(view).toggleFilmDetail(false);
            }

            verify(view, new LastMethodCall()).refreshMenu(false, true, false);
            verify(view).setDiscoverTitle();
            verify(view).replaceFilmLists(true);
        }
    }

    @Test
    public void testOnCloseDetailClicked() throws Exception {
        if (isTablet && selectedFilm) {
            presenter.onCloseDetailClicked();

            verify(view).toggleFilmDetail(false);
            verify(view).refreshMenu(!showDiscover, showDiscover, false);

            if (showDiscover) {
                verify(view).setDiscoverTitle();
            } else {
                verify(view).setFavoritesTitle();
            }
        }
    }

    @Test
    public void testFilmSelectAndDeselect() throws Exception {

        boolean unSelectedFilm = selectedFilm; // invert
        RxStore.SELECTED_FILM.onNext(unSelectedFilm ? SelectedFilm.EMPTY : new SelectedFilm(filmTwo.getId()));
        if (isTablet) {
            verify(view).toggleFilmDetail(!unSelectedFilm);
            verify(view, new LastMethodCall()).refreshMenu(!showDiscover, showDiscover, !unSelectedFilm);

            if (!unSelectedFilm) {
                verify(view).setTitle(filmTwo.getTitle());
            } else if (showDiscover) {
                verify(view).setDiscoverTitle();
            } else {
                verify(view).setFavoritesTitle();
            }
        } else if (!unSelectedFilm) {
            verify(view).startDetailActivity();
        }
    }

    @Test
    public void testNewFilmSelected() throws Exception {
        if (isTablet && selectedFilm) {
            RxStore.SELECTED_FILM.onNext(new SelectedFilm(filmTwo.getId()));
            verify(view, never()).toggleFilmDetail(Mockito.anyBoolean());
            verify(view, never()).refreshMenu(Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyBoolean());
            verify(view, never()).replaceFilmLists(Mockito.anyBoolean());

            verify(view).setTitle(filmTwo.getTitle());
        }
    }
}
