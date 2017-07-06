package com.example.android.bakingchef;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public View mView;
    public ImageView recipePhoto;
    public TextView recipeTitle;
    public RecipeOnClickListener onClickListener;

    public RecipesViewHolder(View view, RecipeOnClickListener onClickListener) {
        super(view);
        this.onClickListener = onClickListener;
        mView = view;
        mView.setOnClickListener(this);
        recipePhoto = (ImageView) view.findViewById(R.id.recipe_photo);
        recipeTitle = (TextView) view.findViewById(R.id.recipe_name);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + recipeTitle.getText() + "'";
    }

    @Override
    public void onClick(View v) {
        this.onClickListener.onClick(getAdapterPosition());
    }
}
