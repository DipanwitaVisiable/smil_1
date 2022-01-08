package com.example.crypedu.Fragment;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Adapter.ExamAdapter;
import com.example.crypedu.Adapter.ExamSyllabusAdapter;
import com.example.crypedu.Adapter.ExaminationAdapter;
import com.example.crypedu.Adapter.SubjectAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.ExamInfo;
import com.example.crypedu.Pojo.ExamSyllabusInfo;
import com.example.crypedu.Pojo.ExaminationInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ExaminationFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private View myFragmentView;
    private ArrayList<String> subjectArrayList;
    private ListView listView_class;
    private ArrayList<ExamSyllabusInfo> examSubjectArrayList;
    private ArrayList<ExamInfo> examListArrayList;
    private ArrayList<ExaminationInfo> examList;
    private LinearLayout titleLinearLayout;

    public ExaminationFragment() {
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
        myFragmentView = inflater.inflate(R.layout.fragment_examination, container, false);
        titleLinearLayout = myFragmentView.findViewById(R.id.titleLinearLayout);
        titleLinearLayout.setVisibility(View.GONE);
        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);

        Typeface typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), Constants.BubblegumSans_Regular_font);
        TextView copyRight_textView = myFragmentView.findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        /*
          If internet connection is working
          then only call fetchNoticeDetails().
         */
        if (InternetCheckActivity.isConnected()) {
            fetchSyllabusExamList();
        }else {
            showSnack();
        }



        return myFragmentView;
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

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    //--------------------------------
    // Fetch Exam list from server
    // with the 'student id' param.
    //--------------------------------
    public void fetchSyllabusExamList_old() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "exams", requestParams, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            examListArrayList = new ArrayList<>();
                            listView_class = myFragmentView.findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String class_name = jsonObject.getString("exam_name");

                                ExamInfo examInfo = new ExamInfo(id, class_name);
                                examListArrayList.add(i, examInfo);
                            }
                            listView_class.setVisibility(View.VISIBLE);
                            listView_class.setAdapter(new ExamAdapter(Objects.requireNonNull(getActivity()).getApplication(), examListArrayList, getActivity().getLayoutInflater()));
                            listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String s = examListArrayList.get(position).id;
                                    fetchExamDetails(s);
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

    //--------------------------------
    // Fetch Exam details
    // with the 'student id' and 'exam_id' param.
    //--------------------------------
    public void fetchExamDetails_old(String examId) {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Constants.USER_ID);
            requestParams.put("exam_id", examId);
            clientReg.post(Constants.BASE_SERVER + "exam_subjects/", requestParams, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            examList = new ArrayList<>();
                            listView_class = myFragmentView.findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String subject = jsonObject.getString("subject");
                                String exam_name = jsonObject.getString("exam_name");
                                String startTime = jsonObject.getString("start_time");
                                String endTime = jsonObject.getString("end_time");
                                String examDate = jsonObject.getString("exam_date");
                                String time = startTime + " " + endTime;

                                ExaminationInfo examinationInfo = new ExaminationInfo(subject, exam_name, time, examDate);
                                examList.add(i, examinationInfo);
                            }
                            titleLinearLayout.setVisibility(View.VISIBLE);
                            listView_class.setVisibility(View.VISIBLE);
                            listView_class.setAdapter(new ExaminationAdapter(Objects.requireNonNull(getActivity()).getApplication(), examList, getActivity().getLayoutInflater()));
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

    //----------------------------------
    // Fetch subject name from server
    // respective of exam id.
    //----------------------------------
    private void fetchSyllabusExamSubject(String id) {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("exam_id", id);
            clientReg.post(Constants.BASE_SERVER + "exam_subjects", requestParams, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            subjectArrayList = new ArrayList<>();
                            listView_class = myFragmentView.findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String subject = jsonObject.getString("subject");
                                subjectArrayList.add(subject);
                            }
                            listView_class.setAdapter(new SubjectAdapter(Objects.requireNonNull(getActivity()).getApplication(), subjectArrayList, getActivity().getLayoutInflater()));
                            listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String s = subjectArrayList.get(position);
                                    fetchSyllabusExamDetails(s);
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

    //-----------------------------------
    // Fetch all subject details respective
    // of subject name and student Id.
    //-----------------------------------
    private void fetchSyllabusExamDetails_old(String subjectName) {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("subject", subjectName);
            requestParams.put("student_id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "subject_syllabus_details", requestParams, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            examSubjectArrayList = new ArrayList<>();
                            listView_class = myFragmentView.findViewById(R.id.listView_class);
                            listView_class.setVisibility(View.VISIBLE);
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String chapter = jsonObject.getString("chapter");
                                String no_of_class = jsonObject.getString("no_of_class");

                                ExamSyllabusInfo examSyllabusInfo = new ExamSyllabusInfo(chapter, no_of_class);
                                examSubjectArrayList.add(examSyllabusInfo);

                            }
                            listView_class.setAdapter(new ExamSyllabusAdapter(Objects.requireNonNull(getActivity()).getApplication(), examSubjectArrayList, getActivity().getLayoutInflater()));
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


    public void fetchSyllabusExamList() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exams", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        examListArrayList = new ArrayList<>();
                        listView_class = myFragmentView.findViewById(R.id.listView_class);
                        listView_class.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String class_name = jsonObject.getString("exam_name");

                            ExamInfo examInfo = new ExamInfo(id, class_name);
                            examListArrayList.add(i, examInfo);
                        }
                        listView_class.setVisibility(View.VISIBLE);
                        listView_class.setAdapter(new ExamAdapter(Objects.requireNonNull(getActivity()).getApplication(), examListArrayList, getActivity().getLayoutInflater()));
                        listView_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String s = examListArrayList.get(position).id;
                                fetchExamDetails(s);
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }

    public void fetchExamDetails(final String examId) {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "exam_subjects/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        examList = new ArrayList<>();
                        listView_class = myFragmentView.findViewById(R.id.listView_class);
                        listView_class.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String subject = jsonObject.getString("subject");
                            String exam_name = jsonObject.getString("exam_name");
                            String startTime = jsonObject.getString("start_time");
                            String endTime = jsonObject.getString("end_time");
                            String examDate = jsonObject.getString("exam_date");
                            String time = startTime + " " + endTime;

                            ExaminationInfo examinationInfo = new ExaminationInfo(subject, exam_name, time, examDate);
                            examList.add(i, examinationInfo);
                        }
                        titleLinearLayout.setVisibility(View.VISIBLE);
                        listView_class.setVisibility(View.VISIBLE);
                        listView_class.setAdapter(new ExaminationAdapter(Objects.requireNonNull(getActivity()).getApplication(), examList, getActivity().getLayoutInflater()));
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
                params.put("exam_id", examId);
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

    public void fetchSyllabusExamDetails(final String subjectName) {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "subject_syllabus_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        examSubjectArrayList = new ArrayList<>();
                        listView_class = myFragmentView.findViewById(R.id.listView_class);
                        listView_class.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String chapter = jsonObject.getString("chapter");
                            String no_of_class = jsonObject.getString("no_of_class");

                            ExamSyllabusInfo examSyllabusInfo = new ExamSyllabusInfo(chapter, no_of_class);
                            examSubjectArrayList.add(examSyllabusInfo);

                        }
                        listView_class.setAdapter(new ExamSyllabusAdapter(Objects.requireNonNull(getActivity()).getApplication(), examSubjectArrayList, getActivity().getLayoutInflater()));
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
                params.put("subject", subjectName);
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }


}

