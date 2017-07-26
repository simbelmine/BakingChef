package com.example.android.bakingchef.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.TaskStackBuilder;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.bakingchef.R;
import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.activities.MainActivity;
import com.example.android.bakingchef.helpers.DataHelper;
import com.example.android.bakingchef.models.Ingredient;
import com.example.android.bakingchef.models.Recipe;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class New_WidgetProvider extends AppWidgetProvider {
    public static final String NEXT_WIDGET_ACTION = "com.example.android.intent.action.NEXT_WIDGET";
    public static final String PREV_WIDGET_ACTION = "com.example.android.intent.action.PREV_WIDGET";
    public static final String RECIPE = "RecipeData";
    private static ArrayList<Recipe> recipeList;
    private Context context;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        Response.Listener<JSONArray> responseListener = getResponseListener();
        Response.ErrorListener responseErrorListener = getResponseErrorListener();
        DataHelper.makeJsonRequest(MainActivity.URL, responseListener, responseErrorListener);
    }

    public static PendingIntent buildButtonNextPendingIntent(Context context) {
        ++New_WidgetReceiver.clickCount;
        return getButtonPendingIntent(context, NEXT_WIDGET_ACTION);
    }

    public static PendingIntent buildButtonPrevPendingIntent(Context context) {
        --New_WidgetReceiver.clickCount;
        return getButtonPendingIntent(context, PREV_WIDGET_ACTION);
    }

    private static PendingIntent getButtonPendingIntent(Context context, String action) {
        // initiate widget update request
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putParcelableArrayListExtra(RECIPE, recipeList);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent buildButtonGoToPendingIntent(Context context, Recipe recipe) {
        // initiate widget update request
        Intent goToIntent = new Intent(context, DetailActivity.class);
        goToIntent.putExtra(DetailActivity.WIDGET_RECIPE, recipe);

        return TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(goToIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, New_WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }


    private Response.Listener<JSONArray> getResponseListener() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String json = response.toString();
                Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                recipeList = (ArrayList<Recipe>) DataHelper.jsonToCollection(json, listType);

                // initializing widget layout
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                // register for button event
                remoteViews.setOnClickPendingIntent(R.id.next_button, buildButtonNextPendingIntent(context));
                remoteViews.setOnClickPendingIntent(R.id.prev_button, buildButtonPrevPendingIntent(context));
                int idx = New_WidgetReceiver.clickCount < 0 ||
                        (recipeList!= null && New_WidgetReceiver.clickCount >= recipeList.size()) ? 0 : New_WidgetReceiver.clickCount;
                remoteViews.setOnClickPendingIntent(R.id.title, buildButtonGoToPendingIntent(context, recipeList.get(idx)));

                if(recipeList != null) {
                    // updating view with initial data
                    Recipe recipe = recipeList.get(0);

                    remoteViews.setTextViewText(R.id.title, underlineText(recipe.getName()));
                    remoteViews.setTextViewText(R.id.desc, loadAllIngredients(0));
                    loadImage(context, remoteViews, recipe.getImage());
                }

                // request for widget update
                pushWidgetUpdate(context, remoteViews);
            }
        };
    }

    private Response.ErrorListener getResponseErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(MainActivity.TAG, "Volley Response Error: " + error);
            }
        };
    }

    public static String loadAllIngredients(int idx) {
        List<Ingredient> ingredients = recipeList.get(idx).getIngredients();
        StringBuilder sb = new StringBuilder();

        for(Ingredient i : ingredients) {
            sb.append(i.getIngredient() + "\n");
        }

        return sb.toString();
    }

    public static SpannableString underlineText(String str) {
        SpannableString content = new SpannableString(str);
        content.setSpan(new UnderlineSpan(), 0, str.length(), 0);
        return content;
    }


    public static void loadImage(Context context, final RemoteViews rv, String url) {
//        url = "https://www.adherecreative.com/hs-fs/hub/105208/file-16346453-jpeg/images/twitter-small-business-marketing.jpeg";
        if(!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    rv.setImageViewBitmap(R.id.image, bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        }
        else {
            rv.setImageViewResource(R.id.image, R.drawable.baking);
        }
    }
}
