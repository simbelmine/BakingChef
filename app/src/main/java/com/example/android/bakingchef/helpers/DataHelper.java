package com.example.android.bakingchef.helpers;

import android.content.Context;
import android.util.Log;

import com.example.android.bakingchef.activities.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class DataHelper {
    public static Object jsonToCollection(String json, Type type) {
        if(json.isEmpty()) return null;
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public static String collectionToJson(Object collection) {
        Gson gson = new Gson();
        return gson.toJson(collection);
    }

    public static String loadJSONfromAssets(Context context) {
        String json;
        try {
            InputStream io = context.getAssets().open("baking.json");
            int size = io.available();
            byte[] buffer = new byte[size];
            io.read(buffer);
            io.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex) {
            Log.e(MainActivity.TAG, "DataHelper-loadJSONfromAssets: " + ex.getMessage());
            return null;
        }

        return json;
    }
}
