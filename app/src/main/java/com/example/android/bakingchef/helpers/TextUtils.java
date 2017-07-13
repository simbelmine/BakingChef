package com.example.android.bakingchef.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.Html;
import android.widget.TextView;

import com.example.android.bakingchef.DetailActivity;
import com.example.android.bakingchef.R;

public class TextUtils {
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

    public static String getTextFromHTML(String source) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString();
        else
            return Html.fromHtml(source).toString();
    }
}
