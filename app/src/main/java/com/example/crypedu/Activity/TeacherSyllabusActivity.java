package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.TeacherSyllabusInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TeacherSyllabusActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private Spinner subjectSpinner;
    private EditText chapterEditText;
    private Spinner classSpinner;
    private Spinner sectionSpinner;
    private ArrayList<TeacherSyllabusInfo> teacherSyllabusInfoArrayList = new ArrayList<>();
    private ArrayList<String> classArrayList;
    private ArrayList<String> subjectArrayList;
    private String classWorkId;
    private Spinner noOfDaysSpinner;
    private Spinner editSubjectSpinner;
    private EditText editTopicEditText;
    private Spinner editClassNameSpinner;
    private Spinner editSectionSpinner;
    private Spinner editNoOfDaysSpinner;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_syllabus);
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
                Intent intent = new Intent(TeacherSyllabusActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        if (Constants.USER_ID == null && Constants.USER_ID.equalsIgnoreCase("")){
            startActivity(new Intent(TeacherSyllabusActivity.this, SplashActivity.class));
        }

        FloatingActionButton editClassWorkFAButton = findViewById(R.id.editClassWorkFAButton);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        chapterEditText = findViewById(R.id.chapterEditText);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        noOfDaysSpinner = findViewById(R.id.noOfDaysSpinner);
        Button submitButton = findViewById(R.id.submitButton);

        /*
          Fetch all class and subject name from server.
         */
        if (InternetCheckActivity.isConnected()){
            fetchClassDetails();
            fetchSubjectDetails();
        }else {
            showSnack();
        }

        /*
          For edit class work.
         */
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

        // Spinner Drop down elements for section
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

        // Spinner Drop down elements for section
        List<String> categoriesNoOfDays = new ArrayList<>();
        categoriesNoOfDays.add("Select No of days");
        for (int i = 1; i <= 50; i++){
            categoriesNoOfDays.add(""+i);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> noOfDaysAdapterSection = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNoOfDays);

        // Drop down layout style - list view with radio button
        noOfDaysAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        noOfDaysSpinner.setAdapter(noOfDaysAdapterSection);

        // Spinner click listener
        noOfDaysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                if (subjectArrayList != null && classArrayList != null){
                    if (subjectArrayList.size() > 0 && classArrayList.size() > 0){
                        String subjectString = subjectSpinner.getSelectedItem().toString().trim();
                        String classString = classSpinner.getSelectedItem().toString().trim();
                        String noOfDaysString = noOfDaysSpinner.getSelectedItem().toString().trim();
                        String sectionString = sectionSpinner.getSelectedItem().toString().trim();
                        String chapterString = chapterEditText.getText().toString().trim();
                        if (!subjectString.equalsIgnoreCase("") && !subjectString.equalsIgnoreCase("Select Subject") && !classString.equalsIgnoreCase("") && !classString.equalsIgnoreCase("Select Class") && !chapterString.equalsIgnoreCase("") && !noOfDaysString.equalsIgnoreCase("") && !noOfDaysString.equalsIgnoreCase("Select No of days") && !sectionString.equalsIgnoreCase("") && !sectionString.equalsIgnoreCase("Select Section")){
                            if (InternetCheckActivity.isConnected()){
                                insertClassWork(subjectString,classString, chapterString, noOfDaysString, sectionString);
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
            Intent intent = new Intent(TeacherSyllabusActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //--------------------------------
    // Insert Class Work by Teacher.
    //--------------------------------
    public void insertClassWork_old(String subjectString, String classString, String chapterString, String noOfDaysString, String sectionString){
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id",Constants.USER_ID);
            requestParams.put("subject",subjectString);
            requestParams.put("chapter",chapterString);
            requestParams.put("class_name",classString);
            requestParams.put("class_no",noOfDaysString);
            requestParams.put("section",sectionString);
            clientReg.post("http://"+ Constants.server_name +"webservices/websvc/syllabus_planner_app/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            subjectSpinner.setSelection(0);
                            classSpinner.setSelection(0);
                            noOfDaysSpinner.setSelection(0);
                            sectionSpinner.setSelection(0);
                            chapterEditText.setText("");
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
//                            startActivity(new Intent(TeacherSyllabusActivity.this, TeacherSyllabusActivity.class));
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
    public void editClassWork_old(String subjectString, String classString, String chapterString, String noOfDaysString, String sectionString){
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id",classWorkId);
            requestParams.put("subject",subjectString);
            requestParams.put("chapter",chapterString);
            requestParams.put("class_name",classString);
            requestParams.put("class_no",noOfDaysString);
            requestParams.put("section",sectionString);
            clientReg.post("http://"+ Constants.server_name +"webservices/websvc/edit_syllabus/", requestParams, new JsonHttpResponseHandler() {
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
                            editNoOfDaysSpinner.setSelection(0);
                            editTopicEditText.setText("");
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
//                            startActivity(new Intent(TeacherSyllabusActivity.this, TeacherSyllabusActivity.class));
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
    public void fetchClassWorkDetails_old(String teacherId){
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", teacherId);
            clientReg.post("http://"+ Constants.server_name +"webservices/websvc/show_syllabus_details/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            teacherSyllabusInfoArrayList.clear();
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String syllabusId = jsonObject.getString("id");
                                String syllabusSubject = jsonObject.getString("subject");
                                String syllabusChapter = jsonObject.getString("chapter");
                                String syllabusClassName = jsonObject.getString("class_name");
                                String syllabusSection = jsonObject.getString("section");
                                String syllabusNoOfClass = jsonObject.getString("no_of_class");

                                TeacherSyllabusInfo teacherSyllabusInfo = new TeacherSyllabusInfo(syllabusId, syllabusSubject, syllabusChapter,
                                        syllabusClassName, syllabusSection, syllabusNoOfClass);
                                teacherSyllabusInfoArrayList.add(i, teacherSyllabusInfo);
                            }

                            // show dialog box.
                            editDialogBox(teacherSyllabusInfoArrayList);
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
    public void editDialogBox(final ArrayList<TeacherSyllabusInfo> teacherSyllabusInfoArrayList){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TeacherSyllabusActivity.this);
        builderSingle.setTitle("Select chapter");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TeacherSyllabusActivity.this, android.R.layout.select_dialog_singlechoice);

        // For displaying available chapter name onto alert dialog box.
        for (TeacherSyllabusInfo syllabusInfo : teacherSyllabusInfoArrayList){
            arrayAdapter.add(syllabusInfo.syllabusChapter);
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
                AlertDialog.Builder builderInner = new AlertDialog.Builder(TeacherSyllabusActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your selected chapter is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (classArrayList != null && subjectArrayList != null){
                            dialog.dismiss();

                            final Dialog finalDialogBox = new Dialog(TeacherSyllabusActivity.this);
                            finalDialogBox.setContentView(R.layout.teacher_custom_syllabus);
//                            finalDialogBox.setTitle("Title...");

                            /*
                              Store topic name into classWorkInfoArrayList for dialog box.
                             */
                            ArrayList<TeacherSyllabusInfo> teacherSyllabusInfos = new ArrayList<>();
                            for (TeacherSyllabusInfo teacherSyllabusInfo : teacherSyllabusInfoArrayList){
                                if (teacherSyllabusInfo.syllabusChapter.equalsIgnoreCase(strName)){
                                    teacherSyllabusInfos.add(teacherSyllabusInfo);
                                }
                            }

                            // set the custom dialog components - text, image and button
                            editSubjectSpinner = finalDialogBox.findViewById(R.id.subjectSpinner);
                            editTopicEditText = finalDialogBox.findViewById(R.id.topicEditText);
                            editClassNameSpinner = finalDialogBox.findViewById(R.id.classNameSpinner);
                            editSectionSpinner = finalDialogBox.findViewById(R.id.sectionSpinner);
                            editNoOfDaysSpinner = finalDialogBox.findViewById(R.id.noOfDaysSpinner);
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
                            ArrayAdapter<String> dataAdapterSection = new ArrayAdapter<>(TeacherSyllabusActivity.this, android.R.layout.simple_spinner_item, categoriesSection);
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
                            List<String> categoriesNoOfDays = new ArrayList<>();
                            categoriesNoOfDays.add("Select no of days");
                            for (int i = 1; i <= 50; i++){
                                categoriesNoOfDays.add(""+i);
                            }

                            // Creating adapter for spinner
                            ArrayAdapter<String> noOfDaysAdapterSection = new ArrayAdapter<>(TeacherSyllabusActivity.this, android.R.layout.simple_spinner_item, categoriesNoOfDays);
                            // Drop down layout style - list view with radio button
                            noOfDaysAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // attaching data adapter to spinner
                            editNoOfDaysSpinner.setAdapter(noOfDaysAdapterSection);
                            // Spinner click listener
                            editNoOfDaysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(TeacherSyllabusActivity.this, android.R.layout.simple_spinner_item, categories);
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

                            // Spinner Drop down elements
                            List<String> categoriesSubject = new ArrayList<>();
                            categoriesSubject.add("Select Subject");
                            categoriesSubject.addAll(subjectArrayList);
                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapterClass = new ArrayAdapter<>(TeacherSyllabusActivity.this, android.R.layout.simple_spinner_item, categoriesSubject);
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
                              Set all edit field value which has
                              already present in database that means before reassign.
                             */
                            for (TeacherSyllabusInfo teacherSyllabusInfo : teacherSyllabusInfos){
                                int selectionPositionSubject = dataAdapterClass.getPosition(teacherSyllabusInfo.syllabusSubject);
                                editSubjectSpinner.setSelection(selectionPositionSubject);

                                int selectionPositionClass = dataAdapter.getPosition(teacherSyllabusInfo.syllabusClassName);
                                editClassNameSpinner.setSelection(selectionPositionClass);

                                int selectionPositionSection = dataAdapterSection.getPosition(teacherSyllabusInfo.syllabusSection);
                                editSectionSpinner.setSelection(selectionPositionSection);

                                int selectionPositionNoOfDays = noOfDaysAdapterSection.getPosition(teacherSyllabusInfo.syllabusNoOfClass);
                                editNoOfDaysSpinner.setSelection(selectionPositionNoOfDays);

                                editTopicEditText.setText(teacherSyllabusInfo.syllabusChapter);
                                classWorkId = teacherSyllabusInfo.syllabusId;
                            }

                            /*
                              If 'Reassign' button is clicked, close the custom dialog.
                             */
                            reassignButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalDialogBox.dismiss();
                                    if (InternetCheckActivity.isConnected()){
                                        String editSubjectString = editSubjectSpinner.getSelectedItem().toString().trim();
                                        String editClassString = editClassNameSpinner.getSelectedItem().toString().trim();
                                        String editSectionString = editSectionSpinner.getSelectedItem().toString().trim();
                                        String editNoOfDaysString = editNoOfDaysSpinner.getSelectedItem().toString().trim();
                                        String topicString = editTopicEditText.getText().toString().trim();
                                        if (!editSubjectString.equalsIgnoreCase("") && !editSubjectString.equalsIgnoreCase("Select Subject") && !topicString.equalsIgnoreCase("") && !editClassString.equalsIgnoreCase("") && !editClassString.equalsIgnoreCase("Select Class") && !editSectionString.equalsIgnoreCase("") && !editSectionString.equalsIgnoreCase("Select Section") && !editNoOfDaysString.equalsIgnoreCase("") && !editNoOfDaysString.equalsIgnoreCase("Select no of days") && classWorkId != null && !classWorkId.equalsIgnoreCase("")){
                                            editClassWork(editSubjectString, editClassString, topicString, editNoOfDaysString, editSectionString);
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
                            startActivity(new Intent(TeacherSyllabusActivity.this, SplashActivity.class));
                        }
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(TeacherSyllabusActivity.this, MenuActivity.class));
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
            clientReg.get("http://"+ Constants.server_name +"webservices/websvc/get_all_class/", new JsonHttpResponseHandler() {
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
            clientReg.get("http://"+ Constants.server_name +"webservices/websvc/svcsubjects/",params, new JsonHttpResponseHandler() {
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
    private void subjectSpinner(){
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

    public void insertClassWork(final String subjectString, final String classString, final String chapterString, final String noOfDaysString, final String sectionString){
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"webservices/websvc/syllabus_planner_app/", new Response.Listener<String>() {
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
                        noOfDaysSpinner.setSelection(0);
                        sectionSpinner.setSelection(0);
                        chapterEditText.setText("");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
//                            startActivity(new Intent(TeacherSyllabusActivity.this, TeacherSyllabusActivity.class));
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
                params.put("id",Constants.USER_ID);
                params.put("subject",subjectString);
                params.put("chapter",chapterString);
                params.put("class_name",classString);
                params.put("class_no",noOfDaysString);
                params.put("section",sectionString);
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

    public void editClassWork(final String subjectString, final String classString, final String chapterString, final String noOfDaysString, final String sectionString){
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"webservices/websvc/edit_syllabus/", new Response.Listener<String>() {
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
                        editClassNameSpinner.setSelection(0);
                        editSectionSpinner.setSelection(0);
                        editNoOfDaysSpinner.setSelection(0);
                        editTopicEditText.setText("");
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
//                            startActivity(new Intent(TeacherSyllabusActivity.this, TeacherSyllabusActivity.class));
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
                params.put("id",classWorkId);
                params.put("subject",subjectString);
                params.put("chapter",chapterString);
                params.put("class_name",classString);
                params.put("class_no",noOfDaysString);
                params.put("section",sectionString);
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

    public void fetchClassWorkDetails(final String teacherId){
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"webservices/websvc/show_syllabus_details/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        teacherSyllabusInfoArrayList.clear();
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String syllabusId = jsonObject.getString("id");
                            String syllabusSubject = jsonObject.getString("subject");
                            String syllabusChapter = jsonObject.getString("chapter");
                            String syllabusClassName = jsonObject.getString("class_name");
                            String syllabusSection = jsonObject.getString("section");
                            String syllabusNoOfClass = jsonObject.getString("no_of_class");

                            TeacherSyllabusInfo teacherSyllabusInfo = new TeacherSyllabusInfo(syllabusId, syllabusSubject, syllabusChapter,
                                    syllabusClassName, syllabusSection, syllabusNoOfClass);
                            teacherSyllabusInfoArrayList.add(i, teacherSyllabusInfo);
                        }

                        // show dialog box.
                        editDialogBox(teacherSyllabusInfoArrayList);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"webservices/websvc/get_all_class/", new Response.Listener<String>() {
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
        requestQueue.add(stringRequest);
    }

    public void fetchSubjectDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ Constants.server_name +"webservices/websvc/svcsubjects/", new Response.Listener<String>() {
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
        requestQueue.add(stringRequest);
    }
}
