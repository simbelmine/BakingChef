package com.example.android.bakingchef.helpers;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import com.example.android.bakingchef.R;

/**
 * Created by Sve on 7/9/17.
 */

public class TextUtils {
    public static void setTextStyle(Context context, TextView textView, boolean isHeader) {
        setTextAppearance(context, textView, isHeader);
        textView.setPadding(0, 0, 0, (int)context.getResources().getDimension(R.dimen.standard_padding));
    }

    private static void setTextAppearance(Context context, TextView textView, boolean isHeader) {
        if(Build.VERSION.SDK_INT < 23) {
            if(isHeader) {
                textView.setTextAppearance(context, R.style.TextLarge);
            }
            else {
                textView.setTextAppearance(context, R.style.TextMedium);
            }
        }
        else {
            if(isHeader) {
                textView.setTextAppearance(R.style.TextLarge);
            }
            else {
                textView.setTextAppearance(R.style.TextMedium);
            }
        }
    }
}
