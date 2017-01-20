package cz.muni.fi.pv256.movio2.fk410022;

import com.activeandroid.app.Application;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cz.muni.fi.pv256.movio2.fk410022.sync.SyncAdapter;
import cz.muni.fi.pv256.movio2.fk410022.util.image.SizeHelper;

public class App extends Application {

    private static boolean isTablet;

    public static boolean isTablet() {
        return isTablet;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SyncAdapter.initializeSyncAdapter(this);

        isTablet = getResources().getBoolean(R.bool.isTablet);

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
