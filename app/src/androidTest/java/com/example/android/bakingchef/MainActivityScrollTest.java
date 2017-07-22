package com.example.android.bakingchef;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingchef.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingchef.TestUtils.atPosition;

@RunWith(AndroidJUnit4.class)
public class MainActivityScrollTest {
    public static final int POSITION = 3;
    public static final String RECIPE_NAME = "Cheesecake";

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recyclerViewScrollToPositionTest() {
           onView(withId(R.id.item_list))
                .perform(scrollToPosition(POSITION))
                .check(matches(atPosition(POSITION, hasDescendant(withText(RECIPE_NAME)))));
    }
}
