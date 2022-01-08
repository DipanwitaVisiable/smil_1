package com.activity.smi;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by INDID on 04-04-2018.
 */

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private static App mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        FirebaseApp.initializeApp(this);
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setDeveloperModeEnabled(BuildConfig.DEBUG)
//                .build();
//        remoteConfig.setConfigSettings(configSettings);
        //final Map<String, Object> defaultValue = new HashMap<>();
        //defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE, true);
        //defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION, "43");
        //defaultValue.put(UpdateHelper.KEY_UPDATE_URL, "https://play.google.com/store/apps/details?id=com.activity.smi");
        //remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(10)// fetch every 10 SEC
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            remoteConfig.activateFetched();
                            Log.d(TAG, "remote config is fetched.");

                        }
                    }
                });
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(InternetCheckActivity.ConnectivityReceiverListener listener) {
        InternetCheckActivity.connectivityReceiverListener = listener;
    }

}
