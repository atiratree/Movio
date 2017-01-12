package cz.muni.fi.pv256.movio2.fk410022.db.model;

import android.support.annotation.NonNull;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.DbContract;
import cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre;

@Table(name = DbContract.Film.TABLE, id = DbContract.BaseEntity.ID)
public class Film extends Model {

    @Column(name = DbContract.Film.MOVIE_DB_ID)
    private Long movieDbId;

    @Column(name = DbContract.Film.TITLE)
    private String title;

    @Column(name = DbContract.Film.DESCRIPTION)
    private String description;

    @Column(name = DbContract.Film.RELEASE_DATE)
    private Date releaseDate;

    @Column(name = DbContract.Film.LATE_RELEASE_DATE)
    private Date lateReleaseDate;

    @Column(name = DbContract.Film.POSTER_PATH_ID)
    private String posterPathId;

    @Column(name = DbContract.Film.BACKDROP_PATH_ID)
    private String backdropPathId;

    @Column(name = DbContract.Film.POPULARITY)
    private double popularity;

    @Column(name = DbContract.Film.RATING)
    private double rating;

    @Column(name = DbContract.Film.RATING_VOTE_COUNT)
    private long ratingVoteCount;

    private transient Collection<Genre> genresToPersist;

    public Film() {
        super();
    }

    @NonNull
    public Long getMovieDbId() {
        return movieDbId == null ? -1 : movieDbId;
    }

    public void setMovieDbId(Long movieDbId) {
        this.movieDbId = movieDbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getLateReleaseDate() {
        return lateReleaseDate;
    }

    public void setLateReleaseDate(Date lateReleaseDate) {
        this.lateReleaseDate = lateReleaseDate;
    }

    public String getPosterPathId() {
        return posterPathId;
    }

    public void setPosterPathId(String posterPathId) {
        this.posterPathId = posterPathId;
    }

    public String getBackdropPathId() {
        return backdropPathId;
    }

    public void setBackdropPathId(String backdropPathId) {
        this.backdropPathId = backdropPathId;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getRatingVoteCount() {
        return ratingVoteCount;
    }

    public void setRatingVoteCount(long ratingVoteCount) {
        this.ratingVoteCount = ratingVoteCount;
    }

    public Collection<Genre> getGenresToPersist() {
        return genresToPersist == null ? Collections.emptyList() : genresToPersist;
    }

    public void setGenresToPersist(Collection<Genre> genresToPersist) {
        this.genresToPersist = genresToPersist;
    }

    public List<FilmGenre> getGenres() {
        return getId() == null ? Collections.emptyList() : getMany(FilmGenre.class, DbContract.FilmGenre.FILM);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        if (!super.equals(o)) return false;

        Film film = (Film) o;

        if (Double.compare(film.popularity, popularity) != 0) return false;
        if (Double.compare(film.rating, rating) != 0) return false;
        if (ratingVoteCount != film.ratingVoteCount) return false;
        if (movieDbId != null ? !movieDbId.equals(film.movieDbId) : film.movieDbId != null)
            return false;
        if (title != null ? !title.equals(film.title) : film.title != null) return false;
        if (description != null ? !description.equals(film.description) : film.description != null)
            return false;
        if (releaseDate != null ? !releaseDate.equals(film.releaseDate) : film.releaseDate != null)
            return false;
        if (lateReleaseDate != null ? !lateReleaseDate.equals(film.lateReleaseDate) : film.lateReleaseDate != null)
            return false;
        if (posterPathId != null ? !posterPathId.equals(film.posterPathId) : film.posterPathId != null)
            return false;
        return backdropPathId != null ? backdropPathId.equals(film.backdropPathId) : film.backdropPathId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (movieDbId != null ? movieDbId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (lateReleaseDate != null ? lateReleaseDate.hashCode() : 0);
        result = 31 * result + (posterPathId != null ? posterPathId.hashCode() : 0);
        result = 31 * result + (backdropPathId != null ? backdropPathId.hashCode() : 0);
        temp = Double.doubleToLongBits(popularity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (ratingVoteCount ^ (ratingVoteCount >>> 32));
        return result;
    }
}

