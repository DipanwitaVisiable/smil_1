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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.ExamAdapter;
import com.example.crypedu.Adapter.ExamDirectAdapter;
import com.example.crypedu.Adapter.ExamSyllabusAdapter;
import com.example.crypedu.Adapter.ExaminationAdapter;
import com.example.crypedu.Adapter.SubjectAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ExamInfo;
import com.example.crypedu.Pojo.ExamSyllabusInfo;
import com.example.crypedu.Pojo.ExaminationInfo;
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

public class ExaminationActivity extends AppCompatActivity {

    private ListView listView_class;
    private LinearLayout title_linearLayout;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<ExamInfo> examListArrayList;
    private ArrayList<String> subjectArrayList;
    private ArrayList<ExamSyllabusInfo> examSubjectArrayList;
    private ArrayList<ExaminationInfo> examList;
    private LinearLayout titleLinearLayout;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
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
                Intent intent = new Intent(ExaminationActivity.this, MenuActivity.class);
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


        titleLinearLayout = findViewById(R.id.titleLinearLayout);
        titleLinearLayout.setVisibility(View.GONE);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);

        /*
          Check internet connection.
         */
        if (InternetCheckActivity.isConnected()){
            fetchSyllabusExamList();
        }else {
            showSnack();
        }

        title_linearLayout = findViewById(R.id.title_linearLayout);
        title_linearLayout.setVisibility(View.GONE);

        listView_class = findViewById(R.id.listView_class);
        listView_class.setVisibility(View.GONE);

        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        TextView class_title_textView = findViewById(R.id.class_title_textView);
        class_title_textView.setTypeface(typeface);
        TextView teacher_title_textView = findViewById(R.id.teacher_title_textView);
        teacher_title_textView.setTypeface(typeface);
        TextView subject_title_textView = findViewById(R.id.subject_title_textView);
        subject_title_textView.setTypeface(typeface);
        TextView chapter_title_textView = findViewById(R.id.chapter_title_textView);
        chapter_title_textView.setTypeface(typeface);
        TextView classes_title_textView = findViewById(R.id.classes_title_textView);
        classes_title_textView.setTypeface(typeface);
        copyRight_textView.setTypeface(typeface);
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
            Intent intent = new Intent(ExaminationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(ExaminationActivity.this, MenuActivity.class));
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
    //--------------------------------
    // Fetch Exam list from server
    // with the 'student id' param.
    //--------------------------------
    public void fetchSyllabusExamList_old(){
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id",Constants.USER_ID);
            Log.d("Exam1", "fetchSyllabusExamList: "+requestParams);
            clientReg.post(Constants.BASE_SERVER +"exams/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        title_linearLayout.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            examListArrayList = new ArrayList<>();
                            listView_class = findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String class_name = jsonObject.getString("exam_name");

                                ExamInfo examInfo = new ExamInfo(id, class_name);
                                examListArrayList.add(i, examInfo);
                            }
                            listView_class.setVisibility(View.VISIBLE);
                            if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                                listView_class.setAdapter(new ExamAdapter(getBaseContext(), examListArrayList, getLayoutInflater()));
                                listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String s = examListArrayList.get(position).id;
                                        fetchExamDetails(s);
                                    }
                                });
                            }
                            if (Constants.USER_ROLE.equalsIgnoreCase("d")){
                                listView_class.setAdapter(new ExamDirectAdapter(getBaseContext(), examListArrayList, getLayoutInflater()));
                                listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String examId = examListArrayList.get(position).id;
                                        Intent intent = new Intent(ExaminationActivity.this, ExamDirectSubActivity.class);
                                        intent.putExtra("exam_id", examId);
                                        startActivity(intent);
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

    //--------------------------------
    // Fetch Exam details
    // with the 'student id' and 'exam_id' param.
    //--------------------------------
    public void fetchExamDetails_old(String examId){
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id",Constants.USER_ID);
            requestParams.put("exam_id", examId);
            Log.d("Exam2", "fetchExamDetails: "+requestParams);
            clientReg.post(Constants.BASE_SERVER +"exam_subjects/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            examList = new ArrayList<>();
                            listView_class = findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String subject = jsonObject.getString("subject");
                                String exam_name = jsonObject.getString("exam_name");
                                String startTime = jsonObject.getString("start_time");
                                String endTime = jsonObject.getString("end_time");
                                String examDate = jsonObject.getString("exam_date");
                                String time = startTime+" "+endTime;
                                ExaminationInfo examinationInfo = new ExaminationInfo(subject, exam_name, time, examDate);
                                examList.add(i, examinationInfo);
                                //if () {
                                   // CalendarHelper.showReminderOneDayBeforeEvent(ExaminationActivity.this, exam_name, subject, examDate, "SMI School");
                                //}
                            }
                            titleLinearLayout.setVisibility(View.VISIBLE);
                            listView_class.setVisibility(View.VISIBLE);
                            listView_class.setClickable(false);
                            listView_class.setAdapter(new ExaminationAdapter(getBaseContext(), examList, getLayoutInflater()));
                            if (InternetCheckActivity.isConnected()){
                                sendNotificationStatus();
                            }else {
                                showSnack();
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
            requestParams.put("student_id",Constants.USER_ID);
            requestParams.put("notification_type","exam");
            Log.d("ExamNoti", "sendNotificationStatus: "+requestParams);
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

    //----------------------------------
    // Fetch subject name from server
    // respective of exam id.
    //----------------------------------
    private void fetchSyllabusExamSubject(String id) {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("exam_id", id);
            clientReg.post(Constants.BASE_SERVER +"exam_subjects/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        title_linearLayout.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            subjectArrayList = new ArrayList<>();
                            listView_class = findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String subject = jsonObject.getString("subject");
                                subjectArrayList.add(subject);
                            }
                            listView_class.setAdapter(new SubjectAdapter(ExaminationActivity.this, subjectArrayList,getLayoutInflater()));
                            listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String s = subjectArrayList.get(position);
                                    fetchSyllabusExamDetails(s);
                                }
                            });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        /*if (requestCode == CalendarHelper.CALENDAR_HELPER_PERMISSION_REQUEST_CODE) {
            if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
                Toast.makeText(this, "Have Calendar Read/Write Permission.", Toast.LENGTH_LONG).show();
            }

        }*/
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //-----------------------------------
    // Fetch all subject details respective
    // of subject name and student Id.
    //-----------------------------------
    private void fetchSyllabusExamDetails_old(String subjectName) {
         /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("subject", subjectName);
            requestParams.put("student_id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER +"subject_syllabus_details/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        title_linearLayout.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            examSubjectArrayList = new ArrayList<>();
                            listView_class = findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String chapter = jsonObject.getString("chapter");
                                String no_of_class = jsonObject.getString("no_of_class");

                                ExamSyllabusInfo examSyllabusInfo = new ExamSyllabusInfo(chapter, no_of_class);
                                examSubjectArrayList.add(examSyllabusInfo);

                            }
                            listView_class.setAdapter(new ExamSyllabusAdapter(ExaminationActivity.this, examSubjectArrayList, getLayoutInflater()));
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

    public void fetchSyllabusExamList() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exams/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    title_linearLayout.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        examListArrayList = new ArrayList<>();
                        listView_class = findViewById(R.id.listView_class);
                        listView_class.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String class_name = jsonObject.getString("exam_name");

                            ExamInfo examInfo = new ExamInfo(id, class_name);
                            examListArrayList.add(i, examInfo);
                        }
                        listView_class.setVisibility(View.VISIBLE);
                        if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                            listView_class.setAdapter(new ExamAdapter(getBaseContext(), examListArrayList, getLayoutInflater()));
                            listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String s = examListArrayList.get(position).id;
                                    fetchExamDetails(s);
                                }
                            });
                        }
                        if (Constants.USER_ROLE.equalsIgnoreCase("d")){
                            listView_class.setAdapter(new ExamDirectAdapter(getBaseContext(), examListArrayList, getLayoutInflater()));
                            listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String examId = examListArrayList.get(position).id;
                                    Intent intent = new Intent(ExaminationActivity.this, ExamDirectSubActivity.class);
                                    intent.putExtra("exam_id", examId);
                                    startActivity(intent);
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

    public void fetchExamDetails(final String examId){
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exam_subjects/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        examList = new ArrayList<>();
                        listView_class = findViewById(R.id.listView_class);
                        listView_class.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String subject = jsonObject.getString("subject");
                            String exam_name = jsonObject.getString("exam_name");
                            String startTime = jsonObject.getString("start_time");
                            String endTime = jsonObject.getString("end_time");
                            String examDate = jsonObject.getString("exam_date");
                            String time = startTime+" "+endTime;
                            ExaminationInfo examinationInfo = new ExaminationInfo(subject, exam_name, time, examDate);
                            examList.add(i, examinationInfo);
                            //if () {
                            // CalendarHelper.showReminderOneDayBeforeEvent(ExaminationActivity.this, exam_name, subject, examDate, "SMI School");
                            //}
                        }
                        titleLinearLayout.setVisibility(View.VISIBLE);
                        listView_class.setVisibility(View.VISIBLE);
                        listView_class.setClickable(false);
                        listView_class.setAdapter(new ExaminationAdapter(getBaseContext(), examList, getLayoutInflater()));
                        if (InternetCheckActivity.isConnected()){
                            sendNotificationStatus();
                        }else {
                            showSnack();
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
                params.put("exam_id", examId);
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

    public void sendNotificationStatus() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_status/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
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
                params.put("notification_type","exam");
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

    public void fetchSyllabusExamDetails(final String subjectName){
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "subject_syllabus_details/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    title_linearLayout.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        examSubjectArrayList = new ArrayList<>();
                        listView_class = findViewById(R.id.listView_class);
                        listView_class.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String chapter = jsonObject.getString("chapter");
                            String no_of_class = jsonObject.getString("no_of_class");

                            ExamSyllabusInfo examSyllabusInfo = new ExamSyllabusInfo(chapter, no_of_class);
                            examSubjectArrayList.add(examSyllabusInfo);

                        }
                        listView_class.setAdapter(new ExamSyllabusAdapter(ExaminationActivity.this, examSubjectArrayList, getLayoutInflater()));
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
                params.put("subject", subjectName);
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
