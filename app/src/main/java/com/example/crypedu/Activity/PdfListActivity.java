package com.example.crypedu.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.PdfListAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PdfListInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PdfListActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Context context;
    private ArrayList<PdfListInfo> pdfList;
    private RecyclerView rv_pdf_list;
    private ProgressBar pb_loader;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context = PdfListActivity.this;
        pb_loader=findViewById(R.id.pb_loader);

        final Toolbar toolbar = findViewById(R.id.toolbar);
       /* setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

//        toolbar.setTitle(Constants.CHAPTER_NAME);
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle("PDF List");

        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rv_pdf_list=findViewById(R.id.rv_subject_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        rv_pdf_list.setLayoutManager(layoutManager);

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

        getPdfList();
    }


    public void getPdfList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "worksheet_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        pdfList=new ArrayList<>();

                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            PdfListInfo pdfListInfo=new PdfListInfo();
                            pdfListInfo.setId(jsonObject1.getString("id"));
                            pdfListInfo.setChapter_name(jsonObject1.getString("chapter_name"));
                            pdfListInfo.setWorksheet_url(jsonObject1.getString("worksheet_url"));

                            pdfList.add(pdfListInfo);
                        }
                        rv_pdf_list.setAdapter(new PdfListAdapter(context, pdfList));
                    }
                    else
                    {
                        Toast.makeText(context, "No days found", Toast.LENGTH_SHORT).show();
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
