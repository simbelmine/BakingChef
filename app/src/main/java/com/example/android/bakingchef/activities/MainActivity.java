package com.example.android.bakingchef.activities;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.bakingchef.AppController;
import com.example.android.bakingchef.R;
import com.example.android.bakingchef.RecipeOnClickListener;
import com.example.android.bakingchef.adapters.RecipesListAdapter;
import com.example.android.bakingchef.helpers.DataHelper;
import com.example.android.bakingchef.models.Recipe;
import com.example.android.bakingchef.widget.CollectionAppWidgetProvider;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URL;
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
    public static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
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

        Response.Listener<JSONArray> responseListener = getResponseListener();
        Response.ErrorListener responseErrorListener = getResponseErrorListener();
        DataHelper.makeJsonRequest(URL, responseListener, responseErrorListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.RECIPE, recipeList.get(position));

        startActivity(intent);
    }

    private Response.Listener<JSONArray> getResponseListener() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String json = response.toString();
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                recipeList = (ArrayList<Recipe>) DataHelper.jsonToCollection(json, listType);

                if(recipeList == null) return;
                if(adapter == null) return;

                adapter.setRecipesList(recipeList);

                /** Refresh If New Data Added: not used in current case **/
                CollectionAppWidgetProvider.sendRefreshWidgetBroadcast(getApplicationContext());
            }
        };
    }

    private Response.ErrorListener getResponseErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Response Error: " + error);
            }
        };
    }
}
