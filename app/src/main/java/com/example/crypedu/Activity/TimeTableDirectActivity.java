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
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.example.crypedu.Adapter.TimeTableNewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.SpecialClassAdapter;
import com.example.crypedu.Adapter.TimeTableAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.SpecialClassInfo;
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

public class TimeTableDirectActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private ListView listView;
    private ArrayList<TimeInfo> timekArrayList;
    private ArrayList<String> timeTableArrayList;
    private ListView special_listView;
    private TextView special_title_textView;
    private TextView messageTextView;
    private Spinner classSpinner, sectionSpinner;
    private String classString = null, sectionString = null;
    private Button submitButton;
    private ArrayList<String> classList = new ArrayList<>(), sectionList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_direct);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/


        Toolbar toolbar = findViewById(R.id.toolbar);

        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        submitButton = findViewById(R.id.submit_spin);
        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    sectionString = sectionSpinner.getItemAtPosition(position).toString();
                    Toast.makeText(TimeTableDirectActivity.this, sectionString, Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    classString = classSpinner.getItemAtPosition(position).toString();
                    Toast.makeText(TimeTableDirectActivity.this, classString, Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classString == null || sectionString == null) {
                    Toast.makeText(TimeTableDirectActivity.this, "Required information", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetCheckActivity.isConnected()) {
                        fetchTimeTable(classString, sectionString);
                        submitButton.setVisibility(View.GONE);
                    }else {
                        showSnack();
                    }
                }
            }
        });
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimeTableDirectActivity.this, MenuActivity.class);
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

        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        messageTextView = findViewById(R.id.messageTextView);
        messageTextView.setTypeface(typeface);

        TextView mon_textView = findViewById(R.id.mon_textView);
        mon_textView.setTypeface(typeface);
        TextView tue_textView = findViewById(R.id.tue_textView);
        tue_textView.setTypeface(typeface);
        TextView wed_textView = findViewById(R.id.wed_textView);
        wed_textView.setTypeface(typeface);
        TextView thu_textView = findViewById(R.id.thu_textView);
        thu_textView.setTypeface(typeface);
        TextView fri_textView = findViewById(R.id.fri_textView);
        fri_textView.setTypeface(typeface);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        listView = findViewById(R.id.listView);
        special_listView = findViewById(R.id.special_listView);
        special_title_textView = findViewById(R.id.special_title_textView);
        special_title_textView.setTypeface(typeface);
        special_title_textView.setVisibility(View.INVISIBLE);
        /*
          If internet connection is working
          then only call getClassSection().
         */
        if (InternetCheckActivity.isConnected()) {
            getClassSection();
        } else {
            showSnack();
        }

    }

    private void getClassSection() {
        final StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_class_all", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("class_details");
                        Log.e("All class ", jsonArray.toString());
                        JSONObject object;
                        classList.add("Select class");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            classList.add(object.getString("class_or_year"));
                        }
                        adapter = new ArrayAdapter<String>(TimeTableDirectActivity.this, R.layout.spinner_layout, classList){
                            @Override
                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if(position == 0){
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                }
                                if (position % 2 == 1) {
                                    // Set the item background color
                                    tv.setBackgroundColor(Color.parseColor("#FFE4B389"));
                                } else {
                                    // Set the alternate item background color
                                    tv.setBackgroundColor(Color.parseColor("#FF99551B"));
                                }
                                return view;
                            }
                        };
                        classSpinner.setAdapter(adapter);
                        jsonArray = jsonObject.getJSONArray("section_details");
                        sectionList.add("Select section");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            sectionList.add(object.getString("section"));
                        }
                        adapter = new ArrayAdapter<String>(TimeTableDirectActivity.this, R.layout.spinner_layout, sectionList){
                            @Override
                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if(position == 0){
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                }
                                if (position % 2 == 1) {
                                    // Set the item background color
                                    tv.setBackgroundColor(Color.parseColor("#FFE4B389"));
                                } else {
                                    // Set the alternate item background color
                                    tv.setBackgroundColor(Color.parseColor("#FF99551B"));
                                }
                                return view;
                            }
                        };
                        sectionSpinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(TimeTableDirectActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String sms = "";

                if (error.networkResponse == null) {
                    if (error instanceof NetworkError) {
                        sms = "Oops. Network error! Try again";

                    } else if (error instanceof ServerError) {
                        sms = "Oops. Server error!";

                    } else if (error instanceof AuthFailureError) {
                        sms = "Oops. Authentication error! Try again";

                    } else if (error instanceof TimeoutError) {
                        sms = "Oops. Timeout error! Try again";
                    }
                }
                Toast.makeText(TimeTableDirectActivity.this, sms, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
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
            Intent intent = new Intent(TimeTableDirectActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //--------------------------------
    // Fetch time table from server
    // respective of student id.
    //--------------------------------
    public void fetchTimeTable_old(String classSt, String sectionSt) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("class", classSt);
            requestParams.put("section", sectionSt);
            clientReg.post(Constants.BASE_SERVER + "get_timetable_dir/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {

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

                                listView.setAdapter(new TimeTableAdapter(TimeTableDirectActivity.this, timekArrayList, getLayoutInflater()));
                                special_title_textView.setVisibility(View.VISIBLE);
                                ArrayList<SpecialClassInfo> specialClassInfoArrayList = new ArrayList<>();
                                for (int i = 0; i < timekArrayList.size(); i++) {
                                    SpecialClassInfo specialClassInfo = new SpecialClassInfo(timekArrayList.get(i).time, timekArrayList.get(i).subject6);
                                    specialClassInfoArrayList.add(specialClassInfo);
                                }
                                special_listView.setAdapter(new SpecialClassAdapter(TimeTableDirectActivity.this, specialClassInfoArrayList, getLayoutInflater()));
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

                                listView.setAdapter(new TimeTableAdapter(TimeTableDirectActivity.this, timekArrayList, getLayoutInflater()));
                                special_title_textView.setVisibility(View.INVISIBLE);
                                special_listView.setVisibility(View.INVISIBLE);
                            }
                            pbHeaderProgress.setVisibility(View.GONE);
                        } else {
                            pbHeaderProgress.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);
                            special_listView.setVisibility(View.GONE);
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
                    Toast.makeText(TimeTableDirectActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    pbHeaderProgress.setVisibility(View.GONE);
                    submitButton.setVisibility(View.VISIBLE);
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
                startActivity(new Intent(TimeTableDirectActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void fetchTimeTable(final String classSt, final String sectionSt) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_timetable_dir/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
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

                            listView.setAdapter(new TimeTableAdapter(TimeTableDirectActivity.this, timekArrayList, getLayoutInflater()));
                            special_title_textView.setVisibility(View.VISIBLE);
                            ArrayList<SpecialClassInfo> specialClassInfoArrayList = new ArrayList<>();
                            for (int i = 0; i < timekArrayList.size(); i++) {
                                SpecialClassInfo specialClassInfo = new SpecialClassInfo(timekArrayList.get(i).time, timekArrayList.get(i).subject6);
                                specialClassInfoArrayList.add(specialClassInfo);
                            }
                            special_listView.setAdapter(new SpecialClassAdapter(TimeTableDirectActivity.this, specialClassInfoArrayList, getLayoutInflater()));
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

                            listView.setAdapter(new TimeTableAdapter(TimeTableDirectActivity.this, timekArrayList, getLayoutInflater()));
                            special_title_textView.setVisibility(View.INVISIBLE);
                            special_listView.setVisibility(View.INVISIBLE);
                        }
                        pbHeaderProgress.setVisibility(View.GONE);
                    } else {
                        pbHeaderProgress.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        special_listView.setVisibility(View.GONE);
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
                params.put("class", classSt);
                params.put("section", sectionSt);
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
