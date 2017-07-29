package com.example.android.bakingchef.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingchef.R;

public class ListFactory implements RemoteViewsService.RemoteViewsFactory {
    private static String[] list;
    private Context context;

    public ListFactory(Context context, Intent intent) {
        this.context = context;

        String ingredientsStr = intent.getStringExtra(WidgetProvider.INGREDIENTS);
        updateList(ingredientsStr);
    }

    public static void updateList(String newList) {
        if(newList == null) return;
        list = newList.split("#");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int getCount() {
        if(list == null || list.length == 0)
            return 0;
        else
            return list.length;
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_item);

        if(position >= 0 && position < list.length) {
            String s = list[position];
            remoteView.setTextViewText(R.id.widget_item_content, s);
        }

        return remoteView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
