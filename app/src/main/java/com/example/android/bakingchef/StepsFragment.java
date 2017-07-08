package com.example.android.bakingchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingchef.models.Recipe;
import com.example.android.bakingchef.models.Step;

import java.util.List;

/**
 * Created by Sve on 7/7/17.
 */

public class StepsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG_SHORT_DESCRIPTION = "short";
    private static final String TAG_DESCRIPTION = "long";
    private Recipe recipe;
    private boolean isTwoPane;
    private int step;
    private Button nextStepBtn;
    private Button prevStepBtn;

    public StepsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(DetailActivity.RECIPE)) {
            recipe = getArguments().getParcelable(DetailActivity.RECIPE);
        }
        if (getArguments().containsKey(DetailActivity.IS_TWO_PANE)) {
            isTwoPane = getArguments().getBoolean(DetailActivity.IS_TWO_PANE);
        }

        nextStepBtn = (Button) getActivity().findViewById(R.id.button_next_step);
        nextStepBtn.setOnClickListener(this);
        prevStepBtn = (Button) getActivity().findViewById(R.id.button_prev_step);
        prevStepBtn.setOnClickListener(this);

        step = 1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        if (recipe != null) {
            LinearLayout stepsLayout = (LinearLayout) rootView.findViewById(R.id.steps_fragment_container);

            if(isTwoPane) {
                addStepsViews(stepsLayout);
            }
            else {
                showStepDetails(stepsLayout, step);
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        LinearLayout stepsLayout = (LinearLayout) getActivity().findViewById(R.id.steps_fragment_container);
        int id = v.getId();
        switch (id) {
            case R.id.button_next_step:
                step++;
                setFocusOnStep();
                showStepDetails(stepsLayout, step);
                break;
            case R.id.button_prev_step:
                step--;
                setFocusOnStep();
                showStepDetails(stepsLayout, step);
                break;
        }
    }

    private void setFocusOnStep() {
        ViewPager viewPager = (ViewPager)getActivity().findViewById(R.id.viewpager);
        viewPager.setCurrentItem(1);
    }

    private void showStepDetails(LinearLayout stepsLayout, int stepIdx) {
        List<Step> steps = recipe.getSteps();
        if(steps == null || steps.size() == 0)
            return;
        int allSteps = steps.size();
        if(stepIdx < 1) {
            step = 1;
            return;
        }
        if(stepIdx > allSteps-1) {
            step = allSteps-1;
            return;
        }
        setButtonsActiveInactive();

        Step step = steps.get(stepIdx);
        String shortDescription = step.getShortDescription();
        String description = step.getDescription();

        removeLayoutViews(stepsLayout);

        TextView shortDescriptionView = new TextView(getContext());
        shortDescriptionView.setText(shortDescription);
        shortDescriptionView.setTag(TAG_SHORT_DESCRIPTION);
        TextView descriptionView = new TextView(getContext());
        descriptionView.setTag(TAG_DESCRIPTION);
        descriptionView.setText(description);

        stepsLayout.addView(shortDescriptionView);
        stepsLayout.addView(descriptionView);
    }

    private void setButtonsActiveInactive() {
        if(step <= 1) {
            nextStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
            prevStepBtn.setTextColor(getResources().getColor(R.color.button_inactive));
        }
        else if(recipe.getSteps() != null && step >= recipe.getSteps().size()-1) {
            nextStepBtn.setTextColor(getResources().getColor(R.color.button_inactive));
            prevStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
        }
        else {
            nextStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
            prevStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
        }
    }

    private void removeLayoutViews(LinearLayout layout) {
        if(layout != null && layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
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
