package com.thetatechno.fluid.ui.activities;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.thetatechno.fluid.R;
import com.thetatechno.fluid.ui.activities.login.LoginActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginActivityTest {

    @Test
    public void testIsActivityInView() {
        ActivityScenario loginAActivity = ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.login_layout)).check(matches(isDisplayed()));
    }
    @Test
    public void  testisLoginButtonDisplayed(){
        ActivityScenario loginAActivity = ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.login_btn)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}