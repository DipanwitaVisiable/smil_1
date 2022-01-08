package com.example.crypedu.Helper;

/**
 * Created by hp on 7/25/2017.
 */
public class FirebaseIDService { /*extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        storeToken(refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }


    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
    *//*
      Persist token to third-party servers.

      Modify this method to associate the user's FCM InstanceID token with any server-side account
      maintained by your application.

      @param token The new token.
     */
    /*
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        Constants.FIREBASE_ID = token;
        Log.e("FIre ", token);
        SharedPreferences pref = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("FIREBASE_ID", Constants.FIREBASE_ID);
        edt.apply();
    }*/
}
