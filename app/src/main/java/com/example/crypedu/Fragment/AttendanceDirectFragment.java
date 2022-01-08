package com.example.crypedu.Fragment;
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
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Adapter.AttendanceDirectAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AttendanceDirectInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class AttendanceDirectFragment extends Fragment implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private Button button;
    private ProgressBar progressBar;
    private FloatingActionButton actionButton;
    private int year = 0, month = 0, day = 0, dateFlag = 0;
    private String dateString = "";
    private List<AttendanceDirectInfo> infoList = new ArrayList<>();
    private LinearLayout linearLayout;
    SimpleDateFormat simpleDateFormat;
    private Spinner classSpinner, sectionSpinner;
    private String classString = null, sectionString = null;
    private ArrayList<String> classList = new ArrayList<>(), sectionList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public AttendanceDirectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_attendance_direct, container, false);
        classSpinner = myFragmentView.findViewById(R.id.class_spinner);
        sectionSpinner = myFragmentView.findViewById(R.id.section_spinner);

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    sectionString = sectionSpinner.getItemAtPosition(position).toString();
                    Toast.makeText(getContext(), sectionString, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), classString, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (InternetCheckActivity.isConnected()){
            getClassSection();
        }

        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);

        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);

        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);
        linearLayout = myFragmentView.findViewById(R.id.add_layout);
        recyclerView = myFragmentView.findViewById(R.id.attendance_list);
        button = myFragmentView.findViewById(R.id.pickDateBn);
        progressBar = myFragmentView.findViewById(R.id.pbHeaderProgress);
        actionButton = myFragmentView.findViewById(R.id.attendance_search);
        progressBar = myFragmentView.findViewById(R.id.pbHeaderProgress);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SpinnerDatePickerDialogBuilder()
                        .context(getContext())
                        .callback(AttendanceDirectFragment.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(year, month, day)
                        .build()
                        .show();
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetCheckActivity.isConnected()) {
                    if (dateString.equalsIgnoreCase("")){
                        Snackbar.make(actionButton, "Please select date", Snackbar.LENGTH_SHORT).show();

                    }else if (classSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Select class")){
                        Snackbar.make(actionButton, "Please select class", Snackbar.LENGTH_SHORT).show();

                    }else if (sectionSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Select section")){
                        Snackbar.make(actionButton, "Please select section", Snackbar.LENGTH_SHORT).show();

                    }/*else if (!dateString.equalsIgnoreCase("") && !classString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("")) {
                        getAttendance(dateString);
                        progressBar.setVisibility(View.VISIBLE);
                    }*/else {
                        getAttendance(dateString);
                        progressBar.setVisibility(View.VISIBLE);

                        //Snackbar.make(actionButton, "Please select required information", Snackbar.LENGTH_SHORT).show();
                    }
                    /*if (!dateString.equalsIgnoreCase("") && !classString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("")) {
                        getAttendance(dateString);
                        progressBar.setVisibility(View.VISIBLE);
                    }else {
                        Snackbar.make(actionButton, "Please select required information", Snackbar.LENGTH_SHORT).show();
                    }*/
                } else {
                    showSnackBar();
                }
            }
        });

        /*
          Check whether working internet
          connection is present or not.
         */
        if (InternetCheckActivity.isConnected()) {
            sendNotificationStatus();
        }else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.parseColor(Constants.colorAccent));
            snackbar.show();
        }

        return myFragmentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getClassSection() {
        final StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_class_all", new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                        adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.spinner_layout, classList){
                            @Override
                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if(position == 0){
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
                        adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, sectionList){
                            @Override
                            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if(position == 0){
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
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String sms = "";

                if (error.networkResponse == null) {
                    if (error instanceof NetworkError) {
                        sms = "Oops. Network error! Try again";

                    } else if (error instanceof ServerError) {
                        sms = "Oops. Server error!";

                    } else if (error instanceof AuthFailureError) {
                        sms = "Oops. Authentication error! Try again";

                    } else if (error instanceof TimeoutError) {
                        sms = "Oops. Timeout error! Try again";
                    }
                }
                Toast.makeText(getContext(), sms, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        queue.add(request);
    }

    //------------------------------
    // Send notification status to server
    // respective of student_id and notification_type.
    //------------------------------
    public void sendNotificationStatus_old() {
        /*
          ProgressBar
         */
        //final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        //pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Constants.USER_ID);
            requestParams.put("notification_type", "attendance");
            clientReg.post(Constants.BASE_SERVER + "notification_status/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        //pbHeaderProgress.setVisibility(View.GONE);
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
                    //pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            //pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = Objects.requireNonNull(getActivity()).getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }



    //------------------------------
    // Displaying toast message.
    //------------------------------
    protected void showSnackBar() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAttendance(final String date){
        Log.e("Called ", "true");
        Log.e("Date ", date);
        // POST parameters

        /*Map<String, String> params = new HashMap<>();
        params.put("src_date", date);
        JsonObjectRequest request  = new JsonObjectRequest(Request.Method.POST, Constants.BASE_SERVER + "get_student_attendence_dir", new JSONObject(params),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response ", "True");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error ", error.getMessage());
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String,String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };*/

       /* StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_student_attendence_dir", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.e("Status ", status);
                    if (status.equalsIgnoreCase("200")){
                        JSONArray jsonArray = jsonObject.getJSONArray("attendence_details");
                        Log.e("Array ", jsonArray.toString());
                        JSONObject object;
                        AttendanceDirectInfo adapter;
                        for (int  i = 0; i<jsonArray.length(); i++){
                            object = jsonArray.getJSONObject(i);
                            adapter = new AttendanceDirectInfo(object.getString("class_or_year"), object.getString("section"), object.getString("total_student"),
                                    object.getString("total_present"), object.getString("present_percent"));
                            infoList.add(adapter);
                        }
                        AttendanceDirectAdapter attendanceAdapter = new AttendanceDirectAdapter(AttendanceDirectActivity.this, infoList);
                        recyclerView.setAdapter(attendanceAdapter);
                    }else {
                        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("Error ", e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error ", error.getMessage());
                Toast.makeText(AttendanceDirectActivity.this, "Something goes to wrong!" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("src_date", date);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);*/


        String url = Constants.BASE_SERVER+"get_student_attendence_dir";

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        Map<String, String> params = new Hashtable<>();
        params.put("src_date", date);
        params.put("class", classString);
        params.put("section", sectionString);
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, createRequestSuccessListener(), createRequestErrorListener());

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsObjRequest);

    }
    private Response.Listener<JSONObject> createRequestSuccessListener(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("===> Response => ", response.toString());
                try {
                    String status = response.getString("status");
                    String message = response.getString("message");
                    Log.e("Status ", status);
                    if (status.equalsIgnoreCase("200")){
                        JSONArray jsonArray = response.getJSONArray("attendence_details");
                        Log.e("Array ", jsonArray.toString());
                        JSONObject object;
                        AttendanceDirectInfo adapter;
                        for (int  i = 0; i<jsonArray.length(); i++){
                            object = jsonArray.getJSONObject(i);
                            adapter = new AttendanceDirectInfo(object.getString("class_or_year"), object.getString("section"), object.getString("total_student"),
                                    object.getString("total_present"), object.getString("present_percent"));
                            infoList.add(adapter);
                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        AttendanceDirectAdapter attendanceAdapter = new AttendanceDirectAdapter(getContext(), infoList);
                        recyclerView.setAdapter(attendanceAdapter);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("Error ", e.getMessage());
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d(Constants.TAG, "===> JsonObjectRequest error ");
                Log.e("Error ", error.toString());

                if (error.networkResponse == null) {
                    if (error instanceof NetworkError) {
                        Toast.makeText(getContext(), "Oops. Network error! Try again", Toast.LENGTH_LONG).show();

                    } else if (error instanceof ServerError) {
                        Toast.makeText(getContext(), "Oops. Server error!", Toast.LENGTH_LONG).show();

                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getContext(), "Oops. Authentication error! Try again", Toast.LENGTH_LONG).show();

                    }else if (error instanceof TimeoutError) {
                        Toast.makeText(getContext(), "Oops. Timeout error! Try again", Toast.LENGTH_LONG).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }


    // Custom Volley request
    public class CustomRequest extends Request<JSONObject> {

        private Response.Listener<JSONObject> listener;
        private Map<String, String> params;

        public CustomRequest(String url, Map<String, String> params,
                             Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
            super(Method.GET, url, errorListener);
            this.listener = reponseListener;
            this.params = params;
        }

        CustomRequest(int method, String url, Map<String, String> params,
                      Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.listener = reponseListener;
            this.params = params;
        }

        protected Map<String, String> getParams() {
            return params;
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }

        @Override
        protected void deliverResponse(JSONObject response) {
            // TODO Auto-generated method stub
            listener.onResponse(response);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDate(int year, int month, int day){
        this.year = year;
        this.month = month+1;
        this.day = day;
        button.setText(this.day+"-"+this.month+"-"+this.year);
        dateString = button.getText().toString().trim();
    }

    public void sendNotificationStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_status/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Constants.USER_ID);
                params.put("notification_type", "attendance");
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

}
