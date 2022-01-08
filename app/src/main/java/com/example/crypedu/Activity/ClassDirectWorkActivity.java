package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.example.crypedu.Adapter.DaysListAdapter;
import com.example.crypedu.Pojo.DaysListInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.App;
import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.ClassDirectWorkAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ClassWorkInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ClassDirectWorkActivity extends AppCompatActivity implements InternetCheckActivity.ConnectivityReceiverListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    private LinearLayout title_linearLayout;
    private ListView listView_classWord;
    private ArrayList<String> classList = new ArrayList<>(), sectionList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private GridView gridview;
    private ArrayList<ClassWorkInfo> classWorkArrayList;
    private CoordinatorLayout coordinatorLayout;
    private Spinner classSpinner, sectionSpinner;
    private String classString = null, sectionString = null;
    private JSONObject jsonObject = null;
    private Button submitButton, pickDate;
    private int year = 0, month = 0, day = 0, dateFlag = 0;
    private String dateString = "";
    SimpleDateFormat simpleDateFormat;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_direct_work);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        requestQueue = Volley.newRequestQueue(this);
        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);

        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);

        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        submitButton = findViewById(R.id.submit_spin);
        pickDate = findViewById(R.id.pickDateBn);
        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    sectionString = sectionSpinner.getItemAtPosition(position).toString();
                    Toast.makeText(ClassDirectWorkActivity.this, sectionString, Toast.LENGTH_LONG).show();
                    submitButton.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    classString = classSpinner.getItemAtPosition(position).toString();
                    Toast.makeText(ClassDirectWorkActivity.this, classString, Toast.LENGTH_LONG).show();
                    submitButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SpinnerDatePickerDialogBuilder()
                        .context(ClassDirectWorkActivity.this)
                        .callback(ClassDirectWorkActivity.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(year, month, day)
                        .build()
                        .show();

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classString == null || sectionString == null) {
                    Toast.makeText(ClassDirectWorkActivity.this, "Required information", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetCheckActivity.isConnected()) {
                        fetchClassWorkDetails(classString, sectionString);
                        submitButton.setVisibility(View.GONE);

                    } else {
                        showSnack();
                    }
                }
            }
        });

        /*
          Active back button on ToolBar.
         */
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setLogo(R.drawable.ic_launcher);
        //actionBar.setDisplayUseLogoEnabled(true);
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        //actionBar.setListNavigationCallbacks(, this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassDirectWorkActivity.this, WorkActivity.class);
                startActivity(intent);
            }
        });

        /*
          Set FloatingActionButton Visibility 'INVISIBLE'.
         */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /*
          Retrieved CoordinatorLayout Id for SnackBar.
         */
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        /*
          If internet connection is working
          then only call fetchClassDetails().
         */
        if (InternetCheckActivity.isConnected()) {
            getClassSection();
            sendNotificationStatus();
        } else {
            showSnack();
        }

        /*
          Retrieved LinearLayout Id and set visibility 'GONE'
          because of It will show all details at the end of
          the process.
         */
        title_linearLayout = findViewById(R.id.title_linearLayout);
        title_linearLayout.setVisibility(View.GONE);

        /*
          Retrieved ListView Id and set visibility 'GONE'
          because of It will show all details at the end of the process.
         */
        listView_classWord = findViewById(R.id.listView_classWord);
        listView_classWord.setVisibility(View.GONE);

        /*
          Set BubbleGumSans Regular custom font.
         */
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        /*
          Set copy right text.
         */
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        TextView class_textView = findViewById(R.id.class_textView);
        class_textView.setTypeface(typeface);
        TextView topic_textView = findViewById(R.id.topic_textView);
        topic_textView.setTypeface(typeface);
        TextView subject_textView = findViewById(R.id.subject_textView);
        subject_textView.setTypeface(typeface);
        TextView date_textView = findViewById(R.id.date_textView);
        date_textView.setTypeface(typeface);
    }

    private void showSnack() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    private void getClassSection() {
        final StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_class_all", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("class_details");
                        Log.e("All class ", jsonArray.toString());
                        JSONObject object;
                        classList.add("Select class");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            classList.add(object.getString("class_or_year"));
                        }
                        adapter = new ArrayAdapter<String>(ClassDirectWorkActivity.this, R.layout.spinner_layout, classList) {
                            @Override
                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                }
                                if (position % 2 == 1) {
                                    // Set the item background color
                                    tv.setBackgroundColor(Color.parseColor("#FFE4B389"));
                                } else {
                                    // Set the alternate item background color
                                    tv.setBackgroundColor(Color.parseColor("#FF99551B"));
                                }
                                return view;
                            }
                        };
                        classSpinner.setAdapter(adapter);
                        jsonArray = jsonObject.getJSONArray("section_details");
                        sectionList.add("Select section");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            object = jsonArray.getJSONObject(i);
                            sectionList.add(object.getString("section"));
                        }
                        adapter = new ArrayAdapter<String>(ClassDirectWorkActivity.this, R.layout.spinner_layout, sectionList) {
                            @Override
                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                }
                                if (position % 2 == 1) {
                                    // Set the item background color
                                    tv.setBackgroundColor(Color.parseColor("#FFE4B389"));
                                } else {
                                    // Set the alternate item background color
                                    tv.setBackgroundColor(Color.parseColor("#FF99551B"));
                                }
                                return view;
                            }
                        };
                        sectionSpinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(ClassDirectWorkActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ClassDirectWorkActivity.this, "Something goes to wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_section_spinner_menu, menu);
        MenuItem item = menu.findItem(R.id.spinner_menu);
        MenuItemCompat.setActionView(item, R.layout.two_spinner_direct);
        View view = MenuItemCompat.getActionView(item);
        classSpinner = view.findViewById(R.id.class_spinner);
        sectionSpinner = view.findViewById(R.id.section_spinner);
        return false;
    }*/

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
            Intent intent = new Intent(ClassDirectWorkActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        App.getInstance().setConnectivityListener(this);
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(ClassDirectWorkActivity.this, WorkActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //------------------------------
    // Send notification status to server
    // respective of student_id and notification_type.
    //------------------------------
    public void sendNotificationStatus_old() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Constants.USER_ID);
            requestParams.put("notification_type", "classwork");
            clientReg.post(Constants.BASE_SERVER + "notification_status/", requestParams, new JsonHttpResponseHandler() {
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

    //--------------------------------
    // Fetch Class work details from server
    // with the 'student Id' param.
    //--------------------------------
    public void fetchClassWorkDetails_old(String cls, String section) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            if (dateString.isEmpty() && dateFlag == 0) {
                requestParams.put("date", "");
            } else if (!dateString.isEmpty() && dateFlag == 1) {
                requestParams.put("date", dateString);
            }
            requestParams.put("class", cls);
            requestParams.put("section", section);
            clientReg.post(Constants.BASE_SERVER + "get_classwork_details_dir", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {

                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        ClassWorkInfo classWorkInfo;
                        if (status.equalsIgnoreCase("200")) {
                            classWorkArrayList = new ArrayList<>();
                            listView_classWord = findViewById(R.id.listView_classWord);
                            listView_classWord.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("class_work_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String subject = jsonObject.getString("subject");
                                String topic = jsonObject.getString("topic");
                                String class_name = jsonObject.getString("class_name");
                                String date_of_class = jsonObject.getString("date_of_class");

                                classWorkInfo = new ClassWorkInfo(id, subject, topic, class_name, date_of_class);
                                classWorkArrayList.add(i, classWorkInfo);
                            }
                            title_linearLayout.setVisibility(View.GONE);
                            listView_classWord.setAdapter(new ClassDirectWorkAdapter(getBaseContext(), classWorkArrayList, getLayoutInflater()));
                        } else {
                            listView_classWord.setVisibility(View.GONE);
                            // adding a text view in CoordinateLayout
                            TextView textViewCoordinatorLayout = new TextView(ClassDirectWorkActivity.this);
                            textViewCoordinatorLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            textViewCoordinatorLayout.setText(message);
                            coordinatorLayout.addView(textViewCoordinatorLayout); // adding textView as child view of coordinate layout.

                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                        pbHeaderProgress.setVisibility(View.GONE);
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
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month + 1;
        this.day = day;
        pickDate.setText(this.day + "-" + this.month + "-" + this.year);
        dateFlag = 1;
        dateString = pickDate.getText().toString().trim();
        submitButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }


    public void sendNotificationStatus() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_status/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
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
                params.put("notification_type", "classwork");
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

    public void fetchClassWorkDetails(final String cls, final String section) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_classwork_details_dir", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    ClassWorkInfo classWorkInfo;
                    if (status.equalsIgnoreCase("200")) {
                        classWorkArrayList = new ArrayList<>();
                        listView_classWord = findViewById(R.id.listView_classWord);
                        listView_classWord.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayList = obj.getJSONArray("class_work_details");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String subject = jsonObject.getString("subject");
                            String topic = jsonObject.getString("topic");
                            String class_name = jsonObject.getString("class_name");
                            String date_of_class = jsonObject.getString("date_of_class");

                            classWorkInfo = new ClassWorkInfo(id, subject, topic, class_name, date_of_class);
                            classWorkArrayList.add(i, classWorkInfo);
                        }
                        title_linearLayout.setVisibility(View.GONE);
                        listView_classWord.setAdapter(new ClassDirectWorkAdapter(getBaseContext(), classWorkArrayList, getLayoutInflater()));
                    } else {
                        listView_classWord.setVisibility(View.GONE);
                        // adding a text view in CoordinateLayout
                        TextView textViewCoordinatorLayout = new TextView(ClassDirectWorkActivity.this);
                        textViewCoordinatorLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        textViewCoordinatorLayout.setText(message);
                        coordinatorLayout.addView(textViewCoordinatorLayout); // adding textView as child view of coordinate layout.

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

                if (dateString.isEmpty() && dateFlag == 0) {
                    params.put("date", "");
                } else if (!dateString.isEmpty() && dateFlag == 1) {
                    params.put("date", dateString);
                }
                params.put("class", cls);
                params.put("section", section);
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
