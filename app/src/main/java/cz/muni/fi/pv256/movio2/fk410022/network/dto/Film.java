package cz.muni.fi.pv256.movio2.fk410022.network.dto;

import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class Film {
    private Long id;
    private String original_title;
    private String overview;
    private String release_date;
    private String backdrop_path;
    private String poster_path;
    private double popularity;
    private double vote_average;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public cz.muni.fi.pv256.movio2.fk410022.db.model.Film toEntity() {
        cz.muni.fi.pv256.movio2.fk410022.db.model.Film film = new cz.muni.fi.pv256.movio2.fk410022.db.model.Film();
        film.setId(id);
        film.setTitle(original_title);
        film.setDescription(overview);
        film.setReleaseDate(DateUtils.convertToDate(release_date));
        film.setPosterPathId(poster_path);
        film.setBackdropPathId(backdrop_path);
        film.setPopularity(popularity);
        film.setRating(vote_average);
        return film;
    }
}

