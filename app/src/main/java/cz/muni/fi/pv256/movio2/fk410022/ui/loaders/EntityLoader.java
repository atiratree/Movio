package cz.muni.fi.pv256.movio2.fk410022.ui.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.activeandroid.Model;
import com.activeandroid.loaders.ModelLoader;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.annimon.stream.Stream;

import java.util.List;

public abstract class EntityLoader<T extends Model> implements LoaderManager.LoaderCallbacks<List<T>> {

    private EntityListener<T> listener;

    private Context context;

    private Class<T> clazz;

    public EntityLoader(Class<T> clazz, EntityListener<T> listener, Context context) {
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
        from.limit(1);

        return new ModelLoader<>(getContext(), clazz, from, true);
    }

    protected abstract void buildOnQuery(Bundle args, From from);

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        listener.onLoadFinished(Stream.of(data).findFirst().orElse(null));
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
    }

    public interface EntityListener<T extends Model> {
        void onLoadFinished(T film);
    }
}
