package cz.muni.fi.pv256.movio2.fk410022;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by suomiy on 10/18/16.
 */

public class Utils {

    public static int addAlphaToColor(int color, float opacity) {
        if (opacity > 1 || opacity < 0) {
            throw new IllegalArgumentException("opacity should be in interval 0-1");
        }

        return ((int) (opacity * 255.0f) << 24) | (color & 0x00ffffff);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
