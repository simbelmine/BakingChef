package com.example.android.bakingchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.android.bakingchef.models.Recipe;
import com.example.android.bakingchef.models.Step;

import java.util.List;

/**
 * Created by Sve on 7/7/17.
 */

public class StepsFragment extends Fragment {
    private Recipe recipe;

    public StepsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(DetailActivity.RECIPE)) {
            recipe = getArguments().getParcelable(DetailActivity.RECIPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        if (recipe != null) {
            LinearLayout stepsLayout = (LinearLayout) rootView.findViewById(R.id.steps_fragment_container);

            addStepsViews(stepsLayout);
        }

        return rootView;
    }

    private void addStepsViews(LinearLayout layout) {
        List<Step> stepsList = recipe.getSteps();
        if(stepsList == null || stepsList.size() == 0)
            return;

        for(Step step : stepsList) {
            Button stepButton = new Button(getContext());
            String shortDescription = step.getShortDescription();
            stepButton.setText(shortDescription);
            layout.addView(stepButton);
        }

        View emptyView = new View(getContext());
        emptyView.setMinimumHeight((int)getContext().getResources().getDimension(R.dimen.standard_margin));
        layout.addView(emptyView);
    }
}
