package com.example.android.bakingchef;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingchef.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipesListAdapter extends RecyclerView.Adapter<RecipesViewHolder> {
    private List<Recipe> recipesList;
    private Context context;
    private RecipeOnClickListener onClickListener;

    public RecipesListAdapter(Context context, List<Recipe> recipesList, RecipeOnClickListener onClickListener) {
        this.context = context;
        this.recipesList = recipesList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_content, parent, false);
        return new RecipesViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(final RecipesViewHolder holder, int position) {
        if(recipesList == null || recipesList.size() == 0) return;
        Recipe recipe = recipesList.get(position);

        loadRecipePhoto(holder, recipe.getImage());
        holder.recipeTitle.setText(recipe.getName());
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

    private void loadRecipePhoto(RecipesViewHolder holder, String url) {
        Picasso picasso = Picasso.with(context);
        if(url.isEmpty()) {
            picasso.load(R.drawable.baking).into(holder.recipePhoto);
        }
        else {
            picasso.load(url).into(holder.recipePhoto);
        }
    }
}