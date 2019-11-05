package com.orhanobut.hawk;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonParser implements Parser {

    private final Gson mGson;

    public GsonParser(GsonBuilder gsonBuilder) {
        mGson = gsonBuilder.create();
    }

    public GsonParser(Gson gson) {
        mGson = gson;
    }

    @Override
    public <T> T fromJson(String content, Type type) throws JsonSyntaxException {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        return mGson.fromJson(content, type);
    }

    @Override
    public String toJson(Object body) {
        return mGson.toJson(body);
    }

    protected Gson getGson() {
        return mGson;
    }

}
