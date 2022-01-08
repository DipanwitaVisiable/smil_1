package com.example.crypedu.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.NotificationAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Helper.PushService;
import com.example.crypedu.Pojo.NotificationInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class NotificationActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private ArrayList<String> notificationTypeList;
    private ArrayList<NotificationInfo> notificationList;
    private ListView listView;
    private RequestQueue requestQueue;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        requestQueue = Volley.newRequestQueue(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(NotificationActivity.this, MenuActivity.class);
                //startActivity(intent);
                onBackPressed();
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


        //startService(
               // new Intent(NotificationActivity.this, PushService.class).putExtra("Id", Constants.USER_ID));

        startService(new Intent(getBaseContext(), PushService.class));

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);
        listView = findViewById(R.id.listView);

        if (InternetCheckActivity.isConnected()){
            fetchNotificationDetails();
        }else {
            showSnack();
        }
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
            Intent intent = new Intent(NotificationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //------------------------------
    // Fetch notification details from server
    // respective of student_id.
    //------------------------------
    public void fetchNotificationDetails_old() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id",Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER +"notification_details/",requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        notificationTypeList = new ArrayList<>();
                        notificationList = new ArrayList<>();
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonObjectArray = obj.getJSONArray("notification_count");
                            for (int i = 0; i < jsonObjectArray.length(); i++) {
                                JSONObject jsonObject = jsonObjectArray.getJSONObject(i);
                                String notificationType = jsonObject.getString("notification_type").trim();
                                notificationTypeList.add(notificationType);
                            }
                            if (notificationTypeList != null && notificationTypeList.size() > 0){
                                Set<String> uniqueSet = new HashSet<>(notificationTypeList);
                                for (String temp : uniqueSet) {
//                                    System.out.println(temp + ": " + Collections.frequency(notificationTypeList, temp));
                                    NotificationInfo notificationInfo = new NotificationInfo(temp, String.valueOf(Collections.frequency(notificationTypeList, temp)));
                                    notificationList.add(notificationInfo);
                                }
                                listView.setAdapter(new NotificationAdapter(getBaseContext(),notificationList, getLayoutInflater()));
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        // ListView Clicked item value
                                        String itemValue = notificationList.get(position).name.trim();
                                        if (itemValue.equalsIgnoreCase("attendance")){
                                            startActivity(new Intent(NotificationActivity.this, AttendanceActivity.class));
                                        }else if (itemValue.equalsIgnoreCase("notice")){
                                            startActivity(new Intent(NotificationActivity.this, NoticeActivity.class));
                                        }else if (itemValue.equalsIgnoreCase("classwork")){
                                            startActivity(new Intent(NotificationActivity.this, ClassWorkActivity.class));
                                        }else if (itemValue.equalsIgnoreCase("homework")){
                                            startActivity(new Intent(NotificationActivity.this, HomeWorkActivity.class));
                                        }else if (itemValue.equalsIgnoreCase("exam")){
                                            startActivity(new Intent(NotificationActivity.this, ExaminationActivity.class));
                                        }else if (itemValue.equalsIgnoreCase("bulletin")){
                                            startActivity(new Intent(NotificationActivity.this, BulletinsActivity.class));
                                        }else if (itemValue.equalsIgnoreCase("Account")){
                                            startActivity(new Intent(NotificationActivity.this, AccountActivity.class));
                                        }else if (itemValue.equalsIgnoreCase("Mediwallet")){
                                            startActivity(new Intent(NotificationActivity.this, MediwalletActivity.class));
                                        }
                                    }
                                });
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
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(NotificationActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }
    public void startService(View view) {
        startService(new Intent(getBaseContext(), PushService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), PushService.class));
    }


    public void fetchNotificationDetails() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_details/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    notificationTypeList = new ArrayList<>();
                    notificationList = new ArrayList<>();
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonObjectArray = obj.getJSONArray("notification_count");
                        for (int i = 0; i < jsonObjectArray.length(); i++) {
                            JSONObject jsonObject = jsonObjectArray.getJSONObject(i);
                            String notificationType = jsonObject.getString("notification_type").trim();
                            notificationTypeList.add(notificationType);
                        }
                        if (notificationTypeList != null && notificationTypeList.size() > 0){
                            Set<String> uniqueSet = new HashSet<>(notificationTypeList);
                            for (String temp : uniqueSet) {
//                                    System.out.println(temp + ": " + Collections.frequency(notificationTypeList, temp));
                                NotificationInfo notificationInfo = new NotificationInfo(temp, String.valueOf(Collections.frequency(notificationTypeList, temp)));
                                notificationList.add(notificationInfo);
                            }
                            listView.setAdapter(new NotificationAdapter(getBaseContext(),notificationList, getLayoutInflater()));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // ListView Clicked item value
                                    String itemValue = notificationList.get(position).name.trim();
                                    if (itemValue.equalsIgnoreCase("attendance")){
                                        startActivity(new Intent(NotificationActivity.this, AttendanceActivity.class));
                                    }else if (itemValue.equalsIgnoreCase("notice")){
                                        startActivity(new Intent(NotificationActivity.this, NoticeActivity.class));
                                    }else if (itemValue.equalsIgnoreCase("classwork")){
                                        startActivity(new Intent(NotificationActivity.this, ClassWorkActivity.class));
                                    }else if (itemValue.equalsIgnoreCase("homework")){
                                        startActivity(new Intent(NotificationActivity.this, HomeWorkActivity.class));
                                    }else if (itemValue.equalsIgnoreCase("exam")){
                                        startActivity(new Intent(NotificationActivity.this, ExaminationActivity.class));
                                    }else if (itemValue.equalsIgnoreCase("bulletin")){
                                        startActivity(new Intent(NotificationActivity.this, BulletinsActivity.class));
                                    }else if (itemValue.equalsIgnoreCase("Account")){
                                        startActivity(new Intent(NotificationActivity.this, AccountActivity.class));
                                    }else if (itemValue.equalsIgnoreCase("Mediwallet")){
                                        startActivity(new Intent(NotificationActivity.this, MediwalletActivity.class));
                                    }
                                }
                            });
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
                params.put("student_id", Constants.USER_ID);
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
