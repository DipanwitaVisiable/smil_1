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
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.FirstTermAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.FirstTermInfo;
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

public class FirstTermActivity extends AppCompatActivity {

    private ListView listView_firstTerm;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<String> subjectArrayList;
    private ArrayList<FirstTermInfo> firstTermInfoArrayList;
    private LinearLayout titleLinearLayout;
    private TextView period, note, subject, halfYearly, marks, grade;
    Typeface typeface;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_term);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        requestQueue = Volley.newRequestQueue(this);

        period = findViewById(R.id.p1);
        note = findViewById(R.id.n1);
        subject = findViewById(R.id.s1);
        halfYearly = findViewById(R.id.h1);
        marks = findViewById(R.id.m1);
        grade = findViewById(R.id.g1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstTermActivity.this, ResultActivity.class);
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


        typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);
        titleLinearLayout = findViewById(R.id.titleLinearLayout);
        titleLinearLayout.setVisibility(View.GONE);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        if (InternetCheckActivity.isConnected()){
            fetchFirstTermResultDetails();
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
            Intent intent = new Intent(FirstTermActivity.this, LoginActivity.class);
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
                startActivity(new Intent(FirstTermActivity.this, ResultActivity.class));
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

    public void fetchFirstTermResultDetails_old(){
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id",Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER +"first_term_result", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        String classCheck = obj.getString("class");
                        if (status.equalsIgnoreCase("200")) {

                            /*if (classCheck.equalsIgnoreCase("I") || classCheck.equalsIgnoreCase("II")){
                                period.setText("Periodic Test");
                                note.setText("Note Book");
                                subject.setText("Subject Enrichment");
                                halfYearly.setText("Half Yearly Exam");
                                marks.setVisibility(View.GONE);
                            }*/ //code off on 14/10/2020
                            firstTermInfoArrayList = new ArrayList<>();
                            listView_firstTerm = findViewById(R.id.listView_firstTerm);
                            listView_firstTerm.setVisibility(View.VISIBLE);
                            ViewCompat.setNestedScrollingEnabled(listView_firstTerm, false);
                            JSONArray jsonArrayList = obj.getJSONArray("term_result");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String subject = jsonObject.getString("subject");
                                String periodic_test = jsonObject.getString("periodic_test");
                                String note_book = jsonObject.getString("note_book");
                                String subject_enrichment = jsonObject.getString("subject_enrichment");
                                String half_early_exam = jsonObject.getString("half_early_exam");
                                String marks_obtained = jsonObject.getString("marks_obtained");
                                String grade = jsonObject.getString("grade");

                                FirstTermInfo firstTermInfo = new FirstTermInfo(classCheck, subject, periodic_test, note_book, subject_enrichment,half_early_exam,marks_obtained,grade);
                                firstTermInfoArrayList.add(i, firstTermInfo);
                            }
                            titleLinearLayout.setVisibility(View.VISIBLE);
                            listView_firstTerm.setVisibility(View.VISIBLE);
                            listView_firstTerm.setClickable(false);
                            listView_firstTerm.setAdapter(new FirstTermAdapter(getBaseContext(), firstTermInfoArrayList, getLayoutInflater()));
                            if(InternetCheckActivity.isConnected()){
                               // sendNotificationStatus();
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

    public void fetchFirstTermResultDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "first_term_result", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    String classCheck = obj.getString("class");
                    if (status.equalsIgnoreCase("200")) {

                            /*if (classCheck.equalsIgnoreCase("I") || classCheck.equalsIgnoreCase("II")){
                                period.setText("Periodic Test");
                                note.setText("Note Book");
                                subject.setText("Subject Enrichment");
                                halfYearly.setText("Half Yearly Exam");
                                marks.setVisibility(View.GONE);
                            }*/ //code off on 14/10/2020
                        firstTermInfoArrayList = new ArrayList<>();
                        listView_firstTerm = findViewById(R.id.listView_firstTerm);
                        listView_firstTerm.setVisibility(View.VISIBLE);
                        ViewCompat.setNestedScrollingEnabled(listView_firstTerm, false);
                        JSONArray jsonArrayList = obj.getJSONArray("term_result");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String subject = jsonObject.getString("subject");
                            String periodic_test = jsonObject.getString("periodic_test");
                            String note_book = jsonObject.getString("note_book");
                            String subject_enrichment = jsonObject.getString("subject_enrichment");
                            String half_early_exam = jsonObject.getString("half_early_exam");
                            String marks_obtained = jsonObject.getString("marks_obtained");
                            String grade = jsonObject.getString("grade");

                            FirstTermInfo firstTermInfo = new FirstTermInfo(classCheck, subject, periodic_test, note_book, subject_enrichment,half_early_exam,marks_obtained,grade);
                            firstTermInfoArrayList.add(i, firstTermInfo);
                        }
                        titleLinearLayout.setVisibility(View.VISIBLE);
                        listView_firstTerm.setVisibility(View.VISIBLE);
                        listView_firstTerm.setClickable(false);
                        listView_firstTerm.setAdapter(new FirstTermAdapter(getBaseContext(), firstTermInfoArrayList, getLayoutInflater()));
                        if(InternetCheckActivity.isConnected()){
                            // sendNotificationStatus();
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
                params.put("student_id",Constants.USER_ID);
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
