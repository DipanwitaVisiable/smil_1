package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.MySpinnerAdapter;
import com.example.crypedu.Constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class RequestActivity extends AppCompatActivity {

    private EditText subject_editText;
    private EditText message_editText;
    private CoordinatorLayout coordinatorLayout;
    private Spinner typeSpinner;
    private Button fromDateButton;
    private Button toDateButton;
    private Calendar myCalendarFrom;
    private Calendar myCalendarTo;
    private String typeRequest = "";
    private RequestQueue requestQueue;
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestActivity.this, MenuActivity.class);
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



        typeSpinner = findViewById(R.id.typeSpinner);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);
        EditText to_editText = findViewById(R.id.to_editText);
        subject_editText = findViewById(R.id.subject_editText);
        message_editText = findViewById(R.id.message_editText);
        Button submit_button = findViewById(R.id.submit_button);
        TextView note_textView = findViewById(R.id.note_textView);
        fromDateButton = findViewById(R.id.fromDateButton);
        toDateButton = findViewById(R.id.toDateButton);
        to_editText.setTypeface(typeface);
        subject_editText.setTypeface(typeface);
        message_editText.setTypeface(typeface);
        submit_button.setTypeface(typeface);
        copyRight_textView.setTypeface(typeface);
        note_textView.setTypeface(typeface);

        Button viewButton = findViewById(R.id.viewButton);
        viewButton.setTypeface(typeface);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestActivity.this, ViewRequestActivity.class));
            }
        });

        /*
          If user will press on Date button
          the only display Calender.
         */
        myCalendarFrom = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener fromDate = new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendarFrom.set(Calendar.YEAR, year);
                        myCalendarFrom.set(Calendar.MONTH, monthOfYear);
                        myCalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setDateFrom();
                    }
                };
        myCalendarTo = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener toDate = new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendarTo.set(Calendar.YEAR, year);
                        myCalendarTo.set(Calendar.MONTH, monthOfYear);
                        myCalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setDateTo();
                    }
                };
        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RequestActivity.this, fromDate, myCalendarFrom
                        .get(Calendar.YEAR), myCalendarFrom.get(Calendar.MONTH),
                        myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RequestActivity.this, toDate, myCalendarTo
                        .get(Calendar.YEAR), myCalendarTo.get(Calendar.MONTH),
                        myCalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Spinner Drop down elements
        List<String> categoriesSection = new ArrayList<>();
        categoriesSection.add("Select request type");
        categoriesSection.add("Leave Request");
        categoriesSection.add("Request for Id");
        categoriesSection.add("Request for Uniform");
        categoriesSection.add("Request for Books");
        categoriesSection.add("Request for Special");
        // Creating adapter for spinner
       // ArrayAdapter<String> dataAdapterSection = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesSection);
        // Drop down layout style - list view with radio button
        //dataAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        //typeSpinner.setAdapter(dataAdapterSection);
        // Spinner click listener
        MySpinnerAdapter adapter = new MySpinnerAdapter(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, categoriesSection);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
       /* typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if (position != 0) {
                    typeRequest = typeSpinner.getSelectedItem().toString().trim();
                }
                //String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if (position != 0) {
                    typeRequest = typeSpinner.getSelectedItem().toString().trim();
                }
                //String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String typeSpinnerString = typeSpinner.getSelectedItem().toString().trim();
                String fromDateString = fromDateButton.getText().toString().trim();
                String toDateString = toDateButton.getText().toString().trim();
                String subject = subject_editText.getText().toString().trim();
                String message = message_editText.getText().toString().trim();
                if (subject.isEmpty()){
                    showMessage("Required subject");
                    return;
                }
                if (typeRequest.isEmpty()){
                    showMessage("Required request type");
                    return;
                }
                if (fromDateString.equalsIgnoreCase("From Date")){
                    showMessage("Required From Date");
                    return;
                }
                if (toDateString.equalsIgnoreCase("To Date")){
                    showMessage("Required To Date");
                    return;
                }
                if (message.isEmpty()){
                    showMessage("Required message");
                    return;
                }
                if (!fromDateString.equalsIgnoreCase("From Date")
                        && !toDateString.equalsIgnoreCase("To Date")) {
                    if (fromDateString.compareTo(toDateString) <= 0) {
                        if (InternetCheckActivity.isConnected()) {
                            submitRequest(subject, message, typeRequest, fromDateString, toDateString);
                            subject_editText.setText("");
                            message_editText.setText("");
                        }else {
                            showSnack();
                        }
                    }
                }
            }
        });
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
            Intent intent = new Intent(RequestActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Snackbar message for validation
    private void showMessage(String s){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }
    //----------------------------
    // Set from Date on Button
    //----------------------------
    private void setDateFrom() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fromDateButton.setText(sdf.format(myCalendarFrom.getTime()));
    }

    //----------------------------
    // Set to Date on Button
    //----------------------------
    private void setDateTo() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        toDateButton.setText(sdf.format(myCalendarTo.getTime()));
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(RequestActivity.this, MenuActivity.class));
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
    // Submit Request to server
    // with 'studentId, message,
    // subject' param.
    //--------------------------------
    public void submitRequest_old(String subject, String message, String typeSpinnerString, String fromDateString, String toDateString) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            requestParams.put("subject", subject);
            requestParams.put("msg", message);
            requestParams.put("msg_type", typeSpinnerString);
            requestParams.put("start_date", fromDateString);
            requestParams.put("end_date", toDateString);
            clientReg.post(Constants.BASE_SERVER + "send_request/", requestParams, new JsonHttpResponseHandler() {
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
                            typeSpinner.setSelection(0);
                            fromDateButton.setText("From");
                            toDateButton.setText("To");
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

    public void submitRequest(final String subject, final String message, final String typeSpinnerString, final String fromDateString, final String toDateString) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "send_request/", new Response.Listener<String>() {
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
                        typeSpinner.setSelection(0);
                        fromDateButton.setText("From");
                        toDateButton.setText("To");
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
                params.put("id", Constants.USER_ID);
                params.put("subject", subject);
                params.put("msg", message);
                params.put("msg_type", typeSpinnerString);
                params.put("start_date", fromDateString);
                params.put("end_date", toDateString);
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
