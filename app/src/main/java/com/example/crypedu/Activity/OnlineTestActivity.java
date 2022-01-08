package com.example.crypedu.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.OnlineTestListAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.OnlineTestInfo;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnlineTestActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Context context;
    private ArrayList<OnlineTestInfo> testList;
    private RecyclerView rv_test_list;
    private ProgressBar pb_loader;
    private TextView tv_no_exam;
    private String reg_id;

    private WebView wv_pay_online;
    private AppBarLayout app_bar;
    private Button btn_test;
    private String exam_name, exam_id, total_question, total_time, exam_taken_id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_days_list);
        setContentView(R.layout.activity_online_test_list);

        btn_test =findViewById(R.id.btn_test);
        app_bar =findViewById(R.id.app_bar);
        app_bar.setVisibility(View.VISIBLE);
        wv_pay_online =findViewById(R.id.wv_pay_online);
        wv_pay_online.getSettings().setJavaScriptEnabled(true);


        context = OnlineTestActivity.this;
        pb_loader=findViewById(R.id.pb_loader);
        tv_no_exam=findViewById(R.id.tv_no_exam);

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        rv_test_list=findViewById(R.id.rv_result_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_test_list.setLayoutManager(layoutManager);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        if (InternetCheckActivity.isConnected()) {
            getQuizList();
        }else {
            Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
        }

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_test.getText().toString().trim().equals("Start Test")) {
//                if (btn_test.getText().toString().trim().equals("Exam Completed") || btn_test.getText().toString().trim().equals("Exam Not Completed") || btn_test.getText().toString().trim().equals("Start Test")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.quiz_dialog_view);
                    dialog.setCancelable(true);
                    Button noB = dialog.findViewById(R.id.noButton);
                    Button yesB = dialog.findViewById(R.id.yesButton);
                    TextView textViewTopic = dialog.findViewById(R.id.testTopic);
                    TextView questions = dialog.findViewById(R.id.testQuestions);
                    TextView duration = dialog.findViewById(R.id.duration);
                    textViewTopic.setText(exam_name);
                    questions.setText(total_question);
                    duration.setText(total_time + " min");
                    noB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    yesB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(context, SmallQuizTestActivity.class);
                            intent.putExtra("quiz_id", exam_id);
                            intent.putExtra("time", total_time);
                            context.startActivity(intent);

                        }
                    });
                    dialog.show();
                }
                else if (btn_test.getText().toString().trim().equals("View Result"))
//                else if (btn_test.getText().toString().trim().equals("View Result") || btn_test.getText().toString().trim().equals("Exam Completed"))
                {
                    Intent intent = new Intent(context, OnlineTestResultActivity.class);
                    intent.putExtra("exam_taken_id", exam_taken_id);
                    intent.putExtra("tot_qus", total_question);
                    intent.putExtra("exam_id", exam_id);
                    context.startActivity(intent);

                }
            }
        });

    }


    public void getQuizList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exam_test", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        tv_no_exam.setVisibility(View.GONE);
                        testList=new ArrayList<>();

                        if (jsonObject.optString("message")!=null && !jsonObject.optString("message").equals("")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("message");
                            if (jsonArray.length() > 0) {
                                rv_test_list.setVisibility(View.GONE);
                                wv_pay_online.setVisibility(View.VISIBLE);
                                btn_test.setVisibility(View.VISIBLE);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    wv_pay_online.loadUrl(jsonObject1.getString("page_link"));
                                    exam_taken_id = jsonObject1.getString("exam_taken_id");
                                    exam_id = jsonObject1.getString("exam_id");
                                    exam_name = jsonObject1.getString("exam_name");
                                    total_time = jsonObject1.getString("tot_time");
                                    total_question = jsonObject1.getString("tot_qus");
                                    btn_test.setText(jsonObject1.getString("status_text"));
                                }
                            }
                        }
                        if (jsonObject.optString("result_array")!=null && !jsonObject.optString("result_array").equals("")) {
                            JSONArray result_array = jsonObject.getJSONArray("result_array");
                            if (result_array.length() > 0) {
                                rv_test_list.setVisibility(View.VISIBLE);
                                wv_pay_online.setVisibility(View.GONE);
                                btn_test.setVisibility(View.GONE);
                                for (int i = 0; i < result_array.length(); i++) {
                                    JSONObject jsonObject2 = result_array.getJSONObject(i);
                                    OnlineTestInfo onlineTestInfo = new OnlineTestInfo();
                                    onlineTestInfo.setExam_id(jsonObject2.getString("exam_id"));
                                    onlineTestInfo.setExam_name(jsonObject2.getString("exam_name"));
                                    onlineTestInfo.setTot_time(jsonObject2.getString("tot_time"));
                                    onlineTestInfo.setTot_qus(jsonObject2.getString("tot_qus"));
                                    onlineTestInfo.setSubject_name(jsonObject2.getString("subject_name"));
                                    onlineTestInfo.setExam_date(jsonObject2.getString("exam_date"));
                                    onlineTestInfo.setStatus(jsonObject2.getString("status"));
                                    onlineTestInfo.setStatus_text(jsonObject2.getString("status_text"));
                                    onlineTestInfo.setExam_taken_id(jsonObject2.getString("exam_taken_id"));
                                    onlineTestInfo.setChapter_name("http://nirbadhngo.com/snrmemorialtrust/smil_link/182");
                                    testList.add(onlineTestInfo);
                                }
                                rv_test_list.setAdapter(new OnlineTestListAdapter(context, testList));
                            }
                        }


                    }
                    else if (status.equalsIgnoreCase("206"))
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage(R.string.force_logout)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else
                    {
                        tv_no_exam.setVisibility(View.VISIBLE);
                        tv_no_exam.setText("Now you have no online test.");
//                        Toast.makeText(context, "No Exam found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loader.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loader.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Constants.USER_ID);
//                params.put("student_id", "2623");
                params.put("reg_id", reg_id);
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

        // Start- To remove timeout error
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
        // End- To remove timeout error
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, MenuActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
