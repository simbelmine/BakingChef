package com.example.android.bakingchef;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingchef.TestUtils.rotateDevice;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTabSwitchTest {
    private static final String INGREDIENT_STR = "Ingredients for 8";
    private static final String STEP_STR = "Starting prep";
    private Activity activity;

    @Rule
    public IntentsTestRule<MainActivity> testRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        activity = testRule.getActivity();
        assertThat(activity, notNullValue());
    }

    @Test
    public void tabSwitchTest() {
        onView(withId(R.id.item_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(DetailActivity.class.getName()));

        rotateDevice(activity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onView(allOf(withText(activity.getResources().getString(R.string.fragment_ingredients_name)), isDescendantOfA(withId(R.id.tabs))))
                .perform(click())
                .check(matches(isDisplayed()));

        onView(withText(INGREDIENT_STR)).check(matches(isDisplayed()));

        onView(allOf(withText(activity.getResources().getString(R.string.fragment_steps_name)), isDescendantOfA(withId(R.id.tabs))))
                .perform(click())
                .check(matches(isDisplayed()));

        /** Button click Next is needed because string duplication on first step **/
        onView(withText(activity.getResources().getString(R.string.next_btn)))
                .perform(click());
        onView(withText(STEP_STR)).check(matches(isDisplayed()));
    }
}
