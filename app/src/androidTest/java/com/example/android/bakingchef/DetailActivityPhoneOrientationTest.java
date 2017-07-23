package com.example.android.bakingchef;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingchef.activities.DetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.android.bakingchef.TestUtils.rotateDevice;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class DetailActivityPhoneOrientationTest {
    private Activity activity;

    @Rule
    public ActivityTestRule<DetailActivity> testRule = new ActivityTestRule<>(DetailActivity.class);

    @Before
    public void setUp() {
        activity = testRule.getActivity();
        assertThat(activity, notNullValue());
    }

    @Test
    public void activityOnOrientationChangedTest() {
        onView(withId(R.id.tabs))
                .check(matches(isDisplayed()));

        rotateDevice(testRule.getActivity(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        onView(withId(R.id.tabs))
                .check(doesNotExist());
    }
}
