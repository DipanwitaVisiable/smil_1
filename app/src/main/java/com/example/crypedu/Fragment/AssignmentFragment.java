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
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Activity.ClassDirectWorkActivity;
import com.example.crypedu.Activity.ClassWorkActivity;
import com.example.crypedu.Activity.HomeDirectWorkActivity;
import com.example.crypedu.Activity.HomeWorkActivity;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Activity.TeacherClassWorkActivity;
import com.example.crypedu.Activity.TeacherHomeWorkActivity;
import com.example.crypedu.Adapter.AccountAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AccountInfo;
import com.google.android.material.snackbar.Snackbar;
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

public class AssignmentFragment extends Fragment{
    TextView tv_noti_count_class,tv_noti_count_home;

    public AssignmentFragment() {
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
        View myFragmentView = inflater.inflate(R.layout.fragment_assignment, container, false);
        /*
          Set BubbleGumSans Regular font.
         */
        Typeface typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getApplication().getAssets(), Constants.BubblegumSans_Regular_font);

        TextView copyRight_textView = myFragmentView.findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);
        TextView homeWork_textView = myFragmentView.findViewById(R.id.homeWork_textView);
        homeWork_textView.setTypeface(typeface);
        TextView classWork_textView = myFragmentView.findViewById(R.id.classWork_textView);
        classWork_textView.setTypeface(typeface);

        /*
          For displaying data either Teacher or Parents.
         */
        if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {
            LinearLayout linearLayout_homeWork;
            LinearLayout linearLayout_classWork;
            if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                linearLayout_homeWork = myFragmentView.findViewById(R.id.linearLayout_homeWork);
                linearLayout_homeWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplication(), TeacherHomeWorkActivity.class);
                        startActivity(intent);
                    }
                });
                linearLayout_classWork = myFragmentView.findViewById(R.id.linearLayout_classWork);
                linearLayout_classWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplication(), TeacherClassWorkActivity.class);
                        startActivity(intent);
                    }
                });

            }else if (Constants.USER_ROLE.equalsIgnoreCase("s")){
                linearLayout_homeWork = myFragmentView.findViewById(R.id.linearLayout_homeWork);
                linearLayout_homeWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplication(), HomeWorkActivity.class);
                        startActivity(intent);
                    }
                });
                linearLayout_classWork = myFragmentView.findViewById(R.id.linearLayout_classWork);
                linearLayout_classWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplication(), ClassWorkActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else if (Constants.USER_ROLE.equalsIgnoreCase("d")){
                linearLayout_homeWork = myFragmentView.findViewById(R.id.linearLayout_homeWork);
                linearLayout_homeWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplication(), HomeDirectWorkActivity.class);
                        startActivity(intent);
                    }
                });
                linearLayout_classWork = myFragmentView.findViewById(R.id.linearLayout_classWork);
                linearLayout_classWork.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplication(), ClassDirectWorkActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
        tv_noti_count_class= myFragmentView.findViewById(R.id.tv_noti_count_class);
        tv_noti_count_home= myFragmentView.findViewById(R.id.tv_noti_count_home);
        fetchAssignmentNotification();

        return myFragmentView;
    }

    private void fetchAssignmentNotification_old() {
        try {
            final RequestParams params = new RequestParams();
            params.put("student_id", Constants.USER_ID);
            Log.d("WorkValue", "getNoti: "+params);
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.post(Constants.BASE_SERVER + "assignment_count_dtls",params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        String status = obj.getString("status");
                        //String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONObject jsonObject = obj.getJSONObject("count");
                            if (!jsonObject.getString("classwork_count").equalsIgnoreCase("0")){
                                tv_noti_count_class.setVisibility(View.VISIBLE);
                                tv_noti_count_class.setText(jsonObject.getString("classwork_count"));
                            }else {
                                tv_noti_count_class.setVisibility(View.GONE);
                            }
                            if (!jsonObject.getString("homework_count").equalsIgnoreCase("0")){
                                tv_noti_count_home.setVisibility(View.VISIBLE);
                                tv_noti_count_home.setText(jsonObject.getString("homework_count"));
                            }else {
                                tv_noti_count_home.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void fetchAssignmentNotification() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "assignment_count_dtls", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    //String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject = obj.getJSONObject("count");
                        if (!jsonObject.getString("classwork_count").equalsIgnoreCase("0")){
                            tv_noti_count_class.setVisibility(View.VISIBLE);
                            tv_noti_count_class.setText(jsonObject.getString("classwork_count"));
                        }else {
                            tv_noti_count_class.setVisibility(View.GONE);
                        }
                        if (!jsonObject.getString("homework_count").equalsIgnoreCase("0")){
                            tv_noti_count_home.setVisibility(View.VISIBLE);
                            tv_noti_count_home.setText(jsonObject.getString("homework_count"));
                        }else {
                            tv_noti_count_home.setVisibility(View.GONE);
                        }
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
                params.put("student_id", Constants.USER_ID);
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
