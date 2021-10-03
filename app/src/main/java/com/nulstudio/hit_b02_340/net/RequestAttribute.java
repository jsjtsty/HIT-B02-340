package com.nulstudio.hit_b02_340.net;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestAttribute {
    JSONObject object;

    public RequestAttribute() {
        object = new JSONObject();
    }

    public RequestAttribute put(String name, int value) throws JSONException {
        object.put(name, value);
        return this;
    }

    public RequestAttribute put(String name, long value) throws JSONException {
        object.put(name, value);
        return this;
    }

    public RequestAttribute put(String name, double value) throws JSONException {
        object.put(name, value);
        return this;
    }

    public RequestAttribute put(String name, boolean value) throws JSONException {
        object.put(name, value);
        return this;
    }

    public RequestAttribute put(String name, Object value) throws JSONException {
        object.put(name, value);
        return this;
    }

    public JSONObject toJsonObject() {
        return object;
    }

    @NonNull
    @Override
    public String toString() {
        return object.toString();
    }
}
