package cz.muni.fi.pv256.movio2.fk410022.util.image;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import cz.muni.fi.pv256.movio2.fk410022.R;

public class SizeHelper {
    private static Size backdropSize = BackdropSize.ORIGINAL; // default sizes
    private static Size posterSize = PosterSize.ORIGINAL;

    public static void init(Context context) {
        backdropSize = getMinimumSize(BackdropSize.values(), getDisplayMaxDimension(context));
        posterSize = getMinimumSize(PosterSize.values(), getMaxPosterWidth(context));
    }

    static Size getBackdropSize() {
        return backdropSize;
    }

    static Size getPosterSize() {
        return posterSize;
    }

    /**
     * Finds minimum size from sizes
     *
     * @param sizes       input data to be searched
     * @param minimumSize minimum size
     * @return Size at least of size, if such Size does not exist maximum size is returned instead
     */
    private static Size getMinimumSize(Size[] sizes, int minimumSize) {
        if (sizes == null) {
            throw new IllegalArgumentException("sizes are null");
        }

        SortedSet<Size> sortedSizes = new TreeSet<>(getSizeComparator());
        sortedSizes.addAll(Arrays.asList(sizes));
        Size min = null;
        for (Size size : sortedSizes) {
            min = size;
            if (min.getValue() >= minimumSize) {
                break;
            }
        }

        return min;
    }

    private static Comparator<Size> getSizeComparator() {
        return (o1, o2) -> o1.getValue() - o2.getValue();
    }

    private static int getDisplayMaxDimension(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return Math.max(size.x, size.y);
    }

    private static int getMaxPosterWidth(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.poster_detail_width);
    }
}
