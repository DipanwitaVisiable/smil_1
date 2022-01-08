package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.SubjectListAdapter;
import com.example.crypedu.Pojo.SubjectListInfo;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.RequestQueue;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.SwitchChildInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.key.Key;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private static final int PERIOD = 2000;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 12;
    private EditText username_editText;
    private EditText password_editText;
    private CoordinatorLayout coordinatorLayout;
    private Typeface typeface;
    private ArrayList<SwitchChildInfo> switchChildInfoArrayList;
    private String firebase_id;
    private RadioButton radioButton;
    private long lastPressedTime;
    private RadioGroup rg;

    private String check_phn_no, check_password;
    private RequestQueue requestQueue;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      requestQueue = Volley.newRequestQueue(this);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Constants.updateFalg = 1;

        /*
          Set BubbleGumSans Regular font.
         */
        typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        /*
          Fetch all id from xml.
         */
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Button button_login = findViewById(R.id.button_login);
        button_login.setTypeface(typeface);
        TextView textView_signup = findViewById(R.id.signup_textView);
        textView_signup.setTypeface(typeface);
        username_editText = findViewById(R.id.username_editText);
        username_editText.setTypeface(typeface);
        password_editText = findViewById(R.id.password_editText);
        password_editText.setTypeface(typeface);
        LinearLayout login_linearLayout = findViewById(R.id.login_linearLayout);
        TextView forgot_textView = findViewById(R.id.forgot_textView);
        forgot_textView.setTypeface(typeface);
        TextView signin_textView = findViewById(R.id.signin_textView);
        signin_textView.setTypeface(typeface);
        TextView privacy_policy_textView = findViewById(R.id.privacy_policy_textView);
        privacy_policy_textView.setTypeface(typeface);
        TextView copyright_textView = findViewById(R.id.copyright_textView);
        copyright_textView.setTypeface(typeface);

        loginReset();

        /*
         * ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.GONE);

        /*
         * for text underline.
         */
        signin_textView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        privacy_policy_textView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        /*
         * When press login button.
         */

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                firebase_id = newToken;
                Log.e("newToken", newToken);
                SharedPreferences prefsForID = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
                prefsForID.edit().putString("FIREBASE_ID", firebase_id).apply();


            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

//                      ProgressBar

                    final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
                    pbHeaderProgress.setVisibility(View.VISIBLE);
                    if (InternetCheckActivity.isConnected()) {
                        pbHeaderProgress.setVisibility(View.GONE);
//                        fetchLogin();
//                      getAllLogin();
                      newFetchLogin();
                    } else {
                        showSnack();
                    }
                }
            }
        });


        /*button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDaysList();
            }
        });*/

        /*
         * Now sign up button is not working.
         */
        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Now sign in button is not working.
         */
        signin_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Now its link to splash screen.
         */
        privacy_policy_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
//                startActivity(intent);
                privacyDialog();
            }
        });

        /*
         * Now its link to splash screen.l
         */
