package com.example.crypedu.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.DaysListAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.DaysListInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreDetailsActivity  extends AppCompatActivity {
  private Context context;
  private String qr_code_details, store_code, store_name, store_owner, phone_no, email, address, discount;
  private TextView tv_store_name, tv_owner, tv_address, tv_discount, tv_email, tv_phone;
  private EditText et_amount;
  private Button btn_proceed;
  private ProgressBar pb_loader;
  private RequestQueue requestQueue;
  private int total_amount, discount_amount, payable_amount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_store_details);
    context=StoreDetailsActivity.this;
    initViews();
  }

  private void initViews() {
    qr_code_details=getIntent().getExtras().getString("qr_code_data");
    tv_store_name = findViewById(R.id.tv_store_name);
    tv_owner = findViewById(R.id.tv_owner);
    tv_address = findViewById(R.id.tv_address);
    tv_email = findViewById(R.id.tv_email);
    tv_phone = findViewById(R.id.tv_phone);
    tv_discount = findViewById(R.id.tv_discount);
    et_amount = findViewById(R.id.et_amount);
    btn_proceed = findViewById(R.id.btn_proceed);
    pb_loader = findViewById(R.id.pb_loader);
    requestQueue = Volley.newRequestQueue(this);

//    String currentString = qr_code_details;
    String[] separated = qr_code_details.split("&&");
    /*store_code= separated[0];
    store_name=separated[1];
    store_owner=separated[2];
    phone_no=separated[3];
    email=separated[4];
    address=separated[5];
    discount=separated[6];*/
    store_code= splitString(separated[0]);
    store_name=splitString(separated[1]);
    store_owner=splitString(separated[2]);
    phone_no=splitString(separated[3]);
    email=splitString(separated[4]);
    address=splitString(separated[5]);
    discount=splitString(separated[6]);




    tv_store_name.setText(store_name);
    tv_owner.setText("Owner: " + store_owner);
    tv_address.setText(address);
    tv_email.setText("Email: " + email);
    tv_phone.setText("Phone No. : " + phone_no);
    tv_discount.setText("You will get " + discount + "% Discount");

    btn_proceed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        total_amount= Integer.parseInt(et_amount.getText().toString());
        int dis= Integer.parseInt(discount);
        discount_amount= (total_amount * dis)/100;
        payable_amount= total_amount-discount_amount;

        if (InternetCheckActivity.isConnected()) {
          submitPayment();
        }else {
          Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
        }
      }
    });

  }

  private String splitString(String currentString) {
    String[] s = currentString.split("=");
    return s[1];
  }

  public void submitPayment() {
    pb_loader.setVisibility(View.VISIBLE);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "do_transaction", new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try {
          pb_loader.setVisibility(View.GONE);
          JSONObject jsonObject = new JSONObject(response);
          String status = jsonObject.getString("status");

          if (status.equalsIgnoreCase("200"))
          {
            Intent intent = new Intent(context, PaymentSuccessfulActivity.class);
            intent.putExtra("qr_code_data", store_code);
            context.startActivity(intent);
            finish();

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
            Toast.makeText(context, "Not success", Toast.LENGTH_SHORT).show();
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
        params.put("unique_code", store_code);
        params.put("payabel_amounnt", String.valueOf(payable_amount));
        params.put("total_amount", et_amount.getText().toString());
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



  /*public void paymentSuccessfulDialog(){
    final Dialog dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.payment_successful_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    TextView tv_unique_code = (TextView) dialog.findViewById(R.id.tv_unique_code);
    tv_unique_code.setText("Store code: " + store_code);

    TextView tv_close = (TextView) dialog.findViewById(R.id.tv_close);
    tv_close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();

        dialog.dismiss();
      }
    });

    dialog.show();
  }*/

  @Override
  protected void onResume() {
    super.onResume();
    et_amount.setText("");
  }
}
