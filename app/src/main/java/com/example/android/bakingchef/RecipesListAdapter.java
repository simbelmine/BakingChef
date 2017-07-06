package com.example.android.bakingchef;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingchef.models.Recipe;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
                .inflate(R.layout.item_list_content, parent, false);
        return new RecipesViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(final RecipesViewHolder holder, int position) {
        if(recipesList == null || recipesList.size() == 0) return;
        Recipe recipe = recipesList.get(position);

        new LoadImage(holder, recipe).execute();
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


    // ToDO : remove this and if URL exist load it with Picasso  else load default

    public class LoadImage extends AsyncTask<Void, Void, Bitmap> {
        private Recipe recipe;
        private RecipesViewHolder holder;

        public LoadImage(RecipesViewHolder holder, Recipe recipe) {
            this.holder = holder;
            this.recipe = recipe;
        }

        @Override
        protected void onPreExecute() {
            holder.recipePhoto.setImageBitmap(getDefaultImage());
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String imageURL = recipe.getImage();

            Bitmap bitmap = null;
            if(imageURL.isEmpty())
                return bitmap;

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(imageURL);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
                return BitmapFactory.decodeStream(bufferedInputStream);
            }
            catch (IOException ex) {
                Log.e(MainActivity.TAG, "RecipeListAdapter: " + ex.getMessage());
                return null;
            }
            finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap == null)
                bitmap = getDefaultImage();

            holder.recipePhoto.setImageBitmap(bitmap);
        }
    }

    private Bitmap getDefaultImage() {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.baking);
    }
}