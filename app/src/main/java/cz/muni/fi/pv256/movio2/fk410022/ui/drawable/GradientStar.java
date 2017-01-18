package cz.muni.fi.pv256.movio2.fk410022.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * inspired by http://www.therealjoshua.com/2012/12/imageviews-and-stars/
 */
public class GradientStar extends Drawable {

    private static final int STROKE_WIDTH = 8;

    private static final int STAR_OPP_ANGLE = 72;
    private static final int STAR_ANGLE_HALF = 18;

    private Paint paint;
    private Paint linePaint;
    private Path path;

    private int startColor;
    private int endColor;

    private int paddingLeft = 7;
    private int paddingRight = 7;
    private int paddingTop = 7;
    private int paddingBottom = 7;

    public GradientStar(int startColor, int endColor, int lineStartColor, int lineEndColor) {
        super();
        this.startColor = startColor;
        this.endColor = endColor;

        path = new Path();
        initPaints(lineStartColor, lineEndColor);
    }

    private void initPaints(int lineStartColor, int lineEndColor) {
        paint = new Paint();
        paint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(STROKE_WIDTH);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeMiter(0);
        linePaint.setAntiAlias(true);
        linePaint.setShader(new LinearGradient(0, 0, 0, getBounds().height(),
                lineEndColor, lineStartColor, Shader.TileMode.CLAMP));
    }

    public void setPadding(int left, int top, int right, int bottom) {
        paddingLeft = left;
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        int bigB = Math.min(bounds.width() - paddingLeft - paddingRight,
                bounds.height() - paddingTop - paddingBottom);
        paint.setShader(new LinearGradient(0, 0, 0, getBounds().height(), endColor, startColor, Shader.TileMode.CLAMP));

        double bigHypot = (bigB / Math.cos(Math.toRadians(STAR_ANGLE_HALF)));
        double bigA = Math.tan(Math.toRadians(18)) * bigB;

        double littleHypot = bigHypot / (2 + Math.cos(Math.toRadians(STAR_OPP_ANGLE)) + Math.cos(Math.toRadians(STAR_OPP_ANGLE)));
        double littleA = Math.cos(Math.toRadians(STAR_OPP_ANGLE)) * littleHypot;
        double littleB = Math.sin(Math.toRadians(STAR_OPP_ANGLE)) * littleHypot;

        int topXPoint = (bounds.width() - paddingLeft - paddingRight) / 2 + paddingLeft;
        int topYPoint = paddingTop;

        path.moveTo(topXPoint, topYPoint);
        path.lineTo((float) (topXPoint + bigA), (float) (topYPoint + bigB));
        path.lineTo((float) (topXPoint - littleA - littleB), (float) (topYPoint + littleB));
        path.lineTo((float) (topXPoint + littleA + littleB), (float) (topYPoint + littleB));
        path.lineTo((float) (topXPoint - bigA), (float) (topYPoint + bigB));
        path.lineTo(topXPoint, topYPoint);

        path.close();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(path, linePaint);
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        linePaint.setAlpha(alpha);
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        linePaint.setColorFilter(cf);
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
