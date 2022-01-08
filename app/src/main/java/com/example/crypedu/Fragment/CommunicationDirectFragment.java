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
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Adapter.ViewRequestDirectAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ViewRequestDirectInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tsongkha.spinnerdatepicker.DatePicker;
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
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class CommunicationDirectFragment extends Fragment implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener{

    private CoordinatorLayout coordinatorLayout;
    private View myFragmentView;
    private TextView copyRight_textView;
    private EditText to_editText, subject_editText, message_editText;
    private Button submit_button, viewButton, fromDateButton, toDateButton;
    private Spinner typeSpinner;
    private Calendar myCalendarFrom, myCalendarTo;
    private Spinner classSpinner, sectionSpinner;
    private String classString = null, sectionString = null;
    private JSONObject jsonObject = null;
    private Button submitButton, pickDate;
    private int year = 0, month = 0, day = 0, dateFlag = 0, sectionFlag = 0;
    private String dateString = "";
    private ArrayList<String> classList = new ArrayList<>(), sectionList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ArrayList<ViewRequestDirectInfo> viewRequestList;
    private ListView listView;
    SimpleDateFormat simpleDateFormat;


    public CommunicationDirectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_communication_direct, container, false);

        simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        /*
          Set BubbleGumSans Regular font.
         */
        Typeface typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getApplication().getAssets(), Constants.BubblegumSans_Regular_font);

        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);

        classSpinner = myFragmentView.findViewById(R.id.class_spinner);
        sectionSpinner = myFragmentView.findViewById(R.id.section_spinner);
        submitButton = myFragmentView.findViewById(R.id.submit_spin);
        pickDate = myFragmentView.findViewById(R.id.pickDateBn);
        listView = myFragmentView.findViewById(R.id.listView);
        /*
          If internet connection is working
          then only call fetchClassDetails().
         */
        if (InternetCheckActivity.isConnected()) {
            getClassSection();
        } else {
            showSnack();
        }

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    sectionString = sectionSpinner.getItemAtPosition(position).toString();
                    sectionFlag = 1;
                    Toast.makeText(getContext(), sectionString, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(), classString, Toast.LENGTH_LONG).show();
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
                        .context(getContext())
                        .callback(CommunicationDirectFragment.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .defaultDate(year, month, day)
                        .build()
                        .show();

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (classString == null) {
                    Toast.makeText(ViewRequestDirectActivity.this, "Please select class", Toast.LENGTH_LONG).show();
                    return;
                }
                if (sectionFlag == 1 && classString == null) {
                    Toast.makeText(ViewRequestDirectActivity.this, "Please select class", Toast.LENGTH_LONG).show();
                    return;
                }*/
                if (classString == null && sectionString == null) {
                    Toast.makeText(getContext(), "Required information", Toast.LENGTH_LONG).show();
                }else {
                    if (InternetCheckActivity.isConnected()) {
                        fetchRequestDetails(classString, sectionString);
                        submitButton.setVisibility(View.GONE);
                    } else {
                        showSnack();
                    }
                }

            }
        });



       /* copyRight_textView = myFragmentView.findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);
        to_editText = myFragmentView.findViewById(R.id.to_editText);
        to_editText.setTypeface(typeface);
        subject_editText = myFragmentView.findViewById(R.id.subject_editText);
        subject_editText.setTypeface(typeface);
        message_editText = myFragmentView.findViewById(R.id.message_editText);
        message_editText.setTypeface(typeface);
        submit_button = myFragmentView.findViewById(R.id.submit_button);
        submit_button.setTypeface(typeface);
        TextView note_textView = myFragmentView.findViewById(R.id.note_textView);
        note_textView.setTypeface(typeface);
        typeSpinner = myFragmentView.findViewById(R.id.typeSpinner);
        fromDateButton = myFragmentView.findViewById(R.id.fromDateButton);
        toDateButton = myFragmentView.findViewById(R.id.toDateButton);
        viewButton = myFragmentView.findViewById(R.id.viewButton);
        viewButton.setTypeface(typeface);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplication(), ViewRequestDirectActivity.class));
            }
        });*/
        /*
          If user will press on Date button
          the only display Calender.
         */
       /* myCalendarFrom = Calendar.getInstance();
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
                new DatePickerDialog(Objects.requireNonNull(getContext()), fromDate, myCalendarFrom
                        .get(Calendar.YEAR), myCalendarFrom.get(Calendar.MONTH),
                        myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Objects.requireNonNull(getContext()), toDate, myCalendarTo
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
        ArrayAdapter<String> dataAdapterSection = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplication(), android.R.layout.simple_spinner_item, categoriesSection);
        // Drop down layout style - list view with radio button
        dataAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        typeSpinner.setAdapter(dataAdapterSection);
        // Spinner click listener
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
//                TextView selectedText = (TextView) parent.getChildAt(0);
//                if (selectedText != null) {
//                    selectedText.setTextColor(Color.BLACK);
//                }
                //((TextView) parent.getChildAt(0)).setTextSize(15);
//               ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                // ((TextView) typeSpinner.getSelectedView()).setTextColor(Color.WHITE);
                // Showing selected spinner item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeSpinnerString = typeSpinner.getSelectedItem().toString().trim();
                String fromDateString = fromDateButton.getText().toString().trim();
                String toDateString = toDateButton.getText().toString().trim();
                String subject = subject_editText.getText().toString().trim();
                String message = message_editText.getText().toString().trim();
                if (subject.length() > 0 && message.length() > 0 && !fromDateString.equalsIgnoreCase("From")
                        && !toDateString.equalsIgnoreCase("To") && !typeSpinnerString.equalsIgnoreCase("Select request type")) {
                    if (fromDateString.compareTo(toDateString) <= 0) {
                        if (InternetCheckActivity.isConnected()) {
                            submitRequest(subject, message, typeSpinnerString, fromDateString, toDateString);
                            subject_editText.setText("");
                            message_editText.setText("");
                        }else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "failed!, due to fromDate and toDate. ", Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorRed));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, " no data found!", Snackbar.LENGTH_LONG);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.parseColor(Constants.colorAccent));
                    snackbar.show();
                }
            }
        });*/

        return myFragmentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                        adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.spinner_layout, classList) {
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
                        adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.spinner_layout, sectionList) {
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
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Something goes to wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        queue.add(request);
    }

    private void showSnack() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    //--------------------------------
    // Fetch Request details from server
    // respective of student_id.
    //--------------------------------
    public void fetchRequestDetails_old(String classSt, String section) {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("class", classSt);
            requestParams.put("section", section);
            if (dateString.isEmpty() && dateFlag == 0) {
                requestParams.put("date", "");
            } else if (!dateString.isEmpty() && dateFlag == 1) {
                requestParams.put("date", dateString);
            }
            clientReg.post(Constants.BASE_SERVER + "communicate_dir/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        viewRequestList = new ArrayList<>();
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArray = obj.getJSONArray("notification_status");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                String subjectStr = jsonObject.getString("subject");
                                String classStr = jsonObject.getString("class");
                                String sectionStr = jsonObject.getString("section");
                                String messageStr = jsonObject.getString("message");
                                String fromDateStr = jsonObject.getString("date");
                                String typeStr = jsonObject.getString("type");

                                ViewRequestDirectInfo viewRequestInfo = new ViewRequestDirectInfo(name, subjectStr, messageStr, fromDateStr, typeStr, classStr, sectionStr);
                                viewRequestList.add(i, viewRequestInfo);
                            }
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(new ViewRequestDirectAdapter(getContext(), viewRequestList, getLayoutInflater()));
                            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String commId = viewRequestList.get(position).commId;
                                    if (InternetCheckActivity.isConnected()){
                                        communicationReply(commId);
                                        Constants.communicationId = commId;
                                    }else {
                                        showSnack();
                                    }
                                }
                            });*/
                        } else {
                            listView.setVisibility(View.GONE);
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


    @SuppressLint("SetTextI18n")
    private void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month + 1;
        this.day = day;
        pickDate.setText(this.day + "-" + this.month + "-" + this.year);
        dateFlag = 1;
        dateString = pickDate.getText().toString().trim();
        submitButton.setVisibility(View.VISIBLE);
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

    //--------------------------------
    // Submit Request to server
    // with 'studentId, message,
    // subject' param.
    //--------------------------------
    public void submitRequest_old(String subject, String message, String typeSpinnerString, String fromDateString, String toDateString) {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
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
                @SuppressLint("SetTextI18n")
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }

    public void submitRequest(final String subject, final String message, final String typeSpinnerString, final String fromDateString, final String toDateString) {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "send_request/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    public void fetchRequestDetails(final String classSt, final String section) {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "communicate_dir/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    viewRequestList = new ArrayList<>();
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = obj.getJSONArray("notification_status");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String subjectStr = jsonObject.getString("subject");
                            String classStr = jsonObject.getString("class");
                            String sectionStr = jsonObject.getString("section");
                            String messageStr = jsonObject.getString("message");
                            String fromDateStr = jsonObject.getString("date");
                            String typeStr = jsonObject.getString("type");

                            ViewRequestDirectInfo viewRequestInfo = new ViewRequestDirectInfo(name, subjectStr, messageStr, fromDateStr, typeStr, classStr, sectionStr);
                            viewRequestList.add(i, viewRequestInfo);
                        }
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(new ViewRequestDirectAdapter(getContext(), viewRequestList, getLayoutInflater()));
                            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String commId = viewRequestList.get(position).commId;
                                    if (InternetCheckActivity.isConnected()){
                                        communicationReply(commId);
                                        Constants.communicationId = commId;
                                    }else {
                                        showSnack();
                                    }
                                }
                            });*/
                    } else {
                        listView.setVisibility(View.GONE);
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
                params.put("class", classSt);
                params.put("section", section);
                if (dateString.isEmpty() && dateFlag == 0) {
                    params.put("date", "");
                } else if (!dateString.isEmpty() && dateFlag == 1) {
                    params.put("date", dateString);
                }
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
