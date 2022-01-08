package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ResetPasswordActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private EditText oldPassword_editText;
    private EditText newPassword_editText;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);

        oldPassword_editText = findViewById(R.id.oldPassword_editText);
        newPassword_editText = findViewById(R.id.newPassword_editText);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        TextView reset_info_text = findViewById(R.id.reset_info_text);
        TextView textView_enterPassword = findViewById(R.id.textView_enterPassword);
        Button resetButton = findViewById(R.id.resetButton);
        copyRight_textView.setText(Constants.strCopyright);

        oldPassword_editText.setTypeface(typeface);
        newPassword_editText.setTypeface(typeface);
        copyRight_textView.setTypeface(typeface);
        resetButton.setTypeface(typeface);
        copyRight_textView.setTypeface(typeface);
        reset_info_text.setTypeface(typeface);
        textView_enterPassword.setTypeface(typeface);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPassword_editText.getText().toString().trim();
                String newPassword = newPassword_editText.getText().toString().trim();
                int passwordLength = newPassword_editText.getText().toString().trim().length();
//                if (!oldPassword.equalsIgnoreCase("") && !newPassword.equalsIgnoreCase("")){
                if (!newPassword.equalsIgnoreCase("")) {
                    if (passwordLength > 6) {
                        if (InternetCheckActivity.isConnected()) {
                            if (Constants.PhoneNo != null && !Constants.PhoneNo.equalsIgnoreCase("")) {
                                resetPassword(newPassword, Constants.PhoneNo);
                            } else {
                                startActivity(new Intent(ResetPasswordActivity.this, SplashActivity.class));
                            }
                        }else {
                            showSnack();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Minimum password length is 6 ", Snackbar.LENGTH_LONG);
                        newPassword_editText.setError("minimum length 6");
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext()," Insufficient data! please try again. ",Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, " Insufficient data! please try again. ", Snackbar.LENGTH_LONG);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.parseColor(Constants.colorAccent));
                    snackbar.show();
                }
            }
        });


    }

    //--------------------------------
    // Reset password.
    //--------------------------------
    public void resetPassword_old(final String password, String PhoneNo) {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            requestParams.put("phone", PhoneNo);
            requestParams.put("password", password);
            clientReg.post(Constants.BASE_SERVER + "parentlogin", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            loginReset();
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                            showNotificationNewPassword(password);
                            newPassword_editText.setText("");
                            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
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
    }

    //-----------------------
    // for logout.
    //-----------------------
    public void loginReset() {
        SharedPreferences.Editor editor = getSharedPreferences("VALUE", Context.MODE_PRIVATE).edit();
        editor.putString("UID", "");
        editor.putString("PWD", "");
        editor.commit();

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_executed", false);
        edt.putString("student_id", "");
        edt.putString("user_role", "");
        edt.putString("profile_name", "");
        edt.putString("phoneNo", "");
        edt.commit();

        /*
          For login with new password.
         */
        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(ResetPasswordActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }
    //-----------------------------
    // Normal Notification for Reset Password
    //-----------------------------
    private void showNotificationNewPassword(String message) {
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setStyle(new Notification.BigTextStyle(builder))
//                .bigText(message)
//                .setBigContentTitle("SMI Liluah")
//                .setSummaryText("Test Notification"))
                .setContentTitle("You've changed your password")
                .setContentText("New Password is " + message)
                .setSmallIcon(R.drawable.ic_notification);

        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert nm != null;
        nm.notify(0, builder.build());
    }

    public void resetPassword(final String password, final String PhoneNo) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "parentlogin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        loginReset();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                        showNotificationNewPassword(password);
                        newPassword_editText.setText("");
                        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
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
                params.put("id", Constants.USER_ID);
                params.put("phone", PhoneNo);
                params.put("password", password);
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
