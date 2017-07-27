package com.example.android.bakingchef.helpers;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import com.example.android.bakingchef.R;
import com.example.android.bakingchef.activities.DetailActivity;

public class MyTextUtils {
    public static void setTextStyle(Context context, TextView textView, String appearance) {
        setTextAppearance(context, textView, appearance);
        textView.setPadding(0, 0, 0, (int)context.getResources().getDimension(R.dimen.standard_padding));
    }

    private static void setTextAppearance(Context context, TextView textView, String appearance) {
        if(Build.VERSION.SDK_INT < 23) {
            switch (appearance) {
                case DetailActivity.SMALL_APPEARANCE:
                    textView.setTextAppearance(context, R.style.TextSmall);
                    break;
                case DetailActivity.MEDIUM_APPEARANCE:
                    textView.setTextAppearance(context, R.style.TextMedium);
                    break;
                case DetailActivity.LARGE_APPEARANCE:
                    textView.setTextAppearance(context, R.style.TextLarge);
                    break;
            }
        }
        else {
            switch (appearance) {
                case DetailActivity.SMALL_APPEARANCE:
                    textView.setTextAppearance(R.style.TextSmall);
                    break;
                case DetailActivity.MEDIUM_APPEARANCE:
                    textView.setTextAppearance(R.style.TextMedium);
                    break;
                case DetailActivity.LARGE_APPEARANCE:
                    textView.setTextAppearance(R.style.TextLarge);
                    break;
            }
        }
    }
}
