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
import com.example.crypedu.Adapter.MediChamberAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.MediChamberSetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MediChamberActivity extends AppCompatActivity {
    private RecyclerView chamber_recyclerView;
    private ArrayList<MediChamberSetterGetter> mediChamberSetterGetterArrayList = new ArrayList<>();
    private MediChamberAdapter mediChamberAdapter;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue requestQueue;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_chamber);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //  Status bar :: Transparent
        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediChamberActivity.this, MediwalletActivity.class);
                startActivity(intent);
            }
        });

        chamber_recyclerView = findViewById(R.id.chamber_recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        TextView tv_list_of_chamber = findViewById(R.id.tv_list_of_chamber);
        tv_list_of_chamber.setTypeface(typeface);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MediChamberActivity.this);
        chamber_recyclerView.setLayoutManager(layoutManager);
        chamber_recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(chamber_recyclerView, false);

      /*
      fetching the chambers
      */

        if (InternetCheckActivity.isConnected()) {
            fetchingMediChambersList();
        } else {
            showSnack();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showSnack() {
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
            Intent intent = new Intent(MediChamberActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void fetchingMediChambersList() {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER_MEDIWALLET + "medi_chamber_list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pbHeaderProgress.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("200")) {
                                JSONArray chamber_list = obj.getJSONArray("chamber_list");
                                mediChamberSetterGetterArrayList.clear();
                                for (int i = 0; i < chamber_list.length(); i++) {
                                    JSONObject object = chamber_list.getJSONObject(i);
                                    MediChamberSetterGetter setterGetter = new MediChamberSetterGetter();
                                    setterGetter.setChamber_id(object.getString("chamber_id"));
                                    setterGetter.setChamber_name(object.getString("chamber_name"));
                                    setterGetter.setChamber_address(object.getString("chamber_address"));
                                    setterGetter.setChamber_desc(object.getString("chamber_desc"));
                                    setterGetter.setChamber_phone(object.getString("chamber_phone"));
                                    setterGetter.setChamber_img(object.getString("chamber_img"));
                                    setterGetter.setChamber_type(object.getString("chamber_type"));
                                    mediChamberSetterGetterArrayList.add(setterGetter);
                                }
                                mediChamberAdapter = new MediChamberAdapter(MediChamberActivity.this, mediChamberSetterGetterArrayList);
                                chamber_recyclerView.setAdapter(mediChamberAdapter);
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //Adding the parameters to the request
                return new HashMap<>();
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
                startActivity(new Intent(this, MediwalletActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
