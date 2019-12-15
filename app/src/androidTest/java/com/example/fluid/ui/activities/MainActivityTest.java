package com.example.fluid.ui.activities;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.fluid.R;
import com.example.fluid.ui.home.AppointmentListAdapter;
import com.example.fluid.utils.App;
import com.example.fluid.utils.PreferenceController;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.Description;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {


    @Rule
    public ActivityScenarioRule activityRole = new ActivityScenarioRule(MainActivity.class);

    @Test
    public void isEmailDisplayed(){
        onView(withId(R.id.emailTxtView)).check(matches(withText(PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_EMAIL))));
    }

    @Test
    public void isUserNameDisplayed(){
        onView(withId(R.id.userNameTxt)).check(matches(withText(PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_USER_NAME))));

    }
    @Test
    public void isMovedToLocations(){
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_location));
        onView(withId(R.id.location_layout)).check(matches(isDisplayed()));
}
    @Test
   public void testIsClickedOnItem(){
        onView(ViewMatchers.withId(R.id.appointmentRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2,
                        click()));
    }
    @Test
    public void isCallTheItem(){
        onView(ViewMatchers.withId(R.id.appointmentRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2,
                        click()));
    }

    @Test
    public void testCalling(){
        onView(withId(R.id.call_fab)).perform(click());

    }

    @Test
    public void testStartAppointment(){
     // onView(withId(R.id.start_fab)).perform(click());
       onView(withId(R.id.call_fab)).check(matches(isEnabled()));
    }

    @Test
    public void itemInMiddleOfList_hasSpecialText() {
        // First, scroll to the view holder using the isInTheMiddle matcher.
        onView(ViewMatchers.withId(R.id.appointmentRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));

        // Check that the item has the special text.
        String middleElementText ="MRN1987";
        onView(withText(middleElementText)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.customer_state_img))).check(matches(hasDrawable()));

    }
    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() == getApplicationContext().getResources().getDrawable(R.drawable.ic_call);
            }
        };
    }



}