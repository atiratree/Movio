package cz.muni.fi.pv256.movio2.fk410022.custom;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cz.muni.fi.pv256.movio2.fk410022.BuildConfig;
import cz.muni.fi.pv256.movio2.fk410022.DebugClass;
import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.ui.utils.UiUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.image.SizeHelper;
import timber.log.Timber;

public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UiUtils.setIsTablet(getResources().getBoolean(R.bool.isTablet));

        Timber.plant(new Timber.DebugTree());

        if (BuildConfig.DEBUG) {
            DebugClass.initialize(this);
        }

        SizeHelper.init(getApplicationContext());

        // global configuration and initialization of ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
