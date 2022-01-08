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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.crypedu.Constants.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SyllabusDirectFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private PDFView pdfView;
    private ProgressBar pdfProgress;
    private RequestQueue requestQueue;
    private Context context;
    private Spinner classSpinner;
    private String classString = null;
    private ArrayList<String> classList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public SyllabusDirectFragment() {
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
        View myFragmentView = inflater.inflate(R.layout.fragment_syllabus_direct, container, false);
        context = getActivity();
        assert context != null;
        requestQueue = Volley.newRequestQueue(context);

        classSpinner = myFragmentView.findViewById(R.id.classSpinner);
        FloatingActionButton submitButton = myFragmentView.findViewById(R.id.class_search);
        pdfProgress = myFragmentView.findViewById(R.id.pdfProgress);
        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);
        if (InternetCheckActivity.isConnected()) {
            getAllClass();
        } else {
            showSnack();
        }
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classString == null) {
                    Toast.makeText(getContext(), "Required information", Toast.LENGTH_LONG).show();
                } else {
                    if (InternetCheckActivity.isConnected()) {
                        pdfProgress.setVisibility(View.VISIBLE);
                        fetchingPDF(classString);
                    }else {
                        showSnack();
                    }
                }
            }
        });

        pdfView = myFragmentView.findViewById(R.id.pdfView);
        pdfView.setVerticalScrollBarEnabled(true);
        pdfView.setSwipeVertical(true);
        pdfView.setFitsSystemWindows(true);

        return myFragmentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAllClass() {
        final StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_class_all", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("class_details");
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
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        queue.add(request);
    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = context.getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }


    //--------------------------------
    // Fetch Subject details from server
    // with the 'class_name' param.
    //--------------------------------
//    public void fetchSyllabusPlanning(){
//        /*
//         *  Progress Bar
//         */
//        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
//        pbHeaderProgress.setVisibility(View.VISIBLE);
//        try {
//            AsyncHttpClient clientReg = new AsyncHttpClient();
//            RequestParams requestParams = new RequestParams();
//            requestParams.put("id",Constants.USER_ID);
//            clientReg.post("http://"+ Constants.server_name +"webservices/websvc/show_syllabus_planning", requestParams, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
//                    try {
//                        pbHeaderProgress.setVisibility(View.GONE);
//                        String status = obj.getString("status");
//                        String message = obj.getString("message");
//                        if (status.equalsIgnoreCase("200")) {
//                            syllabusPlanningArrayList = new ArrayList<SyllabusPlanningInfo>();
//                            listView_class = myFragmentView.findViewById(R.id.listView_class);
//                            listView_class.setVisibility(View.VISIBLE);
//                            JSONArray jsonArrayList = obj.getJSONArray("syllabus_planning");
//                            for (int i = 0; i < jsonArrayList.length(); i++) {
//                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
//                                String id = jsonObject.getString("id");
//                                String class_name = jsonObject.getString("class_name");
//                                String teachers_name = jsonObject.getString("teachers_name");
//                                String subject = jsonObject.getString("subject");
//                                String chapter = jsonObject.getString("chapter");
//                                String no_of_class = jsonObject.getString("exam_name");
//
//                                SyllabusPlanningInfo syllabusPlanningInfo = new SyllabusPlanningInfo(id, class_name, teachers_name, subject, chapter, no_of_class);
//                                syllabusPlanningArrayList.add(i, syllabusPlanningInfo);
//                            }
//                            listView_class.setAdapter(new SyllabusPlanningAdapter(getActivity().getApplication(), syllabusPlanningArrayList, getActivity().getLayoutInflater()));
//                            titleLinearLayout.setVisibility(View.GONE);
//                        } else {
//                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
//                            // Changing action button text color
//                            View sbView = snackbar.getView();
//                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
//                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
//                            snackbar.show();
//                            messageTextView.setText(message);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // When the response returned by REST has Http response code other than '200'
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    pbHeaderProgress.setVisibility(View.GONE);
//                }
//            });
//        } catch (Exception e) {
//            pbHeaderProgress.setVisibility(View.GONE);
//            e.printStackTrace();
//        }
//    }

    //--------------------------------
    // fetchStudentProfile from server
    // with the 'studentID' param.
    //--------------------------------
    public void fetchingPDF(final String classSt) {
        /*
         *  Progress Bar
         */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "syllabus_pdf_new_dir", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    Log.e("STATUS", status);
                    Log.e("MESSAGE", message);
                    if (status.equalsIgnoreCase("200")) {
                        String pdfURL = obj.getString("new_pdf_syllabus");
                        new RetrievePdfStream().execute(pdfURL);//or any URL direct pdf from Internet
                    } else {
                        pdfProgress.setVisibility(View.GONE);
                        pdfView.setVisibility(View.GONE);
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
                params.put("class", classSt);
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
        requestQueue.add(stringRequest);
    }

    @SuppressLint("StaticFieldLeak")
    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfProgress.setVisibility(View.GONE);
            pdfView.fromStream(inputStream).onPageScroll(new OnPageScrollListener() {
                @Override
                public void onPageScrolled(int page, float positionOffset) {
                }
            }).load();
        }
    }
}
