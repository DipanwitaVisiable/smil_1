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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.ViewRequestDirectAdapter;
import com.example.crypedu.Pojo.ViewRequestDirectInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
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

public class WorkActivity extends AppCompatActivity {

    TextView tv_noti_count_class,tv_noti_count_home;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkActivity.this, MenuActivity.class);
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
        tv_noti_count_class= findViewById(R.id.tv_noti_count_class);
        tv_noti_count_home=findViewById(R.id.tv_noti_count_home);
        /*
          Set BubbleGumSans Regular font.
         */
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        TextView homeWork_textView = findViewById(R.id.homeWork_textView);
        homeWork_textView.setTypeface(typeface);
        TextView classWork_textView = findViewById(R.id.classWork_textView);
        classWork_textView.setTypeface(typeface);

        LinearLayout linearLayout_homeWork = findViewById(R.id.linearLayout_homeWork);
        linearLayout_homeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  For displaying data either Teacher or Parents.
                 */
                if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {
                    Intent intent = null;
                    if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                        intent = new Intent(WorkActivity.this, TeacherHomeWorkActivity.class);
                    }else if (Constants.USER_ROLE.equalsIgnoreCase("s")){
                        intent = new Intent(WorkActivity.this, HomeWorkActivity.class);
                    }else if (Constants.USER_ROLE.equalsIgnoreCase("d")){
                        intent = new Intent(WorkActivity.this, HomeDirectWorkActivity.class);
                    }

                    if (intent != null)
                        startActivity(intent);
                }
            }
        });
        LinearLayout linearLayout_classWork = findViewById(R.id.linearLayout_classWork);
        linearLayout_classWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  For displaying data either Teacher or Parents.
                 */
                if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {
                    Intent intent = null;
                    if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                        intent = new Intent(WorkActivity.this, TeacherClassWorkActivity.class);
                    }else if (Constants.USER_ROLE.equalsIgnoreCase("s")){
                        intent = new Intent(WorkActivity.this, ClassWorkActivity.class);
                    }else if (Constants.USER_ROLE.equalsIgnoreCase("d")){
                        intent = new Intent(WorkActivity.this, ClassDirectWorkActivity.class);
                    }
                    if (intent != null)
                        startActivity(intent);
                }
            }
        });

        fetchAssignmentNotification();

    }

    private void fetchAssignmentNotification_old() {
        try {
            final RequestParams params = new RequestParams();
            params.put("student_id", Constants.USER_ID);
            Log.d("WorkValue", "getNoti: "+params);
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.post(Constants.BASE_SERVER + "assignment_count_dtls",params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        String status = obj.getString("status");
                        //String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONObject jsonObject = obj.getJSONObject("count");
                            if (!jsonObject.getString("classwork_count").equalsIgnoreCase("0")){
                                tv_noti_count_class.setVisibility(View.VISIBLE);
                                tv_noti_count_class.setText(jsonObject.getString("classwork_count"));
                            }else {
                                tv_noti_count_class.setVisibility(View.GONE);
                            }
                            if (!jsonObject.getString("homework_count").equalsIgnoreCase("0")){
                                tv_noti_count_home.setVisibility(View.VISIBLE);
                                tv_noti_count_home.setText(jsonObject.getString("homework_count"));
                            }else {
                                tv_noti_count_home.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
            Intent intent = new Intent(WorkActivity.this, LoginActivity.class);
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
                startActivity(new Intent(WorkActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void fetchAssignmentNotification() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "assignment_count_dtls", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    //String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject = obj.getJSONObject("count");
                        if (!jsonObject.getString("classwork_count").equalsIgnoreCase("0")){
                            tv_noti_count_class.setVisibility(View.VISIBLE);
                            tv_noti_count_class.setText(jsonObject.getString("classwork_count"));
                        }else {
                            tv_noti_count_class.setVisibility(View.GONE);
                        }
                        if (!jsonObject.getString("homework_count").equalsIgnoreCase("0")){
                            tv_noti_count_home.setVisibility(View.VISIBLE);
                            tv_noti_count_home.setText(jsonObject.getString("homework_count"));
                        }else {
                            tv_noti_count_home.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
