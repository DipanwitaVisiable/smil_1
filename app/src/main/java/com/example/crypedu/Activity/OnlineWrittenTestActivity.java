package com.example.crypedu.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
import com.example.crypedu.Adapter.OnlineWrittenTestQuestionAdapter;
import com.example.crypedu.Adapter.SmallQuizQuestionAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Helper.CallStateListener;
import com.example.crypedu.Helper.CustomCountDownTimer;
import com.example.crypedu.Helper.NonSwipeableViewPager;
import com.example.crypedu.Pojo.AnswerTable;
import com.example.crypedu.Pojo.QuestionTable;
import com.library.NavigationBar;
import com.library.NvTab;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OnlineWrittenTestActivity extends AppCompatActivity implements NavigationBar.OnTabClick{
  //    private ViewPager viewPager;
  private NonSwipeableViewPager viewPager;
  private AVLoadingIndicatorView av_caf_loader;
  private ImageView noDataImage;
  private TextView tryAgainText;
  Button finishButton, submitButton, preButton;
  ProgressBar answerLoad;
  TextView textTimer, totalQuestion;
  String duration;
  SimpleDateFormat df;
  Calendar c;
  private String currentTime;
  //     CountDownTimer countDownTimer;
  public static CustomCountDownTimer countDownTimer;
  Context context;
  private ArrayList<QuestionTable> quesList = new ArrayList<>();
  private ArrayList<AnswerTable> ansList;
  HashMap<String, ArrayList<AnswerTable>> listHashMap = new HashMap<>();
  String quizId, test_id, test_finish_time;
  NavigationBar bar;
  RelativeLayout l1_top;
  public long  elapsed_time;

  private StringRequest stringRequest_saveQuiz, stringRequest_getQuestion, stringRequest_submitTest;
  private RequestQueue requestQueue_saveQuiz, requestQueue_getQuestion, requestQueue_submitTest;
  private Spinner spinner;
  private ArrayList<String> itemList=new ArrayList<>();
  private int max_page=0;

  public TelephonyManager tm;
  private CallStateListener callStateListener;
  private String reg_id;


  @SuppressLint("SimpleDateFormat")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_small_quiz_test);

    context = OnlineWrittenTestActivity.this;

    SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
    reg_id=device_token.getString("FIREBASE_ID", "");

    //Start- Register telephony manager to detect phone call
    callStateListener=new CallStateListener(context);
    tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    //End- Register telephony manager to detect phone call

    answerLoad = findViewById(R.id.ans_load);
    textTimer = findViewById(R.id.textTimer);
    bar = findViewById(R.id.navBar);
    totalQuestion = findViewById(R.id.text_no_of_question);
    finishButton = findViewById(R.id.test_finish_button);
    submitButton = findViewById(R.id.submit_ans);
    preButton = findViewById(R.id.pre_ans);
    viewPager = findViewById(R.id.viewpager);
    av_caf_loader = findViewById(R.id.av_caf_loader);
    noDataImage = findViewById(R.id.no_data_image);
    tryAgainText = findViewById(R.id.try_again_text);
    //av_caf_loader.setVisibility(View.VISIBLE);
    preButton.setVisibility(View.GONE);
//    submitButton.setVisibility(View.GONE); //code off 10_02_2021
    submitButton.setVisibility(View.VISIBLE);
    c = Calendar.getInstance();
    df = new SimpleDateFormat("HH:mm:ss");
    currentTime = df.format(c.getTime());
    bar.setOnTabClick(this);
    l1_top=findViewById(R.id.l1_top);

    preButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (viewPager.getCurrentItem() > 0) {
          viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
          bar.setCurrentPosition(viewPager.getCurrentItem());

          if (viewPager.getCurrentItem() == 0) {
            preButton.setVisibility(View.GONE);
          }
        }
      }
    });

    Intent intent = getIntent();
    quizId = intent.getStringExtra("quiz_id");
    Constants.QUIZ_ID=intent.getStringExtra("quiz_id");
    duration = intent.getStringExtra("time");
    tryAgainText.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      @Override
      public void onClick(View v) {
        if (InternetCheckActivity.isConnected()) {
          getAllQuestion();
        }else {
          Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
        }

      }
    });

    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        if (submitButton.getText().toString().equalsIgnoreCase("Save & Finish")) {
          callSubmitTestApi();
          countDownTimer.cancel();
          Constants.answerStoreHash.clear();
          Constants.testquestionAnswerStoreHash.clear();
        }
        if (submitButton.getText().toString().equalsIgnoreCase("Save & Next")) {
          answerLoad.setVisibility(View.GONE);
          viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
          preButton.setVisibility(View.GONE);//For disable swipe

        }
        bar.setCurrentPosition(viewPager.getCurrentItem());

