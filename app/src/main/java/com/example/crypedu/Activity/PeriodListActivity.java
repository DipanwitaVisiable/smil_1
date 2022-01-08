package com.example.crypedu.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.PeriodListAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PeriodListInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PeriodListActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Context context;
    private ArrayList<PeriodListInfo> periodList;
    private RecyclerView rv_period_list;
    private ProgressBar pb_loader;
    private String days_id, days_name, reg_id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_list);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context = PeriodListActivity.this;
        pb_loader=findViewById(R.id.pb_loader);

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        //get value from previous class using bundle
        /*Bundle bundle;
        bundle = this.getIntent().getExtras();
         days_id = bundle.getString("days_id");
         days_name = bundle.getString("days_name");*/

         days_id = Constants.DAYS_ID;
         days_name = Constants.DAYS_NAME;

        final Toolbar toolbar = findViewById(R.id.toolbar);
       /* setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        toolbar.setTitle(days_name);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent);*/
                onBackPressed();
            }
        });


        rv_period_list=findViewById(R.id.rv_days_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_period_list.setLayoutManager(layoutManager);

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

        if (InternetCheckActivity.isConnected()) {
            getPeriodList();
        }else {
            Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
        }

    }


    public void getPeriodList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "period_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        periodList=new ArrayList<>();

                        JSONArray jsonArray = jsonObject.getJSONArray("period_list");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PeriodListInfo periodListInfo=new PeriodListInfo();
                            periodListInfo.setId(jsonObject1.getString("id"));
                            periodListInfo.setPeriods(jsonObject1.getString("periods"));

                            periodList.add(periodListInfo);
                        }
                        rv_period_list.setAdapter(new PeriodListAdapter(context, periodList));
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
                        Toast.makeText(context, "No periods found", Toast.LENGTH_SHORT).show();
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
                params.put("day_id", days_id);
                params.put("reg_id", reg_id);
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
