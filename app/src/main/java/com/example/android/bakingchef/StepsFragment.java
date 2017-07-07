package com.example.android.bakingchef;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingchef.models.Ingredient;
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

            addIngredientsView(stepsLayout);
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


    private void addIngredientsView(LinearLayout layout) {
        TextView ingredientsView = new TextView(getContext());
        String ingredientsText = buildIngredientsText();
        if(ingredientsText.isEmpty())
            return;
        ingredientsView.setText(ingredientsText);
        layout.addView(ingredientsView);
    }

    private String buildIngredientsText() {
        StringBuilder builder = new StringBuilder();
        List<Ingredient> ingredientsList = recipe.getIngredients();
        if(ingredientsList == null || ingredientsList.size() == 0)
            return "";
        for(Ingredient ingredient : ingredientsList) {
            double quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();
            String ingredientStr = ingredient.getIngredient();

            builder.append(String.valueOf(quantity) + " ");
            builder.append(measure + " ");
            builder.append(ingredientStr + "\n");
        }

        return builder.toString();
    }
}
