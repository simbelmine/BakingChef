package com.example.android.bakingchef;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipesViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public ImageView recipePhoto;
    public TextView recipeTitle;

    public RecipesViewHolder(View view) {
        super(view);
        mView = view;
        recipePhoto = (ImageView) view.findViewById(R.id.recipe_photo);
        recipeTitle = (TextView) view.findViewById(R.id.recipe_name);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + recipeTitle.getText() + "'";
    }
}
