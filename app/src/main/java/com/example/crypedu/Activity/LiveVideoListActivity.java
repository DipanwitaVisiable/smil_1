package com.example.crypedu.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.LiveVideoListAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.LiveVideoListInfo;
import com.google.android.youtube.player.YouTubeBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LiveVideoListActivity extends YouTubeBaseActivity {

    private RequestQueue requestQueue;
    private Context context;
    private ArrayList<LiveVideoListInfo> liveVideoList;
    private RecyclerView rv_video_list;
    private ProgressBar pb_loader;
    private String reg_id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_videos);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context = LiveVideoListActivity.this;
        pb_loader=findViewById(R.id.pb_loader);

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        /*setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        requestQueue = Volley.newRequestQueue(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent);*/
               onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setTitle(Constants.PERIOD_NAME);

        rv_video_list=findViewById(R.id.rv_video_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_video_list.setLayoutManager(layoutManager);

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
            getLiveVideoList();
        }else {
            Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
        }
    }



    private void getLiveVideoList() {
        pb_loader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "video_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {
                        liveVideoList=new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            LiveVideoListInfo liveVideoListInfo=new LiveVideoListInfo();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            liveVideoListInfo.setId(jsonObject1.getString("id"));
                            liveVideoListInfo.setTopic_name(jsonObject1.getString("topic_name"));
                            liveVideoListInfo.setClass_date(jsonObject1.getString("class_date"));

                            String video_url=jsonObject1.getString("video_link");
                            String shortUrl=video_url.replace("https://www.youtube.com/watch?v=","");
                            liveVideoListInfo.setVideo_link(shortUrl);

                            liveVideoList.add(liveVideoListInfo);

                        }

                        rv_video_list.setAdapter(new LiveVideoListAdapter(context,liveVideoList));

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
                        Toast.makeText(context, "No Video found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loader.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
                pb_loader.setVisibility(View.GONE);

            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams = new HashMap<String, String>();
//                myParams.put("student_id", Constants.USER_ID);
                myParams.put("student_id", Constants.USER_ID);
                myParams.put("day_id", Constants.DAYS_ID);
                myParams.put("period_id", Constants.PERIOD_ID);
                myParams.put("reg_id", reg_id);
                return myParams;
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
