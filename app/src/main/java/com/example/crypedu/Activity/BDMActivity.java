package com.example.crypedu.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.BdmAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.BdmSetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BDMActivity extends AppCompatActivity {
    private RecyclerView parentMeeting_recyclerView;
    private ArrayList<BdmSetterGetter> bdmSetterGetterArrayList = new ArrayList<>();
    private BdmAdapter bdmAdapter;
    private Context context;
    private RequestQueue requestQueue;
    private CoordinatorLayout coordinatorLayout;
    private EditText choose_date;
    private Calendar myCalendarJourney;
    private LinearLayout linearLayout_no_list;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdm);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = BDMActivity.this;
        requestQueue = Volley.newRequestQueue(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BDMActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(BDMActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        parentMeeting_recyclerView = findViewById(R.id.parentMeeting_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        parentMeeting_recyclerView.setLayoutManager(layoutManager);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        linearLayout_no_list = findViewById(R.id.linearLayout_no_list);
        parentMeeting_recyclerView.setVisibility(View.GONE);
        Constants.FETCHED_DATE="";
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);
        TextView tv_list_of_meetings = findViewById(R.id.tv_list_of_meetings);
        tv_list_of_meetings.setTypeface(typeface);


        if (InternetCheckActivity.isConnected()){
            fetchingMeetingList();
        } else {
            showSnack();
        }


        /*
          If user will press on Date button
          the only display Calender.
         */
        myCalendarJourney = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarJourney.set(Calendar.YEAR, year);
                myCalendarJourney.set(Calendar.MONTH, monthOfYear);
                myCalendarJourney.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                showDatePicker(view);
            }
        };

        choose_date = findViewById(R.id.choose_date);
        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, fromDate, myCalendarJourney
                        .get(Calendar.YEAR), myCalendarJourney.get(Calendar.MONTH),
                        myCalendarJourney.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public void showDatePicker(View v) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        choose_date.setText(sdf.format(myCalendarJourney.getTime()));
        Constants.FETCHED_DATE = sdf.format(myCalendarJourney.getTime());
       fetchingMeetingList();
    }

    public void fetchingMeetingList() {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "parent_meeting_details",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pbHeaderProgress.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            Log.e("BDM MESSAGE", message);
                            if (status.equalsIgnoreCase("200")) {
                                parentMeeting_recyclerView.setVisibility(View.VISIBLE);
                                linearLayout_no_list.setVisibility(View.GONE);
                                JSONArray meeting_details = obj.getJSONArray("meeting_details");
                                Log.e("MEETING LIST", meeting_details.toString());
                                bdmSetterGetterArrayList.clear();
                                for (int i = 0; i < meeting_details.length(); i++) {
                                    JSONObject object = meeting_details.getJSONObject(i);
                                    BdmSetterGetter setterGetter = new BdmSetterGetter();
                                    setterGetter.setMeeting_date(object.getString("meeting_date"));
                                    setterGetter.setGuardain_brf(object.getString("guardain_brf"));
                                    setterGetter.setTeacher_brf(object.getString("teacher_brf"));
                                    bdmSetterGetterArrayList.add(setterGetter);
                                }
                                bdmAdapter = new BdmAdapter(context, bdmSetterGetterArrayList);
                                parentMeeting_recyclerView.setAdapter(bdmAdapter);

                            } else {
                                parentMeeting_recyclerView.setVisibility(View.GONE);
                                linearLayout_no_list.setVisibility(View.VISIBLE);
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.parseColor(Constants.colorAccent));
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            pbHeaderProgress.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbHeaderProgress.setVisibility(View.GONE);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Constants.USER_ID);
                params.put("search_date", Constants.FETCHED_DATE);
                Log.e("PUTTING IN BDM ", params.toString());
                //Adding the parameters to the request
                return params;
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
        //Adding request the the queue
        requestQueue.add(stringRequest);

    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(BDMActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
