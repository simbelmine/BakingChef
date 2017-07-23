package com.example.android.bakingchef;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MainActivityItemTest.class,
        MainActivityScrollTest.class,
        MainActivityPhoneOrientationTest.class,
        DetailActivityTabSwitchTest.class,
        DetailActivityTabTest.class,
        DetailActivityCheckTest.class,
        DetailActivityPhoneOrientationTest.class,
        DetailActivityRecipeDataTest.class,
        DetailActivityExoPlayerTest.class
})

public class AllTestsSuite {
}
