package com.nulstudio.hit_b02_340.mgr;

import android.content.Context;

import com.nulstudio.hit_b02_340.common.Environment;
import com.nulstudio.hit_b02_340.exception.NulRuntimeException;
import com.nulstudio.hit_b02_340.hit.ErrorCode;
import com.nulstudio.hit_b02_340.net.NulActions;
import com.nulstudio.hit_b02_340.net.Request;
import com.nulstudio.hit_b02_340.net.RequestAttribute;
import com.nulstudio.hit_b02_340.net.Result;
import com.nulstudio.hit_b02_340.net.ResultAttribute;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ClockManager {
    boolean clock = false;

    private static final ClockManager instance = new ClockManager();

    public boolean isClockAvailable() {
        return clock;
    }

    public static ClockManager getInstance() {
        return instance;
    }

    public boolean syncClock() throws JSONException, IOException, NulRuntimeException {
        Request request = new Request(AccountManager.getInstance().getToken().toString(),
                NulActions.HIT_ACT_CLOCK, NulActions.HIT_ACT_ATTR_CLOCK_FETCH_CLOCK);
        Result result = null;
        try {
            result = request.sendRequest(new URL(Environment.HIT_ENV_CLOCK_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert result != null;
        if(result.getStatus() != ErrorCode.HIT_ERR_OK) {
            throw new NulRuntimeException(result.getErrorMessage());
        }

        ResultAttribute attr = result.getRes();
        clock = attr.getBoolean("avail");
        return clock;
    }

    public boolean clock() throws JSONException, IOException, NulRuntimeException {
        if(!clock) {
            return false;
        }

        Request request = new Request(AccountManager.getInstance().getToken().toString(),
                NulActions.HIT_ACT_CLOCK, NulActions.HIT_ACT_ATTR_CLOCK_CLOCK);
        Result result = null;
        try {
            result = request.sendRequest(new URL(Environment.HIT_ENV_CLOCK_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert result != null;
        if(result.getStatus() != ErrorCode.HIT_ERR_OK) {
            throw new NulRuntimeException(result.getErrorMessage());
        }

        ResultAttribute attr = result.getRes();
        JBManager.getInstance().setJB(attr.getInt("jb"));
        return true;
    }
}
