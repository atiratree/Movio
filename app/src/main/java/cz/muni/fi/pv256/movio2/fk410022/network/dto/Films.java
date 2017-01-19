package cz.muni.fi.pv256.movio2.fk410022.network.dto;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.network.exception.ParseDtoException;

public class Films {
    private int page;

    private int total_pages;

    private Film[] results;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public Film[] getResults() {
        return results;
    }

}
