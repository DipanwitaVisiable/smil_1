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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Constants.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.key.Key;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class SyllabusFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private PDFView pdfView;
    private ProgressBar pdfProgress;
    private Context context;
    String student_id;


    public SyllabusFragment() {
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
        View myFragmentView = inflater.inflate(R.layout.fragment_syllabus, container, false);
        context = getActivity();
        assert context != null;

        pdfView = myFragmentView.findViewById(R.id.pdfView);
        pdfProgress = myFragmentView.findViewById(R.id.pdfProgress);
        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);
        pdfView.setVerticalScrollBarEnabled(true);
        pdfView.setSwipeVertical(true);
        pdfView.setFitsSystemWindows(true);

        //Toast.makeText(getContext(), ""+getActivity().getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE).getString("student_id", null), Toast.LENGTH_SHORT).show();

        student_id=getActivity().getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE).getString("student_id", null);
        /*
          If internet connection is working
          then only call fetchNoticeDetails().
         */
        if (InternetCheckActivity.isConnected() ){
//           Not using from 18th april,2018
            //fetchSyllabusPlanning();
            pdfProgress.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.INVISIBLE);
            fetchingPDF();

        } else {
            showSnack();
        }


        return myFragmentView;
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

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }


    // fetchStudentProfile from server
    // with the 'studentID' param.
    //--------------------------------
    public void fetchingPDF_old() {
        try {
            final RequestParams params = new RequestParams();
            params.put("student_id", Integer.valueOf(student_id));
            Log.d("PdfValue", "getPdf: "+params);
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.post(Constants.BASE_SERVER + "syllabus_pdf_find/", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pdfProgress.setVisibility(View.VISIBLE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            Log.d("PdfResponse",obj.getString("pdf_syllabus") );
                            //Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                            new RetrievePdfStream().execute(obj.getString("pdf_syllabus"));

                        } else {
                            pdfProgress.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pdfProgress.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            pdfProgress.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
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
                    pdfProgress.setVisibility(View.GONE);
                    pdfView.setVisibility(View.VISIBLE);
                }
            }).load();
        }
    }

    public void fetchingPDF() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "syllabus_pdf_find/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pdfProgress.setVisibility(View.VISIBLE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        Log.d("PdfResponse",obj.getString("pdf_syllabus") );
                        //Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();
                        new RetrievePdfStream().execute(obj.getString("pdf_syllabus"));

                    } else {
                        pdfProgress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), ""+message, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pdfProgress.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdfProgress.setVisibility(View.VISIBLE);
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
}
