package com.example.crypedu.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.SecondTermAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.SecondTermInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class SecondTermActivity extends AppCompatActivity {

    private ListView listView_secondTerm;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<String> subjectArrayList;
    private ArrayList<SecondTermInfo> secondTermInfoArrayList;
    private LinearLayout titleLinearLayout;
    Typeface typeface;
    private ImageView image;
    private Context context;
    private RequestQueue requestQueue;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_term);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/


        Toolbar toolbar = findViewById(R.id.toolbar);
        context=SecondTermActivity.this;
        requestQueue = Volley.newRequestQueue(this);

//        buildDialog();//Show image pop up

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondTermActivity.this, ResultActivity.class);
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

        if (InternetCheckActivity.isConnected()) {
            fetchSecondTermResultDetails();
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
            Intent intent = new Intent(SecondTermActivity.this, LoginActivity.class);
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
                startActivity(new Intent(SecondTermActivity.this, ResultActivity.class));
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

    public void fetchSecondTermResultDetails_old() {
        final Dialog dialog = new Dialog(this);
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "second_term_result", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {

                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        String image_popup = obj.getString("image_popup");
                        if (!image_popup.equals("") && image_popup!=null)
                        {

                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = (R.style.DialogAnimation_2);
                            dialog.setContentView(R.layout.readmission_image_pop_up);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setCancelable(true);
                            image = dialog.findViewById(R.id.image);
                            Picasso.with(context)
                                    .load(image_popup)
                                    .noFade()
                                    .into(image);
                            dialog.show();
                        }

                        if (status.equalsIgnoreCase("200")) {
                            secondTermInfoArrayList = new ArrayList<>();
                            listView_secondTerm = findViewById(R.id.listView_secondTerm);
                            listView_secondTerm.setVisibility(View.VISIBLE);
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

                                SecondTermInfo secondTermInfo = new SecondTermInfo(subject,periodic_test,note_book, subject_enrichment, half_early_exam, marks_obtained, grade);
                                secondTermInfoArrayList.add(i, secondTermInfo);
                            }
                            titleLinearLayout.setVisibility(View.VISIBLE);
                            listView_secondTerm.setVisibility(View.VISIBLE);
                            listView_secondTerm.setClickable(false);
                            listView_secondTerm.setAdapter(new SecondTermAdapter(getBaseContext(), secondTermInfoArrayList, getLayoutInflater()));
                            if (InternetCheckActivity.isConnected()) {
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


    //------------------------------
    // Send notification status to server
    // respective of student_id and notification_type.
    //------------------------------
//    public void sendNotificationStatus() {
//        /**
//         * ProgressBar
//         */
//        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
//        pbHeaderProgress.setVisibility(View.VISIBLE);
//        try {
//            AsyncHttpClient clientReg = new AsyncHttpClient();
//            RequestParams requestParams = new RequestParams();
//            requestParams.put("student_id",Constants.USER_ID);
//            requestParams.put("notification_type","exam");
//            clientReg.post("http://"+ Constants.server_name +"webservices/websvc/notification_status/",requestParams, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
//                    try {
//                        pbHeaderProgress.setVisibility(View.GONE);
//                        String status = obj.getString("status");
//                        String message = obj.getString("message");
//                        if (status.equalsIgnoreCase("200")) {
//                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
//
//                            // Changing action button text color
//                            View sbView = snackbar.getView();
//                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
//                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
//                            snackbar.show();
//                        } else {
//                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
//
//                            // Changing action button text color
//                            View sbView = snackbar.getView();
//                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
//                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
//                            snackbar.show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // When the response returned by REST has Http response code other than '200'
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    pbHeaderProgress.setVisibility(View.GONE);
//                }
//            });
//        } catch (Exception e) {
//            pbHeaderProgress.setVisibility(View.GONE);
//            e.printStackTrace();
//        }
//    }


    private void buildDialog() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                greetingImageDialog();

                Constants.flag = false;
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void greetingImageDialog() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = (R.style.DialogAnimation_2);
//        dialog.setContentView(R.layout.greeting_one_time_dialog);
        dialog.setContentView(R.layout.new_pop_up_image);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        image = dialog.findViewById(R.id.image);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "invitaion",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pbHeaderProgress.setVisibility(View.GONE);
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            String invitaion_details_url = jsonResponse.getString("invitaion_details");
                            String invitaion_type = jsonResponse.getString("invitaion_type");

                            if (status.equalsIgnoreCase("200") && invitaion_type.equalsIgnoreCase("1")) {
                                Picasso.with(context)
                                        .load(invitaion_details_url)
                                        .noFade()
                                        .into(image);
                                dialog.show();
                            } else if (status.equalsIgnoreCase("200") && invitaion_type.equalsIgnoreCase("2")) {
                                /* Here will be gif upload code*/
                                pbHeaderProgress.setVisibility(View.GONE);
                                /*gifUrl = invitaion_details_url;
                                greetingGifDialog();*/
                            } else {
                                pbHeaderProgress.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbHeaderProgress.setVisibility(View.GONE);
                        // Toast.makeText(MenuActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //Adding the parameters to the request
//                    params.put(Config.KEY_ID, "3");

                return new HashMap<>();
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    public void fetchSecondTermResultDetails() {
        final Dialog dialog = new Dialog(this);
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "second_term_result", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    String image_popup = obj.getString("image_popup");
                    if (!image_popup.equals("") && image_popup!=null)
                    {

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = (R.style.DialogAnimation_2);
                        dialog.setContentView(R.layout.readmission_image_pop_up);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        image = dialog.findViewById(R.id.image);
                        Picasso.with(context)
                                .load(image_popup)
                                .noFade()
                                .into(image);
                        dialog.show();
                    }

                    if (status.equalsIgnoreCase("200")) {
                        secondTermInfoArrayList = new ArrayList<>();
                        listView_secondTerm = findViewById(R.id.listView_secondTerm);
                        listView_secondTerm.setVisibility(View.VISIBLE);
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

                            SecondTermInfo secondTermInfo = new SecondTermInfo(subject,periodic_test,note_book, subject_enrichment, half_early_exam, marks_obtained, grade);
                            secondTermInfoArrayList.add(i, secondTermInfo);
                        }
                        titleLinearLayout.setVisibility(View.VISIBLE);
                        listView_secondTerm.setVisibility(View.VISIBLE);
                        listView_secondTerm.setClickable(false);
                        listView_secondTerm.setAdapter(new SecondTermAdapter(getBaseContext(), secondTermInfoArrayList, getLayoutInflater()));
                        if (InternetCheckActivity.isConnected()) {
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
