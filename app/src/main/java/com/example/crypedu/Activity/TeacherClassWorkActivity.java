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
import com.example.crypedu.Pojo.TeacherClassWorkInfo;
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

public class TeacherClassWorkActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private EditText topicEditText, editTopicEditText;
    private Spinner classSpinner, editSubjectSpinner, editClassNameSpinner, editSectionSpinner, sectionSpinner,subjectSpinner;
    private TextView dateTextView;
    private Calendar myCalendar;

    private Button dateButton, editDateButton;
    private ArrayList<TeacherClassWorkInfo> teacherClassWorkInfoArrayList = new ArrayList<>();
    private ArrayList<String> classArrayList, subjectArrayList;
    private String classWorkId;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_work);
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
                Intent intent = new Intent(TeacherClassWorkActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        /*
          If Constant.USER_ID is null and empty.
         */
        if (Constants.USER_ID == null && Constants.USER_ID.equalsIgnoreCase("")) {
            startActivity(new Intent(TeacherClassWorkActivity.this, SplashActivity.class));
        }

        FloatingActionButton editClassWorkFAButton = findViewById(R.id.editClassWorkFAButton);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        topicEditText = findViewById(R.id.topicEditText);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        dateTextView = findViewById(R.id.dateTextView);
        Button submitButton = findViewById(R.id.submitButton);
        dateButton = findViewById(R.id.dateButton);

        if (InternetCheckActivity.isConnected()) {
            fetchClassDetails();
            fetchSubjectDetails();
        }else {
            showSnack();
        }

        editClassWorkFAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetCheckActivity.isConnected()) {
                    if (Constants.USER_ID != null) {
                        fetchClassWorkDetails(Constants.USER_ID);
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
        final DatePickerDialog.OnDateSetListener date = new
                DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                };
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TeacherClassWorkActivity.this, date, myCalendar
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
                        String dateString = dateTextView.getText().toString().trim();
                        if (!subjectString.equalsIgnoreCase("") && !subjectString.equalsIgnoreCase("Select Subject") && !classString.equalsIgnoreCase("") && !classString.equalsIgnoreCase("Select Class") && !topicString.equalsIgnoreCase("") && !dateString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("Select Section")) {
                            if (InternetCheckActivity.isConnected()) {
                                insertClassWork(subjectString, classString, topicString, dateString, sectionString);
                            }else {
                                showSnack();
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
            Intent intent = new Intent(TeacherClassWorkActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //--------------------------------
    // Insert Class Work by Teacher.
    //--------------------------------
    public void insertClassWork_old(String subjectString, String classString, String topicString, String dateString, String sectionString) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            requestParams.put("subject", subjectString);
            requestParams.put("topic", topicString);
            requestParams.put("class_name", classString);
            requestParams.put("date_of_class", dateString);
            requestParams.put("section", sectionString);
            clientReg.post(Constants.BASE_SERVER + "cw_hw_app/", requestParams, new JsonHttpResponseHandler() {
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
                            dateTextView.setText("");
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
//                            startActivity(new Intent(TeacherClassWorkActivity.this, TeacherClassWorkActivity.class));
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
    // Edit Class Work by Teacher.
    //--------------------------------
    public void editClassWork_old(String classWorkId, String subjectString, String topicString, String classString, String sectionString, String dateString) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", classWorkId);
            requestParams.put("subject", subjectString);
            requestParams.put("topic", topicString);
            requestParams.put("class_name", classString);
            requestParams.put("date_of_class", dateString);
            requestParams.put("section", sectionString);
            clientReg.post(Constants.BASE_SERVER + "edit_classwork/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            editSubjectSpinner.setSelection(0);
                            editTopicEditText.setText("");
                            editClassNameSpinner.setSelection(0);
                            editSectionSpinner.setSelection(0);
                            editDateButton.setText("");
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
//                            startActivity(new Intent(TeacherClassWorkActivity.this, TeacherClassWorkActivity.class));
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
    // Fetch all classwork details by
    // the help of teacherId.
    //--------------------------------
    public void fetchClassWorkDetails_old(String teacherId) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", teacherId);
            clientReg.post(Constants.BASE_SERVER + "show_classwork_details/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            teacherClassWorkInfoArrayList.clear();
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String classWorkId = jsonObject.getString("id");
                                String classWorkSubject = jsonObject.getString("subject");
                                String classWorkTopic = jsonObject.getString("topic");
                                String className = jsonObject.getString("class_name");
                                String section = jsonObject.getString("section");
                                String dateOfClass = jsonObject.getString("date_of_class");

                                TeacherClassWorkInfo teacherClassWorkInfo = new TeacherClassWorkInfo(classWorkId, classWorkSubject, classWorkTopic,
                                        className, section, dateOfClass);
                                teacherClassWorkInfoArrayList.add(i, teacherClassWorkInfo);
                            }

                            // show dialog box.
                            editDialogBox(teacherClassWorkInfoArrayList);
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

    //---------------------------------------------
    // Alert dialog box for displaying topic name.
    // @param teacherClassWorkInfoArrayList
    //---------------------------------------------
    public void editDialogBox(final ArrayList<TeacherClassWorkInfo> teacherClassWorkInfoArrayList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TeacherClassWorkActivity.this);
        builderSingle.setTitle("Select topic name");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TeacherClassWorkActivity.this, android.R.layout.select_dialog_singlechoice);

        // For displaying available topic name onto alert dialog box.
        for (TeacherClassWorkInfo teacherClassWorkInfo : teacherClassWorkInfoArrayList) {
            arrayAdapter.add(teacherClassWorkInfo.classWorkTopic);
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
                AlertDialog.Builder builderInner = new AlertDialog.Builder(TeacherClassWorkActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your selected classwork is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (classArrayList != null && subjectArrayList != null) {
                            dialog.dismiss();

                            final Dialog finalDialogBox = new Dialog(TeacherClassWorkActivity.this);
                            finalDialogBox.setContentView(R.layout.teacher_custom);

                            /*
                              Store topic name into classWorkInfoArrayList for dialog box.
                             */
                            ArrayList<TeacherClassWorkInfo> classWorkInfoArrayList = new ArrayList<>();
                            for (TeacherClassWorkInfo teacherClassWorkInfo : teacherClassWorkInfoArrayList) {
                                if (teacherClassWorkInfo.classWorkTopic.equalsIgnoreCase(strName)) {
                                    classWorkInfoArrayList.add(teacherClassWorkInfo);
                                }
                            }

                            // set the custom dialog components - text, image and button
                            editSubjectSpinner = finalDialogBox.findViewById(R.id.subjectSpinner);
                            editTopicEditText = finalDialogBox.findViewById(R.id.topicEditText);
                            editClassNameSpinner = finalDialogBox.findViewById(R.id.classNameSpinner);
                            editSectionSpinner = finalDialogBox.findViewById(R.id.sectionSpinner);
                            editDateButton = finalDialogBox.findViewById(R.id.dateButton);
                            Button reassignButton = finalDialogBox.findViewById(R.id.reassignButton);

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
                            ArrayAdapter<String> dataAdapterSection = new ArrayAdapter<>(TeacherClassWorkActivity.this, android.R.layout.simple_spinner_item, categoriesSection);
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
                                    //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
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
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(TeacherClassWorkActivity.this, android.R.layout.simple_spinner_item, categories);
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
                            ArrayAdapter<String> dataAdapterClass = new ArrayAdapter<>(TeacherClassWorkActivity.this, android.R.layout.simple_spinner_item, categoriesSubject);
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

                            dateButton.setOnClickListener(new View.OnClickListener() {
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
                                                    dateButton.setText(sdf.format(myCalendar.getTime()));
                                                }
                                            };
                                    dateButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new DatePickerDialog(TeacherClassWorkActivity.this, date, myCalendar
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
                            for (TeacherClassWorkInfo teacherClassWorkInfo : classWorkInfoArrayList) {
                                int selectionPositionSubject = dataAdapterClass.getPosition(teacherClassWorkInfo.classWorkSubject);
                                editSubjectSpinner.setSelection(selectionPositionSubject);

                                int selectionPositionClass = dataAdapter.getPosition(teacherClassWorkInfo.className);
                                editClassNameSpinner.setSelection(selectionPositionClass);

                                int selectionPositionSection = dataAdapterSection.getPosition(teacherClassWorkInfo.section);
                                editSectionSpinner.setSelection(selectionPositionSection);

                                editTopicEditText.setText(teacherClassWorkInfo.classWorkTopic);
                                editDateButton.setText(teacherClassWorkInfo.dateOfClass);
                                classWorkId = teacherClassWorkInfo.classWorkId;
                            }

                            reassignButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalDialogBox.dismiss();
                                    if (InternetCheckActivity.isConnected()) {
                                        String editSubjectString = editSubjectSpinner.getSelectedItem().toString().trim();
                                        String editClassString = editClassNameSpinner.getSelectedItem().toString().trim();
                                        String editSectionString = editSectionSpinner.getSelectedItem().toString().trim();
                                        String topicString = editTopicEditText.getText().toString().trim();
                                        String dateString = editDateButton.getText().toString().trim();
                                        if (!editSubjectString.equalsIgnoreCase("") && !editSubjectString.equalsIgnoreCase("Select Subject") && !topicString.equalsIgnoreCase("") && !editClassString.equalsIgnoreCase("") && !editClassString.equalsIgnoreCase("Select Class") && !editSectionString.equalsIgnoreCase("") && !editSectionString.equalsIgnoreCase("Select Section") && !dateString.equalsIgnoreCase("") && classWorkId != null && !classWorkId.equalsIgnoreCase("")) {
                                            editClassWork(classWorkId, editSubjectString, topicString, editClassString, editSectionString, dateString);
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
                            startActivity(new Intent(TeacherClassWorkActivity.this, SplashActivity.class));
                        }
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    //------------------------
    // Set date onto EditText.
    //------------------------
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTextView.setText(sdf.format(myCalendar.getTime()));
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(TeacherClassWorkActivity.this, MenuActivity.class));
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void insertClassWork(final String subjectString, final String classString, final String topicString, final String dateString, final String sectionString) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "cw_hw_app/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        subjectSpinner.setSelection(0);
                        classSpinner.setSelection(0);
                        sectionSpinner.setSelection(0);
                        topicEditText.setText("");
                        dateTextView.setText("");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
//                            startActivity(new Intent(TeacherClassWorkActivity.this, TeacherClassWorkActivity.class));
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
                params.put("date_of_class", dateString);
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

    public void editClassWork(final String classWorkId, final String subjectString, final String topicString, final String classString, final String sectionString, final String dateString) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "edit_classwork/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        editSubjectSpinner.setSelection(0);
                        editTopicEditText.setText("");
                        editClassNameSpinner.setSelection(0);
                        editSectionSpinner.setSelection(0);
                        editDateButton.setText("");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
//                            startActivity(new Intent(TeacherClassWorkActivity.this, TeacherClassWorkActivity.class));
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
                params.put("id", classWorkId);
                params.put("subject", subjectString);
                params.put("topic", topicString);
                params.put("class_name", classString);
                params.put("date_of_class", dateString);
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

    public void fetchClassWorkDetails(final String teacherId) {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "show_classwork_details/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        teacherClassWorkInfoArrayList.clear();
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String classWorkId = jsonObject.getString("id");
                            String classWorkSubject = jsonObject.getString("subject");
                            String classWorkTopic = jsonObject.getString("topic");
                            String className = jsonObject.getString("class_name");
                            String section = jsonObject.getString("section");
                            String dateOfClass = jsonObject.getString("date_of_class");

                            TeacherClassWorkInfo teacherClassWorkInfo = new TeacherClassWorkInfo(classWorkId, classWorkSubject, classWorkTopic,
                                    className, section, dateOfClass);
                            teacherClassWorkInfoArrayList.add(i, teacherClassWorkInfo);
                        }

                        // show dialog box.
                        editDialogBox(teacherClassWorkInfoArrayList);
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

    public void fetchClassDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_all_class/", new Response.Listener<String>() {
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
