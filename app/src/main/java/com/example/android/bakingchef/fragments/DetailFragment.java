package com.example.android.bakingchef.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingchef.R;
import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.activities.MainActivity;
import com.example.android.bakingchef.adapters.ViewPagerAdapter;
import com.example.android.bakingchef.helpers.PaneUtils;
import com.example.android.bakingchef.models.Recipe;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link DetailActivity}
 * on handsets.
 */
public class DetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    /**
     * The dummy content this fragment is presenting.
     */
    private Recipe recipe;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TextView titleView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(DetailActivity.RECIPE)) {
            recipe = getArguments().getParcelable(DetailActivity.RECIPE);
        }

        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        initViews(rootView);

        if (!PaneUtils.isTwoPane(getActivity()) && !PaneUtils.isLandscape(getActivity())) {
            if (viewPager != null) {
                setupAdapter();
                setupViewPager();
                tabLayout.setupWithViewPager(viewPager);
            }
        }

        if (recipe != null && titleView != null) {
            titleView.setText(recipe.getName());
        }

        return rootView;
    }

    private void setupAdapter() {
        adapter.setRecipe(recipe);
        adapter.addFragment(new IngredientsFragment(), getResources().getString(R.string.fragment_ingredients_name));
        adapter.addFragment(new StepsFragment(), getResources().getString(R.string.fragment_steps_name));
    }

    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }

    private void initViews(View rootView) {
        titleView = (TextView) rootView.findViewById(R.id.item_detail);
        if(titleView == null) {
            titleView = (TextView) getActivity().findViewById(R.id.item_detail);
        }
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
    }
}
