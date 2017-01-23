package cz.muni.fi.pv256.movio2.fk410022;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.leakcanary.LeakCanary;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class DebugClass {

    private static final boolean DEBUG_STETHO = true;
    private static final boolean DEBUG_HTTP_CLIENT = false;

    private static final boolean LEAK_CANARY = false;
    private static final boolean STRICT_MODE = false;

    public static void initialize(Application application) {
        initStrictMode();
        initStetho(application.getApplicationContext());
        initLeakCanary(application);
    }

    public static void buildDebugClient(Retrofit.Builder retrofitBuilder) {
        if (DEBUG_STETHO && DEBUG_HTTP_CLIENT) {
            final OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();
            retrofitBuilder.client(client);
        }
    }

    private static void initStetho(Context context) {
        if (DEBUG_STETHO) {
            Stetho.initializeWithDefaults(context);
        }
    }

    private static void initLeakCanary(Application application) {
        if (LEAK_CANARY && !LeakCanary.isInAnalyzerProcess(application)) {
            LeakCanary.install(application);
        }
    }

    private static void initStrictMode() {
        if (STRICT_MODE) {
            StrictMode.ThreadPolicy.Builder tpb = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                tpb.penaltyFlashScreen();
            }
            StrictMode.setThreadPolicy(tpb.build());

            StrictMode.VmPolicy.Builder vmpb = new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .penaltyLog();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                vmpb.detectLeakedClosableObjects();
            }
            StrictMode.setVmPolicy(vmpb.build());
        }
    }
}

