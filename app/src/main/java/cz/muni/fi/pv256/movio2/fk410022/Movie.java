package cz.muni.fi.pv256.movio2.fk410022;

/**
 * Created by suomiy on 10/4/16.
 */

public class Movie {

    private String name;

    private int pictureId;

    public Movie(String name, int pictureId) {
        this.name = name;
        this.pictureId = pictureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }
}
