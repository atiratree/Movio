package cz.muni.fi.pv256.movio2.fk410022.network.dto;

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
