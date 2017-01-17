package cz.muni.fi.pv256.movio2.fk410022.network.dto;

import java.util.ArrayList;
import java.util.List;

public class Films {
    private int page;
    private Film[] results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Film[] getResults() {
        return results;
    }

    public void setResults(Film[] results) {
        this.results = results;
    }

    public int getResultsCount() {
        return results == null ? 0 : results.length;
    }

    public List<cz.muni.fi.pv256.movio2.fk410022.model.Film> toEntityList() {
        List<cz.muni.fi.pv256.movio2.fk410022.model.Film> films = new ArrayList<>();

        if (results != null) {
            for (Film film : results) {
                films.add(film.toEntity());
            }
        }

        return films;
    }
}

