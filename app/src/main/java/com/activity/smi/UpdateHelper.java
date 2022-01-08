package com.activity.smi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by INDID on 04-04-2018.
 */

public class UpdateHelper {
    private static final String KEY_UPDATE_ENABLE = "is_update";
    private static final String KEY_UPDATE_VERSION = "version";
    private static final String KEY_UPDATE_URL = "update_url";
    private OnUpdateCheckListener onUpdateCheckListener;
    private Context context;

    //constructor of UpdateHelper
    private UpdateHelper(Context context, OnUpdateCheckListener onUpdateCheckListener) {
        this.onUpdateCheckListener = onUpdateCheckListener;
        this.context = context;
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public void check() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        Log.e("Bool Update helper ", String.valueOf(remoteConfig.getBoolean(KEY_UPDATE_ENABLE)));

        if (remoteConfig.getBoolean(KEY_UPDATE_ENABLE)) {
            String currentVersion = remoteConfig.getString(KEY_UPDATE_VERSION);
            String appVersion = getAppVersion(context);
            String updateURL = remoteConfig.getString(KEY_UPDATE_URL);

            if (!TextUtils.equals(currentVersion, appVersion) && onUpdateCheckListener != null) {
                Log.e("Version ", currentVersion + " , "+appVersion);
                onUpdateCheckListener.onUpdateCheckListener(updateURL);
            }
        }
    }

    private String getAppVersion(Context context) {
        String result = "";

        try {
            PackageManager manager = context.getPackageManager();
            result = manager.getPackageInfo(context.getPackageName(), 0).versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    // inner interface
    public interface OnUpdateCheckListener {
        // abstract method
        void onUpdateCheckListener(String urlApp);
    }

    // nested class Builder
    public static class Builder {
        private Context context;
        private OnUpdateCheckListener onUpdateCheckListener;

        public Builder(Context context) {
            this.context = context;
        }


        public UpdateHelper build() {
            return new UpdateHelper(context, onUpdateCheckListener);
        }

        public UpdateHelper check() {
            UpdateHelper updateHelper = build();
            updateHelper.check();

            return updateHelper;
        }

    }
}
