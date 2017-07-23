package com.example.android.bakingchef;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.activities.MainActivity;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class DetailActivityExoPlayerTest {
    @Rule
    public IntentsTestRule<MainActivity> testRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void exoPlayerDisplayedTest() {
        onView(withId(R.id.item_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(DetailActivity.class.getName()));

        onView(allOf(instanceOf(SimpleExoPlayerView.class), withChild(withId(R.id.player_view))))
                .perform(click())
                .check(matches(isDisplayed()));
    }
}
