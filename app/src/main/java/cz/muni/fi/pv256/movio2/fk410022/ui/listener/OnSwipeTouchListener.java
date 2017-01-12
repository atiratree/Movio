package cz.muni.fi.pv256.movio2.fk410022.ui.listener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * http://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
 */
public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;

    private final OnSwipeListener onSwipeListener;

    public OnSwipeTouchListener(Context ctx) {
        this(ctx, null);
    }

    public OnSwipeTouchListener(Context ctx, OnSwipeListener onSwipeListener) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        this.onSwipeListener = onSwipeListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    private void onSwipeRight() {
        if(onSwipeListener != null) {
            onSwipeListener.onSwipeRight();
        }
    }

    private void onSwipeLeft() {
        if(onSwipeListener != null) {
            onSwipeListener.onSwipeLeft();
        }
    }

    private void onSwipeTop() {
        if(onSwipeListener != null) {
            onSwipeListener.onSwipeTop();
        }
    }

    private void onSwipeBottom() {
        if(onSwipeListener != null) {
            onSwipeListener.onSwipeBottom();
        }
    }
}
