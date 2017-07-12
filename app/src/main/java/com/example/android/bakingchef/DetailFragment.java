package com.example.android.bakingchef;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private boolean isTwoPane;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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

        if (getArguments().containsKey(DetailActivity.IS_TWO_PANE)) {
            isTwoPane = getArguments().getBoolean(DetailActivity.IS_TWO_PANE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        initViews(rootView);

        if(!isTwoPane) {
            if(viewPager != null) {
                setupViewPager();
                tabLayout.setupWithViewPager(viewPager);
            }
        }

        if (recipe != null) {
            titleView.setText(recipe.getName());
        }

        return rootView;
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.setRecipe(recipe);
        adapter.setIsTwoPane(isTwoPane);
        adapter.addFragment(new IngredientsFragment(), "Ingredients");
        adapter.addFragment(new StepsFragment(), "Steps");
        viewPager.setAdapter(adapter);
    }

    private void initViews(View rootView) {
        titleView = (TextView) rootView.findViewById(R.id.item_detail);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
    }
}
