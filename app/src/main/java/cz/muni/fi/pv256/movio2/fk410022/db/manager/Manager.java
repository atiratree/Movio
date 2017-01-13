package cz.muni.fi.pv256.movio2.fk410022.db.manager;

import com.activeandroid.Model;

public abstract class Manager<T extends Model> {

    public void delete(T entity) {
        entity.delete();
        notifyDependentTables();
    }

    public Long save(T entity) {
        Long result = entity.save();
        notifyDependentTables();
        return result;
    }

    public abstract void notifyDependentTables();
}
