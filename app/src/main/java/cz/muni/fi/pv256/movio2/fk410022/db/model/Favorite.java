package cz.muni.fi.pv256.movio2.fk410022.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

import cz.muni.fi.pv256.movio2.fk410022.db.provider.DbContract;

@Table(name = DbContract.Favorites.TABLE, id = DbContract.BaseEntity.ID)
public class Favorite extends Model {

    @Column(name = DbContract.Favorites.FILM)
    private Film film;

    @Column(name = DbContract.Favorites.FAVORITE)
    private boolean favorite;

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        if (!super.equals(o)) return false;

        Favorite favorite1 = (Favorite) o;

        if (favorite != favorite1.favorite) return false;
        return film != null ? film.equals(favorite1.film) : favorite1.film == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (film != null ? film.hashCode() : 0);
        result = 31 * result + (favorite ? 1 : 0);
        return result;
    }
}

