package com.example.android.bakingchef.helpers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.android.bakingchef.AppController;
import com.example.android.bakingchef.activities.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class DataHelper {
    public static Object jsonToCollection(String json, Type type) {
        if(TextUtils.isEmpty(json)) return null;
        json = fixJson(json);
        return new GsonBuilder().create().fromJson(json, type);
    }

    private static String fixJson(String json) {
        /** Fix for wrong character */
        /** Couldn't find better way */
        return json.replaceAll("\uFFFD", "\u00b0");
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

    public static void makeJsonRequest(String url, Response.Listener<JSONArray> responseListener, Response.ErrorListener responseErrorListener) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                responseListener,
                responseErrorListener
        );

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
