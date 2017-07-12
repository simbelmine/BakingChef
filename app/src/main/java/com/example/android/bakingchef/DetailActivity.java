package com.example.android.bakingchef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.example.android.bakingchef.helpers.PaneUtils;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MainActivity}.
 */
public class DetailActivity extends AppCompatActivity {
    public static final String RECIPE = "recipe_list";
    public static final String IS_TWO_PANE = "is_two_pane";
    public static final String CURRENT_STEP = "current_step";
    public static final String DETAILS_PREFS = "DetailsPrefs";
    private static final String DETAILS_FRAGMENT = "DetailsFragment";
    private static final String STEPS_FRAGMENT = "StepsFragment";
    private static final String INGREDIENTS_FRAGMENT = "IngredientsFragment";
    public static final String SMALL_APPEARANCE = "SmallTextAppearance";
    public static final String MEDIUM_APPEARANCE = "MediumTextAppearance";
    public static final String LARGE_APPEARANCE = "LargeTextAppearance";

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        sharedPrefs = getSharedPreferences(DETAILS_PREFS, MODE_PRIVATE);

        setupActionBar();
//        isTwoPane = PaneUtils.isTwoPane(this);

                // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(RECIPE, getIntent().getParcelableExtra(RECIPE));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container_layout, fragment)
                    .commit();


            if(PaneUtils.isTwoPane(this) && PaneUtils.isTablet(this) || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                ingredientsFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.ingredients_container, ingredientsFragment)
                        .commit();

                StepsFragment stepsFragment = new StepsFragment();
                stepsFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.steps_container, stepsFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_detail);

        Bundle arguments = new Bundle();
        arguments.putParcelable(RECIPE, getIntent().getParcelableExtra(RECIPE));

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.details_container_layout, fragment, DETAILS_FRAGMENT)
                .commit();

        if(PaneUtils.isTwoPane(this) && PaneUtils.isTablet(this) || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ingredients_container, ingredientsFragment, INGREDIENTS_FRAGMENT)
                    .commit();

            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.steps_container, stepsFragment, STEPS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sharedPrefs.edit().clear().commit();
        StepsFragment.releasePlayer();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
