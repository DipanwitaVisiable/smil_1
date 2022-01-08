package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
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
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.ClassWorkAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ClassWorkInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ClassWorkActivity extends AppCompatActivity {

    private LinearLayout title_linearLayout;
    private ListView listView_classWord;
    private ArrayList<String> classArrayList;
    private GridView gridview;
    private ArrayList<ClassWorkInfo> classWorkArrayList;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_work);
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
                Intent intent = new Intent(ClassWorkActivity.this, WorkActivity.class);
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

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(ClassWorkActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        /*
          Retrieved CoordinatorLayout Id for SnackBar.
         */
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        /*
          If internet connection is working
          then only call fetchClassDetails().
         */
        if (InternetCheckActivity.isConnected()){
            fetchClassWorkDetails();
            sendNotificationStatus();
        }else {
            showSnack();
        }

        /*
          Retrieved LinearLayout Id and set visibility 'GONE'
          because of It will show all details at the end of
          the process.
         */
        title_linearLayout = findViewById(R.id.title_linearLayout);
        title_linearLayout.setVisibility(View.GONE);

        listView_classWord = findViewById(R.id.listView_classWord);
        listView_classWord.setVisibility(View.GONE);

        /*
          Set BubbleGumSans Regular custom font.
         */
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        /*
          Set copy right text.
         */
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        TextView class_textView = findViewById(R.id.class_textView);
        class_textView.setTypeface(typeface);
        TextView topic_textView = findViewById(R.id.topic_textView);
        topic_textView.setTypeface(typeface);
        TextView subject_textView = findViewById(R.id.subject_textView);
        subject_textView.setTypeface(typeface);
        TextView date_textView = findViewById(R.id.date_textView);
        date_textView.setTypeface(typeface);
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(ClassWorkActivity.this, WorkActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //------------------------------
    // Send notification status to server
    // respective of student_id and notification_type.
    //------------------------------
    public void sendNotificationStatus_old() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id",Constants.USER_ID);
            requestParams.put("notification_type","classwork");
            clientReg.post(Constants.BASE_SERVER +"notification_status/",requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
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

    //--------------------------------
    // Fetch Class work details from server
    // with the 'student Id' param.
    //--------------------------------
    public void fetchClassWorkDetails_old(){
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id",Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER +"get_classwork_details/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        title_linearLayout.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            classWorkArrayList = new ArrayList<>();
                            listView_classWord = findViewById(R.id.listView_classWord);
                            listView_classWord.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("class_work_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String subject = jsonObject.getString("subject");
                                String topic = jsonObject.getString("topic");
                                String class_name = jsonObject.getString("class_name");
                                String date_of_class = jsonObject.getString("date_of_class");

                                ClassWorkInfo classWorkInfo = new ClassWorkInfo(id, subject, topic, class_name, date_of_class);
                                classWorkArrayList.add(i, classWorkInfo);
                            }
                            listView_classWord.setAdapter(new ClassWorkAdapter(getBaseContext(), classWorkArrayList, getLayoutInflater()));
                        } else {
                            // adding a text view in CoordinateLayout
                            TextView textViewCoordinatorLayout = new TextView(ClassWorkActivity.this);
                            textViewCoordinatorLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            textViewCoordinatorLayout.setText(message);
                            coordinatorLayout.addView(textViewCoordinatorLayout); // adding textView as child view of coordinate layout.

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

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    public void sendNotificationStatus() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_status/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
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
                params.put("notification_type", "classwork");
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

    public void fetchClassWorkDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_classwork_details/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    title_linearLayout.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        classWorkArrayList = new ArrayList<>();
                        listView_classWord = findViewById(R.id.listView_classWord);
                        listView_classWord.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayList = obj.getJSONArray("class_work_details");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String subject = jsonObject.getString("subject");
                            String topic = jsonObject.getString("topic");
                            String class_name = jsonObject.getString("class_name");
                            String date_of_class = jsonObject.getString("date_of_class");

                            ClassWorkInfo classWorkInfo = new ClassWorkInfo(id, subject, topic, class_name, date_of_class);
                            classWorkArrayList.add(i, classWorkInfo);
                        }
                        listView_classWord.setAdapter(new ClassWorkAdapter(getBaseContext(), classWorkArrayList, getLayoutInflater()));
                    } else {
                        // adding a text view in CoordinateLayout
                        TextView textViewCoordinatorLayout = new TextView(ClassWorkActivity.this);
                        textViewCoordinatorLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        textViewCoordinatorLayout.setText(message);
                        coordinatorLayout.addView(textViewCoordinatorLayout); // adding textView as child view of coordinate layout.

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
                params.put("id",Constants.USER_ID);
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
