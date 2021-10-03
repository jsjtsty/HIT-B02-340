package com.nulstudio.hit_b02_340.mgr;

import android.content.Context;

import com.nulstudio.hit_b02_340.R;
import com.nulstudio.hit_b02_340.common.Environment;
import com.nulstudio.hit_b02_340.exception.LoginException;
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

public final class JBManager {
    private int JB = 0;

    private final static JBManager instance = new JBManager();

    public static JBManager getInstance() {
        return instance;
    }

    public int getJB() {
        return JB;
    }

    public void setJB(int jb) {
        JB = jb;
    }

    public int syncJB() throws IOException, JSONException {
        Request request = new Request(AccountManager.getInstance().getToken().toString(),
                NulActions.HIT_ACT_JB, NulActions.HIT_ACT_ATTR_JB_FETCH,
                new RequestAttribute().put("uid", AccountManager.getInstance().getUid()));
        Result result = null;
        try {
            result = request.sendRequest(new URL(Environment.HIT_ENV_JB_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert result != null;
        if(result.getStatus() != ErrorCode.HIT_ERR_OK) {
            throw new NulRuntimeException(result.getErrorMessage());
        }

        ResultAttribute attr = result.getRes();
        JB = attr.getInt("jb");
        return JB;
    }

    public static class TransferResult {
        public TransferResult(int remain, int destRemain, int transfer) {
            this.remain = remain;
            this.destRemain = destRemain;
            this.transfer = transfer;
        }

        public int remain;
        public int destRemain;
        public int transfer;
    }

    public TransferResult transfer(int count) {
        return null;
    }
}
