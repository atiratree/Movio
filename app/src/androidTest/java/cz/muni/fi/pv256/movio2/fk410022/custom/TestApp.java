package cz.muni.fi.pv256.movio2.fk410022.custom;

import android.app.Application;

import cz.muni.fi.pv256.movio2.fk410022.BuildConfig;
import cz.muni.fi.pv256.movio2.fk410022.DebugClass;

public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            DebugClass.initialize(this);
        }
    }
}
