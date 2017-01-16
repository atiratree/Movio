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

    private transient Date lateReleaseDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLateReleaseDate() {
        return lateReleaseDate;
    }

    public void setLateReleaseDate(Date lateReleaseDate) {
        this.lateReleaseDate = lateReleaseDate;
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

        if (lateReleaseDate != null ? !lateReleaseDate.equals(toPersist.getLateReleaseDate()) : toPersist.getLateReleaseDate() != null) {
            toPersist.setLateReleaseDate(lateReleaseDate);
            return true;
        }

        // check and prepare genres
        List<Genre> allRestGenres = Stream.of(genre_ids).map(Genre::fromId).filter(v -> v != null).collect(Collectors.toList());
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

        return id != null ? id.equals(film.id) : film.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

