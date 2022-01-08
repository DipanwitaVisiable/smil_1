package com.example.crypedu.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.activity.smi.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class ImageShowingActivity  extends AppCompatActivity {
    private Button btnIvClose;
    private String image_url;
    private Context context;
    private ZoomageView iv_preview_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_image);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=ImageShowingActivity.this;
        btnIvClose=findViewById(R.id.btnIvClose);
        iv_preview_image=findViewById(R.id.iv_preview_image);

        Intent intent=getIntent();
        image_url=intent.getStringExtra("image_url");
        Picasso.with(context)
                .load(image_url)
                .placeholder(R.drawable.progress_animation)
                .into(iv_preview_image);

        btnIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
}
