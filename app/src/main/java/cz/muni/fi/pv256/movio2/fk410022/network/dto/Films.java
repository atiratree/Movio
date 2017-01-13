package cz.muni.fi.pv256.movio2.fk410022.network.dto;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.network.exception.ParseDtoException;

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

}
