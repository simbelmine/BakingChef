package com.example.android.bakingchef.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.bakingchef.fragments.DetailFragment;
import com.example.android.bakingchef.fragments.IngredientsFragment;
import com.example.android.bakingchef.R;
import com.example.android.bakingchef.fragments.StepsFragment;
import com.example.android.bakingchef.helpers.PaneUtils;
import com.example.android.bakingchef.models.Recipe;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MainActivity}.
 */
public class DetailActivity extends AppCompatActivity {
    public static final String RECIPE = "recipe_list";
    public static final String WIDGET_RECIPE = "WidgetRecipe";
    public static final String CURRENT_STEP = "current_step";
    public static final String CHECKED_INGREDIENTS = "CheckedIngredients";
    public static final String DETAILS_PREFS = "DetailsPrefs";
    public static final String IS_PANE_OPENED = "IsSlidingPaneWasOpened";
    public static final int PANE_DELAY = 1000;
    private static final String DETAILS_FRAGMENT = "DetailsFragment";
    private static final String STEPS_FRAGMENT = "StepsFragment";
    private static final String INGREDIENTS_FRAGMENT = "IngredientsFragment";
    public static final String SMALL_APPEARANCE = "SmallTextAppearance";
    public static final String MEDIUM_APPEARANCE = "MediumTextAppearance";
    public static final String LARGE_APPEARANCE = "LargeTextAppearance";

    private SharedPreferences sharedPrefs;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        sharedPrefs = getSharedPreferences(DETAILS_PREFS, MODE_PRIVATE);

        setupActionBar();

                // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        recipe = getRecipe();

        if (savedInstanceState == null) {
            setFragments(true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_detail);

        setFragments(false);
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
        clearSharedPreferences();
        StepsFragment.releasePlayer();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void clearSharedPreferences() {
        sharedPrefs.edit().remove(CURRENT_STEP).commit();
        sharedPrefs.edit().remove(CHECKED_INGREDIENTS).commit();
    }

    private Recipe getRecipe() {
        Bundle bundle = getIntent().getExtras();
        Recipe recipe = bundle.getParcelable(RECIPE);
        Recipe recipeWidget = bundle.getParcelable(WIDGET_RECIPE);

        if(recipe != null) return recipe;
        else if(recipeWidget != null) return recipeWidget;

        return null;
    }

    private void setFragments(boolean isFirstTime) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RECIPE, recipe);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);
        if(isFirstTime) {
            addFragment(fragment, R.id.details_container_layout);
        }
        else {
            replaceFragment(fragment, R.id.details_container_layout);
        }


        if(PaneUtils.isTwoPane(this) && PaneUtils.isTablet(this) || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(arguments);
            if(isFirstTime) {
                addFragment(ingredientsFragment, R.id.ingredients_container);
            }
            else {
                replaceFragment(ingredientsFragment, R.id.ingredients_container);
            }

            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setArguments(arguments);
            if(isFirstTime) {
                addFragment(stepsFragment, R.id.steps_container);
            }
            else {
                replaceFragment(stepsFragment, R.id.steps_container);
            }
        }
    }

    private void addFragment(Fragment fragment, int containerId) {
        getSupportFragmentManager().beginTransaction()
                .add(containerId, fragment)
                .commit();
    }

    private void replaceFragment(Fragment fragment, int containerId) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerId, fragment, DETAILS_FRAGMENT)
                .commit();
    }
}
