package com.example.crypedu.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Constants.Titanic;
import com.example.crypedu.Constants.TitanicTextView;

import java.util.Objects;

public class DemoMediwalletActivity extends AppCompatActivity {
    Titanic titanic;
    TitanicTextView myTitanicTextView;
    Typeface typeface;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_mediwallet);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        titanic = new Titanic();
        myTitanicTextView = findViewById(R.id.titanic_tv);
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);
        myTitanicTextView.setTypeface(typeface);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoMediwalletActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        titanic.start(myTitanicTextView);

    }

    @Override
    protected void onStop() {
        super.onStop();
        titanic.cancel();
    }
}
