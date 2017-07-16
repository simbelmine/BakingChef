package com.example.android.bakingchef.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingchef.R;
import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.helpers.DataHelper;
import com.example.android.bakingchef.models.Recipe;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(DetailActivity.EXTRA_LABEL, recipe.getName());
        rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

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
}
