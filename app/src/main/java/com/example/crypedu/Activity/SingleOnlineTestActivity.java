package com.example.crypedu.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.google.android.material.appbar.AppBarLayout;
import java.util.Objects;

public class SingleOnlineTestActivity extends AppCompatActivity {
    private Context context;
    private WebView wv_pay_online;
    private AppBarLayout app_bar;
    private ProgressBar pb_loader;
    private TextView tv_no_exam;
    private String page_link;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_online_test);

        app_bar = findViewById(R.id.app_bar);
        app_bar.setVisibility(View.VISIBLE);
        wv_pay_online = findViewById(R.id.wv_pay_online);
        wv_pay_online.getSettings().setJavaScriptEnabled(true);
        context = SingleOnlineTestActivity.this;
        pb_loader = findViewById(R.id.pb_loader);
        tv_no_exam = findViewById(R.id.tv_no_exam);

        Intent i = getIntent();
        page_link = i.getExtras().getString("page_link");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
            wv_pay_online.loadUrl(page_link);
        } else {
            Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
        }
    }
}