//        saveQuiz();
      }
    });

    if (InternetCheckActivity.isConnected()) {
      getAllQuestion();
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
    if (touchPosition>max_page)
    {
      bar.setCurrentPosition(viewPager.getCurrentItem());
      Toast.makeText(context, "Please answer this question first", Toast.LENGTH_LONG).show();
    }
    else {
      nvTab=nvTab;
      viewPager.setCurrentItem(touchPosition);
      bar.setCurrentPosition(viewPager.getCurrentItem());
    }

  }
  public void finishTest(View view) {

    ContextThemeWrapper themedContext;
    themedContext = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(themedContext);
    dialogBuilder.setCancelable(false);
    dialogBuilder.setTitle("Finish Test");
    dialogBuilder.setIcon(R.drawable.ic_alarm_clock);
    dialogBuilder.setMessage("Are you sure to finish the test?");
    dialogBuilder.setPositiveButton("Sure", null);
    dialogBuilder.setNegativeButton("I'm just kidding", null);
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();

    Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
    // override the text color of positive button
    positiveButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
    positiveButton.setAllCaps(false);
    // provides custom implementation to positive button click
    positiveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        onPositiveButtonClicked(alertDialog);

      }
    });

    Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
    // override the text color of negative button
    negativeButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
    negativeButton.setAllCaps(false);
    // provides custom implementation to negative button click
    negativeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        onNegativeButtonClicked(alertDialog);

      }
    });


  }

  @SuppressLint("SimpleDateFormat")
  private void onPositiveButtonClicked(AlertDialog alertDialog) {
        /*c = Calendar.getInstance();
        df = new SimpleDateFormat("HH:mm:ss");
        Constants.END_TIME = df.format(c.getTime());*/

    Intent i = new Intent(context, ExamFinishActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("test_id", test_id);
        /*long f_time= elapsed_time/1000;
        if (f_time>60)
            test_finish_time= (f_time/60) + " min " + (f_time%60)+ " sec";
        else
            test_finish_time=f_time + " sec";
        bundle.putString("test_time", test_finish_time);*/
    i.putExtras(bundle);
    //To clear previous activity, used this below line
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(i);

    alertDialog.dismiss();
    if (countDownTimer==null)
      finish();
    else {
      countDownTimer.cancel();
      finish();
    }
  }

  private void onNegativeButtonClicked(AlertDialog alertDialog) {
    alertDialog.dismiss();
  }

  private void saveQuiz() {
    requestQueue_saveQuiz= Volley.newRequestQueue(context);
    answerLoad.setVisibility(View.VISIBLE);
    stringRequest_saveQuiz=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "save_question",
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          try {
            JSONObject jsonObject=new JSONObject(response);
            String status=jsonObject.getString("status");
            String message=jsonObject.getString("message");
            if (status.equalsIgnoreCase("200")) {

              if (submitButton.getText().toString().equalsIgnoreCase("Save & Finish")) {
                callSubmitTestApi();


                countDownTimer.cancel();
                Constants.answerStoreHash.clear();
                Constants.testquestionAnswerStoreHash.clear();
//                                    finish();

              }
              if (submitButton.getText().toString().equalsIgnoreCase("Save & Next")) {
                answerLoad.setVisibility(View.GONE);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//                                    preButton.setVisibility(View.VISIBLE);
                preButton.setVisibility(View.GONE);//For disable swipe

              }
              bar.setCurrentPosition(viewPager.getCurrentItem());

            } else {
              answerLoad.setVisibility(View.GONE);
              Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
          } catch (JSONException e) {
            e.printStackTrace();
            answerLoad.setVisibility(View.GONE);
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

        myParams.put("student_id", Constants.USER_ID);
//                myParams.put("student_id", "2365");
        myParams.put("exam_id", quizId);
        myParams.put("question_id", Constants.CHOOSE_QUESTION_ID);
        myParams.put("answer_id", Constants.ANSWER_ID);
        myParams.put("exam_taken_id", test_id);

        return myParams;
      }
    };

    // Start- To remove timeout error
    stringRequest_saveQuiz.setRetryPolicy(new RetryPolicy() {
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

    requestQueue_saveQuiz.add(stringRequest_saveQuiz);

  }

  private void callSubmitTestApi() {

    requestQueue_submitTest= Volley.newRequestQueue(context);
    stringRequest_submitTest=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "submit_test",
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          try {
            answerLoad.setVisibility(View.GONE);
            JSONObject jsonObject=new JSONObject(response);
            String status=jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              Intent i = new Intent(context, ExamFinishActivity.class);
              Bundle bundle = new Bundle();
              bundle.putString("test_id", test_id);
              i.putExtras(bundle);
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(i);
              finish();

            } else {

              Toast.makeText(context, "Test not completed", Toast.LENGTH_LONG).show();
            }
          } catch (JSONException e) {
            e.printStackTrace();
            answerLoad.setVisibility(View.GONE);
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

        myParams.put("student_id", Constants.USER_ID);
//                myParams.put("student_id", "2365");
//                myParams.put("exam_taken_id", "1");
        myParams.put("exam_taken_id", test_id);

        return myParams;
      }
    };

    // Start- To remove timeout error
    stringRequest_submitTest.setRetryPolicy(new RetryPolicy() {
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

    requestQueue_submitTest.add(stringRequest_submitTest);
  }

  /*private void getAllQuestion() {
    requestQueue_getQuestion= Volley.newRequestQueue(context);
    answerLoad.setVisibility(View.VISIBLE);
    stringRequest_getQuestion=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "start_test",
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          try {
            answerLoad.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
            l1_top.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {

              JSONArray question_array = jsonObject.getJSONArray("message");

              quesList.clear();
              listHashMap.clear();
              QuestionTable questionSetGet;
              AnswerTable answerSetGet;
              for (int i = 0; i < question_array.length(); i++) {

                JSONObject object1 = question_array.getJSONObject(i);
                questionSetGet = new QuestionTable();
                questionSetGet.setQuestion(object1.getString("question"));
                questionSetGet.setQuestionId(object1.getString("question_id"));
                test_id=object1.getString("exam_taken_id");
                //added
//                                    questionSetGet.setDirections(object1.getString("directions"));
                quesList.add(questionSetGet);
                JSONArray answer_array = object1.getJSONArray("ans_arr");
                ansList = new ArrayList<>();
                for (int j = 0; j < answer_array.length(); j++) {
                  JSONObject object2 = answer_array.getJSONObject(j);
                  answerSetGet = new AnswerTable();
                  answerSetGet.setAnswerId(object2.getString("answer_id"));
                  answerSetGet.setAnswer(object2.getString("answer"));
//                                        answerSetGet.setAns_stat(object2.getString("ans_stat"));
                  ansList.add(answerSetGet);
                }
                listHashMap.put(questionSetGet.getQuestionId(), ansList);

              }
              SmallQuizQuestionAdapter quizTestPageAdapter = new SmallQuizQuestionAdapter(context, quesList, listHashMap, submitButton);
              viewPager.setAdapter(quizTestPageAdapter);
              startTimer(Integer.parseInt(duration));
              totalQuestion.setText(1 + "/" + listHashMap.size());
              setup(true, listHashMap.size());

              Constants.TOTAL_QUES = String.valueOf(question_array.length());

              viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                  if (position == 0) {
                    preButton.setVisibility(View.GONE);
                  } else {
//                                            preButton.setVisibility(View.VISIBLE);
                    preButton.setVisibility(View.GONE);// For disable swipe
                  }
                  if (position + 1 == listHashMap.size()) {
                    submitButton.setText("Save & Finish");
                  } else {
                    submitButton.setText("Save & Next");


                  }
                  bar.setCurrentPosition(position);


                  totalQuestion.setText(position + 1 + "/" + listHashMap.size());
                  submitButton.setVisibility(View.GONE);
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
              Toast.makeText(context, "No Question found", Toast.LENGTH_LONG).show();
            }
            av_caf_loader.hide();
            // dialog.dismiss();

          } catch (JSONException e) {
            answerLoad.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
            tryAgainText.setVisibility(View.VISIBLE);
            av_caf_loader.hide();
            // dialog.dismiss();
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
        myParams.put("exam_id", quizId);
        myParams.put("student_id", Constants.USER_ID);
        myParams.put("reg_id", reg_id);
        return myParams;
      }
    };

    // Start- To remove timeout error
    stringRequest_getQuestion.setRetryPolicy(new RetryPolicy() {
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

    requestQueue_getQuestion.add(stringRequest_getQuestion);

  }*/


  private void startTimer(final int minute) {
    countDownTimer = new CustomCountDownTimer(60 * minute * 1000, 500) {
      @SuppressLint({"DefaultLocale", "SetTextI18n"})
      @Override
      public void onTick(long leftTimeInMilliseconds) {
        long seconds = leftTimeInMilliseconds / 1000;
        textTimer.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));

        //Start code to calculate test timing
        elapsed_time = minute*60*1000 - leftTimeInMilliseconds;
      }

      @SuppressLint("SetTextI18n")
      @Override
      public void onFinish() {
        if (textTimer.getText().equals("00:00")) {
          textTimer.setText("STOP");

          callSubmitTestApi();
                    /*Intent i = new Intent(context, ExamFinishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("test_id", test_id);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);*/

          countDownTimer.cancel();
          Constants.answerStoreHash.clear();
          Constants.testquestionAnswerStoreHash.clear();
        }
      }
    }.start();

  }

  @Override
  public void onBackPressed() {

    Toast.makeText(context, "You have to complete this test", Toast.LENGTH_SHORT).show();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit alert");
        builder.setMessage("Are you sure to quit the test ?");
        builder.setIcon(R.drawable.ic_alarm_clock);
        builder.setCancelable(false);
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Constants.answerStoreHash.clear();
                Constants.answerQuestionStoreHash.clear();
                Constants.questionAnswerStoreHash.clear();
                if (countDownTimer==null)
                    finish();
                else {
                    countDownTimer.cancel();
                    finish();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();*/

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
  }

  private void getAllQuestion() {
    requestQueue_getQuestion= Volley.newRequestQueue(context);
    answerLoad.setVisibility(View.VISIBLE);
    stringRequest_getQuestion=new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exam_test",
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          try {
            answerLoad.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
            l1_top.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {

//              JSONArray question_array = jsonObject.getJSONArray("message");

              quesList.clear();
              listHashMap.clear();
              QuestionTable questionSetGet;
              AnswerTable answerSetGet;

              //Start to store static data for written test
              for (int i = 0; i < 10; i++) {
                questionSetGet = new QuestionTable();
                  questionSetGet.setQuestion("question "+ String.valueOf(i));
                  questionSetGet.setQuestionId("10" + String.valueOf(i));
                  test_id="100" + String.valueOf(i);
                  quesList.add(questionSetGet);
                  ansList = new ArrayList<>();
                  if (i%2==0)
                  {
                    for (int j = 0; j < 4; j++) {
                      answerSetGet = new AnswerTable();
                      answerSetGet.setAnswerId("11" + String.valueOf(j));
                      answerSetGet.setAnswer("Answer " + String.valueOf(j));
                      ansList.add(answerSetGet);
                    }
                  }


                listHashMap.put(questionSetGet.getQuestionId(), ansList);

              }
              //End to store static data for written test

              OnlineWrittenTestQuestionAdapter quizTestPageAdapter = new OnlineWrittenTestQuestionAdapter(context, quesList, listHashMap, submitButton);
              viewPager.setAdapter(quizTestPageAdapter);
//              startTimer(Integer.parseInt(duration));
              startTimer(Integer.parseInt("30"));
              totalQuestion.setText(1 + "/" + listHashMap.size());
              setup(true, listHashMap.size());

//              Constants.TOTAL_QUES = String.valueOf(question_array.length());
              Constants.TOTAL_QUES = "10";

              viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                  if (position == 0) {
                    preButton.setVisibility(View.GONE);
                  } else {
//                                            preButton.setVisibility(View.VISIBLE);
                    preButton.setVisibility(View.GONE);// For disable swipe
                  }
                  if (position + 1 == listHashMap.size()) {
                    submitButton.setText("Save & Finish");
                  } else {
                    submitButton.setText("Save & Next");


                  }
                  bar.setCurrentPosition(position);


                  totalQuestion.setText(position + 1 + "/" + listHashMap.size());
//                  submitButton.setVisibility(View.GONE); //code off 10_02_2021
                  submitButton.setVisibility(View.VISIBLE);
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
              Toast.makeText(context, "No Question found", Toast.LENGTH_LONG).show();
            }
            av_caf_loader.hide();
            // dialog.dismiss();

          } catch (JSONException e) {
            answerLoad.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
            tryAgainText.setVisibility(View.VISIBLE);
            av_caf_loader.hide();
            // dialog.dismiss();
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
        /*myParams.put("exam_id", quizId);
        myParams.put("student_id", Constants.USER_ID);
        myParams.put("reg_id", reg_id);*/
        myParams.put("exam_id", "16");
        myParams.put("student_id", "2623");
        return myParams;
      }
    };

    // Start- To remove timeout error
    stringRequest_getQuestion.setRetryPolicy(new RetryPolicy() {
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

    requestQueue_getQuestion.add(stringRequest_getQuestion);

  }

}
