package com.example.android.bakingchef;


import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingchef.activities.DetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTabTest {
    private Activity activity;

    @Rule
    public ActivityTestRule<DetailActivity> testRule = new ActivityTestRule<>(DetailActivity.class);

    @Before
    public void setUp() {
        activity = testRule.getActivity();
        assertThat(activity, notNullValue());
    }

    @Test
    public void swipePageTest() {
        onView(withId(R.id.viewpager))
                .check(matches(isDisplayed()))
                .perform(swipeLeft());
    }

    @Test
    public void tabLayoutTest() {
        onView(withId(R.id.tabs))
                .perform(click())
                .check(matches(isDisplayed()));
    }
}
