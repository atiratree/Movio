package cz.muni.fi.pv256.movio2.fk410022.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Film implements Parcelable {
    private Long id;
    private String title;
    private String description;
    private Date releaseDate;
    private String posterPathId;
    private String backdropPathId;
    private double popularity;
    private double rating;

    public Film() {
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
    }

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        public Film createFromParcel(Parcel pc) {
            return new Film(pc);
        }

        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

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

