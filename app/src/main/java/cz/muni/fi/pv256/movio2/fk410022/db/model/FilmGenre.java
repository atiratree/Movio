package cz.muni.fi.pv256.movio2.fk410022.db.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import cz.muni.fi.pv256.movio2.fk410022.db.DbContract;

@Table(name = DbContract.FilmGenre.TABLE, id = DbContract.BaseEntity.ID)
public class FilmGenre extends Model {

    @Column(name = DbContract.FilmGenre.FILM)
    private Film film;

    @Column(name = DbContract.FilmGenre.GENRE)
    private cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre genre;

    public FilmGenre() {
        super();
    }

    public FilmGenre(Film film, cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre genre) {
        super();
        this.film = film;
        this.genre = genre;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre getGenre() {
        return genre;
    }

    public void setGenre(cz.muni.fi.pv256.movio2.fk410022.db.enums.Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilmGenre)) return false;
        if (!super.equals(o)) return false;

        FilmGenre filmType = (FilmGenre) o;

        if (film != null ? !film.equals(filmType.film) : filmType.film != null) return false;
        return genre == filmType.genre;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (film != null ? film.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        return result;
    }
}

