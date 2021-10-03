package com.nulstudio.hit_b02_340.net;

public final class NulActions {
    private NulActions() {}

    public static final int HIT_ACT_LOGIN = 1;
    public static final int HIT_ACT_ATTR_LOGIN_LOGIN = 100;
    public static final int HIT_ACT_ATTR_LOGIN_REGISTER = 101;

    public static final int HIT_ACT_JB = 2;
    public static final int HIT_ACT_ATTR_JB_FETCH = 200;
    public static final int HIT_ACT_ATTR_JB_ADD = 201;
    public static final int HIT_ACT_ATTR_JB_USE = 202;
    public static final int HIT_ACT_ATTR_JB_UPDATE = 203;
    public static final int HIT_ACT_ATTR_JB_TRANSFER = 204;

    public static final int HIT_ACT_ACCOUNT = 3;
    public static final int HIT_ACT_ATTR_ACCOUNT_FETCH_INVITE_CODE = 300;
    public static final int HIT_ACT_ATTR_ACCOUNT_UPDATE_INVITE_CODE = 301;
    public static final int HIT_ACT_ATTR_ACCOUNT_ADD_INVITE_CODE = 302;
    public static final int HIT_ACT_ATTR_ACCOUNT_BLOCK_INVITE_CODE = 303;
    public static final int HIT_ACT_ATTR_ACCOUNT_FETCH_USER = 304;
    public static final int HIT_ACT_ATTR_ACCOUNT_FETCH_ALL_INVITE_CODE = 305;

    public static final int HIT_ACT_CLOCK = 4;
    public static final int HIT_ACT_ATTR_CLOCK_CLOCK = 400;
    public static final int HIT_ACT_ATTR_CLOCK_FETCH_CLOCK = 401;

    public static final int HIT_ACT_UPDATE = 5;
    public static final int HIT_ACT_ATTR_UPDATE_FETCH_CUR_CHANNEL = 500;
    public static final int HIT_ACT_ATTR_UPDATE_FETCH_CUR_UPDATE = 501;
    public static final int HIT_ACT_ATTR_UPDATE_FETCH_UPDATE_WITHOUT_TOKEN = 502;
    public static final int HIT_ACT_ATTR_UPDATE_FETCH_UPDATE = 503;
    public static final int HIT_ACT_ATTR_UPDATE_CHANGE_CUR_CHANNEL = 504;
    public static final int HIT_ACT_ATTR_UPDATE_CHANGE_CHANNEL = 505;
}
