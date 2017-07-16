package com.example.android.bakingchef.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Sve on 7/14/17.
 */

public class WidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this, intent);
    }
}
