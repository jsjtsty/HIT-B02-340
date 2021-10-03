package com.nulstudio.hit_b02_340.common;

import android.content.Context;

import com.nulstudio.hit_b02_340.R;

public class Strings {
    private static Strings instance = null;

    private Strings(Context context) {
        HIT_STR_UPD_TITLE = context.getString(R.string.str_download_title);
        HIT_STR_UPD_DESCRIPTION = context.getString(R.string.str_download_description);
    }

    public static Strings initInstance(Context context) {
        instance = new Strings(context);
        return instance;
    }

    public static Strings getInstance() {
        return instance;
    }

    public String HIT_STR_UPD_TITLE;
    public String HIT_STR_UPD_DESCRIPTION;
}
