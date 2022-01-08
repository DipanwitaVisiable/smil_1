package com.example.crypedu.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;

import java.util.Objects;

public class SmartClassActivity  extends AppCompatActivity {
    private Context context;
    private RelativeLayout rl_live_video, rl_worksheet, rl_written_test;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_class);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/
        context = SmartClassActivity.this;
      Constants.FROM_SCREEN="online_classes";

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rl_live_video=findViewById(R.id.rl_live_video);
        rl_worksheet=findViewById(R.id.rl_worksheet);
        rl_written_test=findViewById(R.id.rl_written_test);

        rl_live_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DaysListActivity.class);
//                Intent intent = new Intent(context, SmallQuizViewSolutionActivity.class);
                startActivity(intent);
            }
        });
        rl_worksheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubjectListActivity.class);
                startActivity(intent);
            }
        });

      rl_written_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, OnlineWrittenTestActivity.class);
              if (ActivityCompat.checkSelfPermission(SmartClassActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(context, QRCodeScannerActivity.class);
                startActivity(intent);
              } else {
                ActivityCompat.requestPermissions(SmartClassActivity.this, new
                  String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
              }


                /*Intent intent = new Intent(context, QRCodeScannerActivity.class);
                startActivity(intent);*/
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent);*/

                Intent i = new Intent(context, MenuActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(context, MenuActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    // start- add code
    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
      Intent intent = new Intent(context, QRCodeScannerActivity.class);
      startActivity(intent);
    }
    else {
      Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
    }
    // end- add code

  }
}
