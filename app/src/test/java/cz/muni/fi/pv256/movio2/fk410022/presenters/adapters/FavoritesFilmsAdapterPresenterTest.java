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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cz.muni.fi.pv256.movio2.fk410022.App;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.FilmAdapterContract;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.fixed.FavoriteFilmsAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.fixed.FixedFilmAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.utils.DataUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.MockUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.ParamUtils;
import de.schauderhaft.rules.parameterized.Generator;
import de.schauderhaft.rules.parameterized.ListGenerator;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {App.class, RxStore.class, RxDbObservables.class, Film.class})
public class FavoritesFilmsAdapterPresenterTest {

    @Rule
    public TestName name = new TestName();

    private Boolean isTablet;
    private Boolean emptyDataSet;

    @Rule
    @SuppressWarnings("unchecked")
    public Generator<Boolean[]> parameters = new ListGenerator(ParamUtils.getTruthTable(2));

    private void initializeParams() {
        final Boolean[] params = parameters.value();
        isTablet = params[0];
        emptyDataSet = params[1];
        System.out.println(String.format(Locale.ENGLISH, "%s: run %d  isTablet=%b emptyDataSet=%b",
                name.getMethodName(), parameters.runIndex(), isTablet, emptyDataSet));
    }

    @Mock
    private FilmAdapterContract.View view;

    @Mock
    private Film filmOne;

    @Mock
    private Film filmTwo;

    private FixedFilmAdapterPresenter presenter;

    private List<Film> dataSet;

    @Before
    public void setUp() throws Exception {
        initializeParams();

        DataUtils.setFilmOneValues(filmOne);
        DataUtils.setFilmTwoValues(filmTwo);

        PowerMockito.mockStatic(RxStore.class);
        MockUtils.setFinalStatic(RxStore.class, "SELECTED_FILM", new SerializedSubject<>(BehaviorSubject.create(SelectedFilm.EMPTY)));

        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.isTablet()).thenReturn(isTablet);

        PowerMockito.mockStatic(RxDbObservables.class);

        dataSet = emptyDataSet ? Collections.emptyList() : DataUtils.createFakeList(100, filmOne, filmTwo);
        PowerMockito.when(RxDbObservables.createFavoriteFilmsObservable()).thenReturn(rx.Observable.just(dataSet));

        initializeTestAndReset();
    }

    private void initializeTestAndReset() {
        presenter = new FavoriteFilmsAdapterPresenter().setView(view).initialize();

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
