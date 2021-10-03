package com.nulstudio.hit_b02_340.mgr;

import android.content.Context;
import android.content.SharedPreferences;

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
import com.nulstudio.hit_b02_340.net.Token;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public final class AccountManager {
    private static final AccountManager instance = new AccountManager();

    private AccountManager() {}

    private Token token = null;
    private String userName = null;
    private int uid = 0;
    private int priority = 0;
    private boolean isLoggedIn = false;

    public Token getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public int getUid() {
        return uid;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public int getPriority() {
        return priority;
    }

    public static AccountManager getInstance() {
        return instance;
    }

    protected void saveInstance(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.key_preference_file),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token.getToken());
        editor.putString("userName", userName);
        editor.putInt("uid", uid);
        editor.putInt("priority", priority);
        editor.putBoolean("isLoginSaved", true);
        editor.apply();
    }

    public boolean readInstance(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(context.getString(R.string.key_preference_file), Context.MODE_PRIVATE);
        boolean isLoginSaved = preferences.getBoolean("isLoginSaved", false);
        if(!isLoginSaved) {
            return false;
        }
        userName = preferences.getString("userName", null);
        if(userName == null) {
            return false;
        }
        String strToken = preferences.getString("token", null);
        if(strToken == null) {
            return false;
        }
        token = new Token(strToken);
        uid = preferences.getInt("uid", 0);
        if(uid == 0) {
            return false;
        }
        priority = preferences.getInt("priority", -1);
        if(priority == -1) {
            return false;
        }
        isLoggedIn = true;
        return true;
    }

    public AccountManager login(Context context, String userName, String hashPass)
            throws NulRuntimeException, IOException, JSONException {
        return login(context, new Token(userName, hashPass));
    }

    public AccountManager login(Context context, Token token)
            throws NulRuntimeException, IOException, JSONException {
        Request request = new Request(token.getToken(), NulActions.HIT_ACT_LOGIN,
                NulActions.HIT_ACT_ATTR_LOGIN_LOGIN);
        Result result = null;
        try {
            result = request.sendRequest(new URL(Environment.HIT_ENV_LOGIN_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert result != null;

        if(result.getStatus() != ErrorCode.HIT_ERR_OK) {
            switch (result.getStatus()) {
                case ErrorCode.HIT_ERR_USER_NOT_EXIST:
                    throw new LoginException(context.getString(R.string.err_user_not_exist));
                case ErrorCode.HIT_ERR_WRONG_PASSWORD:
                    throw new LoginException(context.getString(R.string.err_wrong_password));
                default:
                    throw new NulRuntimeException(result.getErrorMessage());
            }
        }

        ResultAttribute info = result.getRes();
        uid = info.getInt("uid");
        priority = info.getInt("priority");
        this.userName = userName;
        isLoggedIn = true;
        this.token = token;
        this.saveInstance(context);
        return this;
    }

    public AccountManager register(Context context, String userName, String hashPass,
                                   String inviteCode) throws IOException, JSONException {
        Token token = new Token(userName, hashPass);
        Request request = new Request(token.getToken(), NulActions.HIT_ACT_LOGIN,
                NulActions.HIT_ACT_ATTR_LOGIN_REGISTER,
                new RequestAttribute().put("invite_code", inviteCode));
        Result result = null;
        try {
            result = request.sendRequest(new URL(Environment.HIT_ENV_LOGIN_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert result != null;

        if(result.getStatus() != ErrorCode.HIT_ERR_OK) {
            switch (result.getStatus()) {
                case ErrorCode.HIT_ERR_INVALID_INVITE_CODE:
                    throw new LoginException(context.getString(R.string.err_invalid_invite_code));
                case ErrorCode.HIT_ERR_INVITE_CODE_USED_UP:
                    throw new LoginException(context.getString(R.string.err_invite_code_used_up));
                case ErrorCode.HIT_ERR_INVITE_CODE_BLOCKED:
                    throw new LoginException(context.getString(R.string.err_invite_code_blocked));
                case ErrorCode.HIT_ERR_USER_ALREADY_EXIST:
                    throw new LoginException(context.getString(R.string.err_user_already_exist));
                default:
                    throw new NulRuntimeException(result.getErrorMessage());
            }
        }

        ResultAttribute info = result.getRes();
        uid = info.getInt("uid");
        priority = info.getInt("priority");
        this.userName = userName;
        isLoggedIn = true;
        this.token = token;
        this.saveInstance(context);
        return this;
    }
}
