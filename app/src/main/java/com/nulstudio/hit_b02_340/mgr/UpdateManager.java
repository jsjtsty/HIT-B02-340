package com.nulstudio.hit_b02_340.mgr;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.nulstudio.hit_b02_340.R;
import com.nulstudio.hit_b02_340.common.Environment;
import com.nulstudio.hit_b02_340.common.Strings;
import com.nulstudio.hit_b02_340.exception.NulRuntimeException;
import com.nulstudio.hit_b02_340.hit.ErrorCode;
import com.nulstudio.hit_b02_340.net.NulActions;
import com.nulstudio.hit_b02_340.net.Request;
import com.nulstudio.hit_b02_340.net.RequestAttribute;
import com.nulstudio.hit_b02_340.net.Result;
import com.nulstudio.hit_b02_340.net.ResultAttribute;
import com.nulstudio.hit_b02_340.net.Token;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public final class UpdateManager {
    private static final UpdateManager instance = new UpdateManager();

    public static final int HIT_UPD_RELEASE_RELEASE = 0;
    public static final int HIT_UPD_RELEASE_RC = 1;
    public static final int HIT_UPD_RELEASE_BETA = 2;
    public static final int HIT_UPD_RELEASE_ALPHA = 3;
    public static final int HIT_UPD_RELEASE_INSIDER_PREVIEW = 4;
    public static final int HIT_UPD_RELEASE_TECHNICAL_PREVIEW = 5;
    public static final int HIT_UPD_RELEASE_DEVELOPMENT = 6;

    public static final int HIT_UPD_CHANNEL_RELEASE = 0;
    public static final int HIT_UPD_CHANNEL_RC = 1;
    public static final int HIT_UPD_CHANNEL_BETA = 2;
    public static final int HIT_UPD_CHANNEL_ALPHA = 3;
    public static final int HIT_UPD_CHANNEL_INSIDER_PREVIEW = 4;
    public static final int HIT_UPD_CHANNEL_TECHNICAL_PREVIEW = 5;
    public static final int HIT_UPD_CHANNEL_DEVELOPMENT = 6;

    public static class Release {
        public int build;
        public int releaseLevel;
        public String updateUrl;
        public String versionString;
        public String description;
        public int size;

        public Release(int build, int releaseLevel, String updateUrl, String versionString, int size, String description) {
            this.build = build;
            this.releaseLevel = releaseLevel;
            this.updateUrl = updateUrl;
            this.versionString = versionString;
            this.size = size;
            this.description = description;
        }
    }

    private Release lastRelease = null;

    private UpdateManager() {

    }

    public static UpdateManager getInstance() {
        return instance;
    }

    public int getReleaseChannel() throws IOException, JSONException {
        Request request = new Request(AccountManager.getInstance().getToken().toString(), NulActions.HIT_ACT_UPDATE,
                NulActions.HIT_ACT_ATTR_UPDATE_FETCH_CUR_CHANNEL);

        Result result = null;

        try {
            result = request.sendRequest(new URL(Environment.HIT_ENV_UPDATE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert result != null;

        ResultAttribute resultAttribute = result.getRes();
        return resultAttribute.getInt("channel");
    }

    public Release checkForUpdateWithoutToken() throws JSONException, IOException {
        Request request = new Request("", NulActions.HIT_ACT_UPDATE,
                NulActions.HIT_ACT_ATTR_UPDATE_FETCH_UPDATE_WITHOUT_TOKEN);

        Result result = null;

        try {
            result = request.sendRequest(new URL(Environment.HIT_ENV_UPDATE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert result != null;

        if(result.getRes() == null) {
            return null;
        }

        ResultAttribute resultAttribute = result.getRes();

        this.lastRelease = new Release(resultAttribute.getInt("build"),
                resultAttribute.getInt("release_level"),
                resultAttribute.getString("update_url"),
                resultAttribute.getString("version_string"),
                resultAttribute.getInt("size"),
                resultAttribute.getString("description"));
        return this.lastRelease;
    }

    public Release checkForUpdate() throws NulRuntimeException, JSONException, IOException {
        Token token = AccountManager.getInstance().getToken();
        Request request = new Request(token.getToken(), NulActions.HIT_ACT_UPDATE,
                NulActions.HIT_ACT_ATTR_UPDATE_FETCH_CUR_UPDATE);

        Result result = null;

        try {
            result = request.sendRequest(new URL(Environment.HIT_ENV_UPDATE_URL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        assert result != null;

        if(result.getStatus() != ErrorCode.HIT_ERR_OK) {
            throw new NulRuntimeException(result.getErrorMessage());
        }

        if(result.getRes() == null) {
            return null;
        }

        ResultAttribute resultAttribute = result.getRes();

        this.lastRelease = new Release(resultAttribute.getInt("build"),
                resultAttribute.getInt("release_level"),
                resultAttribute.getString("update_url"),
                resultAttribute.getString("version_string"),
                resultAttribute.getInt("size"),
                resultAttribute.getString("description"));

        return this.lastRelease;
    }

    public long downloadUpdate(Context context) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.lastRelease.updateUrl));
        request.setAllowedOverRoaming(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(Strings.getInstance().HIT_STR_UPD_TITLE);
        request.setDescription(Strings.getInstance().HIT_STR_UPD_DESCRIPTION);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationUri(Uri.fromFile(new File(context.getExternalFilesDir("update"),
                "update-" + this.lastRelease.build + ".apk")));
        DownloadManager downloadManager =
                (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        return downloadManager.enqueue(request);
    }
}
