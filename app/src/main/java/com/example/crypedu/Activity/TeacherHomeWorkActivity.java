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
import com.example.crypedu.Pojo.TeacherHomeWorkInfo;
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

public class TeacherHomeWorkActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private EditText topicEditText,editTopicEditText;
    private TextView fromDateTextView,toDateTextView;
    private Button editFromDateButton;
    private Button editToDateButton;
    private Calendar myCalendar;
    private ArrayList<TeacherHomeWorkInfo> teacherHomeWorkInfoArrayList = new ArrayList<>();
    private ArrayList<String> classArrayList, subjectArrayList;
    private String homeWorkId;
    private Spinner editSubjectSpinner, editClassNameSpinner, editSectionSpinner,classSpinner,subjectSpinner,sectionSpinner;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home_work);
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
                Intent intent = new Intent(TeacherHomeWorkActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        FloatingActionButton editHomeWorkFAButton = findViewById(R.id.editHomeWorkFAButton);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        topicEditText = findViewById(R.id.topicEditText);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        fromDateTextView = findViewById(R.id.fromDateTextView);
        toDateTextView = findViewById(R.id.toDateTextView);
        Button submitButton = findViewById(R.id.submitButton);
        Button fromDateButton = findViewById(R.id.fromDateButton);
        Button toDateButton = findViewById(R.id.toDateButton);

        /*
          Fetch all class and subject name from server.
         */
        if (InternetCheckActivity.isConnected()) {
            fetchClassDetails();
            fetchSubjectDetails();
        }else {
            showSnack();
        }

        /*
          For edit class work.
         */
        editHomeWorkFAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetCheckActivity.isConnected()) {
                    if (Constants.USER_ID != null) {
                        fetchHomeWorkDetails(Constants.USER_ID);
                    }
                }else {
                    showSnack();
                }
            }
        });

        /*
          Set date for class work.
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
                        updateLabelFrom();
                    }
                };
        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TeacherHomeWorkActivity.this, fromDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener toDate = new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabelTo();
                    }
                };
        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TeacherHomeWorkActivity.this, toDate, myCalendar
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
                if (subjectArrayList != null && classArrayList != null) {
                    if (subjectArrayList.size() > 0 && classArrayList.size() > 0) {
                        String subjectString = subjectSpinner.getSelectedItem().toString().trim();
                        String classString = classSpinner.getSelectedItem().toString().trim();
                        String sectionString = sectionSpinner.getSelectedItem().toString().trim();
                        String topicString = topicEditText.getText().toString().trim();
                        String fromDateString = fromDateTextView.getText().toString().trim();
                        String toDateString = toDateTextView.getText().toString().trim();
                        if (!subjectString.equalsIgnoreCase("") && !subjectString.equalsIgnoreCase("Select Subject") && !classString.equalsIgnoreCase("") && !classString.equalsIgnoreCase("Select Class") && !topicString.equalsIgnoreCase("") && !fromDateString.equalsIgnoreCase("") && !toDateString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("Select Section")) {
                            /*
                              Difference between fromDate and toDate minimum 1 day.
                             */
                            if (fromDateString.compareTo(toDateString) <= 0) {
                                if (InternetCheckActivity.isConnected()) {
                                    assignHomeWork(subjectString, classString, topicString, fromDateString, sectionString, toDateString);
                                }else {
                                    showSnack();
                                }
                            } else {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, " Reassign failed!, due to fromDate and toDate. ", Snackbar.LENGTH_LONG);
                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.parseColor(Constants.colorRed));
                                snackbar.show();
                            }
                        } else {
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
            Intent intent = new Intent(TeacherHomeWorkActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //--------------------------------
    // Insert Class Work by Teacher.
    //--------------------------------
    public void assignHomeWork_old(String subjectString, String classString, String topicString, String fromDateString, String sectionString, String toDateString) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            requestParams.put("subject", subjectString);
            requestParams.put("topic", topicString);
            requestParams.put("class_name", classString);
            requestParams.put("date_of_class", fromDateString);
            requestParams.put("section", sectionString);
            requestParams.put("complete_date", toDateString);
            clientReg.post(Constants.BASE_SERVER + "home_work_app/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            subjectSpinner.setSelection(0);
                            classSpinner.setSelection(0);
                            sectionSpinner.setSelection(0);
                            topicEditText.setText("");
                            fromDateTextView.setText("");
                            toDateTextView.setText("");
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
//                            startActivity(new Intent(TeacherHomeWorkActivity.this, TeacherHomeWorkActivity.class));
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
    // Fetch all homework details by
    // the help of teacherId.
    //--------------------------------
    public void fetchHomeWorkDetails_old(String teacherId) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", teacherId);
            clientReg.post(Constants.BASE_SERVER + "show_homework_details/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            teacherHomeWorkInfoArrayList.clear();
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String homeWorkId = jsonObject.getString("id");
                                String homeWorkSubject = jsonObject.getString("subject");
                                String homeWorkTopic = jsonObject.getString("topic");
                                String className = jsonObject.getString("class_name");
                                String section = jsonObject.getString("section");
                                String fromDate = jsonObject.getString("date_of_class");
                                String toDate = jsonObject.getString("complete_date");

                                TeacherHomeWorkInfo teacherHomeWorkInfo = new TeacherHomeWorkInfo(homeWorkId, homeWorkSubject, homeWorkTopic,
                                        className, section, fromDate, toDate);
                                teacherHomeWorkInfoArrayList.add(i, teacherHomeWorkInfo);
                            }

                            // show dialog box.
                            if (teacherHomeWorkInfoArrayList != null) {
                                editDialogBox(teacherHomeWorkInfoArrayList);
                            } else {
                                startActivity(new Intent(TeacherHomeWorkActivity.this, SplashActivity.class));
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

    public void editDialogBox(final ArrayList<TeacherHomeWorkInfo> teacherHomeWorkInfoArrayList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TeacherHomeWorkActivity.this);
        builderSingle.setTitle("Select topic");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TeacherHomeWorkActivity.this, android.R.layout.select_dialog_singlechoice);

        for (TeacherHomeWorkInfo teacherHomeWorkInfo : teacherHomeWorkInfoArrayList) {
            arrayAdapter.add(teacherHomeWorkInfo.homeWorkTopic);
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
                AlertDialog.Builder builderInner = new AlertDialog.Builder(TeacherHomeWorkActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your selected topic is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (classArrayList != null && subjectArrayList != null) {
                            dialog.dismiss();

                            final Dialog finalDialogBox = new Dialog(TeacherHomeWorkActivity.this);
                            finalDialogBox.setContentView(R.layout.teacher_home_work_custom);

                            /*
                              Store topic name into classWorkInfoArrayList for dialog box.
                             */
                            ArrayList<TeacherHomeWorkInfo> homeWorkInfoArrayList = new ArrayList<>();
                            for (TeacherHomeWorkInfo teacherHomeWorkInfo : teacherHomeWorkInfoArrayList) {
                                if (teacherHomeWorkInfo.homeWorkTopic.equalsIgnoreCase(strName)) {
                                    homeWorkInfoArrayList.add(teacherHomeWorkInfo);
                                }
                            }

                            // set the custom dialog components - text, image and button
                            editSubjectSpinner = finalDialogBox.findViewById(R.id.subjectSpinner);
                            editTopicEditText = finalDialogBox.findViewById(R.id.topicEditText);
                            editClassNameSpinner = finalDialogBox.findViewById(R.id.classNameSpinner);
                            editSectionSpinner = finalDialogBox.findViewById(R.id.sectionSpinner);
                            editFromDateButton = finalDialogBox.findViewById(R.id.fromDateButton);
                            editToDateButton = finalDialogBox.findViewById(R.id.toDateButton);
                            Button reassignButton = finalDialogBox.findViewById(R.id.reassignButton);

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
                            ArrayAdapter<String> dataAdapterSection = new ArrayAdapter<>(TeacherHomeWorkActivity.this, android.R.layout.simple_spinner_item, categoriesSection);
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

                            // Spinner Drop down elements
                            List<String> categories = new ArrayList<>();
                            categories.add("Select Class");
                            categories.addAll(classArrayList);
                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(TeacherHomeWorkActivity.this, android.R.layout.simple_spinner_item, categories);
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
                              Edit subject spinner.
                             */
                            // Spinner Drop down elements
                            List<String> categoriesSubject = new ArrayList<>();
                            categoriesSubject.add("Select Subject");
                            categoriesSubject.addAll(subjectArrayList);
                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapterClass = new ArrayAdapter<>(TeacherHomeWorkActivity.this, android.R.layout.simple_spinner_item, categoriesSubject);
                            // Drop down layout style - list view with radio button
                            dataAdapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to spinner
                            editSubjectSpinner.setAdapter(dataAdapterClass);
                            // Spinner click listener
                            editSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            editFromDateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
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
                                                    editFromDateButton.setText(sdf.format(myCalendar.getTime()));
                                                }
                                            };
                                    editFromDateButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new DatePickerDialog(TeacherHomeWorkActivity.this, date, myCalendar
                                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                        }
                                    });
                                }
                            });

                            /*
                              Edit To Date button.
                             */
                            editToDateButton.setOnClickListener(new View.OnClickListener() {
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
                                                    editToDateButton.setText(sdf.format(myCalendar.getTime()));
                                                }
                                            };
                                    editToDateButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new DatePickerDialog(TeacherHomeWorkActivity.this, date, myCalendar
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
                            for (TeacherHomeWorkInfo teacherHomeWorkInfo : homeWorkInfoArrayList) {
                                int selectionPositionSubject = dataAdapterClass.getPosition(teacherHomeWorkInfo.homeWorkSubject);
                                editSubjectSpinner.setSelection(selectionPositionSubject);

                                int selectionPositionClass = dataAdapter.getPosition(teacherHomeWorkInfo.className);
                                editClassNameSpinner.setSelection(selectionPositionClass);

                                int selectionPositionSection = dataAdapterSection.getPosition(teacherHomeWorkInfo.section);
                                editSectionSpinner.setSelection(selectionPositionSection);

                                editTopicEditText.setText(teacherHomeWorkInfo.homeWorkTopic);
                                editFromDateButton.setText(teacherHomeWorkInfo.fromDate);
                                editToDateButton.setText(teacherHomeWorkInfo.toDate);
                                homeWorkId = teacherHomeWorkInfo.homeWorkId;
                            }

                            /*
                              If 'Reassign' button is clicked, close the custom dialog.
                             */
                            reassignButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalDialogBox.dismiss();
                                    if (InternetCheckActivity.isConnected()) {
                                        String editSubjectString = editSubjectSpinner.getSelectedItem().toString().trim();
                                        String editClassString = editClassNameSpinner.getSelectedItem().toString().trim();
                                        String editSectionString = editSectionSpinner.getSelectedItem().toString().trim();
                                        String topicString = editTopicEditText.getText().toString().trim();
                                        String fromDateString = editFromDateButton.getText().toString().trim();
                                        String toDateString = editToDateButton.getText().toString().trim();

                                        if (!editSubjectString.equalsIgnoreCase("") && !editSubjectString.equalsIgnoreCase("Select Subject") && !topicString.equalsIgnoreCase("") && !editClassString.equalsIgnoreCase("") && !editClassString.equalsIgnoreCase("Select Class") && !editSectionString.equalsIgnoreCase("") && !editSectionString.equalsIgnoreCase("Select Section") && !fromDateString.equalsIgnoreCase("") && !toDateString.equalsIgnoreCase("") && homeWorkId != null && !homeWorkId.equalsIgnoreCase("")) {
                                            if (fromDateString.compareTo(toDateString) <= 0) {
                                                editHomeWork(homeWorkId, editSubjectString, topicString, editClassString, editSectionString, fromDateString, toDateString);
                                            } else {
                                                Snackbar snackbar = Snackbar.make(coordinatorLayout, " Reassign failed!, due to fromDate and toDate. ", Snackbar.LENGTH_LONG);
                                                // Changing action button text color
                                                View sbView = snackbar.getView();
                                                TextView textView = sbView.findViewById(R.id.snackbar_text);
                                                textView.setTextColor(Color.parseColor(Constants.colorRed));
                                                snackbar.show();
                                            }
                                        } else {
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
                        } else {
                            startActivity(new Intent(TeacherHomeWorkActivity.this, SplashActivity.class));
                        }
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    //--------------------------------
    // Edit Home Work by Teacher.
    //--------------------------------
    public void editHomeWork_old(String homeWorkId, String editSubjectString, String topicString, String editClassString,
                             String editSectionString, String fromDateString, String toDateString) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", homeWorkId);
            requestParams.put("subject", editSubjectString);
            requestParams.put("topic", topicString);
            requestParams.put("class_name", editClassString);
            requestParams.put("date_of_class", fromDateString);
            requestParams.put("section", editSectionString);
            requestParams.put("complete_date", toDateString);
            clientReg.post(Constants.BASE_SERVER + "edit_homework/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            editSubjectSpinner.setSelection(0);
                            editClassNameSpinner.setSelection(0);
                            editSectionSpinner.setSelection(0);
                            editTopicEditText.setText("");
                            editFromDateButton.setText("");
                            editToDateButton.setText("");
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
//                            startActivity(new Intent(TeacherHomeWorkActivity.this, TeacherHomeWorkActivity.class));
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

    //------------------------
    // Set from date onto TextView.
    //------------------------
    private void updateLabelFrom() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fromDateTextView.setText(sdf.format(myCalendar.getTime()));
    }

    //------------------------
    // Set to date onto TextView.
    //------------------------
    private void updateLabelTo() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        toDateTextView.setText(sdf.format(myCalendar.getTime()));
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(TeacherHomeWorkActivity.this, MenuActivity.class));
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
    public void fetchClassDetails_old() {

        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.get(Constants.BASE_SERVER + "get_all_class/", new JsonHttpResponseHandler() {
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
    private void classSpinner() {
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

    //------------------------------
    // Fetch subject details from
    // server.
    //------------------------------
    public void fetchSubjectDetails_old() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            RequestParams params = new RequestParams();
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.get(Constants.BASE_SERVER + "svcsubjects/", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            subjectArrayList = new ArrayList<>();
                            JSONArray jsonArrayList = obj.getJSONArray("subjects");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String subjectName = jsonObject.getString("subject");
                                subjectArrayList.add(i, subjectName);
                            }
                            subjectSpinner();
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
                    pbHeaderProgress.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    //-----------------------------
    // Set subject spinner content
    // from server.
    //-----------------------------
    private void subjectSpinner() {
        // Spinner Drop down elements
        List<String> categoriesSubject = new ArrayList<>();
        categoriesSubject.add("Select Subject");
        categoriesSubject.addAll(subjectArrayList);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterClass = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesSubject);

        // Drop down layout style - list view with radio button
        dataAdapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        subjectSpinner.setAdapter(dataAdapterClass);

        // Spinner click listener
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void assignHomeWork(final String subjectString, final String classString, final String topicString, final String fromDateString, final String sectionString, final String toDateString) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "home_work_app/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        subjectSpinner.setSelection(0);
                        classSpinner.setSelection(0);
                        sectionSpinner.setSelection(0);
                        topicEditText.setText("");
                        fromDateTextView.setText("");
                        toDateTextView.setText("");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
//                            startActivity(new Intent(TeacherHomeWorkActivity.this, TeacherHomeWorkActivity.class));
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
                params.put("subject", subjectString);
                params.put("topic", topicString);
                params.put("class_name", classString);
                params.put("date_of_class", fromDateString);
                params.put("section", sectionString);
                params.put("complete_date", toDateString);
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

    public void fetchHomeWorkDetails(final String teacherId) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "show_homework_details/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        teacherHomeWorkInfoArrayList.clear();
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String homeWorkId = jsonObject.getString("id");
                            String homeWorkSubject = jsonObject.getString("subject");
                            String homeWorkTopic = jsonObject.getString("topic");
                            String className = jsonObject.getString("class_name");
                            String section = jsonObject.getString("section");
                            String fromDate = jsonObject.getString("date_of_class");
                            String toDate = jsonObject.getString("complete_date");

                            TeacherHomeWorkInfo teacherHomeWorkInfo = new TeacherHomeWorkInfo(homeWorkId, homeWorkSubject, homeWorkTopic,
                                    className, section, fromDate, toDate);
                            teacherHomeWorkInfoArrayList.add(i, teacherHomeWorkInfo);
                        }

                        // show dialog box.
                        if (teacherHomeWorkInfoArrayList != null) {
                            editDialogBox(teacherHomeWorkInfoArrayList);
                        } else {
                            startActivity(new Intent(TeacherHomeWorkActivity.this, SplashActivity.class));
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
                params.put("id", teacherId);
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

    public void editHomeWork(final String homeWorkId, final String editSubjectString, final String topicString, final String editClassString,
                             final String editSectionString, final String fromDateString, final String toDateString) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "edit_homework/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        editSubjectSpinner.setSelection(0);
                        editClassNameSpinner.setSelection(0);
                        editSectionSpinner.setSelection(0);
                        editTopicEditText.setText("");
                        editFromDateButton.setText("");
                        editToDateButton.setText("");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
//                            startActivity(new Intent(TeacherHomeWorkActivity.this, TeacherHomeWorkActivity.class));
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
                params.put("id", homeWorkId);
                params.put("subject", editSubjectString);
                params.put("topic", topicString);
                params.put("class_name", editClassString);
                params.put("date_of_class", fromDateString);
                params.put("section", editSectionString);
                params.put("complete_date", toDateString);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_all_class/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
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

    public void fetchSubjectDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "svcsubjects/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        subjectArrayList = new ArrayList<>();
                        JSONArray jsonArrayList = obj.getJSONArray("subjects");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String subjectName = jsonObject.getString("subject");
                            subjectArrayList.add(i, subjectName);
                        }
                        subjectSpinner();
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