//        forgot_textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        /*
         * set copyright text with symbol.
         */
        String strCopyright = "\u00a9" + " SNR Memorial Trust. All Rights Reserved.";
        copyright_textView.setText(strCopyright);

        /*
         * store username and password.
         */
        if (GetLogIn().length() > 0) {
            SharedPreferences prefs = getSharedPreferences("VALUE", MODE_PRIVATE);
            username_editText.setText(prefs.getString("UID", ""));
            password_editText.setText(prefs.getString("PWD", ""));
            //button_login.callOnClick();
        }

        checkPermission();

    }

    //-----------------------
    // for auto login.
    //-----------------------
    public String GetLogIn() {
        SharedPreferences prefs = getSharedPreferences("VALUE", MODE_PRIVATE);
        return prefs.getString("UID", "");
    }

    //-------------------------
    // Store Profile credential
    // in shared preferences.
    //-------------------------
    public void StoreUserDetails() {
        SharedPreferences.Editor editor = getSharedPreferences("VALUE", MODE_PRIVATE).edit();
        editor.putString("PROFILENAME", Constants.PROFILENAME);
        editor.putString("USERID", Constants.USER_ID);
        editor.apply();

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, true);
        edt.putString(Key.KEY_STUDENT_ID, Constants.USER_ID);
        edt.apply();
    }

    //-------------------------
    // Store Login credential
    // in shared preferences.
    //-------------------------
    public void StoreLogIn() {
        SharedPreferences.Editor editor = getSharedPreferences("VALUE", MODE_PRIVATE).edit();
        editor.putString("UID", username_editText.getText().toString().trim());
        editor.putString("PWD", password_editText.getText().toString());
        editor.apply();

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString(Key.KEY_STUDENT_ID, Constants.USER_ID);
        edt.putString(Key.KEY_USER_ROLE, Constants.USER_ROLE);
        edt.putString(Key.KEY_PROFILE_NAME, Constants.PROFILENAME);
        edt.putString(Key.KEY_PHONE_NO, Constants.PhoneNo);
        edt.putString(Key.KEY_USER_RATING, Constants.USER_RATING);
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, true);
        edt.apply();
    }


    private void privacyDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.privacy_dialog);
        TextView text1 = dialog.findViewById(R.id.text1);
        text1.setTypeface(typeface);
        TextView text2 = dialog.findViewById(R.id.text2);
        text2.setTypeface(typeface);
        TextView text3 = dialog.findViewById(R.id.text3);
        text3.setTypeface(typeface);
        Button ok = dialog.findViewById(R.id.button);
        ok.setTypeface(typeface);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        minimizeApp();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.", Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

    //-----------------------
    // Double click for minimize.
    //-----------------------
    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //-----------------------------
    // Check whether login details
    // success or failure respective
    // of username and password.
    //-----------------------------
    public void fetchLogin() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            final String userName = username_editText.getText().toString().trim(); //code off
            String password = password_editText.getText().toString();

            RequestParams params = new RequestParams();
            params.put("username", userName);
            params.put("password", password);
            /*params.put("count", count);
            params.put("username", u_name);
            params.put("password", pass);
            params.put("student_id", student_id);*/
            params.put("reg_id", firebase_id);
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.setConnectTimeout(50000); // 50 Seconds increase it for more time
            // AsyncHttpClient clientReg = new AsyncHttpClient(true, 80, 443);

