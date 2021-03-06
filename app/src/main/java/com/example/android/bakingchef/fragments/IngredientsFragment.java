package com.example.android.bakingchef.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingchef.R;
import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.activities.MainActivity;
import com.example.android.bakingchef.helpers.DataHelper;
import com.example.android.bakingchef.helpers.PaneUtils;
import com.example.android.bakingchef.helpers.MyTextUtils;
import com.example.android.bakingchef.models.Ingredient;
import com.example.android.bakingchef.models.Recipe;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class IngredientsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private Set<String> checkedIngredientsSet;
    private SharedPreferences sharedPrefs;
    private Recipe recipe;

    public IngredientsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getContext().getSharedPreferences(DetailActivity.DETAILS_PREFS, Context.MODE_PRIVATE);
        checkedIngredientsSet = new HashSet<>();

        if (getArguments().containsKey(DetailActivity.RECIPE)) {
            recipe = getArguments().getParcelable(DetailActivity.RECIPE);
        }

        if(sharedPrefs != null && sharedPrefs.contains(DetailActivity.CHECKED_INGREDIENTS)) {
            String json = sharedPrefs.getString(DetailActivity.CHECKED_INGREDIENTS, null);
            Type type = new TypeToken<HashSet<String>>() {}.getType();
            checkedIngredientsSet = (HashSet<String>) DataHelper.jsonToCollection(json, type);
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
            addIngredientViews(stepsLayout);
            
            checkBoxes(rootView);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(checkedIngredientsSet != null) {
            String json = DataHelper.collectionToJson(checkedIngredientsSet);
            sharedPrefs.edit().putString(DetailActivity.CHECKED_INGREDIENTS, json).commit();

            for(String s: checkedIngredientsSet) {
                Log.v(MainActivity.TAG, s);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            checkedIngredientsSet.add((String) buttonView.getTag());
        }
        else {
            checkedIngredientsSet.remove(buttonView.getTag());
        }
    }

    private void addServingsView(LinearLayout layout) {
        TextView servingsView = new TextView(getContext());
        int servings = recipe.getServings();
        servingsView.setText(getResources().getString(R.string.servings_ingredients) + " " + String.valueOf(servings));
        MyTextUtils.setTextStyle(getContext(), servingsView, DetailActivity.LARGE_APPEARANCE);
        layout.addView(servingsView);
    }

    private void addIngredientViews(LinearLayout layout) {
        List<Ingredient> ingredientsList = recipe.getIngredients();
        if(ingredientsList == null || ingredientsList.size() == 0)
            return;

        for(Ingredient ingredient : ingredientsList) {
            LinearLayout singleLayout = new LinearLayout(getContext());
            singleLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    (int)getResources().getDimension(R.dimen.text_view_height));

            ContextThemeWrapper newContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.checkBoxStyle);
            CheckBox checkBox = new CheckBox(newContext);
            TextView textView = new TextView(getContext());

            checkBox.setTag(ingredient.getIngredient());
            checkBox.setOnCheckedChangeListener(this);

            String ingredientStr = buildIngredientText(ingredient);
            textView.setText(ingredientStr);
            MyTextUtils.setTextStyle(getContext(), textView, DetailActivity.MEDIUM_APPEARANCE);

            singleLayout.addView(checkBox);
            singleLayout.addView(textView);

            layout.addView(singleLayout, params);
        }
    }

    private String buildIngredientText(Ingredient ingredient) {
        StringBuilder builder = new StringBuilder();
        double quantity = ingredient.getQuantity();
        String measure = ingredient.getMeasure();
        String ingredientStr = ingredient.getIngredient();

        builder.append(String.valueOf(quantity) + " ");
        builder.append(measure + " ");
        builder.append(ingredientStr + "\n");
        if(!PaneUtils.isTwoPane(getActivity())) builder.append("\n");

        return builder.toString();
    }

    private void checkBoxes(View rootView) {
        if(checkedIngredientsSet != null && checkedIngredientsSet.size() > 0) {
            for (String tag : checkedIngredientsSet) {
                ((CheckBox) rootView.findViewWithTag(tag)).setChecked(true);
            }
        }
    }
}
