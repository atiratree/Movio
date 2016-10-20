package cz.muni.fi.pv256.movio2.fk410022.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by suomiy on 10/4/16.
 */

public class Film implements Parcelable {
    private String title;
    private String description;
    private long releaseDate;
    private double popularity;
    private int coverPath;
    private int backdrop;

    public Film(Parcel pc) {
        title = pc.readString();
        description = pc.readString();
        releaseDate = pc.readLong();
        popularity = pc.readDouble();
        coverPath = pc.readInt();
        backdrop = pc.readInt();
    }

    public Film(String title, String description, long releaseDate, double popularity, int coverPath, int backdrop) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.coverPath = coverPath;
        this.backdrop = backdrop;
    }

    @Override
    public int describeContents() {
        return 0;
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

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(int coverPath) {
        this.coverPath = coverPath;
    }

    public int getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(int backdrop) {
        this.backdrop = backdrop;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeLong(releaseDate);
        parcel.writeDouble(popularity);
        parcel.writeInt(coverPath);
        parcel.writeInt(backdrop);
    }

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        public Film createFromParcel(Parcel pc) {
            return new Film(pc);
        }

        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
}

