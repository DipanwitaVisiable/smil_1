package com.example.crypedu.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
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
import com.example.crypedu.Adapter.PathologyAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PathologySetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PathologyActivity extends AppCompatActivity {
    private RecyclerView pathology_recyclerView;
    private ArrayList<PathologySetterGetter> pathologySetterGetters = new ArrayList<>();
    private PathologyAdapter pathologyAdapter;
    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue requestQueue;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology);
       /* if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context = PathologyActivity.this;
        requestQueue= Volley.newRequestQueue(context);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  Status bar :: Transparent
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(context, PathologyServiceActivity.class));
                onBackPressed();
            }
        });

        pathology_recyclerView = findViewById(R.id.pathology_recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        TextView tv_list_of_pathology = findViewById(R.id.tv_list_of_pathology);
        tv_list_of_pathology.setTypeface(typeface);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        pathology_recyclerView.setLayoutManager(layoutManager);
        pathology_recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(pathology_recyclerView, false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


         /*
      fetching the pathology
      */

         if (InternetCheckActivity.isConnected()) {
             fetchingPathologyList();
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
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void fetchingPathologyList(){
         /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER_MEDIWALLET + "medi_pathology_list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pbHeaderProgress.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("200")) {
                                //Log.e("message PathologyPage", message);
                                JSONArray pathology_list = obj.getJSONArray("pathology_list");
                                pathologySetterGetters.clear();
                                for (int i = 0; i < pathology_list.length(); i++) {
                                    //Log.e("list PathologyPage", String.valueOf(pathology_list));
                                    JSONObject object = pathology_list.getJSONObject(i);
                                    PathologySetterGetter setterGetter = new PathologySetterGetter();
                                    setterGetter.setPathology_name(object.getString("lab_name"));
                                    setterGetter.setPathology_address(object.getString("lab_address"));
                                    setterGetter.setPathology_description(object.getString("lab_desc"));
                                    setterGetter.setPathology_id(object.getString("lab_id"));
                                    setterGetter.setPathology_discount(object.getString("discount"));
                                    setterGetter.setPathology_phn(object.getString("lab_phone"));
                                    setterGetter.setPathology_image(object.getString("lab_img"));
                                    pathologySetterGetters.add(setterGetter);
                                }
                                pathologyAdapter = new PathologyAdapter(context, pathologySetterGetters);
                                pathology_recyclerView.setAdapter(pathologyAdapter);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbHeaderProgress.setVisibility(View.GONE);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("path_id", Constants.PATHOLOGY_SRVS_ID);
                //Log.e("ID put PathologyPage", Constants.PATHOLOGY_SRVS_ID);
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
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //startActivity(new Intent(context, PathologyServiceActivity.class));
                onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
