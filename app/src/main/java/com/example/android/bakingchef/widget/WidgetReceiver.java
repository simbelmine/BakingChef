package com.example.android.bakingchef.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingchef.R;
import com.example.android.bakingchef.models.Recipe;

import java.util.ArrayList;

import static com.example.android.bakingchef.widget.WidgetProvider.NEXT_WIDGET_ACTION;
import static com.example.android.bakingchef.widget.WidgetProvider.PREV_WIDGET_ACTION;
import static com.example.android.bakingchef.widget.WidgetProvider.RECIPE;


public class WidgetReceiver extends BroadcastReceiver {
    public static int clickCount = 0;
    private ArrayList<Recipe> recipeList;

    @Override
    public void onReceive(Context context, Intent intent) {
        recipeList = intent.getParcelableArrayListExtra(RECIPE);
        String action = intent.getAction();

        switch (action) {
            case NEXT_WIDGET_ACTION:
                updateWidgetPictureAndButtonListener(context, action);
                break;
            case PREV_WIDGET_ACTION:
                updateWidgetPictureAndButtonListener(context, action);
                break;
            default:
                updateWidgetPictureAndButtonListener(context, "");
                break;
        }
    }

    private void updateWidgetPictureAndButtonListener(Context context, String action) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);



        if(recipeList != null && clickCount < recipeList.size()) {
            // re-registering for click listener
            if(action.equals(NEXT_WIDGET_ACTION))
                remoteViews.setOnClickPendingIntent(R.id.next_button,
                        WidgetProvider.buildButtonNextPendingIntent(context));
            if(action.equals(PREV_WIDGET_ACTION))
                remoteViews.setOnClickPendingIntent(R.id.prev_button,
                        WidgetProvider.buildButtonPrevPendingIntent(context));

            if(recipeList != null) {
                if(clickCount > recipeList.size()-1)
                    clickCount = 0;
                if(clickCount < 0)
                    clickCount = recipeList.size()-1;
            }

            // updating view
            Recipe recipe = recipeList.get(clickCount);
            remoteViews.setTextViewText(R.id.title, WidgetProvider.underlineText(recipe.getName()));
            remoteViews.setTextViewText(R.id.desc, WidgetProvider.loadAllIngredients(clickCount));
            WidgetProvider.loadImage(context, remoteViews, recipe.getImage());


            remoteViews.setOnClickPendingIntent(R.id.title,
                    WidgetProvider.buildButtonGoToPendingIntent(context, recipe));

            WidgetProvider.pushWidgetUpdate(context.getApplicationContext(),
                    remoteViews);

        }
    }
}
