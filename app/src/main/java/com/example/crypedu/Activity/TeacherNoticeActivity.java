package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.TeacherNoticeInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
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

public class TeacherNoticeActivity extends AppCompatActivity {

    private ArrayList<String> classArrayList;
    private CoordinatorLayout coordinatorLayout;
    private Spinner classSpinner;
    private EditText subjectEditText;
    private TextView dateTextView;
    private Spinner sectionSpinner;
    private EditText messageEditText;
    private Calendar myCalendar;
    private ArrayList<TeacherNoticeInfo> teacherNoticeInfoArrayList = new ArrayList<>();
    private String noticeId;
    private EditText editSubjectEditText;
    private Spinner editClassNameSpinner;
    private Spinner editSectionSpinner;
    private Button editDateButton;
    private EditText editMessageEditText;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_notice);
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
                Intent intent = new Intent(TeacherNoticeActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        subjectEditText = findViewById(R.id.subjectEditText);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        dateTextView = findViewById(R.id.dateTextView);
        Button dateButton = findViewById(R.id.dateButton);
        messageEditText = findViewById(R.id.messageEditText);
        Button submitButton = findViewById(R.id.submitButton);

        /*
          If internet connection is available
          then only call
          fetchClassDetails().
         */
        if (InternetCheckActivity.isConnected()){
            fetchClassDetails();
        }else {
            showSnack();
        }

        /*
          For edit notice.
         */
        FloatingActionButton fab = findViewById(R.id.editFAButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetCheckActivity.isConnected()) {
                    if (Constants.USER_ID != null && !Constants.USER_ID.equalsIgnoreCase("")) {
                        fetchAllNoticeDetails(Constants.USER_ID);
                    }
                }else {
                    showSnack();
                }
            }
        });

        /*
          If user will press on Date button
          the only display Calender.
         */
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener fromDate = new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setDate();
                    }
                };
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TeacherNoticeActivity.this, fromDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Spinner Drop down elements
        List<String> categoriesSection = new ArrayList<>();
        categoriesSection.add("Select Section");
        categoriesSection.add("A");
        categoriesSection.add("B");
        categoriesSection.add("C");
        categoriesSection.add("D");
        categoriesSection.add("E");
        categoriesSection.add("F");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterSection = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesSection);
        // Drop down layout style - list view with radio button
        dataAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sectionSpinner.setAdapter(dataAdapterSection);
        // Spinner click listener
        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classArrayList != null){
                    if (classArrayList.size() > 0){
                        String classString = classSpinner.getSelectedItem().toString().trim();
                        String sectionString = sectionSpinner.getSelectedItem().toString().trim();
                        String subjectString = subjectEditText.getText().toString().trim();
                        String dateString = dateTextView.getText().toString().trim();
                        String messageString = messageEditText.getText().toString().trim();
                        if (!subjectString.equalsIgnoreCase("") && !classString.equalsIgnoreCase("") && !classString.equalsIgnoreCase("Select Class") && !dateString.equalsIgnoreCase("") && !messageString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("Select Section")){
                            if (InternetCheckActivity.isConnected()) {
                                if (!Constants.USER_ID.equalsIgnoreCase("") && Constants.USER_ID != null) {
                                    submitNotice(classString, sectionString, subjectString, dateString, messageString);
                                }
                            }else {
                                showSnack();
                            }
                        }else{
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, " Fill up all the fields!", Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
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
            Intent intent = new Intent(TeacherNoticeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Fetch all existing notice details for
     * a particular teacher by the help of Teacher Id.
     * @param userId
     */
    private void fetchAllNoticeDetails_old(String userId) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", userId);
            clientReg.post("http://"+ Constants.server_name +"/sudhirmemorial/webservices/websvc/show_notice_details/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            teacherNoticeInfoArrayList.clear();
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String noticeId = jsonObject.getString("id");
                                String noticeSubject = jsonObject.getString("notice_subject");
                                String noticeMessage = jsonObject.getString("notice");
                                String noticeClassName = jsonObject.getString("class_name");
                                String noticeDate = jsonObject.getString("notice_date");
                                String noticeSection = jsonObject.getString("section");

                                TeacherNoticeInfo teacherNoticeInfo = new TeacherNoticeInfo(noticeId, noticeSubject, noticeMessage,
                                        noticeClassName, noticeDate, noticeSection);
                                teacherNoticeInfoArrayList.add(i, teacherNoticeInfo);
                            }

                            // show dialog box.
                            if (teacherNoticeInfoArrayList != null){
                                editDialogBox(teacherNoticeInfoArrayList);
                            }else {
                                startActivity(new Intent(TeacherNoticeActivity.this, SplashActivity.class));
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

    public void editDialogBox(final ArrayList<TeacherNoticeInfo> teacherNoticeInfoArrayList){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TeacherNoticeActivity.this);
        builderSingle.setTitle("Select subject");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TeacherNoticeActivity.this, android.R.layout.select_dialog_singlechoice);
        /*
          For displaying all notice subject onto alert dialog box.
         */
        for (TeacherNoticeInfo teacherNoticeInfo : teacherNoticeInfoArrayList){
            arrayAdapter.add(teacherNoticeInfo.noticeSubject);
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(TeacherNoticeActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your selected subject is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (classArrayList != null){
                            dialog.dismiss();

                            final Dialog finalDialogBox = new Dialog(TeacherNoticeActivity.this);
                            finalDialogBox.setContentView(R.layout.teacher_notice_custom);

                            /*
                              Store topic name into classWorkInfoArrayList for dialog box.
                             */
                            ArrayList<TeacherNoticeInfo> noticeInfoArrayList = new ArrayList<>();
                            for (TeacherNoticeInfo teacherHomeWorkInfo : teacherNoticeInfoArrayList){
                                if (teacherHomeWorkInfo.noticeSubject.equalsIgnoreCase(strName)){
                                    noticeInfoArrayList.add(teacherHomeWorkInfo);
                                }
                            }

                            // set the custom dialog components - text, image and button
                            editSubjectEditText = finalDialogBox.findViewById(R.id.subjectEditText);
                            editClassNameSpinner = finalDialogBox.findViewById(R.id.classSpinner);
                            editSectionSpinner = finalDialogBox.findViewById(R.id.sectionSpinner);
                            editDateButton = finalDialogBox.findViewById(R.id.dateButton);
                            editMessageEditText = finalDialogBox.findViewById(R.id.messageEditText);
                            final Button editSubmitButton = finalDialogBox.findViewById(R.id.submitButton);

                            /*
                              Edit section spinner.
                             */
                            // Spinner Drop down elements
                            List<String> categoriesSection = new ArrayList<>();
                            categoriesSection.add("Select Section");
                            categoriesSection.add("A");
                            categoriesSection.add("B");
                            categoriesSection.add("C");
                            categoriesSection.add("D");
                            categoriesSection.add("E");
                            categoriesSection.add("F");
                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapterSection = new ArrayAdapter<>(TeacherNoticeActivity.this, android.R.layout.simple_spinner_item, categoriesSection);
                            // Drop down layout style - list view with radio button
                            dataAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to spinner
                            editSectionSpinner.setAdapter(dataAdapterSection);
                            // Spinner click listener
                            editSectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    // On selecting a spinner item
                                    String item = parent.getItemAtPosition(position).toString();
                                    // Showing selected spinner item
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                            /*
                              Edit classname spinner.
                             */
                            // Spinner Drop down elements
                            List<String> categories = new ArrayList<>();
                            categories.add("Select Class");
                            categories.addAll(classArrayList);
                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(TeacherNoticeActivity.this, android.R.layout.simple_spinner_item, categories);
                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to spinner
                            editClassNameSpinner.setAdapter(dataAdapter);
                            // Spinner click listener
                            editClassNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    // On selecting a spinner item
                                    String item = parent.getItemAtPosition(position).toString();
                                    // Showing selected spinner item
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                            /*
                              Edit From Date button.
                             */
                            editDateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /*
                                      Set date for class work.
                                     */
                                    myCalendar = Calendar.getInstance();
                                    final DatePickerDialog.OnDateSetListener date = new
                                            DatePickerDialog.OnDateSetListener() {

                                                @Override
                                                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                                      int dayOfMonth) {
                                                    // TODO Auto-generated method stub
                                                    myCalendar.set(Calendar.YEAR, year);
                                                    myCalendar.set(Calendar.MONTH, monthOfYear);
                                                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                                    String myFormat = "yyyy-MM-dd"; //In which you need put here
                                                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                                    editDateButton.setText(sdf.format(myCalendar.getTime()));
                                                }
                                            };
                                    editDateButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new DatePickerDialog(TeacherNoticeActivity.this, date, myCalendar
                                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                        }
                                    });
                                }
                            });

                            /*
                              Set all edit field value which has
                              already present in database that means before reassign.
                             */
                            for (TeacherNoticeInfo teacherNoticeInfo : noticeInfoArrayList){
                                int selectionPositionClass = dataAdapter.getPosition(teacherNoticeInfo.noticeClassName);
                                editClassNameSpinner.setSelection(selectionPositionClass);

                                int selectionPositionSection = dataAdapterSection.getPosition(teacherNoticeInfo.noticeSection);
                                editSectionSpinner.setSelection(selectionPositionSection);

                                editSubjectEditText.setText(teacherNoticeInfo.noticeSubject);
                                editMessageEditText.setText(teacherNoticeInfo.noticeMessage);
                                editDateButton.setText(teacherNoticeInfo.noticeDate);
                                noticeId = teacherNoticeInfo.noticeId;
                            }

                            /*
                              If 'Reassign' button is clicked, close the custom dialog.
                             */
                            editSubmitButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalDialogBox.dismiss();
                                    if (InternetCheckActivity.isConnected()){
                                        String editClassString = editClassNameSpinner.getSelectedItem().toString().trim();
                                        String editSectionString = editSectionSpinner.getSelectedItem().toString().trim();
                                        String editSubjectString = editSubjectEditText.getText().toString().trim();
                                        String editMessageString = editMessageEditText.getText().toString().trim();
                                        String editDateString = editDateButton.getText().toString().trim();

                                        if (!editSubjectString.equalsIgnoreCase("") && !editClassString.equalsIgnoreCase("") && !editClassString.equalsIgnoreCase("Select Class") && !editSectionString.equalsIgnoreCase("") && !editSectionString.equalsIgnoreCase("Select Section") && !editMessageString.equalsIgnoreCase("") && !editDateString.equalsIgnoreCase("")){
                                            editNotice(editSubjectString, editClassString, editSectionString, editMessageString, editDateString);
                                        }else {
                                            Snackbar snackbar = Snackbar.make(coordinatorLayout, " Reassign failed!, fill up all information correctly. ", Snackbar.LENGTH_LONG);
                                            // Changing action button text color
                                            View sbView = snackbar.getView();
                                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                                            textView.setTextColor(Color.parseColor(Constants.colorRed));
                                            snackbar.show();
                                        }
                                    }else {
                                        showSnack();
                                    }
                                }
                            });
                            finalDialogBox.show();
                        }else {
                            startActivity(new Intent(TeacherNoticeActivity.this, SplashActivity.class));
                        }
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    /**
     * Sending notice details to the server respective of a notice Id.
     * @param editSubjectString
     * @param editClassString
     * @param editSectionString
     * @param editMessageString
     * @param editDateString
     */
    private void editNotice_old(String editSubjectString, String editClassString, String editSectionString, String editMessageString, String editDateString) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id",noticeId);
            requestParams.put("notice_subject", editSubjectString);
            requestParams.put("notice", editMessageString);
            requestParams.put("class_name", editClassString);
            requestParams.put("notice_date", editDateString);
            requestParams.put("section", editSectionString);
            clientReg.post("http://"+ Constants.server_name +"/sudhirmemorial/webservices/websvc/edit_notice/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            editSubjectEditText.setText("");
                            editClassNameSpinner.setSelection(0);
                            editSectionSpinner.setSelection(0);
                            editDateButton.setText("");
                            editMessageEditText.setText("");
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
//                            startActivity(new Intent(TeacherNoticeActivity.this, TeacherNoticeActivity.class));
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

    /**
     * Submit notice to the server by Teacher.
     * @param classString
     * @param sectionString
     * @param subjectString
     * @param dateString
     * @param messageString
     */
    private void submitNotice_old(String classString, String sectionString, String subjectString, String dateString, String messageString) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            requestParams.put("notice_subject", subjectString);
            requestParams.put("notice", messageString);
            requestParams.put("class_name", classString);
            requestParams.put("notice_date", dateString);
            requestParams.put("section", sectionString);
            clientReg.post("http://"+ Constants.server_name +"/sudhirmemorial/webservices/websvc/notice_board_app/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            subjectEditText.setText("");
                            classSpinner.setSelection(0);
                            sectionSpinner.setSelection(0);
                            dateTextView.setText("");
                            messageEditText.setText("");
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
//                            startActivity(new Intent(TeacherNoticeActivity.this, TeacherNoticeActivity.class));
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

    //----------------------------
    // Set date onto date TextView.
    //----------------------------
    private void setDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTextView.setText(sdf.format(myCalendar.getTime()));
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(TeacherNoticeActivity.this, MenuActivity.class));
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
    // Fetch Class details from server.
    //--------------------------------
    public void fetchClassDetails_old(){
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.get("http://"+ Constants.server_name +"/sudhirmemorial/webservices/websvc/get_all_class/", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            classArrayList = new ArrayList<>();
                            JSONArray jsonArrayList = obj.getJSONArray("classes");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String class_or_year = jsonObject.getString("class_or_year");
                                classArrayList.add(i, class_or_year);
                            }
                            classSpinner();
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

    //--------------------------
    // Set class spinner content
    // from server.
    //--------------------------
    private void classSpinner(){
        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("Select Class");
        categories.addAll(classArrayList);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        classSpinner.setAdapter(dataAdapter);
        // Spinner click listener
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void fetchAllNoticeDetails(final String userId) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"/sudhirmemorial/webservices/websvc/show_notice_details/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        teacherNoticeInfoArrayList.clear();
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String noticeId = jsonObject.getString("id");
                            String noticeSubject = jsonObject.getString("notice_subject");
                            String noticeMessage = jsonObject.getString("notice");
                            String noticeClassName = jsonObject.getString("class_name");
                            String noticeDate = jsonObject.getString("notice_date");
                            String noticeSection = jsonObject.getString("section");

                            TeacherNoticeInfo teacherNoticeInfo = new TeacherNoticeInfo(noticeId, noticeSubject, noticeMessage,
                                    noticeClassName, noticeDate, noticeSection);
                            teacherNoticeInfoArrayList.add(i, teacherNoticeInfo);
                        }

                        // show dialog box.
                        if (teacherNoticeInfoArrayList != null){
                            editDialogBox(teacherNoticeInfoArrayList);
                        }else {
                            startActivity(new Intent(TeacherNoticeActivity.this, SplashActivity.class));
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
                params.put("id", userId);
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

    public void editNotice(final String editSubjectString, final String editClassString, final String editSectionString, final String editMessageString, final String editDateString) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"/sudhirmemorial/webservices/websvc/edit_notice/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        editSubjectEditText.setText("");
                        editClassNameSpinner.setSelection(0);
                        editSectionSpinner.setSelection(0);
                        editDateButton.setText("");
                        editMessageEditText.setText("");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
//                            startActivity(new Intent(TeacherNoticeActivity.this, TeacherNoticeActivity.class));
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
                params.put("id",noticeId);
                params.put("notice_subject", editSubjectString);
                params.put("notice", editMessageString);
                params.put("class_name", editClassString);
                params.put("notice_date", editDateString);
                params.put("section", editSectionString);
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

    public void submitNotice(final String classString, final String sectionString, final String subjectString, final String dateString, final String messageString) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"/sudhirmemorial/webservices/websvc/notice_board_app/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        subjectEditText.setText("");
                        classSpinner.setSelection(0);
                        sectionSpinner.setSelection(0);
                        dateTextView.setText("");
                        messageEditText.setText("");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
//                            startActivity(new Intent(TeacherNoticeActivity.this, TeacherNoticeActivity.class));
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
                params.put("notice_subject", subjectString);
                params.put("notice", messageString);
                params.put("class_name", classString);
                params.put("notice_date", dateString);
                params.put("section", sectionString);
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

    public void fetchClassDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"/sudhirmemorial/webservices/websvc/get_all_class/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        classArrayList = new ArrayList<>();
                        JSONArray jsonArrayList = obj.getJSONArray("classes");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String class_or_year = jsonObject.getString("class_or_year");
                            classArrayList.add(i, class_or_year);
                        }
                        classSpinner();
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
