package cz.muni.fi.pv256.movio2.fk410022;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cz.muni.fi.pv256.movio2.fk410022.util.image.SizeHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            DebugClass.initialize(getApplicationContext());
        }

        SizeHelper.init(getApplicationContext());

        // global configuration and initialization of ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .memoryCacheSizePercentage(35)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
