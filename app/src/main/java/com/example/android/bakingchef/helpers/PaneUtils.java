package com.example.android.bakingchef.helpers;

import android.app.Activity;
import android.content.res.Configuration;

import com.example.android.bakingchef.R;

public class PaneUtils {
    public static boolean isTwoPane(Activity activity) {
        return activity.findViewById(R.id.steps_container) != null;
    }
    public static boolean isTablet(Activity activity) {
        return activity.getResources().getBoolean(R.bool.is_tablet);
    }

    public  static boolean isLandscape(Activity activity) {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
