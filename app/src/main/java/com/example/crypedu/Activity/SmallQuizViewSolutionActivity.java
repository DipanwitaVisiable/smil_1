package com.example.crypedu.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.SmallQuizSolutionQuestionAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AnswerSolutionGetSet;
import com.example.crypedu.Pojo.QuestionSolutionGetSet;
import com.library.NavigationBar;
import com.library.NvTab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SmallQuizViewSolutionActivity extends AppCompatActivity implements NavigationBar.OnTabClick{
    private ViewPager viewPager;
    private ImageView noDataImage;
    private TextView tryAgainText;
    ProgressBar answerLoad;
    String exam_taken_id, exam_id,reg_id;
    Context context;
    NavigationBar bar;
    private ArrayList<QuestionSolutionGetSet> quesSolutionList = new ArrayList<>();
    private ArrayList<AnswerSolutionGetSet> ansSolutionList;
    HashMap<String, ArrayList<AnswerSolutionGetSet>> solutionListHashMap = new HashMap<>();
    private StringRequest stringRequest_getSolution;
    private RequestQueue requestQueue_getSolution;
    private Toolbar toolbar;
    private RequestQueue requestQueue;
    private LinearLayout ll_home, ll_profile, ll_exam, ll_share;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_quiz_view_solution);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context = SmallQuizViewSolutionActivity.this;

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        Intent intent = getIntent();
        exam_taken_id = intent.getStringExtra("exam_taken_id");
        exam_id = intent.getStringExtra("exam_id");

        noDataImage=findViewById(R.id.no_data_image);
        tryAgainText=findViewById(R.id.try_again_text);
        bar=findViewById(R.id.navBar);
        answerLoad=findViewById(R.id.ans_load);
        viewPager=findViewById(R.id.viewpager);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("View Solution");
        toolbar.setTitleTextColor(Color.WHITE);


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


        if (InternetCheckActivity.isConnected()) {
            getViewSolution();
        }else {
            Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
        }

    }
    private void setup(boolean reset, int count) {
        if (reset)
            bar.resetItems();
        bar.setTabCount(count);
        bar.animateView(3000);
        bar.setCurrentPosition(viewPager.getCurrentItem() <= 0 ? 0 : viewPager.getCurrentItem());
    }

    @Override
    public void onTabClick(int touchPosition, NvTab prev, NvTab nvTab) {
        viewPager.setCurrentItem(touchPosition);
        bar.setCurrentPosition(viewPager.getCurrentItem());

    }


    private void getViewSolution() {
        requestQueue_getSolution= Volley.newRequestQueue(context);
        answerLoad.setVisibility(View.VISIBLE);
        stringRequest_getSolution=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "view_solution",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            answerLoad.setVisibility(View.GONE);
//                            bar.setVisibility(View.VISIBLE);

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
//                            String message = jsonObject.getString("message");
                            if (status.equalsIgnoreCase("200")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("message");

                                quesSolutionList.clear();
                                solutionListHashMap.clear();
                                QuestionSolutionGetSet questionSetGet;
                                AnswerSolutionGetSet answerSetGet;
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    questionSetGet = new QuestionSolutionGetSet();
                                    questionSetGet.setQuestion_id(object1.getString("question_id"));
                                    questionSetGet.setQuestion(object1.getString("question"));
                                    questionSetGet.setStudent_answer_id(object1.getString("std_ans_id"));
                                    quesSolutionList.add(questionSetGet);
                                    JSONArray jsonArray1 = object1.getJSONArray("ans_dtls");
                                    ansSolutionList = new ArrayList<>();
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject object2 = jsonArray1.getJSONObject(j);
                                        answerSetGet = new AnswerSolutionGetSet();
                                        answerSetGet.setAnswer_id(object2.getString("answer_id"));
                                        answerSetGet.setAnswer(object2.getString("answer"));
                                        answerSetGet.setAnswer_status(object2.getString("ans_status"));
//                                        answerSetGet.setStudent_answer(object1.getString("std_ans_id"));
                                        ansSolutionList.add(answerSetGet);
                                    }
                                    solutionListHashMap.put(questionSetGet.getQuestion_id(), ansSolutionList);
                                }
                                SmallQuizSolutionQuestionAdapter solutionTestPageAdapter = new SmallQuizSolutionQuestionAdapter(context, quesSolutionList, solutionListHashMap);
                                viewPager.setAdapter(solutionTestPageAdapter);
                                setup(true, solutionListHashMap.size());
                                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        bar.setCurrentPosition(position);

                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });

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
                                noDataImage.setVisibility(View.VISIBLE);
                                tryAgainText.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "No question found", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            answerLoad.setVisibility(View.GONE);
                            noDataImage.setVisibility(View.VISIBLE);
                            tryAgainText.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
                answerLoad.setVisibility(View.GONE);

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>myParams=new HashMap<String, String>();
                /*myParams.put("student_id", "2634");
                myParams.put("exam_id", "6");
                myParams.put("exam_taken_id", "3");*/

                myParams.put("student_id", Constants.USER_ID);
                myParams.put("exam_id", exam_id);
                myParams.put("exam_taken_id", exam_taken_id);
                myParams.put("reg_id", reg_id);
                return myParams;
            }
        };

        // Start- To remove timeout error
        stringRequest_getSolution.setRetryPolicy(new RetryPolicy() {
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

        requestQueue_getSolution.add(stringRequest_getSolution);

    }


}
