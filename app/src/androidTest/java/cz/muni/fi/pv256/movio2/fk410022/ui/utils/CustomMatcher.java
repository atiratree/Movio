package cz.muni.fi.pv256.movio2.fk410022.ui.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatcher {
    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View item) {
                ViewParent parent = item.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && item.equals(((ViewGroup) parent).getChildAt(position));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<View> hasDrawable() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return item instanceof ImageView && ((ImageView) item).getDrawable() != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Is ImageView and has drawable");
            }
        };
    }

    public static Matcher<View> withBackgroundColor(final int color) {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View view) {
                if (view.getBackground() instanceof ColorDrawable) {
                    final int c = view.getResources().getColor(color);
                    return c == ((ColorDrawable) view.getBackground()).getColor();
                }

                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: " + color);
            }
        };
    }

    public static Matcher<View> withBackgroundTintColor(final int color) {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View item) {
                final int c = item.getResources().getColor(color);
                return item instanceof FloatingActionButton && ((FloatingActionButton) item).getBackgroundTintList().getDefaultColor() == c;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with background tint color: " + color);
            }
        };
    }

    public static Matcher<View> withDrawableResource(final int drawableId) {
        return new BoundedMatcher<View, View>(View.class) {
            @Override
            public boolean matchesSafely(View item) {
                final Drawable.ConstantState state = item.getResources().getDrawable(drawableId).getConstantState();
                return item instanceof FloatingActionButton && ((FloatingActionButton) item).getDrawable().getConstantState() == state;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with drawable resource: " + drawableId);
            }
        };
    }
}
