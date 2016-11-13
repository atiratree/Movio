package cz.muni.fi.pv256.movio2.fk410022.util.image;

import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import cz.muni.fi.pv256.movio2.fk410022.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;

public class ImageHelper {
    public static void displayPoster(Film film, ImageView view) {
        displayImage(film.getPosterPathId(), SizeHelper.getPosterSize(), view);
    }

    public static void displayPoster(Film film, ImageAware imageAware) {
        displayImage(film.getPosterPathId(), SizeHelper.getPosterSize(), imageAware);
    }

    public static void displayBackdrop(Film film, ImageView view) {
        displayImage(film.getBackdropPathId(), SizeHelper.getBackdropSize(), view);
    }

    public static void loadPoster(Film film) {
        loadImage(film.getPosterPathId(), SizeHelper.getPosterSize());
    }

    public static void loadBackdrop(Film film) {
        loadImage(film.getBackdropPathId(), SizeHelper.getBackdropSize());
    }

    public static String makePosterPath(String idPart) {
        return makeFinalPath(idPart, SizeHelper.getPosterSize());
    }

    private static String makeFinalPath(String idPart, Size size) {
        return String.format("%s%s%s", Constants.IMAGE_BASE_URL, size.getUrlPart(), idPart);
    }

    private static void loadImage(String idPart, Size size) {
        if (!TextUtils.isEmpty(idPart) && size != null) {
            ImageLoader.getInstance().loadImage(makeFinalPath(idPart, size), null);
        }
    }

    private static void displayImage(String idPart, Size size, ImageView view) {
        if (!TextUtils.isEmpty(idPart) && size != null && view != null) {
            ImageLoader.getInstance().displayImage(makeFinalPath(idPart, size), view);
        }
    }

    private static void displayImage(String idPart, Size size, ImageAware imageAware) {
        if (!TextUtils.isEmpty(idPart) && size != null && imageAware != null) {
            ImageLoader.getInstance().displayImage(makeFinalPath(idPart, size), imageAware);
        }
    }
}
