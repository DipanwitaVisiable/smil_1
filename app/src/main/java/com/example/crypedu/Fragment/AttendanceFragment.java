package com.example.crypedu.Fragment;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.example.crypedu.Activity.AttendanceLateReasonActivity;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Adapter.CalendarAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.AttendanceInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class AttendanceFragment extends Fragment {

    public Calendar month, itemmonth;// calendar instances.
    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    public ArrayList<String> items; // container to store calendar items which

    int i;
    RelativeLayout previous,next;
    private ArrayList<AttendanceInfo> attendanceArrayList;
    private GridView gridview;
    private TextView present_textView;
    private TextView absent_textView;
    private TextView holiday_textView;
    private TextView leave_textView;
    private TextView messageTextView;
    private ImageView imageView;
    private View myFragmentView;
    private CoordinatorLayout coordinatorLayout;
    TextView title;
    Typeface typeface;
    FloatingActionButton fab;


    public AttendanceFragment() {
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
        myFragmentView = inflater.inflate(R.layout.fragment_attendance, container, false);

        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);
        /*
          Retrieved Cartoon ImageView Id and set Visibility 'INVISIBLE'.
         */
        imageView = myFragmentView.findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);

        /*
          Set BubbleGumSans Regular font.
         */
        typeface = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getApplication().getAssets(), Constants.BubblegumSans_Regular_font);

        /*
          Retrieved Present, Holiday, Leave
          and Absent Id for showing total no of days.
         */
        present_textView = myFragmentView.findViewById(R.id.present_textView);
        holiday_textView = myFragmentView.findViewById(R.id.holiday_textView);
        leave_textView = myFragmentView.findViewById(R.id.leave_textView);
        absent_textView = myFragmentView.findViewById(R.id.absent_textView);
        TextView copyRight_textView = myFragmentView.findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        messageTextView = myFragmentView.findViewById(R.id.messageTextView);
        messageTextView.setTypeface(typeface);
        Button lateReasonButton = myFragmentView.findViewById(R.id.lateReasonButton);
        lateReasonButton.setTypeface(typeface);

        lateReasonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), AttendanceLateReasonActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        /*
          Set BubbleGumSans Regular font on
          Present, Absent, Holiday and Leave.
         */
        present_textView.setTypeface(typeface);
        holiday_textView.setTypeface(typeface);
        leave_textView.setTypeface(typeface);
        absent_textView.setTypeface(typeface);
        copyRight_textView.setTypeface(typeface);

        SharedPreferences pref = getActivity().getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        /*
          Retrieved Current date from System.
         */
        month = Calendar.getInstance();
        itemmonth = (Calendar) month.clone();

        /*
          Display Current Month with Year on
          the Top of Calendar.
         */
        title = myFragmentView.findViewById(R.id.title);
       /* title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
        title.setTypeface(typeface);*/

        /*
          RelativeLayout for displaying
          previous Month of current year.
         */
        previous = myFragmentView.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                if (adapter != null) {
                    refreshCalendar();
                }
            }
        });

        /*
          RelativeLayout for displaying
          next month of current year.
         */
        next = myFragmentView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                if (adapter != null) {
                    refreshCalendar();
                }
            }
        });

        /*
          Check whether working internet
          connection is present or not.
         */
        if (InternetCheckActivity.isConnected()) {
            fetchDate();
        }else {
            showSnack();
        }
        fab = myFragmentView.findViewById(R.id.fabAttendence);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                fetchDate();
            }
        });


        return myFragmentView;
    }

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
    }*/

    //----------------------------
    // Showing calendar next
    // current month.
    //----------------------------
    @SuppressLint("SetTextI18n")
    protected void setNextMonth() {
        int absentCount = 0, presentCount = 0, holidayCount = 0, leaveCount = 0;
        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) + 1),
                    month.getActualMinimum(Calendar.MONTH), 1);
            int nextMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.date;
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (nextMonth == currentMonth) {
                        if (attendanceInfo.attendence_status.equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }
                }
            }
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
            int nextMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.date;
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (nextMonth == currentMonth) {
                        if (attendanceInfo.attendence_status.equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }
                }
            }
        }
        present_textView.setText("PRESENT: " + presentCount);
        absent_textView.setText("ABSENT: " + absentCount);
        holiday_textView.setText("HOLIDAY: " + holidayCount);
        leave_textView.setText("LEAVE: " + leaveCount);
    }

    //-----------------------------
    // Showing calendar previous
    // current month.
    //-----------------------------
    @SuppressLint("SetTextI18n")
    protected void setPreviousMonth() {
        int absentCount = 0, presentCount = 0, holidayCount = 0, leaveCount = 0;
        if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) - 1),
                    month.getActualMaximum(Calendar.MONTH), 1);
            int previousMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.date;
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (previousMonth == currentMonth) {
                        if (attendanceInfo.attendence_status.equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }
                }
            }
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
            int previousMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.date;
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (previousMonth == currentMonth) {
                        if (attendanceInfo.attendence_status.equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }
                }
            }
        }
        present_textView.setText("PRESENT: " + presentCount);
        absent_textView.setText("ABSENT: " + absentCount);
        holiday_textView.setText("HOLIDAY: " + holidayCount);
        leave_textView.setText("LEAVE: " + leaveCount);
    }

    //----------------------------
    // Showing calendar
    // current month with no of
    // present, absent, holiday and leave.
    //----------------------------
    @SuppressLint("SetTextI18n")
    protected void setCurrentMonth() {
        int absentCount = 0, presentCount = 0, holidayCount = 0, leaveCount = 0;
        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
           // month.set((month.get(Calendar.YEAR)),
                    //month.getActualMinimum(Calendar.MONTH), 1);
            int nextMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.date;
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (nextMonth == currentMonth) {
                        if (attendanceInfo.attendence_status.equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }
                }
            }
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH));
            int nextMonth = month.get(Calendar.MONTH) + 1;
            if (attendanceArrayList != null) {
                for (AttendanceInfo attendanceInfo : attendanceArrayList) {
                    String str = attendanceInfo.date;
                    String str1 = str.substring(5, 7);
                    String str2 = str1.replaceFirst("^0+(?!$)", "");
                    int currentMonth = Integer.valueOf(str2);
                    if (nextMonth == currentMonth) {
                        if (attendanceInfo.attendence_status.equalsIgnoreCase("a")) {
                            absentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("p")) {
                            presentCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("h")) {
                            holidayCount++;
                        } else if (attendanceInfo.attendence_status.equalsIgnoreCase("l")) {
                            leaveCount++;
                        }
                    }
                }
            }
        }
        refreshCalendar();
        present_textView.setText("PRESENT: " + presentCount);
        absent_textView.setText("ABSENT: " + absentCount);
        holiday_textView.setText("HOLIDAY: " + holidayCount);
        leave_textView.setText("LEAVE: " + leaveCount);
    }

    //------------------------------
    // Displaying toast message.
    //------------------------------
    protected void showSnackBar(String string) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, string, Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    //-------------------------------
    // Refresh calendar.
    //-------------------------------
    public void refreshCalendar() {
        TextView title = myFragmentView.findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    //--------------------------------
    // Retrieved student attendance
    // details respective of student Id.
    //--------------------------------
    public void fetchDate_old() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        //fab.setVisibility(View.GONE);

        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Integer.parseInt(Constants.USER_ID));
            Log.d("AttenFragValue", "fetchDate: "+requestParams);
            clientReg.post(Constants.BASE_SERVER + "get_student_attendence", requestParams, new JsonHttpResponseHandler() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        Log.d("AttenFragResponse", "onSuccess: "+obj);
                        if (status.equalsIgnoreCase("200")) {
                            attendanceArrayList = new ArrayList<>();
                            gridview = myFragmentView.findViewById(R.id.gridView);
                            JSONArray jsonArrayList = obj.getJSONArray("attendence_details");
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String std_id = jsonObject.getString("std_id");
                                String attendence_status = jsonObject.getString("attendence_status");
                                String date = jsonObject.getString("date");
                                String late_reason = jsonObject.getString("late_reason");

                                AttendanceInfo attendanceInfo = new AttendanceInfo(id, std_id, attendence_status, date, late_reason);
                                attendanceArrayList.add(i, attendanceInfo);
                            }
                            month=Calendar.getInstance();
                            adapter = new CalendarAdapter(Objects.requireNonNull(getActivity()).getApplication(), (GregorianCalendar) month, attendanceArrayList);
                            GridView gridview = myFragmentView.findViewById(R.id.gridview);
                            gridview.setAdapter(adapter);
                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                                  ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                                    String selectedGridDate = CalendarAdapter.dayString.get(position);
                                    String[] separatedTime = selectedGridDate.split("-");
                                    String gridvalueString = separatedTime[2].replaceFirst("^0*", "");// taking last part of date. ie; 2 from 2012-12-02.
                                    int gridvalue = Integer.parseInt(gridvalueString);
                                    // navigate to next or previous month on clicking offdays.
                                    if ((gridvalue > 10) && (position < 8)) {
                                        setPreviousMonth();
                                        refreshCalendar();
                                    } else if ((gridvalue < 7) && (position > 28)) {
                                        setNextMonth();
                                        refreshCalendar();
                                    }
//                                  ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                                    showSnackBar(selectedGridDate);
                                }
                            });
                            items = new ArrayList<>();
                            /*
                             * Set current month absent, present, holiday and leave.
                             */
                            setCurrentMonth();

                            Display display = getActivity().getWindowManager().getDefaultDisplay();
                            float width = display.getWidth();
                            TranslateAnimation animation = new TranslateAnimation(-1000, width - 50, 0, 0); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
                            animation.setDuration(2000); // animation duration
                            animation.setRepeatCount(1); // animation repeat count
                            animation.setRepeatMode(2); // repeat animation (left to right, right to left )
                            animation.setFillAfter(true);
                            imageView.startAnimation(animation); // start animation
                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            snackbar.show();
                            messageTextView.setText(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        fab.setVisibility(View.VISIBLE);

                    }
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    pbHeaderProgress.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);

                }
            });
        } catch (Exception e) {
            pbHeaderProgress.setVisibility(View.GONE);
            e.printStackTrace();
            fab.setVisibility(View.VISIBLE);

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

    public void fetchDate() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "get_student_attendence", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    Log.d("AttenFragResponse", "onSuccess: "+obj);
                    if (status.equalsIgnoreCase("200")) {
                        attendanceArrayList = new ArrayList<>();
                        gridview = myFragmentView.findViewById(R.id.gridView);
                        JSONArray jsonArrayList = obj.getJSONArray("attendence_details");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String std_id = jsonObject.getString("std_id");
                            String attendence_status = jsonObject.getString("attendence_status");
                            String date = jsonObject.getString("date");
                            String late_reason = jsonObject.getString("late_reason");

                            AttendanceInfo attendanceInfo = new AttendanceInfo(id, std_id, attendence_status, date, late_reason);
                            attendanceArrayList.add(i, attendanceInfo);
                        }
                        month=Calendar.getInstance();
                        adapter = new CalendarAdapter(Objects.requireNonNull(getActivity()).getApplication(), (GregorianCalendar) month, attendanceArrayList);
                        GridView gridview = myFragmentView.findViewById(R.id.gridview);
                        gridview.setAdapter(adapter);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                                  ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                                String selectedGridDate = CalendarAdapter.dayString.get(position);
                                String[] separatedTime = selectedGridDate.split("-");
                                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");// taking last part of date. ie; 2 from 2012-12-02.
                                int gridvalue = Integer.parseInt(gridvalueString);
                                // navigate to next or previous month on clicking offdays.
                                if ((gridvalue > 10) && (position < 8)) {
                                    setPreviousMonth();
                                    refreshCalendar();
                                } else if ((gridvalue < 7) && (position > 28)) {
                                    setNextMonth();
                                    refreshCalendar();
                                }
//                                  ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                                showSnackBar(selectedGridDate);
                            }
                        });
                        items = new ArrayList<>();
                        /*
                         * Set current month absent, present, holiday and leave.
                         */
                        setCurrentMonth();

                        Display display = getActivity().getWindowManager().getDefaultDisplay();
                        float width = display.getWidth();
                        TranslateAnimation animation = new TranslateAnimation(-1000, width - 50, 0, 0); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
                        animation.setDuration(2000); // animation duration
                        animation.setRepeatCount(1); // animation repeat count
                        animation.setRepeatMode(2); // repeat animation (left to right, right to left )
                        animation.setFillAfter(true);
                        imageView.startAnimation(animation); // start animation
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                        messageTextView.setText(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pbHeaderProgress.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.GONE);
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
