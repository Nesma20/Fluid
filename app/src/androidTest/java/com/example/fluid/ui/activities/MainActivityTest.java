package com.example.fluid.ui.activities;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewFinder;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.bumptech.glide.request.ResourceCallback;
import com.example.fluid.R;
import com.example.fluid.ui.activities.main.MainActivity;
import com.example.fluid.ui.activities.main.MainViewModel;
import com.example.fluid.utils.App;
import com.example.fluid.utils.PreferenceController;

import org.hamcrest.Description;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

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

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {


    @Rule
    public ActivityScenarioRule activityRole = new ActivityScenarioRule(MainActivity.class);

    @Test
    public void isEmailDisplayed() {
        onView(withId(R.id.emailTxtView)).check(matches(withText(PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_EMAIL))));
    }

    @Test
    public void isUserNameDisplayed() {
        onView(withId(R.id.userNameTxt)).check(matches(withText(PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_USER_NAME))));

    }

    @Test
    public void isMovedToLocations() {
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
    public void testCallingBtn() {
        onView(withId(R.id.call_fab)).perform(click());

    }

    @Test
    public void chooseFirstPatientToCheckIn() {
        testStartAppointment();
        onView(withId(R.id.arrive_or_checkin_list_dialog)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.alert_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));

    }

    @Test
    public void arrivePatient() {
        for (int i = 0; i < 5; i++) {
            clickOnArriveBtn();
            waitViewShown(withId(R.id.arrive_or_checkin_list_dialog));
            // wait until data is displayed
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(ViewMatchers.withId(R.id.alert_recyclerview)).check(matches(isDisplayed()));
            onView(ViewMatchers.withId(R.id.alert_recyclerview))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                            click()));
        }
    }

    @Test
    public void clickOnArriveBtn() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.confirm_arrive_fab)).perform(click());

    }


    @Test
    public void testStartAppointment() {
        onView(withId(R.id.start_fab)).perform(click());
    }

    @Test
    public void itemInMiddleOfList_hasSpecialText() {
        // First, scroll to the view holder using the isInTheMiddle matcher.
        onView(ViewMatchers.withId(R.id.appointmentRecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));

        // Check that the item has the special text.
        String middleElementText = "MRN1985";
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

    public void waitViewShown(Matcher<View> matcher) {
        IdlingResource idlingResource = new ViewShownIdlingResource(matcher);///
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(matcher).check(matches(isDisplayed()));
        } finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}

class ViewShownIdlingResource implements IdlingResource {

    private static final String TAG = ViewShownIdlingResource.class.getSimpleName();

    private final Matcher<View> viewMatcher;
    private ResourceCallback resourceCallback;

    public ViewShownIdlingResource(final Matcher<View> viewMatcher) {
        this.viewMatcher = viewMatcher;
    }

    @Override
    public boolean isIdleNow() {
        View view = getView(viewMatcher);
        boolean idle = view == null || view.isShown();
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    @Override
    public String getName() {
        return this + viewMatcher.toString();
    }

    private static View getView(Matcher<View> viewMatcher) {
        try {
            ViewInteraction viewInteraction = onView(viewMatcher);
            Field finderField = viewInteraction.getClass().getDeclaredField("viewFinder");
            finderField.setAccessible(true);
            ViewFinder finder = (ViewFinder) finderField.get(viewInteraction);
            return finder.getView();
        } catch (Exception e) {
            return null;
        }
    }
}