package com.example.android.bakingchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
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
    private Recipe recipe;
    private boolean isTwoPane;
    private int step;
    private Button nextStepBtn;
    private Button prevStepBtn;
    private  ViewPager viewPager;

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

        viewPager = (ViewPager)getActivity().findViewById(R.id.viewpager);

        step = 0;
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
                if(!isStepsOnFocus()) {
                    setFocusOnStep();
                    return;
                }
                step++;
                showStepDetails(stepsLayout, step);
                break;
            case R.id.button_prev_step:
                if(!isStepsOnFocus()) {
                    setFocusOnStep();
                    return;
                }
                step--;
                showStepDetails(stepsLayout, step);
                break;
        }

        if(v instanceof Button && isTwoPane) {
            int tag = (int)v.getTag();
            LinearLayout stepsDetailsContainer = (LinearLayout) getActivity().findViewById(R.id.steps_details_container);
            TextView sDescView = new TextView(getContext());
            TextView lDescView = new TextView(getContext());
            removeLayoutViews(stepsDetailsContainer);

            List<Step> steps = recipe.getSteps();
            if(steps == null || steps.size() == 0)
                return;
            Step step = steps.get(tag);
            String sDesc = step.getShortDescription();
            String lDesc = step.getDescription();

            sDescView.setText(sDesc);
            lDescView.setText(lDesc);

            stepsDetailsContainer.addView(sDescView);
            stepsDetailsContainer.addView(lDescView);
        }
    }

    private void setFocusOnStep() {
        viewPager.setCurrentItem(1);
    }

    private boolean isStepsOnFocus() {
        return viewPager.getCurrentItem() == 1;
    }

    private void showStepDetails(LinearLayout stepsLayout, int stepIdx) {
        List<Step> steps = recipe.getSteps();
        if(steps == null || steps.size() == 0)
            return;
        int allSteps = steps.size();
        if(stepIdx < 0) {
            step = 0;
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
        TextView descriptionView = new TextView(getContext());
        descriptionView.setText(description);

        stepsLayout.addView(shortDescriptionView);
        stepsLayout.addView(descriptionView);
    }

    private void setButtonsActiveInactive() {
        if(step <= 0) {
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
            stepButton.setOnClickListener(this);
            stepButton.setTag(step.getStepId());
            stepButton.setText(shortDescription);
            stepButton.setTextColor(getResources().getColor(R.color.main_txt_color));
            stepButton.setBackgroundResource(R.drawable.selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)getResources()
                    .getDimension(R.dimen.button_width), (int)getResources().getDimension(R.dimen.button_height));
            params.setMargins(0, 0, 0, (int)getResources().getDimension(R.dimen.standard_margin));
            params.gravity= Gravity.CENTER_HORIZONTAL;
            layout.addView(stepButton, params);
        }

        View emptyView = new View(getContext());
        emptyView.setMinimumHeight((int)getContext().getResources().getDimension(R.dimen.standard_margin));
        layout.addView(emptyView);
    }
}
