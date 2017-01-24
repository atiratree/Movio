package cz.muni.fi.pv256.movio2.fk410022.ui.utils;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.test.espresso.Espresso;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class EspressoUtils {

    public static void clickOnMenuItem(@IdRes int id, @StringRes int textId) {
        try {
            onView(withId(id)).perform(click());
        } catch (Exception x) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(textId)).perform(click());
        }
    }

    public static void menuItemDisplayed(@IdRes int id, @StringRes int textId) {
        try {
            onView(withId(id)).check(matches(isDisplayed()));
            onView(withId(id)).check(matches(withText(textId)));
        } catch (Exception x) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(textId)).check(matches(isDisplayed()));
        }
    }

    public static void menuItemNotDisplayed(@IdRes int id, @StringRes int textId) {
        boolean hiddenMenuExists = false;
        try {
            onView(withContentDescription("More options")).check(matches(isDisplayed()));
            hiddenMenuExists = true;
        } catch (Throwable t) {
        }

        onView(withId(id)).check(doesNotExist());
        if (hiddenMenuExists) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(textId)).check(doesNotExist());
            Espresso.pressBack();
        }
    }
}
