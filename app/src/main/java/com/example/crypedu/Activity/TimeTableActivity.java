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
import com.example.crypedu.Adapter.TimeTableNewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.TimeTableAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.TimeInfo;
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

public class TimeTableActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private ListView listView;
    private ArrayList<TimeInfo> timekArrayList;
    private ArrayList<String> timeTableArrayList;
    private ListView special_listView;
    private TextView special_title_textView;
    private TextView messageTextView;
    private TextView mon_textView, tue_textView, wed_textView, thu_textView, fri_textView, sat_textView;

    private RecyclerView recyclerView;
    private Context context;
    private LinearLayout ll_header;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        context=TimeTableActivity.this;

        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeTableActivity.this, MenuActivity.class);
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

        ll_header = findViewById(R.id.ll_header);
        ll_header.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        messageTextView = findViewById(R.id.messageTextView);
        messageTextView.setTypeface(typeface);

        mon_textView = findViewById(R.id.mon_textView);
        mon_textView.setTypeface(typeface);
        tue_textView = findViewById(R.id.tue_textView);
        tue_textView.setTypeface(typeface);
        wed_textView = findViewById(R.id.wed_textView);
        wed_textView.setTypeface(typeface);
        thu_textView = findViewById(R.id.thu_textView);
        thu_textView.setTypeface(typeface);
        fri_textView = findViewById(R.id.fri_textView);
        fri_textView.setTypeface(typeface);
        sat_textView = findViewById(R.id.sat_textView);
        sat_textView.setTypeface(typeface);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        listView = findViewById(R.id.listView);
        special_listView = findViewById(R.id.special_listView);
        special_title_textView = findViewById(R.id.special_title_textView);
        special_title_textView.setTypeface(typeface);
        special_title_textView.setVisibility(View.INVISIBLE);

        if (InternetCheckActivity.isConnected()) {
            fetchTimeTable();
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
            Intent intent = new Intent(TimeTableActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //--------------------------------
    // Fetch time table from server
    // respective of student id.
    //--------------------------------
    /*public void fetchTimeTable() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "get_timetable/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            String dayStatus = obj.getString("day_status");
                            if (dayStatus.equalsIgnoreCase("true")) {
                                timekArrayList = new ArrayList<>();
                                JSONArray jsonArrayList = obj.getJSONArray("timetable_details");
                                for (int i = 0; i < jsonArrayList.length(); i++) {
                                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    //  String sub_time = time.substring(0,5);  now disable
                                    String subject1 = jsonObject.getString("subject1");
                                    String subject2 = jsonObject.getString("subject2");
                                    String subject3 = jsonObject.getString("subject3");
                                    String subject4 = jsonObject.getString("subject4");
                                    String subject5 = jsonObject.getString("subject5");
                                    String subject6 = jsonObject.getString("subject6");
                                    TimeInfo timeInfo = new TimeInfo(time, subject1, subject2, subject3, subject4, subject5, subject6);
                                    timekArrayList.add(i, timeInfo);
                                }

                                listView.setAdapter(new TimeTableAdapter(TimeTableActivity.this, timekArrayList, getLayoutInflater()));
                                special_title_textView.setVisibility(View.VISIBLE);
                                ArrayList<SpecialClassInfo> specialClassInfoArrayList = new ArrayList<>();
                                for (int i = 0; i < timekArrayList.size(); i++) {
                                    SpecialClassInfo specialClassInfo = new SpecialClassInfo(timekArrayList.get(i).time, timekArrayList.get(i).subject6);
                                    specialClassInfoArrayList.add(specialClassInfo);
                                }
                                special_listView.setAdapter(new SpecialClassAdapter(TimeTableActivity.this, specialClassInfoArrayList, getLayoutInflater()));
                            } else if (dayStatus.equalsIgnoreCase("false")) {
                                timekArrayList = new ArrayList<>();
                                JSONArray jsonArrayList = obj.getJSONArray("timetable_details");
                                for (int i = 0; i < jsonArrayList.length(); i++) {
                                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    //  String sub_time = time.substring(0,5); now disable
                                    String subject1 = jsonObject.getString("subject1");
                                    String subject2 = jsonObject.getString("subject2");
                                    String subject3 = jsonObject.getString("subject3");
                                    String subject4 = jsonObject.getString("subject4");
                                    String subject5 = jsonObject.getString("subject5");
                                    TimeInfo timeInfo = new TimeInfo(time, subject1, subject2, subject3, subject4, subject5, "");
                                    timekArrayList.add(i, timeInfo);
                                }
                                listView.setAdapter(new TimeTableAdapter(TimeTableActivity.this, timekArrayList, getLayoutInflater()));
                                special_title_textView.setVisibility(View.INVISIBLE);
                                special_listView.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                            messageTextView.setVisibility(View.VISIBLE);
                            messageTextView.setText(message);
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


    //create 02_06_2020
    public void fetchTimeTable_old() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "get_timetable", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            String dayStatus = obj.getString("day_status");
                            if (dayStatus.equalsIgnoreCase("false")) {
                                timekArrayList = new ArrayList<>();
                                JSONArray jsonArrayList = obj.getJSONArray("timetable_details");
                                for (int i = 0; i < jsonArrayList.length(); i++) {
                                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    //  String sub_time = time.substring(0,5);  now disable
                                    String subject1 = jsonObject.getString("subject1");
                                    String subject2 = jsonObject.getString("subject2");
                                    String subject3 = jsonObject.getString("subject3");
                                    String subject4 = jsonObject.getString("subject4");
                                    String subject5 = jsonObject.getString("subject5");
                                    String subject6 = jsonObject.getString("subject6");
                                    mon_textView.setVisibility(View.VISIBLE);
                                    tue_textView.setVisibility(View.VISIBLE);
                                    wed_textView.setVisibility(View.VISIBLE);
                                    thu_textView.setVisibility(View.VISIBLE);
                                    fri_textView.setVisibility(View.VISIBLE);
                                    sat_textView.setVisibility(View.VISIBLE);
                                    TimeInfo timeInfo = new TimeInfo(time, subject1, subject2, subject3, subject4, subject5, subject6);
                                    timekArrayList.add(i, timeInfo);
                                }

                                recyclerView.setAdapter(new TimeTableNewAdapter(context, timekArrayList));

//                                listView.setAdapter(new TimeTableAdapter(TimeTableActivity.this, timekArrayList, getLayoutInflater()));

                                /*special_title_textView.setVisibility(View.VISIBLE);
                                ArrayList<SpecialClassInfo> specialClassInfoArrayList = new ArrayList<>();
                                for (int i = 0; i < timekArrayList.size(); i++) {
                                    SpecialClassInfo specialClassInfo = new SpecialClassInfo(timekArrayList.get(i).time, timekArrayList.get(i).subject6);
                                    specialClassInfoArrayList.add(specialClassInfo);
                                }
                                special_listView.setAdapter(new SpecialClassAdapter(TimeTableActivity.this, specialClassInfoArrayList, getLayoutInflater()));*/

                            } else if (dayStatus.equalsIgnoreCase("true")) {
                                timekArrayList = new ArrayList<>();
                                JSONArray jsonArrayList = obj.getJSONArray("timetable_details");
                                for (int i = 0; i < jsonArrayList.length(); i++) {
                                    JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                    String time = jsonObject.getString("time");
                                    //  String sub_time = time.substring(0,5); now disable
                                    String subject1 = jsonObject.getString("subject1");
                                    String subject2 = jsonObject.getString("subject2");
                                    String subject3 = jsonObject.getString("subject3");
                                    String subject4 = jsonObject.getString("subject4");
                                    String subject5 = jsonObject.getString("subject5");

                                    mon_textView.setVisibility(View.VISIBLE);
                                    tue_textView.setVisibility(View.VISIBLE);
                                    wed_textView.setVisibility(View.VISIBLE);
                                    thu_textView.setVisibility(View.VISIBLE);
                                    fri_textView.setVisibility(View.VISIBLE);

                                    TimeInfo timeInfo = new TimeInfo(time, subject1, subject2, subject3, subject4, subject5, "");
                                    timekArrayList.add(i, timeInfo);
                                }
                                recyclerView.setAdapter(new TimeTableNewAdapter(context, timekArrayList));

//                                listView.setAdapter(new TimeTableAdapter(TimeTableActivity.this, timekArrayList, getLayoutInflater()));
                                special_title_textView.setVisibility(View.INVISIBLE);
                                special_listView.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                            messageTextView.setVisibility(View.VISIBLE);
                            messageTextView.setText(message);
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
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(TimeTableActivity.this, MenuActivity.class));
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

    public void fetchTimeTable() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_timetable", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        String dayStatus = obj.getString("day_status");
                        if (dayStatus.equalsIgnoreCase("false")) {
                            timekArrayList = new ArrayList<>();
                            JSONArray jsonArrayList = obj.getJSONArray("timetable_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String time = jsonObject.getString("time");
                                //  String sub_time = time.substring(0,5);  now disable
                                String subject1 = jsonObject.getString("subject1");
                                String subject2 = jsonObject.getString("subject2");
                                String subject3 = jsonObject.getString("subject3");
                                String subject4 = jsonObject.getString("subject4");
                                String subject5 = jsonObject.getString("subject5");
                                String subject6 = jsonObject.getString("subject6");
                                mon_textView.setVisibility(View.VISIBLE);
                                tue_textView.setVisibility(View.VISIBLE);
                                wed_textView.setVisibility(View.VISIBLE);
                                thu_textView.setVisibility(View.VISIBLE);
                                fri_textView.setVisibility(View.VISIBLE);
                                sat_textView.setVisibility(View.VISIBLE);
                                TimeInfo timeInfo = new TimeInfo(time, subject1, subject2, subject3, subject4, subject5, subject6);
                                timekArrayList.add(i, timeInfo);
                            }

                            recyclerView.setAdapter(new TimeTableNewAdapter(context, timekArrayList));

//                                listView.setAdapter(new TimeTableAdapter(TimeTableActivity.this, timekArrayList, getLayoutInflater()));

                                /*special_title_textView.setVisibility(View.VISIBLE);
                                ArrayList<SpecialClassInfo> specialClassInfoArrayList = new ArrayList<>();
                                for (int i = 0; i < timekArrayList.size(); i++) {
                                    SpecialClassInfo specialClassInfo = new SpecialClassInfo(timekArrayList.get(i).time, timekArrayList.get(i).subject6);
                                    specialClassInfoArrayList.add(specialClassInfo);
                                }
                                special_listView.setAdapter(new SpecialClassAdapter(TimeTableActivity.this, specialClassInfoArrayList, getLayoutInflater()));*/

                        } else if (dayStatus.equalsIgnoreCase("true")) {
                            timekArrayList = new ArrayList<>();
                            JSONArray jsonArrayList = obj.getJSONArray("timetable_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String time = jsonObject.getString("time");
                                //  String sub_time = time.substring(0,5); now disable
                                String subject1 = jsonObject.getString("subject1");
                                String subject2 = jsonObject.getString("subject2");
                                String subject3 = jsonObject.getString("subject3");
                                String subject4 = jsonObject.getString("subject4");
                                String subject5 = jsonObject.getString("subject5");

                                mon_textView.setVisibility(View.VISIBLE);
                                tue_textView.setVisibility(View.VISIBLE);
                                wed_textView.setVisibility(View.VISIBLE);
                                thu_textView.setVisibility(View.VISIBLE);
                                fri_textView.setVisibility(View.VISIBLE);

                                TimeInfo timeInfo = new TimeInfo(time, subject1, subject2, subject3, subject4, subject5, "");
                                timekArrayList.add(i, timeInfo);
                            }
                            recyclerView.setAdapter(new TimeTableNewAdapter(context, timekArrayList));

//                                listView.setAdapter(new TimeTableAdapter(TimeTableActivity.this, timekArrayList, getLayoutInflater()));
                            special_title_textView.setVisibility(View.INVISIBLE);
                            special_listView.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                        messageTextView.setVisibility(View.VISIBLE);
                        messageTextView.setText(message);
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
