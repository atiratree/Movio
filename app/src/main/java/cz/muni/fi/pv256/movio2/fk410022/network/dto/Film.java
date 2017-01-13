package cz.muni.fi.pv256.movio2.fk410022.network.dto;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
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
    private long vote_count;
    private Integer[] genre_ids;

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

    public long getVote_count() {
        return vote_count;
    }

    public void setVote_count(long vote_count) {
        this.vote_count = vote_count;
    }

    public Integer[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(Integer[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public boolean updateValuesOfDbFilm(cz.muni.fi.pv256.movio2.fk410022.db.model.Film toPersist) {
        if (toPersist == null) {
            throw new IllegalArgumentException("Film to persist cannot be null");
        }

        boolean changed = false;

        if (id != null && !id.equals(toPersist.getMovieDbId())) {
            toPersist.setMovieDbId(id);
            changed = true;
        }

        if (original_title != null ? !original_title.equals(toPersist.getTitle()) : toPersist.getTitle() != null) {
            toPersist.setTitle(original_title);
            changed = true;
        }

        if (overview != null ? !overview.equals(toPersist.getDescription()) : toPersist.getDescription() != null) {
            toPersist.setDescription(overview);
            changed = true;
        }

        Date releaseDate = DateUtils.convertToDate(release_date);
        if (releaseDate != null ? !releaseDate.equals(toPersist.getReleaseDate()) : toPersist.getReleaseDate() != null) {
            toPersist.setReleaseDate(releaseDate);
            changed = true;
        }

        if (poster_path != null ? !poster_path.equals(toPersist.getPosterPathId()) : toPersist.getPosterPathId() != null) {
            toPersist.setPosterPathId(poster_path);
            changed = true;
        }

        if (backdrop_path != null ? !backdrop_path.equals(toPersist.getBackdropPathId()) : toPersist.getBackdropPathId() != null) {
            toPersist.setBackdropPathId(backdrop_path);
            changed = true;
        }

        if (popularity != toPersist.getPopularity()) {
            toPersist.setPopularity(popularity);
            changed = true;
        }

        if (vote_average != toPersist.getRating()) {
            toPersist.setRating(vote_average);
            changed = true;
        }

        if (vote_count != toPersist.getRatingVoteCount()) {
            toPersist.setRatingVoteCount(vote_count);
            changed = true;
        }

        // check and prepare genres
        List<Genre> allRestGenres = Stream.of(genre_ids).map(Genre::fromId).collect(Collectors.toList());
        EnumSet<Genre> allGenres = allRestGenres.isEmpty() ? EnumSet.noneOf(Genre.class) : EnumSet.copyOf(allRestGenres);
        EnumSet<Genre> toPersistGenres = allGenres.clone();
        Set<FilmGenre> toRemoveGenres = new HashSet<>();

        Stream.of(toPersist.getGenres()).forEach(persistedFilmGenre -> {
            Genre persistedGenre = persistedFilmGenre.getGenre();

            if (allGenres.contains(persistedGenre)) { // already persisted
                toPersistGenres.remove(persistedGenre);
            } else {
                toRemoveGenres.add(persistedFilmGenre);
            }
        });

        if (toPersistGenres.size() > 0) {
            changed = true;
            toPersist.setGenresToPersist(toPersistGenres);
        }

        if (toRemoveGenres.size() > 0) {
            changed = true;
            toPersist.setGenresToRemove(toRemoveGenres);
        }

        return changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;

        Film film = (Film) o;

        if (Double.compare(film.popularity, popularity) != 0) return false;
        if (Double.compare(film.vote_average, vote_average) != 0) return false;
        if (id != null ? !id.equals(film.id) : film.id != null) return false;
        if (original_title != null ? !original_title.equals(film.original_title) : film.original_title != null)
            return false;
        if (overview != null ? !overview.equals(film.overview) : film.overview != null)
            return false;
        if (release_date != null ? !release_date.equals(film.release_date) : film.release_date != null)
            return false;
        if (backdrop_path != null ? !backdrop_path.equals(film.backdrop_path) : film.backdrop_path != null)
            return false;
        return poster_path != null ? poster_path.equals(film.poster_path) : film.poster_path == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (original_title != null ? original_title.hashCode() : 0);
        result = 31 * result + (overview != null ? overview.hashCode() : 0);
        result = 31 * result + (release_date != null ? release_date.hashCode() : 0);
        result = 31 * result + (backdrop_path != null ? backdrop_path.hashCode() : 0);
        result = 31 * result + (poster_path != null ? poster_path.hashCode() : 0);
        temp = Double.doubleToLongBits(popularity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vote_average);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

