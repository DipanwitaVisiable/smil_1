package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.annotation.SuppressLint;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.EmployeeAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.EmployeeInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class EmployeeManageActivity extends AppCompatActivity {
    private ListView subjectListView;
    private ArrayList<EmployeeInfo> employeeInfoArrayList = new ArrayList<>();
    private EditText inputSearch;
    private EmployeeAdapter employeeAdapter;
    private Typeface typeface;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manage);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeManageActivity.this, MenuActivity.class);
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

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(EmployeeManageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        /*
          Retrieved CoordinatorLayout Id for SnackBar.
         */
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        /*
          ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        if (InternetCheckActivity.isConnected()){
            pbHeaderProgress.setVisibility(View.GONE);
            fetchEmployeeDetails();
        }else {
            showSnack();
        }

        /*
          Set BubbleGumSans Regular custom font.
         */
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);

        /*
          Employee search EditText by name.
         */
        inputSearch = findViewById(R.id.search_editText);
        inputSearch.setTypeface(typeface);
    }

    //------------------------
    // Hardware back key.
    //------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(EmployeeManageActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //--------------------------------
    // Fetch employee details from server.
    //--------------------------------
    public void fetchEmployeeDetails_old(){
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.get("http://"+ Constants.server_name +"webservices/websvc/list_employees/", new JsonHttpResponseHandler() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        subjectListView = findViewById(R.id.list_view);
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArrayList = obj.getJSONArray("employees");
                            employeeInfoArrayList.clear();
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String empId = jsonObject.getString("emp_id");
                                String empName = jsonObject.getString("full_name");
                                String empAddress = jsonObject.getString("address");
                                String empPincode = jsonObject.getString("pincode");
                                String empDob = jsonObject.getString("dob");
                                String empSex = jsonObject.getString("sex");
                                String empBloodGroup = jsonObject.getString("blood_grp");
                                String empEmail = jsonObject.getString("email");
                                String empMobile = jsonObject.getString("mobile");
                                String empMarital = jsonObject.getString("marital_status");
                                String empSalary = jsonObject.getString("salary");
                                String empFacultyType = jsonObject.getString("faculty_type");
                                String empJoiningDate = jsonObject.getString("joining_date");
                                String empResigningDate = jsonObject.getString("resigning_date");

                                EmployeeInfo employeeInfo = new EmployeeInfo(id, empId, empName, empAddress, empPincode,
                                        empDob, empSex, empBloodGroup, empEmail, empMobile, empMarital, empSalary, empFacultyType,
                                        empJoiningDate, empResigningDate);
                                employeeInfoArrayList.add(i, employeeInfo);
                            }
                            employeeAdapter = new EmployeeAdapter(getBaseContext(), employeeInfoArrayList, getLayoutInflater());
                            subjectListView.setAdapter(employeeAdapter);

                            inputSearch = findViewById(R.id.search_editText);
                            inputSearch.setTypeface(typeface);
                            inputSearch.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    v.setFocusable(true);
                                    v.setFocusableInTouchMode(true);
                                    return false;
                                }
                            });
                            inputSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                    // When user changed the Text
                                }

                                @Override
                                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                              int arg3) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void afterTextChanged(Editable arg0) {
                                    // TODO Auto-generated method stub
                                    String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                                    employeeAdapter.filter(text);
                                }
                            });
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

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    public void fetchEmployeeDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"webservices/websvc/list_employees/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    subjectListView = findViewById(R.id.list_view);
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArrayList = obj.getJSONArray("employees");
                        employeeInfoArrayList.clear();
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String empId = jsonObject.getString("emp_id");
                            String empName = jsonObject.getString("full_name");
                            String empAddress = jsonObject.getString("address");
                            String empPincode = jsonObject.getString("pincode");
                            String empDob = jsonObject.getString("dob");
                            String empSex = jsonObject.getString("sex");
                            String empBloodGroup = jsonObject.getString("blood_grp");
                            String empEmail = jsonObject.getString("email");
                            String empMobile = jsonObject.getString("mobile");
                            String empMarital = jsonObject.getString("marital_status");
                            String empSalary = jsonObject.getString("salary");
                            String empFacultyType = jsonObject.getString("faculty_type");
                            String empJoiningDate = jsonObject.getString("joining_date");
                            String empResigningDate = jsonObject.getString("resigning_date");

                            EmployeeInfo employeeInfo = new EmployeeInfo(id, empId, empName, empAddress, empPincode,
                                    empDob, empSex, empBloodGroup, empEmail, empMobile, empMarital, empSalary, empFacultyType,
                                    empJoiningDate, empResigningDate);
                            employeeInfoArrayList.add(i, employeeInfo);
                        }
                        employeeAdapter = new EmployeeAdapter(getBaseContext(), employeeInfoArrayList, getLayoutInflater());
                        subjectListView.setAdapter(employeeAdapter);

                        inputSearch = findViewById(R.id.search_editText);
                        inputSearch.setTypeface(typeface);
                        inputSearch.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                v.setFocusable(true);
                                v.setFocusableInTouchMode(true);
                                return false;
                            }
                        });
                        inputSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                // When user changed the Text
                            }

                            @Override
                            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                          int arg3) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void afterTextChanged(Editable arg0) {
                                // TODO Auto-generated method stub
                                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                                employeeAdapter.filter(text);
                            }
                        });
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
