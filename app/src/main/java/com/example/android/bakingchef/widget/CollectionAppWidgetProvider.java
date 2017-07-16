package com.example.android.bakingchef.widget;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingchef.R;

public class CollectionAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.collection_widget_layout);
            Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);

            remoteViews.setRemoteAdapter(R.id.widget_stack_view, serviceIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void sendRefreshWidgetBroadcast(Context context) {
        Intent refreshIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        refreshIntent.setComponent(new ComponentName(context, CollectionAppWidgetProvider.class));
        context.sendBroadcast(refreshIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, CollectionAppWidgetProvider.class);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName),R.id.widget_stack_view);
        }

        super.onReceive(context, intent);
    }
}
