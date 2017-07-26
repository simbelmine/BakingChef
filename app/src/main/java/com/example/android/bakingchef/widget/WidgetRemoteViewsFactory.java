package com.example.android.bakingchef.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingchef.R;
import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.helpers.DataHelper;
import com.example.android.bakingchef.models.Ingredient;
import com.example.android.bakingchef.models.Recipe;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private ArrayList<Recipe> recipeList;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        String recipeJSON = DataHelper.loadJSONfromAssets(context);
        Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        recipeList = (ArrayList<Recipe>) DataHelper.jsonToCollection(recipeJSON, type);
    }

    @Override
    public int getCount() {
        return (recipeList == null || recipeList.size() == 0) ? 0 : recipeList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(recipeList == null || recipeList.size() == 0 || (position >= recipeList.size() || position < 0))
            return null;

        Recipe recipe = recipeList.get(position);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.collection_widget_list_item);


        rv.setTextViewText(R.id.widget_item_name, recipe.getName());
        loadImage(rv, recipe.getImage());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(DetailActivity.WIDGET_RECIPE, recipe);
        rv.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);

        return rv;
    }

    @Override
    public long getItemId(int position) {
        return (recipeList == null || recipeList.size() == 0) ? position : recipeList.get(position).getId();
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onDestroy() {

    }

    private void loadImage(RemoteViews rv, String url) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.baking);
        if(!TextUtils.isEmpty(url)) {
            try {
                bitmap = Picasso.with(context).load(url).get();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            rv.setImageViewBitmap(R.id.widget_item_img, bitmap);
        }
        else {
            rv.setImageViewResource(R.id.widget_item_img, R.drawable.baking);
        }
    }
}
