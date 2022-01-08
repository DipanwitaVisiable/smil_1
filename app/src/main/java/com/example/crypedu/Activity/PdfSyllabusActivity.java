package com.example.crypedu.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
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

public class PdfSyllabusActivity extends AppCompatActivity {
    private PDFView pdfView;
    private ProgressBar pdfProgress;
    private CoordinatorLayout coordinatorLayout;
    String student_id;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_syllabus);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/


        Toolbar toolbar = findViewById(R.id.toolbar);
        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        requestQueue = Volley.newRequestQueue(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PdfSyllabusActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        pdfView = findViewById(R.id.pdfView);
        pdfProgress = findViewById(R.id.pdfProgress);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        pdfView.setVerticalScrollBarEnabled(true);
        pdfView.setSwipeVertical(true);
        pdfView.setFitsSystemWindows(true);

       // Toast.makeText(this, ""+getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE).getString("student_id", null), Toast.LENGTH_SHORT).show();
        student_id=getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE).getString("student_id", null);
        if (InternetCheckActivity.isConnected()) {
            pdfProgress.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.INVISIBLE);
            fetchingPDF();
        } else {
            showSnack();
        }

    }


    //--------------------------------
    // fetchStudentProfile from server
    // with the 'studentID' param.
    //--------------------------------
    public void fetchingPDF_old() {
         /*
         *  Progress Bar
         */
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
                            Toast.makeText(PdfSyllabusActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            new ClassRetrievePdfStream().execute(obj.getString("pdf_syllabus"));

                        } else {
                            pdfProgress.setVisibility(View.GONE);
                            Toast.makeText(PdfSyllabusActivity.this, ""+message, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pdfProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            pdfProgress.setVisibility(View.GONE);
            e.printStackTrace();
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


    @SuppressLint("StaticFieldLeak")
    class ClassRetrievePdfStream extends AsyncTask<String, Void, InputStream> {

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
            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
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
                        Toast.makeText(PdfSyllabusActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                        new ClassRetrievePdfStream().execute(obj.getString("pdf_syllabus"));

                    } else {
                        pdfProgress.setVisibility(View.GONE);
                        Toast.makeText(PdfSyllabusActivity.this, ""+message, Toast.LENGTH_SHORT).show();

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
                params.put("student_id", student_id);
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
