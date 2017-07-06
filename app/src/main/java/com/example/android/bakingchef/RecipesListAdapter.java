package com.example.android.bakingchef;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingchef.models.Ingredient;
import com.example.android.bakingchef.models.Recipe;
import com.example.android.bakingchef.models.Step;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;


public class RecipesListAdapter extends RecyclerView.Adapter<RecipesViewHolder> {

    //    private final List<DummyContent.DummyItem> mValues;
    private List<Recipe> recipesList;
    private Context context;

    public RecipesListAdapter(Context context, List<Recipe> recipesList) {
        this.context = context;
        this.recipesList = recipesList;
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipesViewHolder holder, int position) {
        if(recipesList == null || recipesList.size() == 0) return;
        Recipe recipe = recipesList.get(position);



//        Log.v(MainActivity.TAG, "Recipe: ");
//        Log.v(MainActivity.TAG, "ID: " + recipe.getId());
//        Log.v(MainActivity.TAG, "Name: " + recipe.getName());
//        for(Ingredient ingredient : recipe.getIngredients()) {
//            Log.v(MainActivity.TAG, "" + ingredient.getQuantity());
//            Log.v(MainActivity.TAG, "" + ingredient.getMeasure());
//            Log.v(MainActivity.TAG, "" + ingredient.getIngredient());
//        }
//        for(Step step : recipe.getSteps()) {
//            Log.v(MainActivity.TAG, "" + step.getStepId());
//            Log.v(MainActivity.TAG, "" + step.getShortDescription());
//            Log.v(MainActivity.TAG, "" + step.getDescription());
//            Log.v(MainActivity.TAG, "" + step.getVideoURL());
//            Log.v(MainActivity.TAG, "" + step.getThumbnailURL());
//        }
//        Log.v(MainActivity.TAG, "Servings: " + recipe.getServings());
//        Log.v(MainActivity.TAG, "ImageURL: " + recipe.getImage());




        //holder.recipePhoto.setImageBitmap(getImageBitmap(recipe));
        holder.recipeTitle.setText(recipe.getName());

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(DetailFragment.ARG_ITEM_ID, holder.mItem.id);
//                    DetailFragment fragment = new DetailFragment();
//                    fragment.setArguments(arguments);
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
//                } else {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, DetailActivity.class);
//                    intent.putExtra(DetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                    context.startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(recipesList == null || recipesList.size() == 0)
            return 0;
        return recipesList.size();
    }

    @Override
    public long getItemId(int position) {
        return recipesList.get(position).getId();
    }

    public void setRecipesList(List<Recipe> recipesList) {
        this.recipesList = recipesList;
        notifyDataSetChanged();
    }


}