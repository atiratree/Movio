package cz.muni.fi.pv256.movio2.fk410022.rx.message;

public class DetailFragmentState {
    public final Boolean visible;
    public final Boolean filmSelected;

    public DetailFragmentState(Boolean showDiscover, SelectedFilm selectedFilm) {
        this.visible = showDiscover;
        this.filmSelected = selectedFilm.isSelected();
    }

    public boolean needsUpdate() {
        return visible != filmSelected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetailFragmentState)) return false;

        DetailFragmentState that = (DetailFragmentState) o;

        if (visible != null ? !visible.equals(that.visible) : that.visible != null)
            return false;
        return filmSelected != null ? filmSelected.equals(that.filmSelected) : that.filmSelected == null;
    }

    @Override
    public int hashCode() {
        int result = visible != null ? visible.hashCode() : 0;
        result = 31 * result + (filmSelected != null ? filmSelected.hashCode() : 0);
        return result;
    }
}
