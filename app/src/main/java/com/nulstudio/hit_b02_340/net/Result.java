package com.nulstudio.hit_b02_340.net;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Result {
    protected int version;
    protected int status;
    protected String errmsg;
    protected ResultAttribute res;
    protected JSONObject origin;

    public Result(JSONObject res) throws JSONException {
        this.version = res.getInt("version");
        this.status = res.getInt("status");
        this.errmsg = res.getString("errmsg");
        if(!res.isNull("res")) {
            this.res = new ResultAttribute(res.getJSONObject("res"));
        }
        this.origin = res;
    }

    public int getVersion() {
        return version;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errmsg;
    }

    public ResultAttribute getRes() {
        return res;
    }

    @NonNull
    @Override
    public String toString() {
        return origin.toString();
    }
}
