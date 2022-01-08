package com.example.crypedu.Activity;

import android.content.Intent;
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
import android.widget.LinearLayout;
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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.ExaminationDirectAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ExaminationInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ExamDirectSubActivity extends AppCompatActivity {


    private ListView listView;
    private Spinner classSpinner;
    private List<ExaminationInfo> examList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private String classString = null;
    private ArrayList<String> classList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private LinearLayout linearLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_direct_sub);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        listView = findViewById(R.id.list_view);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        linearLayout = findViewById(R.id.titleLinearLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ExamDirectSubActivity.this, MenuActivity.class);
                startActivity(intent);*/
                onBackPressed();
            }
        });
        // collect exam id from intent
        Intent intent = getIntent();
        final String exam_id = intent.getStringExtra("exam_id").trim();

        classSpinner = findViewById(R.id.classSpinner);
        FloatingActionButton submitButton = findViewById(R.id.class_search);
        if (InternetCheckActivity.isConnected()) {
            getAllClass();
        } else {
            showSnack();
        }
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    classString = classSpinner.getItemAtPosition(position).toString();
                    Toast.makeText(ExamDirectSubActivity.this, classString, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classString == null) {
                    Toast.makeText(ExamDirectSubActivity.this, "Required information", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetCheckActivity.isConnected()) {
                        fetchExamDetails(classString, exam_id);
                    }else {
                        showSnack();
                    }
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAllClass() {
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
                        adapter = new ArrayAdapter<String>(ExamDirectSubActivity.this, R.layout.spinner_layout, classList){
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

                    } else {
                        Toast.makeText(ExamDirectSubActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ExamDirectSubActivity.this, "Something goes to wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    //--------------------------------
    // Fetch Exam details
    // with the 'class' and 'exam_id' param.
    //--------------------------------
    public void fetchExamDetails(final String classSt, final String examId) {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);


        StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exam_subjects_dir/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = object.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArrayList = object.getJSONArray("request_data");
                        Log.e("Exam Array ", jsonArrayList.toString());
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String subject = jsonObject.getString("subject");
                            String exam_name = jsonObject.getString("exam_name");
                            String startTime = jsonObject.getString("start_time");
                            String endTime = jsonObject.getString("end_time");
                            String examDate = jsonObject.getString("exam_date");
                            String time = startTime + " " + endTime;

                            ExaminationInfo examinationInfo = new ExaminationInfo(subject, exam_name, time, examDate);
                            examList.add(i, examinationInfo);
                        }
                        ExaminationDirectAdapter adapter = new ExaminationDirectAdapter(ExamDirectSubActivity.this, examList);
                        linearLayout.setVisibility(View.VISIBLE);
                        listView.setAdapter(adapter);
                        if (InternetCheckActivity.isConnected()) {
                            sendNotificationStatus();
                        } else {
                            showSnack();
                        }
                    } else {
                        linearLayout.setVisibility(View.GONE);
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError){
                    Toast.makeText(ExamDirectSubActivity.this, "Time out error, try again later", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ExamDirectSubActivity.this, "Network error, try again later", Toast.LENGTH_LONG).show();

                }
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new Hashtable<>();
                params.put("class", classSt);
                params.put("exam_id", examId);
                return params;
            }
        };

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

    //------------------------------
    // Send notification status to server
    // respective of student_id and notification_type.
    //------------------------------
    public void sendNotificationStatus_old() {
        /*
          ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Constants.USER_ID);
            requestParams.put("notification_type", "exam");
            clientReg.post(Constants.BASE_SERVER + "notification_status/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {

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
    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
               // startActivity(new Intent(ExaminationActivity.this, MenuActivity.class));
                onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendNotificationStatus() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
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
                    pbHeaderProgress.setVisibility(View.GONE);

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
                params.put("notification_type", "exam");
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
//        requestQueue.add(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
