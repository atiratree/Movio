package cz.muni.fi.pv256.movio2.fk410022.rx.message;

public class MenuState {
    public final Boolean showDiscover;
    public final Boolean filmSelected;

    public MenuState(Boolean showDiscover, SelectedFilm selectedFilm) {
        this.showDiscover = showDiscover;
        this.filmSelected = selectedFilm.isSelected();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuState)) return false;

        MenuState that = (MenuState) o;

        if (showDiscover != null ? !showDiscover.equals(that.showDiscover) : that.showDiscover != null)
            return false;
        return filmSelected != null ? filmSelected.equals(that.filmSelected) : that.filmSelected == null;
    }

    @Override
    public int hashCode() {
        int result = showDiscover != null ? showDiscover.hashCode() : 0;
        result = 31 * result + (filmSelected != null ? filmSelected.hashCode() : 0);
        return result;
    }
}
