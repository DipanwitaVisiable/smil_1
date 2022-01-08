package com.example.crypedu.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.activity.smi.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.crypedu.Helper.GifImageView;

public class PaymentSuccessfulActivity extends AppCompatActivity {

  private Context context;
  private String store_code;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.payment_successful_dialog);
    context= PaymentSuccessfulActivity.this;
    store_code=getIntent().getExtras().getString("qr_code_data");

    TextView tv_close = (TextView) findViewById(R.id.tv_close);
    TextView tv_unique_code = (TextView) findViewById(R.id.tv_unique_code);

    GifImageView iv_gif = (GifImageView) findViewById(R.id.iv_gif);
    iv_gif.setGifImageResource(R.drawable.payment_success_gif);

    tv_unique_code.setText("Store code: " + store_code);

    tv_close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
  }
}
