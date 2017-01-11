package cz.muni.fi.pv256.movio2.fk410022.ui.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.activeandroid.Model;
import com.activeandroid.loaders.ModelLoader;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

public abstract class EntitiesLoader<T extends Model> implements LoaderManager.LoaderCallbacks<List<T>> {

    private EntitiesListener<T> listener;

    private Context context;

    private Class<T> clazz;

    public EntitiesLoader(Class<T> clazz, EntitiesListener<T> listener, Context context) {
        this.clazz = clazz;
        this.listener = listener;
        this.context = context.getApplicationContext();
    }

    protected Context getContext() {
        return context;
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        From from = new Select().from(clazz);
        buildOnQuery(args, from);

        return new ModelLoader<>(getContext(), clazz, from, true);
    }

    protected abstract void buildOnQuery(Bundle args, From from);

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        listener.onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
    }

    public interface EntitiesListener<T extends Model> {
        void onLoadFinished(List<T> entities);
    }
}
