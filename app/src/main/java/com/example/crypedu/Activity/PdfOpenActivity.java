package com.example.crypedu.Activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Constants.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PdfOpenActivity extends AppCompatActivity {
    private FloatingActionButton feed_download;
    String pdf;
    private PDFView pdfView;
    private ProgressBar pb_loader;
    Context context;
    Toolbar toolbar;
    TextView toolbar_title;
    private RequestQueue requestQueue;
    private String  pdf_link, reg_id;


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_open);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=PdfOpenActivity.this;
        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(Constants.CHAPTER_NAME);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        feed_download = findViewById(R.id.feed_download);
        pdf=Constants.PDF_URL;
        pb_loader = findViewById(R.id.progressBar);
        requestQueue = Volley.newRequestQueue(this);

        if (Constants.CHAPTER_ID.equals(""))
        {
            pdf_link=pdf;
            if (pdf_link.equals("") || pdf_link.equals("0") || pdf_link==null) {
                pb_loader.setVisibility(View.GONE);
                Toast.makeText(context, "No pdf found", Toast.LENGTH_LONG).show();
            }
            else
                new ClassRetrievePdfStream().execute(pdf_link);

//            feed_download.setVisibility(View.VISIBLE);
//            feed_download.setVisibility(View.GONE);
        }
        else {
            getPdfList();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        pdfView = findViewById(R.id.pdfView);
        pdfView.setVerticalScrollBarEnabled(true);
        pdfView.setSwipeVertical(true);
        pdfView.setFitsSystemWindows(true);


//        pb_loader.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.INVISIBLE);
//        new ClassRetrievePdfStream().execute(pdf);


        //Start download pdf
        feed_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = null;
//                uri = Uri.parse(pdf);
                uri = Uri.parse(pdf_link);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long referene = downloadManager.enqueue(request);
                Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();

            }
        });
        //End download pdf
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
                @SuppressLint("RestrictedApi")
                @Override
                public void loadComplete(int nbPages) {

                    pb_loader.setVisibility(View.GONE);
                    pdfView.setVisibility(View.VISIBLE);
                    feed_download.setVisibility(View.VISIBLE);

                }
            }).load();
        }
    }


    public void getPdfList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "worksheet_list", new Response.Listener<String>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(String response) {
                try {
//                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {

                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                            pdf_link=jsonObject1.getString("worksheet_url");

                            if (pdf_link.equals("") || pdf_link.equals("0") || pdf_link==null) {
                                pb_loader.setVisibility(View.GONE);
                                Toast.makeText(context, "No pdf found", Toast.LENGTH_LONG).show();
                            }
                            else
                                new ClassRetrievePdfStream().execute(pdf_link);

//                            feed_download.setVisibility(View.VISIBLE);
//                            feed_download.setVisibility(View.GONE);

                        }
                    }
                    else if (status.equalsIgnoreCase("206"))
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage(R.string.force_logout)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else
                    {
                        Toast.makeText(context, "No PDF found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loader.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loader.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Constants.USER_ID);
                params.put("subject_id", Constants.SUBJECT_ID);
                params.put("chapter_id", Constants.CHAPTER_ID);
                params.put("reg_id", reg_id);
                Log.e("STUDENT ID", Constants.USER_ID);
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

}
