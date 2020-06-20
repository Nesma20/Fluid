package com.thetatechno.fluid.ui.activities;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.thetatechno.fluid.R;
import com.thetatechno.fluid.ui.activities.main.MainAgentActivity;
import com.thetatechno.fluid.ui.activities.main.SimpleIdlingResource;
import com.thetatechno.fluid.EspressoTestingIdlingResource;
import com.thetatechno.fluid.utils.App;
import com.thetatechno.fluid.utils.PreferenceController;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainAgentActivityTest {

    private SimpleIdlingResource mIdlingResource;
    private SimpleIdlingResource mIdlingResourceForCheckIn;
    @Rule
    public ActivityScenarioRule activityRole = new ActivityScenarioRule(MainAgentActivity.class);


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
    public void swipeViewPager(){
        onView(withId(R.id.view_pager)).perform(swipeLeft());

    }


    @Before
    public void registerIdlingResource() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainAgentActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<MainAgentActivity>() {
            @Override
            public void perform(MainAgentActivity activity) {

                IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource());
                num = activity.getNumberOfCustomerAtFirstLocation();

            }
        });


    }

    @Test
    public void callAndStartAppointmentAndEnd() {
        onView(withId(R.id.view_pager)).perform(swipeLeft());
        for (int i = 0; i < num; i++) {
            onView(withId(R.id.call_fab)).perform(click());
            onView(withId(R.id.start_fab)).perform(click());
            onView(withId(R.id.start_fab)).perform(click());
        }
        //  onView(withId(R.id.arrive_or_checkin_list_dialog)).check(matches(isDisplayed()));
//        onView(ViewMatchers.withId(R.id.alert_recyclerview))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
//                        click()));
//        onView(withId(R.id.start_fab)).perform(click());

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
        if (mIdlingResourceForCheckIn != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResourceForCheckIn);

        }
    }

    int num;
    MainAgentActivity mainAgentActivity;

   @Before
    public void registerIdlingResourceForCheckIn() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainAgentActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<MainAgentActivity>() {
            @Override
            public void perform(MainAgentActivity activity) {
                mIdlingResourceForCheckIn = (SimpleIdlingResource) activity.getIdlingResourceForEnableButton();
                IdlingRegistry.getInstance().register(mIdlingResourceForCheckIn);
                IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource());
                mainAgentActivity = activity;
            }
        });
    }

    @Test
    public void arriveAllPatientAtSpecificLocation() {

        num = mainAgentActivity.getNumberofUnArrivedCustomer();

        for (int i = 0; i < num; i++) {
            onView(withId(R.id.confirm_arrive_fab)).perform(click());
            onView(ViewMatchers.withId(R.id.alert_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0,
                    click()));

        }
    }

    @After
    public void unregisterIdlingResourceForCheckin() {

        if (mIdlingResourceForCheckIn != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResourceForCheckIn);
        }
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
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("has drawable");

            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() == getApplicationContext().getResources().getDrawable(R.drawable.ic_call);
            }
        };
    }


}