package com.example.android.bakingchef;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static com.example.android.bakingchef.TestUtils.rotateDevice;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class DetailActivityCheckTest {
    private static final String INGREDIENT_TAG = "Graham Cracker crumbs";

    @Rule
    public IntentsTestRule<MainActivity> testRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void ingredientCheckUncheckTest() {
        onView(withId(R.id.item_list))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, click())
                );

        intended(hasComponent(DetailActivity.class.getName()));

        rotateDevice(testRule.getActivity(), ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onView(withId(R.id.tabs))
                .check(matches(isDisplayed()));

        onView(withTagValue(is((Object) INGREDIENT_TAG)))
                .check(matches(not(isChecked())));
    }
}
