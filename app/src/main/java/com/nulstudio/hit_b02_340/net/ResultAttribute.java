package com.nulstudio.hit_b02_340.net;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultAttribute {
    JSONObject object;

    public ResultAttribute(JSONObject object) {
        this.object = object;
    }

    public String getString(String name) throws JSONException {
        return object.getString(name);
    }

    public int getInt(String name) throws JSONException {
        return object.getInt(name);
    }

    public long getLong(String name) throws JSONException {
        return object.getLong(name);
    }

    public double getDouble(String name) throws JSONException {
        return object.getDouble(name);
    }

    public boolean getBoolean(String name) throws JSONException {
        return object.getBoolean(name);
    }

    public JSONObject getObject(String name) throws JSONException {
        return object.getJSONObject(name);
    }

    @NonNull
    @Override
    public String toString() {
        return object.toString();
    }
}
