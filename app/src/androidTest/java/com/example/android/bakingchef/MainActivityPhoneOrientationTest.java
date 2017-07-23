package com.example.android.bakingchef;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingchef.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingchef.TestUtils.atPosition;
import static com.example.android.bakingchef.TestUtils.isTablet;
import static com.example.android.bakingchef.TestUtils.rotateDevice;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MainActivityPhoneOrientationTest {
    public static final int POSITION = 3;
    public static final String RECIPE_NAME = "Cheesecake";
    private Activity activity;

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        activity = testRule.getActivity();
        assertThat(activity, notNullValue());
    }

    @Test
    public void recyclerViewOnOrientationChanged() {
        if(!isTablet(testRule.getActivity())) {
            onView(withId(R.id.item_list))
                    .check(matches(not(atPosition(POSITION, hasDescendant(withText(RECIPE_NAME))))));
            rotateDevice(testRule.getActivity(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            onView(withId(R.id.item_list))
                    .check(matches(atPosition(POSITION, hasDescendant(withText(RECIPE_NAME)))));
        }
    }
}
