package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;

import java.util.Objects;

public class ResultActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MenuActivity.class);
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


        coordinatorLayout = findViewById(R.id.coordinatorLayout);


        LinearLayout linearLayout_firstTerm = findViewById(R.id.linearLayout_firstTerm);
        linearLayout_firstTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  For displaying data Parents.
                 */
                if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {
                    if (Constants.USER_ROLE.equalsIgnoreCase("s") || Constants.USER_ROLE.equalsIgnoreCase("d")) {
                        Intent intent = new Intent(ResultActivity.this, FirstTermActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        LinearLayout linearLayout_secondTerm = findViewById(R.id.linearLayout_secondTerm);
        linearLayout_secondTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  For displaying data Parents.
                 */
                if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {
                    if (Constants.USER_ROLE.equalsIgnoreCase("s") || Constants.USER_ROLE.equalsIgnoreCase("d")) {
                        Intent intent = new Intent(ResultActivity.this, SecondTermActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


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
            Intent intent = new Intent(ResultActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(ResultActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }
}
