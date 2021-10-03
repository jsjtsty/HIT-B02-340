package com.nulstudio.hit_b02_340.net;

import com.nulstudio.hit_b02_340.hit.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class Request {
    protected final int version = Constants.HIT_PTL_VERSION_SERVER;
    protected String token;
    protected int action;
    protected int attr;
    protected RequestAttribute request;

    public Request(String token, int action, int attr) {
        this.token = token;
        this.action = action;
        this.attr = attr;
        this.request = new RequestAttribute();
    }

    public Request(String token, int action, int attr, RequestAttribute request) {
        this.token = token;
        this.action = action;
        this.attr = attr;
        this.request = request;
    }

    public void setAttribute(RequestAttribute attribute) {
        this.request = attribute;
    }

    public Result sendRequest(URL url) throws IOException, JSONException {
        return sendRequest(url, 15000);
    }

    public Result sendRequest(URL url, int timeout) throws IOException, JSONException {
        NetRequest net = new NetRequest(url);
        JSONObject root = new JSONObject();

        root.put("version", Request.this.version);
        root.put("token", Request.this.token);
        root.put("action", Request.this.action);
        root.put("attr", Request.this.attr);
        if (Request.this.request == null) {
            root.put("request", 0);
        } else {
            root.put("request", Request.this.request.toJsonObject());
        }
        String resStr = net.post(root.toString(), timeout);
        JSONObject res = new JSONObject(resStr);

        return new Result(res);
    }
}
