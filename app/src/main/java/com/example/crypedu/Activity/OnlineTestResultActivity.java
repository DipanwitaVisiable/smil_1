package com.example.crypedu.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.crypedu.Constants.Constants;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnlineTestResultActivity extends AppCompatActivity {

    PieChart pieChart;
    PieDataSet dataSet;
    PieData data;
    ProgressBar progressBar;
    ArrayList<Entry> yvalues = new ArrayList<>();
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<>();
    TextView incorrectAns, correctAns, notAns;
    private TextView tv_total_question, tv_total_time, tv_message;
    LinearLayout linearLayout1, linearLayout2;
    Button btn_view_solution;
    Context context;
    String exam_id, test_time, exam_taken_id,tot_qus, reg_id;
    private StringRequest stringRequest_viewResult;
    private RequestQueue requestQueue_viewResult ;
    private Toolbar toolbar;
    int total_correct_int=0, total_wrong_int=0, total_attempt_int=0, total_skip_int=0;

    private LinearLayout ll_home, ll_profile, ll_exam, ll_share;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_test_result);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=OnlineTestResultActivity.this;

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        exam_taken_id = intent.getStringExtra("exam_taken_id");
        tot_qus = intent.getStringExtra("tot_qus");
        exam_id = intent.getStringExtra("exam_id");

        pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(false);
        progressBar = findViewById(R.id.wheel_refresh);
        tv_total_question = findViewById(R.id.tv_total_question);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_message = findViewById(R.id.tv_message);
        linearLayout1 = findViewById(R.id.l1);
        linearLayout2 = findViewById(R.id.l2);
        incorrectAns = findViewById(R.id.inc_ans);
        correctAns = findViewById(R.id.c_ans);
        notAns = findViewById(R.id.not_ans);
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        btn_view_solution=findViewById(R.id.btn_view_solution);

        if (InternetCheckActivity.isConnected()) {
            getResult();
        }else {
            Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
        }


        btn_view_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SmallQuizViewSolutionActivity.class);
                intent.putExtra("exam_taken_id", exam_taken_id);
                intent.putExtra("exam_id", exam_id);
                context.startActivity(intent);
            }
        });


    }

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }


    private void getResult() {
        requestQueue_viewResult= Volley.newRequestQueue(context);
        progressBar.setVisibility(View.VISIBLE);
        stringRequest_viewResult=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "view_result",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            final JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("200")) {
                                pieChart.setVisibility(View.VISIBLE);
                                JSONObject object = new JSONObject(jsonObject.getString("message"));

                                pieChart.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorBlack));

                                int total_correct_int=0, total_wrong_int=0, total_attempt_int=0, total_skip_int=0;

                                total_correct_int=Integer.parseInt(object.optString("correct"));
                                total_wrong_int=Integer.parseInt(object.optString("incorrect"));

                                tv_total_question.setText("Total Questions: " + tot_qus);
                                tv_message.setText("Total correct: " + object.optString("correct") + "   |   " +
                                        "Total wrong: " + object.optString("incorrect"));

                                if (total_correct_int > 0) {

                                    yvalues.add(new Entry(total_correct_int, 0));
                                    colors.add(getResources().getColor(R.color.colorPresent));
//                                    xVals.add("Correct");
                                    xVals.add(" ");
                                    correctAns.setText(String.valueOf(total_correct_int));
                                }
                                if (total_wrong_int > 0) {
                                    yvalues.add(new Entry(total_wrong_int, 1));
                                    colors.add(getResources().getColor(R.color.incorrect));
//                                    xVals.add("Wrong");
                                    xVals.add(" ");
                                    incorrectAns.setText(String.valueOf(total_wrong_int));
                                }


                                pieChart.setDescription("");
                                dataSet = new PieDataSet(yvalues, "");

                                data = new PieData(xVals, dataSet);
                                pieChart.setData(data);
                                pieChart.setDrawHoleEnabled(false);
                                pieChart.setTransparentCircleRadius(20f);
                                pieChart.setRotationEnabled(false);//For Rotation
                                pieChart.setDrawSliceText(false);//For Text
                                pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad); // Rotate Event
                                dataSet.setSliceSpace(1f);
                                dataSet.setColors(colors);
                                data.setValueTextSize(13f);
                                pieChart.setDrawHoleEnabled(true);
                                data.setValueTextColor(Color.WHITE);
                                data.setValueFormatter(new MyValueFormatter());
                                pieChart.setTouchEnabled(false);
                                linearLayout1.setVisibility(View.VISIBLE);
                                linearLayout2.setVisibility(View.VISIBLE);

                                progressBar.setVisibility(View.GONE);

                                Constants.answerCheckHash.clear();
                                Constants.answerStoreHash.clear();
                                Constants.testquestionAnswerStoreHash.clear();

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
                            else {
                                pieChart.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getBaseContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pb_loader.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Constants.USER_ID);
                params.put("exam_taken_id", exam_taken_id);
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
        stringRequest_viewResult.setRetryPolicy(new RetryPolicy() {
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
        requestQueue_viewResult.add(stringRequest_viewResult);

    }

}

