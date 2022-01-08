package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.smi.R;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AccountInfo;

import java.util.ArrayList;
import java.util.Objects;

public class AccountActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<AccountInfo> accountArrayList;
    private ListView account_listView;
    private WebView wv_pay_online;
    private Context context;
    private TextView tv_form_link;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context=AccountActivity.this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        tv_form_link = findViewById(R.id.tv_form_link);
        wv_pay_online = findViewById(R.id.wv_pay_online);
        wv_pay_online.getSettings().setJavaScriptEnabled(true);
        wv_pay_online.loadUrl(Constants.PAY_ONLINE_URL);

        tv_form_link.setText(Constants.PAY_ONLINE_URL);

    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        wv_pay_online.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        });

        // Javascript inabled on webview
        wv_pay_online.getSettings().setJavaScriptEnabled(true);

        //Load url in webview
        wv_pay_online.loadUrl(url);


    }




   /* // Start- this code is close on 28_04_2020
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });


        //textView = (TextView)findViewById(R.id.textView);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);
        //textView.setTypeface(typeface);
        copyRight_textView.setTypeface(typeface);


        if (InternetCheckActivity.isConnected()) {
            //fetchFeesDeatils();
            fetchPaymentDetails();
            sendNotificationStatus();
        }else {
            showSnack();
        }
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
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //------------------------------
    // Send notification status to server
    // respective of student_id and notification_type.
    //------------------------------
    public void sendNotificationStatus() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id",Constants.USER_ID);
            requestParams.put("notification_type","account");
            clientReg.post(Constants.BASE_SERVER +"notification_status/",requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(AccountActivity.this, MenuActivity.class));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private void fetchPaymentDetails() {

        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "payment_status", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            accountArrayList = new ArrayList<>();
                            account_listView = findViewById(R.id.account_listView);
                            JSONArray jsonArrayList = obj.getJSONArray("fees_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String date = jsonObject.getString("created_date");
                                String account = jsonObject.getString("grand_total");
                                String subject = "Fees";
                                String postBy = "Admin";

                                AccountInfo accountInfo = new AccountInfo(subject, "on Date: " + date, "Received a sum of amount ₹ " + account + "/-", postBy);
                                accountArrayList.add(i, accountInfo);
                            }
                            account_listView.setAdapter(new AccountAdapter(getBaseContext(), accountArrayList, getLayoutInflater()));
                            account_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //String itemValue = account_listView.getItemAtPosition(position).toString().trim();
                                }
                            });
//                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        } else {
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }


    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }
    // End- this code is close on 28_04_2020*/


    //------------------------------
    // Fetch subject details from server
    // for launching fees notification.
    //------------------------------
//    public void fetchFeesDeatils() {
//        /**
//         * ProgressBar
//         */
//        final ProgressBar pbHeaderProgress = (ProgressBar) findViewById(R.id.pbHeaderProgress);
//        pbHeaderProgress.setVisibility(View.VISIBLE);
//        try {
//            AsyncHttpClient clientReg = new AsyncHttpClient();
//            RequestParams requestParams = new RequestParams();
//            requestParams.put("id",Constants.USER_ID);
//            clientReg.post("http://"+ Constants.server_name +"webservices/websvc/fee_status/",requestParams, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
//                    try {
//                        pbHeaderProgress.setVisibility(View.GONE);
//                        String status = obj.getString("status");
//                        String message = obj.getString("message");
//                        if (status.equalsIgnoreCase("200")) {
//                            JSONObject jsonObject = obj.getJSONObject("fees_details");
//                            String amount = jsonObject.getString("amount");
//                            String feesStatus = jsonObject.getString("fees_status");
//                            if (feesStatus.equalsIgnoreCase("true")){
//                                textView.setText(amount +" "+ message);
//                            }
//                        } else {
//                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
//
//                            // Changing action button text color
//                            View sbView = snackbar.getView();
//                            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
//                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
//                            snackbar.show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // When the response returned by REST has Http response code other than '200'
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    pbHeaderProgress.setVisibility(View.GONE);
//                }
//            });
//        } catch (Exception e) {
//            pbHeaderProgress.setVisibility(View.GONE);
//            e.printStackTrace();
//        }
//    }
}
