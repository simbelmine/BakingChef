package com.example.android.bakingchef;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


import com.example.android.bakingchef.models.Recipe;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MainActivity extends AppCompatActivity implements RecipeOnClickListener {
    public static final String TAG = "chef";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private ArrayList<Recipe> recipeList;
    private RecipesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        getData.execute();
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns()));
        }

        adapter = new RecipesListAdapter(this, null, this);
        recyclerView.setAdapter(adapter);
    }

    private String loadJSONfromAssets() {
        String json;
        try {
            InputStream io = getAssets().open("baking.json");
            int size = io.available();
            byte[] buffer = new byte[size];
            io.read(buffer);
            io.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex) {
            Log.e(TAG, "MainActivity: " + ex.getMessage());
            return null;
        }

        return json;
    }

    AsyncTask<Void, Void, Void> getData = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
            recipeList = new GsonBuilder().create().fromJson(loadJSONfromAssets(), listType);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(recipeList == null) return;
            if(adapter == null) return;

            adapter.setRecipesList(recipeList);
        }
    };

    @Override
    public void onClick(int position) {
//        if (mTwoPane) {
//            Bundle arguments = new Bundle();
//            arguments.putParcelable(DetailFragment.RECIPE, recipeList.get(position));
//            DetailFragment fragment = new DetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.item_detail_container, fragment)
//                    .commit();
//        } else {
//            Intent intent = new Intent(this, DetailActivity.class);
//            intent.putExtra(DetailFragment.RECIPE, recipeList.get(position));
//
//            startActivity(intent);
//        }

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.RECIPE, recipeList.get(position));

        startActivity(intent);
    }
}
