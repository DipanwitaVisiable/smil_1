package com.example.crypedu.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.volley.RetryPolicy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Constants.Constants;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.squareup.picasso.Picasso;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class HealthCardActivity extends AppCompatActivity {
    BottomSheetBehavior behavior;
    private AppCompatTextView textView_cardNo;
    private AppCompatTextView textView_name;
    private AppCompatTextView textView_address;
    private AppCompatTextView textView_valid_from;
    private AppCompatTextView textView_valid_to;
    private CircleImageView imageView_user;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue requestQueue;
    private PDFView pdfView;
    private ProgressBar pbHeaderProgress;
    private String apollo_url, pdfURL;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_card);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthCardActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
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
            Intent intent = new Intent(HealthCardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        /*
          Set BubbleGumSans Regular custom font.
         */
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        /*
          Finding all ids
         */
        textView_cardNo = findViewById(R.id.textView_cardNo);
        textView_cardNo.setTypeface(typeface);

        AppCompatTextView textView_title = findViewById(R.id.textView_title);
        textView_title.setTypeface(typeface);

        textView_name = findViewById(R.id.textView_name);
        textView_name.setTypeface(typeface);

        textView_address = findViewById(R.id.textView_address);
        textView_address.setTypeface(typeface);

        textView_valid_from = findViewById(R.id.textView_valid_from);
        textView_valid_from.setTypeface(typeface);

        textView_valid_to = findViewById(R.id.textView_valid_to);
        textView_valid_to.setTypeface(typeface);

        AppCompatTextView textView_Hospitals = findViewById(R.id.textView_Hospitals);
        textView_Hospitals.setTypeface(typeface);

        AppCompatTextView textView_Terms = findViewById(R.id.textView_Terms);
        textView_Terms.setTypeface(typeface);

        imageView_user = findViewById(R.id.imageView_user);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);


        pdfView = findViewById(R.id.pdfView_terms);

        pdfView.setVerticalScrollBarEnabled(true);
        pdfView.setSwipeVertical(true);
        pdfView.setFitsSystemWindows(true);
        /*
          If internet connection is working
          then only call fetchClassDetails().
         */
        if (InternetCheckActivity.isConnected()) {
            fetchHealthCardDetails();

            fetchingTermsPDF();
        }else {
            showSnack();
        }

        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        ImageView bottomSheet_icon = findViewById(R.id.bottomSheet_icon);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        // toolbar.setVisibility(View.VISIBLE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        toolbar.setVisibility(View.GONE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        toolbar.setVisibility(View.GONE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        toolbar.setVisibility(View.VISIBLE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        toolbar.setVisibility(View.VISIBLE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });

        bottomSheet_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        textView_Hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(apollo_url));
                startActivity(i);
            }
        });

        textView_Terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent termsIntent = new Intent(HealthCardActivity.this, TermsConditionActivity.class);
                startActivity(termsIntent);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (InternetCheckActivity.isConnected()) {
            fetchHealthCardDetails();

            fetchingTermsPDF();
        }else {
            showSnack();
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

    public void fetchHealthCardDetails() {
        /*
         *  Progress Bar
         */
        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "health_card", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    Log.e("STATUS", status);
                    Log.e("MESSAGE", message);
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject card_details = obj.getJSONObject("card_details");
                        String name = card_details.getString("student_name");
                        String address = card_details.getString("address");
                        String card_no = card_details.getString("card_no");
                        String card_valid_frm = card_details.getString("card_valid_frm");
                        String card_valid_to = card_details.getString("card_valid_to");
                        String image_url = card_details.getString("image_url");
                        textView_name.setText(name);
                        textView_address.setText(address);
                        textView_cardNo.setText(String.format("CARD NO:  %s", card_no));
                        textView_valid_from.setText(String.format("Valid from: %s", card_valid_frm));
                        textView_valid_to.setText(String.format("Valid to: %s", card_valid_to));
                        if (!image_url.equals("")) {
                            Picasso.with(getBaseContext()).load(image_url).placeholder(R.drawable.placeholder).noFade().into(imageView_user);
                        }

                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    pbHeaderProgress.setVisibility(View.GONE);
                    e.printStackTrace();

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


    //--------------------------------
    // fetchStudentProfile from server
    // with the 'studentID' param.
    //--------------------------------
    public void fetchingTermsPDF() {
        /*
         *  Progress Bar
         */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_SERVER + "health_card_pdf", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    Log.e("STATUS", status);
                    Log.e("MESSAGE", message);
                    if (status.equalsIgnoreCase("200")) {

                        pdfURL = obj.getString("pdf_link");
                        apollo_url = obj.getString("apallo_url");

                        Log.e("PDF URL TERMS", pdfURL);
                        new RetrievePdfStream().execute(pdfURL);//or any URL direct pdf from Internet
                    } else {
                        Log.e("203 pdf MESSAGE", message);
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

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true;
                } else {
                    startActivity(new Intent(HealthCardActivity.this, MenuActivity.class));
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
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
            //pdfProgress.setVisibility(View.GONE);
            pdfView.fromStream(inputStream).onPageScroll(new OnPageScrollListener() {
                @Override
                public void onPageScrolled(int page, float positionOffset) {
                }
            }).load();
        }
    }

}
