package cz.muni.fi.pv256.movio2.fk410022.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * http://stackoverflow.com/questions/5763304/disable-scrollview-programmatically
 */
public class LockableScrollView extends ScrollView {

    private boolean mScrollable = true;

    public LockableScrollView(Context context) {
        super(context);
    }

    public LockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollable(boolean scrollable) {
        mScrollable = scrollable;
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return mScrollable && super.onTouchEvent(ev);
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mScrollable && super.onInterceptTouchEvent(ev);
    }
}
