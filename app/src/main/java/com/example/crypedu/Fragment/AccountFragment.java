package com.example.crypedu.Fragment;
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
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Adapter.AccountAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AccountInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class AccountFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private View myFragmentView;
    private TextView textView;
    private ArrayList<AccountInfo> accountArrayList;
    private ListView account_listView;
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_account, container, false);
        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), Constants.BubblegumSans_Regular_font);
        //textView = (TextView)myFragmentView.findViewById(R.id.textView);
        //textView.setTypeface(typeface);
        TextView copyRight_textView = myFragmentView.findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        if (InternetCheckActivity.isConnected()){
            //fetchFeesDeatils();
            fetchAccountDetails();
        }else {
            showSnack();
        }

        return myFragmentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = Objects.requireNonNull(getActivity()).getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void fetchAccountDetails_old() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER +"payment_status",requestParams, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            accountArrayList = new ArrayList<>();
                            account_listView = myFragmentView.findViewById(R.id.account_listView);
                            JSONArray jsonArrayList = obj.getJSONArray("fees_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String date = jsonObject.getString("created_date");
                                String account = jsonObject.getString("grand_total");
                                String subject="Fees";
                                String postBy="Admin";

                                AccountInfo accountInfo = new AccountInfo( subject, "on Date: "+date, "Received a sum of amount ₹ "+account+"/-",postBy);
                                accountArrayList.add(i, accountInfo);
                            }
                            account_listView.setAdapter(new AccountAdapter(Objects.requireNonNull(getActivity()).getApplication(), accountArrayList, getLayoutInflater()));
                            account_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String itemValue = account_listView.getItemAtPosition(position).toString().trim();
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

    //------------------------------
    // Fetch subject details from server
    // for launching fees notification.
    //------------------------------
//    public void fetchFeesDeatils() {
//        /**
//         * ProgressBar
//         */
//        final ProgressBar pbHeaderProgress = (ProgressBar) myFragmentView.findViewById(R.id.pbHeaderProgress);
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
//                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
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


    public void fetchAccountDetails() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "payment_status", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        accountArrayList = new ArrayList<>();
                        account_listView = myFragmentView.findViewById(R.id.account_listView);
                        JSONArray jsonArrayList = obj.getJSONArray("fees_details");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String date = jsonObject.getString("created_date");
                            String account = jsonObject.getString("grand_total");
                            String subject="Fees";
                            String postBy="Admin";

                            AccountInfo accountInfo = new AccountInfo( subject, "on Date: "+date, "Received a sum of amount ₹ "+account+"/-",postBy);
                            accountArrayList.add(i, accountInfo);
                        }
                        account_listView.setAdapter(new AccountAdapter(Objects.requireNonNull(getActivity()).getApplication(), accountArrayList, getLayoutInflater()));
                        account_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String itemValue = account_listView.getItemAtPosition(position).toString().trim();
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", Constants.USER_ID);
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
    }
}
