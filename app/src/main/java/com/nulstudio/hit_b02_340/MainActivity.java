package com.nulstudio.hit_b02_340;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.FragmentTransaction;

import com.nulstudio.hit_b02_340.common.Environment;
import com.nulstudio.hit_b02_340.common.Strings;
import com.nulstudio.hit_b02_340.exception.NulRuntimeException;
import com.nulstudio.hit_b02_340.mgr.AccountManager;
import com.nulstudio.hit_b02_340.mgr.UpdateManager;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView versionView = findViewById(R.id.tv_main_version);
        try {
            versionView.setText("版本 " + this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        CountDownTimer timer = new CountDownTimer(3000, 3000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        };

        timer.start();

        Environment.HIT_ENV_FILES_DIR = this.getFilesDir().toString();
        Environment.HIT_ENV_EXTERNAL_FILES_DIR = this.getExternalFilesDir("").toString();

        Strings.initInstance(this);

        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

        HitApplication.executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    UpdateManager.Release release_var = null;
                    if (AccountManager.getInstance().readInstance(MainActivity.this)) {
                        Log.d("Test", "With Token.");
                        release_var = UpdateManager.getInstance().checkForUpdate();
                    }
                    else {
                        Log.d("Test", "Without Token.");
                        release_var = UpdateManager.getInstance().checkForUpdateWithoutToken();
                    }

                    Log.d("Hit340", "1");

                    if(release_var != null) {
                        Log.d("Hit340", "2");
                        final UpdateManager.Release release = release_var;
                        int build = MainActivity.this.getPackageManager()
                                .getPackageInfo(getPackageName(), 0).versionCode;
                        if(build < release.build) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    timer.cancel();

                                    final boolean[] isDownloadComplete = {false};
                                    final boolean[] isRegistered = {false};

                                    View view = MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_update, null);
                                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, R.style.TransparentDialog)
                                            .setView(view).create();

                                    Button updateButton = view.findViewById(R.id.btn_update);
                                    Button dismissButton = view.findViewById(R.id.btn_update_cancel);

                                    updateButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            long upId = UpdateManager.getInstance().downloadUpdate(MainActivity.this);
                                            final DownloadManager downloadManager = ((DownloadManager)MainActivity.this.getSystemService(Context.DOWNLOAD_SERVICE));
                                            final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
                                                @SuppressLint("DefaultLocale")
                                                @Override
                                                public void onReceive(Context context, Intent intent) {
                                                    DownloadManager.Query query = new DownloadManager.Query();
                                                    query.setFilterById(upId);
                                                    Cursor cursor = downloadManager.query(query);

                                                    if (cursor.moveToFirst()) {
                                                        @SuppressLint("Range")
                                                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                                                        switch (status) {
                                                            case DownloadManager.STATUS_SUCCESSFUL:
                                                                isDownloadComplete[0] = true;
                                                                Intent intentInstall = new Intent(Intent.ACTION_VIEW);
                                                                File file = new File(MainActivity.this.getExternalFilesDir("update"), "update-" + release.build + ".apk");
                                                                Uri apkUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName(), file);
                                                                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                                intentInstall.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                                                intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                context.startActivity(intentInstall);
                                                                cursor.close();
                                                                break;
                                                            case DownloadManager.STATUS_FAILED:
                                                                cursor.close();
                                                                break;
                                                        }
                                                    }
                                                }
                                            };

                                            Timer timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @SuppressLint("DefaultLocale")
                                                @Override
                                                public void run() {
                                                    if(isDownloadComplete[0]) {
                                                        timer.cancel();
                                                        dismissButton.setText("100%");
                                                    }
                                                    else {
                                                        DownloadManager.Query query = new DownloadManager.Query();
                                                        query.setFilterById(upId);
                                                        Cursor cursor = downloadManager.query(query);
                                                        if(cursor.moveToFirst()) {
                                                            int downloadBytesIdx = cursor.getColumnIndexOrThrow(
                                                                    DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                                                            int totalBytesIdx = cursor.getColumnIndexOrThrow(
                                                                    DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                                                            long totalBytes = cursor.getLong(totalBytesIdx);
                                                            long downloadBytes = cursor.getLong(downloadBytesIdx);
                                                            double percent = (double) downloadBytes / totalBytes * 100;
                                                            if(percent <= 0.0) {
                                                                percent = 0.0001;
                                                            }
                                                            dismissButton.setText(String.format("%.0f%%", percent));
                                                        }
                                                    }
                                                }
                                            }, 500, 500);

                                            if(!isRegistered[0]) {
                                                MainActivity.this.registerReceiver(downloadReceiver,
                                                        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                                isRegistered[0] = true;
                                            }
                                            updateButton.setText(R.string.txt_update_downloading);
                                            updateButton.setEnabled(false);
                                            dismissButton.setText("0%");
                                        }
                                    });

                                    dismissButton.setOnClickListener                                                                                                                                                                                                                                                                                                                                               (new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                            timer.start();
                                        }
                                    });

                                    DisplayMetrics metrics = new DisplayMetrics();
                                    ((WindowManager)MainActivity.this.getSystemService(Context.WINDOW_SERVICE))
                                            .getDefaultDisplay().getMetrics(metrics);

                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                    dialog.getWindow().setLayout((metrics.widthPixels * 3 / 4), metrics.heightPixels * 4 / 7);

                                    TextView title = view.findViewById(R.id.tv_update_title),
                                            size = view.findViewById(R.id.tv_update_size),
                                            features = view.findViewById(R.id.tv_update_attr);

                                    title.setText(String.format(MainActivity.this.getString(R.string.txt_update),
                                            release.versionString));
                                    size.setText(String.format(MainActivity.this.getString(R.string.txt_update_size),
                                            release.size / 1024 / 1024));
                                    features.setText(String.format(MainActivity.this.getString(R.string.txt_update_attr),
                                            release.description));

                                    RelativeLayout layout = view.findViewById(R.id.layout_update_main);
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
                                    params.topMargin = metrics.heightPixels / 5;
                                    layout.setLayoutParams(params);
                                }
                            });
                        }
                    }
                }
                catch (NulRuntimeException exception) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "NulRuntimeException: " + exception.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        Window window = getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}