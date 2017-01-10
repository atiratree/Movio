package cz.muni.fi.pv256.movio2.fk410022.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.Date;

import cz.muni.fi.pv256.movio2.fk410022.db.provider.CursorHelper;

import static cz.muni.fi.pv256.movio2.fk410022.db.provider.DbContract.Film.*;

public class Film implements Parcelable {
    private Long id;
    private String title;
    private String description;
    private Date releaseDate;
    private String posterPathId;
    private String backdropPathId;
    private double popularity;
    private double rating;
    private boolean favorite;

    public Film() {
    }

    public Film (Cursor cursor){
        CursorHelper cursorHelper = new CursorHelper(cursor);

        id = cursorHelper.getLong(ID);
        title = cursorHelper.getString(TITLE);
        description = cursorHelper.getString(DESCRIPTION);
        releaseDate = cursorHelper.getTimestamp(RELEASE_DATE);
        posterPathId = cursorHelper.getString(POSTER_PATH_ID);
        backdropPathId = cursorHelper.getString(BACKDROP_PATH_ID);
        popularity = cursorHelper.getDouble(POPULARITY);
        rating = cursorHelper.getDouble(RATING);
        favorite = cursorHelper.getBoolean(FAVORITE);
    }

    public Film(Parcel pc) {
        id = pc.readLong();
        title = pc.readString();
        description = pc.readString();
        releaseDate = new Date(pc.readLong());
        posterPathId = pc.readString();
        backdropPathId = pc.readString();
        popularity = pc.readDouble();
        rating = pc.readDouble();
        favorite = pc.readInt() != 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public static Creator<Film> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeLong(releaseDate.getTime());
        parcel.writeString(posterPathId);
        parcel.writeString(backdropPathId);
        parcel.writeDouble(popularity);
        parcel.writeDouble(rating);
        parcel.writeInt(favorite ? 1 : 0);
    }

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        public Film createFromParcel(Parcel pc) {
            return new Film(pc);
        }

        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    public ContentValues toValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, getId());
        contentValues.put(TITLE, getTitle());
        contentValues.put(DESCRIPTION, getTitle());
        contentValues.put(RELEASE_DATE, new Timestamp(getReleaseDate().getTime()).toString());
        contentValues.put(POSTER_PATH_ID, getPosterPathId());
        contentValues.put(BACKDROP_PATH_ID, getBackdropPathId());
        contentValues.put(POPULARITY, getPopularity());
        contentValues.put(RATING, getRating());
        contentValues.put(FAVORITE, isFavorite());

        return contentValues;
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

