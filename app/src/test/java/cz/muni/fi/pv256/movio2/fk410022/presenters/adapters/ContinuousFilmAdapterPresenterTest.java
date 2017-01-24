package cz.muni.fi.pv256.movio2.fk410022.presenters.adapters;

import org.junit.Assert;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cz.muni.fi.pv256.movio2.fk410022.App;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadNextPageIntent;
import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.FilmAdapterContract;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous.AnimatedFilmsAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous.PopularFilmsAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous.ScifiFilmsAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.utils.DataUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.MockUtils;
import de.schauderhaft.rules.parameterized.Generator;
import de.schauderhaft.rules.parameterized.ListGenerator;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {App.class, RxStore.class, RxDbObservables.class, Film.class})
public class ContinuousFilmAdapterPresenterTest {

    @Rule
    public TestName name = new TestName();

    private Boolean emptyDataSet;
    private FilmListType filmListType;

    @Rule
    @SuppressWarnings("unchecked")
    public Generator<Object[]> parameters = new ListGenerator(Arrays.asList(
            new Object[]{
                    FilmListType.RECENT_POPULAR_MOVIES,
                    true,
            },
            new Object[]{
                    FilmListType.RECENT_POPULAR_MOVIES,
                    false
            },
            new Object[]{
                    FilmListType.CURRENT_YEAR_POPULAR_ANIMATED_MOVIES,
                    true
            },
            new Object[]{
                    FilmListType.CURRENT_YEAR_POPULAR_ANIMATED_MOVIES,
                    false
            },
            new Object[]{
                    FilmListType.HIGHLY_RATED_SCIFI_MOVIES,
                    true
            },
            new Object[]{
                    FilmListType.HIGHLY_RATED_SCIFI_MOVIES,
                    false
            }));

    private void initializeParams() {
        final Object[] params = parameters.value();
        filmListType = (FilmListType) params[0];
        emptyDataSet = (Boolean) params[1];

        System.out.println(String.format(Locale.ENGLISH, "%s: run %d  filmListType=%s emptyDataSet=%b",
                name.getMethodName(), parameters.runIndex(), filmListType, emptyDataSet));
    }

    @Mock
    private FilmAdapterContract.View view;

    @Mock
    private Film filmOne;

    @Mock
    private Film filmTwo;

    @Mock
    private DownloadNextPageIntent downloadNextPageIntent;

    private FilmAdapterContract.Presenter presenter;

    private List<Film> dataSet;

    @Before
    public void setUp() throws Exception {
        initializeParams();

        DataUtils.setFilmOneValues(filmOne);
        DataUtils.setFilmTwoValues(filmTwo);

        PowerMockito.mockStatic(RxStore.class);
        MockUtils.setFinalStatic(RxStore.class, "SELECTED_FILM", new SerializedSubject<>(BehaviorSubject.create(SelectedFilm.EMPTY)));

        PowerMockito.mockStatic(RxDbObservables.class);

        dataSet = emptyDataSet ? Collections.emptyList() : DataUtils.createFakeList(100, filmOne, filmTwo);
        choosePresenter(filmListType);
        initializeTestAndReset();
    }

    private void choosePresenter(FilmListType type) {
        final Observable<List<Film>> dsObservable = Observable.just(dataSet);

        switch (type) {
            case RECENT_POPULAR_MOVIES:
                PowerMockito.when(RxDbObservables.createPopularFilmsObservable()).thenReturn(dsObservable);
                presenter = new PopularFilmsAdapterPresenter(downloadNextPageIntent);
                break;
            case CURRENT_YEAR_POPULAR_ANIMATED_MOVIES:
                PowerMockito.when(RxDbObservables.createAnimatedFilmsObservable()).thenReturn(dsObservable);
                presenter = new AnimatedFilmsAdapterPresenter(downloadNextPageIntent);
                break;
            case HIGHLY_RATED_SCIFI_MOVIES:
                PowerMockito.when(RxDbObservables.createScifiFilmsObservable()).thenReturn(dsObservable);
                presenter = new ScifiFilmsAdapterPresenter(downloadNextPageIntent);
                break;
        }
    }

    private void initializeTestAndReset() {
        PowerMockito.when(view.createAttachViewObservable()).thenReturn(Observable.empty());
        presenter.setView(view).initialize();

        verify(view).refreshDataSet(dataSet);

        Mockito.reset(view);
        Mockito.reset(filmOne);
        Mockito.reset(filmTwo);
    }

    @Test
    public void testOnFilmClicked() {
        presenter.onFilmClicked(filmOne.getId());

        RxStore.SELECTED_FILM.subscribe(film -> Assert.assertEquals(film.id, filmOne.getId()));
    }
}
