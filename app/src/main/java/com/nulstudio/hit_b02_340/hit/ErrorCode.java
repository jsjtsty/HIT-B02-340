package com.nulstudio.hit_b02_340.hit;

public final class ErrorCode {
    private ErrorCode() {}

    public static final int HIT_ERR_OK = 0;
    public static final int HIT_ERR_SCRIPT_ERROR = 1;
    public static final int HIT_ERR_INVALID_REQUEST = 2;
    public static final int HIT_ERR_PRIORITY = 3;
    public static final int HIT_ERR_LOW_PROTOCOL_VERSION = 5;
    public static final int HIT_ERR_HIGH_PROTOCOL_VERSION = 6;
    public static final int HIT_ERR_UNSUPPORTED_VERSION = 7;
    public static final int HIT_ERR_INVALID_SQL_CONNECTION = 10;
    public static final int HIT_ERR_SQL_EXCEPTION = 11;
    public static final int HIT_ERR_INVALID_USERNAME = 20;
    public static final int HIT_ERR_USER_NOT_EXIST = 21;
    public static final int HIT_ERR_WRONG_PASSWORD = 22;
    public static final int HIT_ERR_INVALID_TOKEN = 23;
    public static final int HIT_ERR_USER_ALREADY_EXIST = 24;
    public static final int HIT_ERR_INVALID_INVITE_CODE = 30;
    public static final int HIT_ERR_INVITE_CODE_USED_UP = 31;
    public static final int HIT_ERR_INVITE_CODE_BLOCKED = 32;
    public static final int HIT_ERR_ALREADY_CLOCKED = 100;

}
