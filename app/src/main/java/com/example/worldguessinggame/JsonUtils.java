package com.example.worldguessinggame;

import android.content.Context;

import com.google.gson.Gson;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonUtils {

    public static List<String> readNamesFromAssets(Context context) throws Exception {

        InputStream is = context.getAssets().open("names.json");
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();

        String json = new String(buffer, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        NameListJson data = gson.fromJson(json, NameListJson.class);

        return data.names;
    }
}