//          Toast.makeText(LoginActivity.this, student_id + "  count:  " + count, Toast.LENGTH_LONG).show();

            clientReg.post(Constants.BASE_SERVER + "svclogins", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {

                        String status = obj.getString("status");
                        String message = obj.getString("message");


                        if (status.equalsIgnoreCase("200")) {
                            String id, firstName, lastName;
                            JSONObject jsonObject = obj.getJSONObject("info");
                            id = jsonObject.getString("id");
                            firstName = jsonObject.getString("firstname");
                            lastName = jsonObject.getString("lastname");
                            String role = jsonObject.getString("role");
                            String u_rating = jsonObject.getString("usr_rating");


//                              Role 's' for student login else if role 't' teacher else if role 'd' director for else if 'A' for admin.

                            if (role.equalsIgnoreCase("s")) {

                                 // For student login, if childType 'm' means parent having multiple child
                                  //else childType 's' means parent having single child.

                                String childType = jsonObject.getString("childType");
                                if (childType.equalsIgnoreCase("m")) {
                                    JSONArray areaListJsonArray = jsonObject.getJSONArray("student_code");
                                    switchChildInfoArrayList = new ArrayList<>();
                                    for (int i = 0; i < areaListJsonArray.length(); i++) {
                                        JSONObject cuisineJsonObject = areaListJsonArray.getJSONObject(i);
                                        String studentCode = cuisineJsonObject.getString("student_code");
                                        String fName = cuisineJsonObject.getString("First_Name");
                                        String lName = cuisineJsonObject.getString("Last_Name");
                                        String studentId = cuisineJsonObject.getString("Student_id");
                                        String user_rating = cuisineJsonObject.getString("usr_rating");
                                        SwitchChildInfo switchChildInfo = new SwitchChildInfo(studentCode, fName, lName, studentId, user_rating);
                                        switchChildInfoArrayList.add(i, switchChildInfo);
                                    }
                                    if (switchChildInfoArrayList != null) {
                                        showMultipleChild(role, switchChildInfoArrayList, userName);
                                    }
                                } else if (childType.equalsIgnoreCase("s")) {
                                    Constants.PROFILENAME = firstName + " " + lastName;
                                    Constants.USER_ID = id;
                                    Constants.USER_ROLE = role;
                                    Constants.PhoneNo = userName;
                                    Constants.USER_RATING = u_rating;
                                    StoreLogIn();
                                    StoreUserDetails();
                                    saveDeviceTokenWithUserIdApi(Constants.USER_ID);
                                }
                            } else if (role.equalsIgnoreCase("d")) {

                                Constants.PROFILENAME = firstName + " " + lastName;
                                Constants.USER_ID = id;
                                Constants.USER_ROLE = role;
                                Constants.PhoneNo = userName;
                                Constants.USER_RATING = u_rating;
                                StoreLogIn();
                                StoreUserDetails();
                                saveDeviceTokenWithUserIdApi(Constants.USER_ID);
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                        pbHeaderProgress.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    //------------------------------
    // Fetch subject details from server,
    // now its not used.
    //------------------------------
    /*    public void fetchFeesDeatils() {
     */

    /**
     * ProgressBar
     *//*
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id",Constants.USER_ID);
            clientReg.post("http://"+ Constants.server_name +"webservices/websvc/fee_status/",requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONObject jsonObject = obj.getJSONObject("fees_details");
                            String amount = jsonObject.getString("amount");
                            String feesStatus = jsonObject.getString("fees_status");
                            if (feesStatus.equalsIgnoreCase("true")){
                                showNotification(amount +" "+message);
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }*/

    //--------------------------
    // checking whether username
    // and password field empty
    // and within length or not
    // at the time of submition.
    //--------------------------
    public boolean validate() {
        boolean valid = true;

        String username = username_editText.getText().toString();
        String password = password_editText.getText().toString();

        if (username.isEmpty() || username.trim().length() == 0) {
            username_editText.setError("enter username");
            valid = false;
        } else if (username.length() < 10) {
            username_editText.setError("at least 10 characters");
            valid = false;
        } else {
            username_editText.setError(null);
        }
        if (password.isEmpty() || password.trim().length() == 0) {
            password_editText.setError("enter password");
            valid = false;
        } else if (password.length() < 6) {
            password_editText.setError("minimum length 6");
            valid = false;
        } else {
            password_editText.setError(null);
        }
        return valid;
    }


    private void showSnack() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

//    private void showNotification(String message) {
//        final Notification.Builder builder = new Notification.Builder(this);
//        builder.setStyle(new Notification.BigTextStyle(builder)
//                .bigText(message)
//                .setBigContentTitle("SMI Liluah")
//                .setSummaryText("Fees Notification"))
//                .setContentTitle("Fees Notification")
//                .setContentText("Fees Notification")
//                .setSmallIcon(android.R.drawable.sym_def_app_icon);
//
//        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.notify(0, builder.build());
//
// }


    /**
     * Alert box for multiple child of a particular parent.
     */
    public void showMultipleChild(final String role, final ArrayList<SwitchChildInfo> switchChildInfoArrayList, final String phoneNo) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_multiple_child);
        List<String> stringList = new ArrayList<>();  // here is list
        for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
            stringList.add(switchChildInfo.fName + " " + switchChildInfo.lName);
        }
        TextView alertTitleTextView = dialog.findViewById(R.id.alertTitleTextView);
        alertTitleTextView.setTypeface(typeface);
        rg = dialog.findViewById(R.id.radioGroup);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setTypeface(typeface);
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                int radioButtonID = rg.getCheckedRadioButtonId();
                RadioButton radioButton = rg.findViewById(radioButtonID);
                String selectChild = radioButton.getText().toString().trim();
                //Log.e("Select from radio ", selectChild);
                for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
                    String childName = switchChildInfo.fName + " " + switchChildInfo.lName;
                    if (childName.equalsIgnoreCase(selectChild)) {
                        Constants.PROFILENAME = childName;
                        //Log.e("After select ", Constants.PROFILENAME);
                        Constants.USER_ID = switchChildInfo.studentId;
                        Constants.USER_ROLE = role;
                        Constants.PhoneNo = phoneNo;
                        Constants.USER_RATING = switchChildInfo.user_rating;
                        /*Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);*/ //close-  This code is added on saveDeviceTokenWithUserIdApi()
                        StoreLogIn();
                        StoreUserDetails();

                        saveDeviceTokenWithUserIdApi( Constants.USER_ID);
                    }
                }
            }
        });
        dialog.show();
    }


    public void checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                }
            } else {
            }
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //send();
                } else {
                    //code for deny
                }
                break;
        }
    }

    public void saveDeviceTokenWithUserIdApi_old(final String student_id) {
        /*
          ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", student_id);
            requestParams.put("reg_id", firebase_id);
            clientReg.post(Constants.BASE_SERVER + "update_reg_id", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Try again", Snackbar.LENGTH_LONG);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    public void loginReset() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
        editor.apply();
        Constants.PROFILENAME = "";
        Constants.USER_ROLE = "";
        Constants.PhoneNo = "";
        Constants.USER_ID = "";

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, false);
        edt.putString(Key.KEY_STUDENT_ID, Constants.USER_ID);
        edt.putString(Key.KEY_USER_ROLE, Constants.USER_ROLE);
        edt.putString(Key.KEY_PROFILE_NAME, Constants.PROFILENAME);
        edt.putString(Key.KEY_PHONE_NO, Constants.PhoneNo);
        edt.apply();
    }



  public void getAllLogin() {
    count=0;
    final ProgressBar pb_loader = findViewById(R.id.pbHeaderProgress);
    pb_loader.setVisibility(View.VISIBLE);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_SERVER + "all_svclogin", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try {
          pb_loader.setVisibility(View.GONE);
          JSONObject jsonObject = new JSONObject(response);
          String status = jsonObject.getString("status");

          if (status.equalsIgnoreCase("200")) {
            JSONArray id_list = jsonObject.getJSONArray("info");
            for (int i = 0; i < id_list.length(); i++) {
//              for (int i = 0; i < 500; i++) {
              JSONObject object = id_list.getJSONObject(i);
              String u_name= object.getString("username");
              String pass= object.getString("password");
              String student_id= object.getString("student_id");
//              fetchLogin(u_name, pass, student_id);
//              newFetchLogin(u_name, pass, student_id);
            }
          }

        } catch (JSONException e) {
          e.printStackTrace();
          pb_loader.setVisibility(View.GONE);

        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        pb_loader.setVisibility(View.GONE);
      }
    }) ;
    stringRequest.setRetryPolicy(new RetryPolicy() {
      @Override
      public int getCurrentTimeout() {
        return 50000;
      }

      @Override
      public int getCurrentRetryCount() {
        return 50000;
      }

      @Override
      public void retry(VolleyError error) throws VolleyError {

      }
    });
    requestQueue.add(stringRequest);
  }

  public void newFetchLogin() {
    final ProgressBar pb_loader = findViewById(R.id.pbHeaderProgress);
    pb_loader.setVisibility(View.VISIBLE);
    final String userName = username_editText.getText().toString().trim(); //code off
    String password = password_editText.getText().toString();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "svclogin", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try {
          pb_loader.setVisibility(View.GONE);
          JSONObject jsonObj = new JSONObject(response);
          String status = jsonObj.getString("status");
          String message = jsonObj.getString("message");

          if (status.equalsIgnoreCase("200")) {
                            String id, firstName, lastName;
                            JSONObject jsonObject = jsonObj.getJSONObject("info");
                            id = jsonObject.getString("id");
                            firstName = jsonObject.getString("firstname");
                            lastName = jsonObject.getString("lastname");
                            String role = jsonObject.getString("role");
                            String u_rating = jsonObject.getString("usr_rating");


//                              Role 's' for student login else if role 't' teacher else if role 'd' director for else if 'A' for admin.

                            if (role.equalsIgnoreCase("s")) {

                                 // For student login, if childType 'm' means parent having multiple child
                                  //else childType 's' means parent having single child.

                                String childType = jsonObject.getString("childType");
                                if (childType.equalsIgnoreCase("m")) {
                                    JSONArray areaListJsonArray = jsonObject.getJSONArray("student_code");
                                    switchChildInfoArrayList = new ArrayList<>();
                                    for (int i = 0; i < areaListJsonArray.length(); i++) {
                                        JSONObject cuisineJsonObject = areaListJsonArray.getJSONObject(i);
                                        String studentCode = cuisineJsonObject.getString("student_code");
                                        String fName = cuisineJsonObject.getString("First_Name");
                                        String lName = cuisineJsonObject.getString("Last_Name");
                                        String studentId = cuisineJsonObject.getString("Student_id");
                                        String user_rating = cuisineJsonObject.getString("usr_rating");
                                        SwitchChildInfo switchChildInfo = new SwitchChildInfo(studentCode, fName, lName, studentId, user_rating);
                                        switchChildInfoArrayList.add(i, switchChildInfo);
                                    }
                                    if (switchChildInfoArrayList != null) {
                                        showMultipleChild(role, switchChildInfoArrayList, userName);
                                    }
                                } else if (childType.equalsIgnoreCase("s")) {
                                    Constants.PROFILENAME = firstName + " " + lastName;
                                    Constants.USER_ID = id;
                                    Constants.USER_ROLE = role;
                                    Constants.PhoneNo = userName;
                                    Constants.USER_RATING = u_rating;
                                    StoreLogIn();
                                    StoreUserDetails();
                                    saveDeviceTokenWithUserIdApi(Constants.USER_ID);
                                }
                            } else if (role.equalsIgnoreCase("d")) {

                                Constants.PROFILENAME = firstName + " " + lastName;
                                Constants.USER_ID = id;
                                Constants.USER_ROLE = role;
                                Constants.PhoneNo = userName;
                                Constants.USER_RATING = u_rating;
                                StoreLogIn();
                                StoreUserDetails();
                                saveDeviceTokenWithUserIdApi(Constants.USER_ID);
                            }
          }
          else {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
          }


        } catch (JSONException e) {
          e.printStackTrace();

        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        pb_loader.setVisibility(View.GONE);
      }
    }) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("count", "0");
        params.put("username", username_editText.getText().toString().trim());
        params.put("password", password_editText.getText().toString());
        params.put("student_id", "2615");
        params.put("reg_id", firebase_id);
        return checkParams(params);
      }

      private Map<String, String> checkParams(Map<String, String> map) {
        for (Map.Entry<String, String> pairs : map.entrySet()) {
          if (pairs.getValue() == null) {
            map.put(pairs.getKey(), "");
          }
        }
        return map;
      }
    };
    stringRequest.setRetryPolicy(new RetryPolicy() {
      @Override
      public int getCurrentTimeout() {
        return 50000;
      }

      @Override
      public int getCurrentRetryCount() {
        return 50000;
      }

      @Override
      public void retry(VolleyError error) throws VolleyError {

      }
    });
    requestQueue.add(stringRequest);
  }

    public void saveDeviceTokenWithUserIdApi(final String student_id) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "update_reg_id", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Try again", Snackbar.LENGTH_LONG);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pbHeaderProgress.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", student_id);
                params.put("reg_id", firebase_id);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                for (Map.Entry<String, String> pairs : map.entrySet()) {
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

}
