package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.example.crypedu.Adapter.DaysListAdapter;
import com.example.crypedu.Pojo.DaysListInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Adapter.BookAdapter;
import com.example.crypedu.Adapter.BookTitleAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.BookInfo;
import com.example.crypedu.Pojo.LibraryBookInfo;
import com.example.crypedu.Pojo.SubjectInfo;
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

public class BookManageActivity extends AppCompatActivity {

    private ArrayList<String> stringArrayList = new ArrayList<>();
    private ArrayList<SubjectInfo> subjectInfoArrayList = new ArrayList<>();
    private ArrayList<LibraryBookInfo> bookArrayList = new ArrayList<>();
    private GridView gridView;
    private ListView listView;
    private CoordinatorLayout coordinatorLayout;
    private TextView messageTextView;
    private RequestQueue requestQueue;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manage);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        /*
          Active back button on ToolBar.
         */
        requestQueue = Volley.newRequestQueue(this);
        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookManageActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        /*
          Set FloatingActionButton Visibility 'INVISIBLE'.
         */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(BookManageActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        /*
          Retrieved CoordinatorLayout Id for SnackBar.
         */
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);
        messageTextView = findViewById(R.id.messageTextView);
        messageTextView.setTypeface(typeface);

        listView = findViewById(R.id.listView);
        listView.setVisibility(View.GONE);

        /*
          ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);

        /*
          If internet connection is working
          then only call fetchSubjectIssueDetails().
         */
        if (InternetCheckActivity.isConnected()) {
            if (Constants.USER_ID != null && !Constants.USER_ID.equalsIgnoreCase("")) {
                pbHeaderProgress.setVisibility(View.GONE);
                fetchSubjectIssueDetails(Constants.USER_ID);
            }
        }else {
            showSnack();
        }
    }

    //------------------------
    // Hardware back key.
    //------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(BookManageActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //------------------------------
    // Fetch subject details from
    // server, now its not used.
    //------------------------------
    public void fetchManageBooks() {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            RequestParams params = new RequestParams();
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.get(Constants.BASE_SERVER +"svcsubjects/",params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArrayList = obj.getJSONArray("subjects");
                            gridView = findViewById(R.id.gridView);
                            gridView.setAdapter(new BookTitleAdapter(getBaseContext(), stringArrayList, getLayoutInflater()));
                            // ListView Item Click Listener
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // ListView Clicked item value
                                    String itemValue = gridView.getItemAtPosition(position).toString().trim();
                                    for (SubjectInfo subjectInfo : subjectInfoArrayList){
                                        if (subjectInfo.subjectName.equalsIgnoreCase(itemValue)){
                                            /*
                                              ProgressBar
                                             */
                                            final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
                                            pbHeaderProgress.setVisibility(View.VISIBLE);
                                            if (InternetCheckActivity.isConnected()){
                                                pbHeaderProgress.setVisibility(View.GONE);
                                                fetchSubjectDetails(subjectInfo.subjectId);
                                            }else {
                                                showSnack();
                                            }
                                        }
                                    }
                                }
                            });
                            stringArrayList.clear();
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String subjectId = jsonObject.getString("id");
                                String subjectName = jsonObject.getString("subject");

                                SubjectInfo subjectInfo = new SubjectInfo(subjectId, subjectName);
                                subjectInfoArrayList.add(i,subjectInfo);
                                stringArrayList.add(i, subjectName);
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
                    pbHeaderProgress.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    //-------------------------------
    // Fetch book details from server
    // with respective of student id.
    //-------------------------------
    public void fetchSubjectDetails_old(final String subjectId){
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            RequestParams params = new RequestParams();
            params.put("subject_id", Integer.valueOf(subjectId));
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.post(Constants.BASE_SERVER +"svcsubjectbooks/",params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArrayList = obj.getJSONArray("books");
                            bookArrayList.clear();
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String bookId = jsonObject.getString("id");
                                String bookTitle = jsonObject.getString("title");
                                String bookAuthor = jsonObject.getString("author");
                                String bookPublisher = jsonObject.getString("publisher");
                                String bookEdition = jsonObject.getString("edition");
                                String bookSubjectId = jsonObject.getString("subject_id");
                                String bookPrice = jsonObject.getString("price");
                                String bookIsbnNumber = jsonObject.getString("ISBN_number");
                                String bookShortDescription = jsonObject.getString("short_desc");

                                BookInfo bookInfo = new BookInfo(bookId, bookTitle, bookAuthor, bookPublisher, bookEdition,
                                        bookSubjectId, bookPrice, bookIsbnNumber, bookShortDescription);
//                                bookArrayList.add(i, bookInfo);
                            }
                            gridView.setVisibility(View.GONE);
                            listView = findViewById(R.id.listView);
                            listView.setVisibility(View.VISIBLE);
//                            listView.setAdapter(new BookAdapter(getBaseContext(), bookArrayList, getLayoutInflater()));
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

    //-------------------------------
    // Fetch book issue details from server
    // with respective of student id.
    //-------------------------------
    public void fetchSubjectIssueDetails_old(final String studentId){
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            RequestParams params = new RequestParams();
            params.put("student_id", studentId);
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.post(Constants.BASE_SERVER +"issue_books/",params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArrayList = obj.getJSONArray("request_data");
                            bookArrayList.clear();
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String bookId = jsonObject.getString("book_id");
                                String bookTitle = jsonObject.getString("title");
                                String bookAuthor = jsonObject.getString("author");
                                String bookPublisher = jsonObject.getString("publisher");
                                String bookEdition = jsonObject.getString("edition");
                                String bookIssuedDate = jsonObject.getString("issue_date");
                                String bookIssuedDays = jsonObject.getString("issued_days");
                                String bookReturnDate = jsonObject.getString("return_date");
                                String bookStatus = jsonObject.getString("status");

                                LibraryBookInfo libraryBookInfo = new LibraryBookInfo(bookId, bookTitle, bookAuthor, bookPublisher, bookEdition,
                                        bookIssuedDate, bookIssuedDays, bookReturnDate, bookStatus);
                                bookArrayList.add(i, libraryBookInfo);
                            }
                            listView = findViewById(R.id.listView);
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(new BookAdapter(getBaseContext(), bookArrayList, getLayoutInflater()));
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                            messageTextView.setText(message);
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

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    public void fetchSubjectDetails(final String subjectId){
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "svcsubjectbooks/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArrayList = obj.getJSONArray("books");
                        bookArrayList.clear();
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String bookId = jsonObject.getString("id");
                            String bookTitle = jsonObject.getString("title");
                            String bookAuthor = jsonObject.getString("author");
                            String bookPublisher = jsonObject.getString("publisher");
                            String bookEdition = jsonObject.getString("edition");
                            String bookSubjectId = jsonObject.getString("subject_id");
                            String bookPrice = jsonObject.getString("price");
                            String bookIsbnNumber = jsonObject.getString("ISBN_number");
                            String bookShortDescription = jsonObject.getString("short_desc");

                            BookInfo bookInfo = new BookInfo(bookId, bookTitle, bookAuthor, bookPublisher, bookEdition,
                                    bookSubjectId, bookPrice, bookIsbnNumber, bookShortDescription);
//                                bookArrayList.add(i, bookInfo);
                        }
                        gridView.setVisibility(View.GONE);
                        listView = findViewById(R.id.listView);
                        listView.setVisibility(View.VISIBLE);
//                            listView.setAdapter(new BookAdapter(getBaseContext(), bookArrayList, getLayoutInflater()));
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
                params.put("subject_id", subjectId);
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

    public void fetchSubjectIssueDetails(final String studentId){
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "issue_books/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArrayList = obj.getJSONArray("request_data");
                        bookArrayList.clear();
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String bookId = jsonObject.getString("book_id");
                            String bookTitle = jsonObject.getString("title");
                            String bookAuthor = jsonObject.getString("author");
                            String bookPublisher = jsonObject.getString("publisher");
                            String bookEdition = jsonObject.getString("edition");
                            String bookIssuedDate = jsonObject.getString("issue_date");
                            String bookIssuedDays = jsonObject.getString("issued_days");
                            String bookReturnDate = jsonObject.getString("return_date");
                            String bookStatus = jsonObject.getString("status");

                            LibraryBookInfo libraryBookInfo = new LibraryBookInfo(bookId, bookTitle, bookAuthor, bookPublisher, bookEdition,
                                    bookIssuedDate, bookIssuedDays, bookReturnDate, bookStatus);
                            bookArrayList.add(i, libraryBookInfo);
                        }
                        listView = findViewById(R.id.listView);
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(new BookAdapter(getBaseContext(), bookArrayList, getLayoutInflater()));
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                        messageTextView.setText(message);
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
                params.put("student_id", studentId);
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
