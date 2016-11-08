package cz.muni.fi.pv256.movio2.fk410022.model.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import cz.muni.fi.pv256.movio2.fk410022.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;

public enum FilmListStore {
    INSTANCE;
    private final Map<FilmListType, List<Film>> mappings = new EnumMap<>(FilmListType.class);

    public void putAll(FilmListType type, Collection<Film> films) {
        Utils.checkNotNull(type);
        if (films != null) {
            mappings.put(type, new ArrayList<>(films));
        }
    }

    public List<Film> getAll(FilmListType type) {
        return mappings.get(type) == null ? Collections.emptyList() : Collections.unmodifiableList(mappings.get(type));
    }

    public boolean isInitialized(FilmListType type) {
        return mappings.get(type) != null;
    }

    public boolean isEmpty(FilmListType type) {
        List<Film> films = mappings.get(type);
        return films == null || films.size() == 0;
    }
}
