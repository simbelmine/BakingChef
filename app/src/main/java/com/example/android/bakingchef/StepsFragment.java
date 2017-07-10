package com.example.android.bakingchef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingchef.helpers.TextHelper;
import com.example.android.bakingchef.models.Recipe;
import com.example.android.bakingchef.models.Step;

import java.util.List;

public class StepsFragment extends Fragment implements View.OnClickListener {
    private Recipe recipe;
    private boolean isTwoPane;
    private int step;
    private Button nextStepBtn;
    private Button prevStepBtn;
    private  ViewPager viewPager;
    private SharedPreferences sharedPrefs;

    public StepsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getActivity().getSharedPreferences(DetailActivity.DETAILS_PREFS, Context.MODE_PRIVATE);

        if (getArguments().containsKey(DetailActivity.RECIPE)) {
            recipe = getArguments().getParcelable(DetailActivity.RECIPE);
        }
        if (getArguments().containsKey(DetailActivity.IS_TWO_PANE)) {
            isTwoPane = getArguments().getBoolean(DetailActivity.IS_TWO_PANE);
        }

        nextStepBtn = (Button) getActivity().findViewById(R.id.button_next_step);
        prevStepBtn = (Button) getActivity().findViewById(R.id.button_prev_step);

        if(nextStepBtn != null && prevStepBtn != null) {
            nextStepBtn.setOnClickListener(this);
            prevStepBtn.setOnClickListener(this);
        }

        viewPager = (ViewPager)getActivity().findViewById(R.id.viewpager);
        if(sharedPrefs != null && sharedPrefs.contains(DetailActivity.CURRENT_STEP)) {
            step = sharedPrefs.getInt(DetailActivity.CURRENT_STEP, 0);
        }
        else {
            step = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        if (recipe != null) {
            LinearLayout stepsLayout = (LinearLayout) rootView.findViewById(R.id.steps_fragment_container);
            if(stepsLayout == null) return rootView;

            if(isTwoPane) {
                addStepsViews(stepsLayout);
                showStepDetailsTwoPane(step);
            }
            else {
                if(step > 0 && !isStepsOnFocus()) {
                    setFocusOnStep();
                }
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
            step = tag;
            showStepDetailsTwoPane(tag);
        }

        saveCurrentStep();
    }

    private void showStepDetailsTwoPane(int stepIdx) {
        LinearLayout stepsDetailsContainer = (LinearLayout) getActivity().findViewById(R.id.steps_details_container);
        if(stepsDetailsContainer == null) return;
        TextView sDescView = new TextView(getContext());
        TextView lDescView = new TextView(getContext());
        removeLayoutViews(stepsDetailsContainer);

        List<Step> steps = recipe.getSteps();
        if(steps == null || steps.size() == 0)
            return;
        Step step = steps.get(stepIdx);
        String sDesc = step.getShortDescription();
        String lDesc = step.getDescription();

        sDescView.setText(sDesc);
        TextHelper.setTextStyle(getContext(), sDescView, true);
        lDescView.setText(lDesc);
        TextHelper.setTextStyle(getContext(), lDescView, false);

        stepsDetailsContainer.addView(sDescView);
        stepsDetailsContainer.addView(lDescView);
    }

    private void setFocusOnStep() {
        viewPager.setCurrentItem(1);
    }

    private boolean isStepsOnFocus() {
        return viewPager.getCurrentItem() == 1;
    }

    private void showStepDetails(LinearLayout stepsLayout, int stepIdx) {
        List<Step> steps = recipe.getSteps();
        if (steps == null || steps.size() == 0)
            return;
        int allSteps = steps.size();
        if (stepIdx < 0) {
            step = 0;
            return;
        }
        if (stepIdx > allSteps - 1) {
            step = allSteps - 1;
            return;
        }
        setButtonsActiveInactive();

        Step step = steps.get(stepIdx);
        String shortDescription = step.getShortDescription();
        String description = step.getDescription();

        removeLayoutViews(stepsLayout);

        TextView shortDescriptionView = new TextView(getContext());
        shortDescriptionView.setText(shortDescription);
        TextHelper.setTextStyle(getContext(), shortDescriptionView, true);
        TextView descriptionView = new TextView(getContext());
        descriptionView.setText(description);
        TextHelper.setTextStyle(getContext(), descriptionView, false);

        stepsLayout.addView(shortDescriptionView);
        stepsLayout.addView(descriptionView);
    }

    private void setButtonsActiveInactive() {
        if(nextStepBtn != null && prevStepBtn != null) {
            if (step <= 0) {
                nextStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
                prevStepBtn.setTextColor(getResources().getColor(R.color.button_inactive));
            } else if (recipe.getSteps() != null && step >= recipe.getSteps().size() - 1) {
                nextStepBtn.setTextColor(getResources().getColor(R.color.button_inactive));
                prevStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
            } else {
                nextStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
                prevStepBtn.setTextColor(getResources().getColor(R.color.main_txt_color));
            }
        }
    }

    private void removeLayoutViews(LinearLayout layout) {
        if(layout != null && layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
    }

    private void addStepsViews(LinearLayout layout) {
        List<Step> stepsList = recipe.getSteps();
        if (stepsList == null || stepsList.size() == 0)
            return;

        for (Step step : stepsList) {
            Button stepButton = new Button(getContext());
            String shortDescription = step.getShortDescription();
            stepButton.setOnClickListener(this);
            stepButton.setTag(step.getStepId());
            stepButton.setText(shortDescription);
            stepButton.setTextColor(getResources().getColor(R.color.main_txt_color));
            stepButton.setBackgroundResource(R.drawable.selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources()
                    .getDimension(R.dimen.button_width), (int) getResources().getDimension(R.dimen.button_height));
            params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.standard_margin));
            params.gravity = Gravity.CENTER_HORIZONTAL;
            layout.addView(stepButton, params);
        }

        View emptyView = new View(getContext());
        emptyView.setMinimumHeight((int) getContext().getResources().getDimension(R.dimen.standard_margin));
        layout.addView(emptyView);
    }

    private void saveCurrentStep() {
        if(sharedPrefs != null) {
            sharedPrefs.edit().putInt(DetailActivity.CURRENT_STEP, step).commit();
        }
    }
}
