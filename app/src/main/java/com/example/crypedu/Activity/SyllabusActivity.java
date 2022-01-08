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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.SyllabusPlanningAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ExamInfo;
import com.example.crypedu.Pojo.SyllabusPlanningInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class SyllabusActivity extends AppCompatActivity {

    private ListView listView_class;
    private LinearLayout title_linearLayout, titleLinearLayout;
    private GridView gridview;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<SyllabusPlanningInfo> syllabusPlanningArrayList;
    private ArrayList<ExamInfo> examListArrayList;
    private ArrayList<String> subjectArrayList, classArrayList;
    private TextView messageTextView;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
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
                Intent intent = new Intent(SyllabusActivity.this, MenuActivity.class);
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

        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);

        if (InternetCheckActivity.isConnected()) {
//            Not using from 18th april,2018
            //  fetchSyllabusPlanning();
        }else {
            showSnack();
        }

        titleLinearLayout = findViewById(R.id.titleLinearLayout);
        titleLinearLayout.setVisibility(View.INVISIBLE);

        messageTextView = findViewById(R.id.messageTextView);
        messageTextView.setTypeface(typeface);

        title_linearLayout = findViewById(R.id.title_linearLayout);
        title_linearLayout.setVisibility(View.GONE);

        listView_class = findViewById(R.id.listView_class);
        listView_class.setVisibility(View.GONE);

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
            Intent intent = new Intent(SyllabusActivity.this, LoginActivity.class);
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
                startActivity(new Intent(SyllabusActivity.this, MenuActivity.class));
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
    // Fetch Subject details from server
    // with the 'class_name' param.
    //--------------------------------
    public void fetchSyllabusPlanning() {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "show_syllabus_planning", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        title_linearLayout.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            syllabusPlanningArrayList = new ArrayList<>();
                            listView_class = findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("syllabus_planning");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String class_name = jsonObject.getString("class_name");
                                String teachers_name = jsonObject.getString("teachers_name");
                                String subject = jsonObject.getString("subject");
                                String chapter = jsonObject.getString("chapter");
                                String no_of_class = jsonObject.getString("exam_name");

                                SyllabusPlanningInfo syllabusPlanningInfo = new SyllabusPlanningInfo(id, class_name, teachers_name, subject, chapter, no_of_class);
                                syllabusPlanningArrayList.add(i, syllabusPlanningInfo);
                            }
                            listView_class.setAdapter(new SyllabusPlanningAdapter(getBaseContext(), syllabusPlanningArrayList, getLayoutInflater()));
                            listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String itemValue = listView_class.getItemAtPosition(position).toString().trim();
                                }
                            });
                            titleLinearLayout.setVisibility(View.INVISIBLE);
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                            messageTextView.setText(message);
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
}
