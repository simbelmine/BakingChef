package com.example.android.bakingchef;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingchef.helpers.TextHelper;
import com.example.android.bakingchef.models.Ingredient;
import com.example.android.bakingchef.models.Recipe;

import java.util.List;


public class IngredientsFragment extends Fragment {
    private static final char BULLET = 167;
    private Recipe recipe;
    private boolean isTwoPane;

    public IngredientsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(DetailActivity.RECIPE)) {
            recipe = getArguments().getParcelable(DetailActivity.RECIPE);
        }
        if (getArguments().containsKey(DetailActivity.IS_TWO_PANE)) {
            isTwoPane = getArguments().getBoolean(DetailActivity.IS_TWO_PANE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        if (recipe != null) {
            LinearLayout stepsLayout = (LinearLayout) rootView.findViewById(R.id.ingredients_fragment_container);
            if(stepsLayout == null) return rootView;

            addServingsView(stepsLayout);
            addIngredientsView(stepsLayout);
        }

        return rootView;
    }

    private void addServingsView(LinearLayout layout) {
        TextView servingsView = new TextView(getContext());
        int servings = recipe.getServings();
        servingsView.setText(getResources().getString(R.string.servings_ingredients) + " " + String.valueOf(servings));
        TextHelper.setTextStyle(getContext(), servingsView, true);
        layout.addView(servingsView);
    }

    private void addIngredientsView(LinearLayout layout) {
        TextView ingredientsView = new TextView(getContext());
        String ingredientsText = buildIngredientsText();
        if(ingredientsText.isEmpty())
            return;
        ingredientsView.setText(ingredientsText);
        TextHelper.setTextStyle(getContext(), ingredientsView, false);
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

            builder.append(BULLET + " " + String.valueOf(quantity) + " ");
            builder.append(measure + " ");
            builder.append(ingredientStr + "\n");
            if(!isTwoPane) builder.append("\n");
        }

        return builder.toString();
    }
}
