package com.example.crypedu.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.AssignmentHistoryListAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AssignmentFileModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AssignmentHistoryListActivity extends AppCompatActivity {
  private RequestQueue requestQueue;
  private Context context;
  private ArrayList<AssignmentFileModel> assignmentFileList;
  private RecyclerView rv_history_list;
  private ProgressBar pb_loader;
  private String reg_id, exam_id, exam_date, end_time;
  private Button btn_reply;

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assignment_view);

    context = AssignmentHistoryListActivity.this;
    pb_loader = findViewById(R.id.pb_loader);
    rv_history_list = findViewById(R.id.rv_history_list);
    btn_reply = findViewById(R.id.btn_reply);

    final Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    toolbar.setTitleTextColor(Color.WHITE);
    toolbar.setNavigationIcon(R.drawable.ic_left_arrow);

    requestQueue = Volley.newRequestQueue(this);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    rv_history_list = findViewById(R.id.rv_history_list);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    rv_history_list.setLayoutManager(layoutManager);

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

    SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
    reg_id = device_token.getString("FIREBASE_ID", "");


    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    exam_id = bundle.getString("exam_id");
    exam_date = bundle.getString("exam_date");
    end_time = bundle.getString("end_time");

    btn_reply.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, AssignmentReplyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("exam_id", exam_id);
        bundle.putString("exam_date", exam_date);
        bundle.putString("end_time", end_time);
        intent.putExtras(bundle);
        context.startActivity(intent);
      }
    });

    if (InternetCheckActivity.isConnected()) {
      getAssignmentHistoryList();
    } else {
      Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
    }
  }

  public void getAssignmentHistoryList() {
    pb_loader.setVisibility(View.VISIBLE);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "student_exam_file", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try {
          pb_loader.setVisibility(View.GONE);
          JSONObject jsonObject = new JSONObject(response);
          String status = jsonObject.getString("status");

          if (status.equalsIgnoreCase("200"))
          {
            assignmentFileList=new ArrayList<>();
              JSONArray jsonArray = jsonObject.getJSONArray("message");
              for (int i=0; i<jsonArray.length(); i++)
              {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                AssignmentFileModel topicListInfo=new AssignmentFileModel();
                topicListInfo.setFile_url(jsonObject1.getString("upload_file"));
                assignmentFileList.add(topicListInfo);
              }
              rv_history_list.setAdapter(new AssignmentHistoryListAdapter(context, assignmentFileList));
          }

          else if (status.equalsIgnoreCase("206"))
          {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.force_logout)
              .setCancelable(false)
              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  Intent intent = new Intent(context, LoginActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(intent);
                }
              });
            AlertDialog alert = builder.create();
            alert.show();
          }
          else
          {
            Toast.makeText(context, "No Assignment found", Toast.LENGTH_SHORT).show();
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
        params.put("exam_id", exam_id);
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
