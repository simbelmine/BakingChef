package com.example.android.bakingchef.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.android.bakingchef.R;
import com.example.android.bakingchef.activities.DetailActivity;
import com.example.android.bakingchef.activities.MainActivity;

public class CollectionAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.collection_widget_layout);

            setRemoteAdapter(context, remoteViews);
            setOnClickItemEventHandler(context, remoteViews);
            addOnClickItemTemplate(context, remoteViews);

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

    private void setRemoteAdapter(Context context, RemoteViews remoteViews) {
        Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
        remoteViews.setRemoteAdapter(R.id.widget_stack_view, serviceIntent);
    }

    private void setOnClickItemEventHandler(Context context, RemoteViews remoteViews) {
        Intent clickIntent = new Intent(context, MainActivity.class);
        PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_item_container, clickPendingIntent);
    }

    private void addOnClickItemTemplate(Context context, RemoteViews remoteViews) {
        Intent clickIntentTemplate = new Intent(context, DetailActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_stack_view, clickPendingIntentTemplate);
    }
}
