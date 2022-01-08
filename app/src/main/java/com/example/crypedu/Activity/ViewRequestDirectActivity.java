package com.example.crypedu.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.ReplyCommunicationAdapter;
import com.example.crypedu.Adapter.ViewRequestDirectAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ReplyCommunicationInfo;
import com.example.crypedu.Pojo.ViewRequestDirectInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ViewRequestDirectActivity extends AppCompatActivity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    private CoordinatorLayout coordinatorLayout;
    private ArrayList<ViewRequestDirectInfo> viewRequestList;
    private ListView listView;
    private ArrayList<ReplyCommunicationInfo> replyList;
    private Spinner classSpinner, sectionSpinner;
    private String classString = null, sectionString = null;
    private JSONObject jsonObject = null;
    private Button submitButton, pickDate;
    private int year = 0, month = 0, day = 0, dateFlag = 0, sectionFlag = 0;
    private String dateString = "";
    private ArrayList<String> classList = new ArrayList<>(), sectionList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    SimpleDateFormat simpleDateFormat;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_direct);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);

        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);

        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        submitButton = findViewById(R.id.submit_spin);
        pickDate = findViewById(R.id.pickDateBn);
        /*
          If internet connection is working
          then only call fetchClassDetails().
         */
        if (InternetCheckActivity.isConnected()) {
            getClassSection();
        } else {
            showSnack();
        }

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    sectionString = sectionSpinner.getItemAtPosition(position).toString();
                    sectionFlag = 1;
                    Toast.makeText(ViewRequestDirectActivity.this, sectionString, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ViewRequestDirectActivity.this, classString, Toast.LENGTH_LONG).show();
                    submitButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SpinnerDatePickerDialogBuilder()
                        .context(ViewRequestDirectActivity.this)
                        .callback(ViewRequestDirectActivity.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(year, month, day)
                        .build()
                        .show();

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
               /* if (classString == null) {
                    Toast.makeText(ViewRequestDirectActivity.this, "Please select class", Toast.LENGTH_LONG).show();
                    return;
                }
                if (sectionFlag == 1 && classString == null) {
                    Toast.makeText(ViewRequestDirectActivity.this, "Please select class", Toast.LENGTH_LONG).show();
                    return;
                }*/
                if (classString == null && sectionString == null) {
                    Toast.makeText(ViewRequestDirectActivity.this, "Required information", Toast.LENGTH_LONG).show();
                }else {
                    if (InternetCheckActivity.isConnected()) {
                        if (dateString.isEmpty() && dateFlag == 0){
                            pickDate.setText("Pick date(Optional");
                        }
                        fetchRequestDetails(classString, sectionString);
                        submitButton.setVisibility(View.GONE);
                    } else {
                        showSnack();
                    }
                }

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ViewRequestDirectActivity.this, MenuActivity.class);
                startActivity(intent);*/
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

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        listView = findViewById(R.id.listView);
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
                        adapter = new ArrayAdapter<String>(ViewRequestDirectActivity.this, R.layout.spinner_layout, classList) {
                            @Override
                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
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
                        adapter = new ArrayAdapter<String>(ViewRequestDirectActivity.this, R.layout.spinner_layout, sectionList) {
                            @Override
                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
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
                        Toast.makeText(ViewRequestDirectActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewRequestDirectActivity.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void showSnack() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
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
            Intent intent = new Intent(ViewRequestDirectActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //--------------------------------
    // Fetch Request details from server
    // respective of student_id.
    //--------------------------------
    public void fetchRequestDetails_old(String classSt, String section) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("class", classSt);
            requestParams.put("section", section);
            if (dateString.isEmpty() && dateFlag == 0) {
                requestParams.put("date", "");
            } else if (!dateString.isEmpty() && dateFlag == 1) {
                requestParams.put("date", dateString);
                dateFlag = 0;
                dateString = "";
            }
            clientReg.post(Constants.BASE_SERVER + "communicate_dir/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        viewRequestList = new ArrayList<>();
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArray = obj.getJSONArray("notification_status");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String subjectStr = jsonObject.getString("subject");
                                String classStr = jsonObject.getString("class");
                                String sectionStr = jsonObject.getString("section");
                                String messageStr = jsonObject.getString("message");
                                String fromDateStr = jsonObject.getString("date");
                                String typeStr = jsonObject.getString("type");

                                ViewRequestDirectInfo viewRequestInfo = new ViewRequestDirectInfo(name, subjectStr, messageStr, fromDateStr, typeStr, classStr, sectionStr);
                                viewRequestList.add(i, viewRequestInfo);
                            }
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(new ViewRequestDirectAdapter(ViewRequestDirectActivity.this, viewRequestList, getLayoutInflater()));
                            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String commId = viewRequestList.get(position).commId;
                                    if (InternetCheckActivity.isConnected()){
                                        communicationReply(commId);
                                        Constants.communicationId = commId;
                                    }else {
                                        showSnack();
                                    }
                                }
                            });*/
                        } else {
                            listView.setVisibility(View.GONE);
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
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

    //------------------------------------------
    // Fetch communication details respective of
    // communication ID.
    //------------------------------------------
    private void communicationReply(String commId) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("communicate_id", commId);
            clientReg.post(Constants.BASE_SERVER + "reply_communicate/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        replyList = new ArrayList<>();
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArray = obj.getJSONArray("notification_reply");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String replyMessage = jsonObject.getString("reply_message");
                                String requestBy = jsonObject.getString("request_by");
                                String date = jsonObject.getString("created_date");

                                ReplyCommunicationInfo replyCommunicationInfo = new ReplyCommunicationInfo(replyMessage, requestBy, date);
                                replyList.add(i, replyCommunicationInfo);
                            }
                            listView.setAdapter(new ReplyCommunicationAdapter(getBaseContext(), replyList, getLayoutInflater()));
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

    //------------------------------------------
    // Reply communication respective of
    // communication ID.
    //------------------------------------------
    private void communicationReplySubmit(String commId, String message) {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("communicate_id", commId);
            requestParams.put("reply_msg", message);
            clientReg.post(Constants.BASE_SERVER + "reply_communicate_submit/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        replyList = new ArrayList<>();
                        if (status.equalsIgnoreCase("200")) {
                            startActivity(new Intent(ViewRequestDirectActivity.this, ViewRequestDirectActivity.class));
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

    private void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month + 1;
        this.day = day;
        pickDate.setText(this.day + "-" + this.month + "-" + this.year);
        dateFlag = 1;
        dateString = pickDate.getText().toString().trim();
        submitButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }

    public void fetchRequestDetails(final String classSt, final String section) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "communicate_dir/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    viewRequestList = new ArrayList<>();
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = obj.getJSONArray("notification_status");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String subjectStr = jsonObject.getString("subject");
                            String classStr = jsonObject.getString("class");
                            String sectionStr = jsonObject.getString("section");
                            String messageStr = jsonObject.getString("message");
                            String fromDateStr = jsonObject.getString("date");
                            String typeStr = jsonObject.getString("type");

                            ViewRequestDirectInfo viewRequestInfo = new ViewRequestDirectInfo(name, subjectStr, messageStr, fromDateStr, typeStr, classStr, sectionStr);
                            viewRequestList.add(i, viewRequestInfo);
                        }
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(new ViewRequestDirectAdapter(ViewRequestDirectActivity.this, viewRequestList, getLayoutInflater()));
                            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String commId = viewRequestList.get(position).commId;
                                    if (InternetCheckActivity.isConnected()){
                                        communicationReply(commId);
                                        Constants.communicationId = commId;
                                    }else {
                                        showSnack();
                                    }
                                }
                            });*/
                    } else {
                        listView.setVisibility(View.GONE);
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
                params.put("class", classSt);
                params.put("section", section);
                if (dateString.isEmpty() && dateFlag == 0) {
                    params.put("date", "");
                } else if (!dateString.isEmpty() && dateFlag == 1) {
                    params.put("date", dateString);
                    dateFlag = 0;
                    dateString = "";
                }
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
