package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Helper.PushService;
import com.key.Key;

import me.leolin.shortcutbadger.ShortcutBadger;

public class SplashActivity extends Activity  {

    String newVersion;
    String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Constants.updateFalg = 1;
        setContentView(R.layout.activity_splash);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        stopService(new Intent(getBaseContext(), PushService.class));
        ShortcutBadger.removeCount(this);
        // Retrieved id.
        ImageView imageView = findViewById(R.id.imageView);

        /*
          Logo animation.
         */
        Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_screen_animation);
        imageView.startAnimation(anim);



        /*
          Thread for calling Login
          activity after 2 seconds.
         */

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
                if (pref.getBoolean(Key.KEY_ACTIVITY_EXE, false)) {
                    Constants.USER_ID = pref.getString(Key.KEY_STUDENT_ID, "");
                    Constants.USER_ROLE = pref.getString(Key.KEY_USER_ROLE, "");
                    Constants.PROFILENAME = pref.getString(Key.KEY_PROFILE_NAME, "");
                    Constants.PhoneNo = pref.getString(Key.KEY_PHONE_NO, "");

                    Log.e("STUDENT ID ", Constants.USER_ID);
                    Log.e("STUDENT ROLE ", Constants.USER_ROLE);
                    Log.e("STUDENT NAME ", Constants.PROFILENAME);
                    Log.e("STUDENT PHONE ", Constants.PhoneNo);

                    Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("GO TO LOGIN PAGE ", "YES");
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                SplashActivity.this.finish();
            }
        };
        handler.postDelayed(runnable, 2000);


//        new FetchAppVersionFromGooglePlayStore().execute();
//        try {
//            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//            Log.e("CURRENT VERSION", currentVersion);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    /* ---------------------------------
      For forcefully update.
     ---------------------------------
     */

//    class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {
//
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.activity.smi" + "&hl=it")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
//                        .select("div[itemprop=softwareVersion]")
//                        .first()
//                        .ownText();
//                return newVersion;
//            } catch (Exception e) {
//                return newVersion;
//            }
//        }
//
//        protected void onPostExecute(String string) {
//            newVersion = string;
//            if (newVersion != null && !newVersion.isEmpty()) {
//                String[] array = currentVersion.split("\\.");
//                Log.e("ARRAY 1", array.toString());
//                String[] array2 = newVersion.split("\\.");
//                Log.e("ARRAY 2", array2.toString());
//                if (Float.valueOf(array[0]) < Float.valueOf(array2[0])) {
//                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.activity.smi"));
//                    startActivity(i);
//                    final String apkurl = "market://details?id=" + "com.activity.smi";
//                    final Uri marketUri = Uri.parse(apkurl);
//
//                    Intent promptInstall = new Intent(Intent.ACTION_VIEW).setData(marketUri);
//                    startActivity(promptInstall);
//                } else {
//                   /*
//                     * Thread for calling Login
//                    * activity after 2 seconds.*/
//
//                    Handler handler = new Handler();
//                    Runnable runnable = new Runnable() {
//                        public void run() {
//                            SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
//                            if (pref.getBoolean("activity_executed", false)) {
//                                Constants.USER_ID = pref.getString("student_id", "");
//                                Constants.USER_ROLE = pref.getString("user_role", "");
//                                Constants.PROFILENAME = pref.getString("profile_name", "");
//                                Constants.PhoneNo = pref.getString("phoneNo", "");
//
//                                Log.e("STUDENT ID ", Constants.USER_ID);
//                                Log.e("STUDENT ROLE ", Constants.USER_ROLE);
//                                Log.e("STUDENT NAME ", Constants.PROFILENAME);
//                                Log.e("STUDENT PHONE ", Constants.PhoneNo);
//
//                                Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                Log.e("GO TO LOGIN PAGE ", "YES");
//                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//                    };
//                    handler.postDelayed(runnable, 2000);
//                }
//            } else {
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
//    }


//-------------------------------------------------------------------------------------------------------------------------

//    private class GetVersionCode extends AsyncTask<Void, String, String> {
//        @Override
//        protected String doInBackground(Void... voids) {
//
//            try {
//                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.activity.smi" + "&hl=it")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
//                        .select("div[itemprop=softwareVersion]")
//                        .first()
//                        .ownText();
//                return newVersion;
//            } catch (Exception e) {
//                return newVersion;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String onlineVersion) {
//            super.onPostExecute(onlineVersion);
//
//            if (!currentVersion.equalsIgnoreCase(onlineVersion)) {
//                //show dialog
//                new AlertDialog.Builder(SplashActivity.this)
//                        .setTitle("Updated app available!")
//                        .setMessage("Please update first.")
//                        .setCancelable(false)
//                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                                try {
//                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.activity.smi")));
//                                } catch (ActivityNotFoundException anfe) {
//                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.activity.smi")));
//                                }
//                            }
//                        })
////                        .setNegativeButton("Later", new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int which) {
////                                // do nothing
////                                dialog.dismiss();
////                               // new GetVersionCode().execute();
////                            }
////                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//
//            }else {
//                 /*
//                     * Thread for calling Login
//                    * activity after 2 seconds.*/
//
//                Handler handler = new Handler();
//                Runnable runnable = new Runnable() {
//                    public void run() {
//                        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
//                        if (pref.getBoolean("activity_executed", false)) {
//                            Constants.USER_ID = pref.getString("student_id", "");
//                            Constants.USER_ROLE = pref.getString("user_role", "");
//                            Constants.PROFILENAME = pref.getString("profile_name", "");
//                            Constants.PhoneNo = pref.getString("phoneNo", "");
//
//                            Log.e("STUDENT ID ", Constants.USER_ID);
//                            Log.e("STUDENT ROLE ", Constants.USER_ROLE);
//                            Log.e("STUDENT NAME ", Constants.PROFILENAME);
//                            Log.e("STUDENT PHONE ", Constants.PhoneNo);
//
//                            Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Log.e("GO TO LOGIN PAGE ", "YES");
//                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                };
//                handler.postDelayed(runnable, 2000);
//            }
//        }
//    }
}