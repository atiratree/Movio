package cz.muni.fi.pv256.movio2.fk410022.rx.message;

public class SelectedFilm {

    public static final SelectedFilm EMPTY = new SelectedFilm();

    public final Long id;

    public SelectedFilm() {
        id = null;
    }

    public SelectedFilm(Long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return id != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectedFilm)) return false;

        SelectedFilm that = (SelectedFilm) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
