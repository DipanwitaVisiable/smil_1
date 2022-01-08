package com.example.crypedu.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.example.crypedu.Activity.AssignmentTopicListActivity;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.NewBusMapActivity;
import com.activity.smi.NewBusMapDirectActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Activity.AccountActivity;
import com.example.crypedu.Activity.AttendanceActivity;
import com.example.crypedu.Activity.AttendanceDirectActivity;
import com.example.crypedu.Activity.BDMActivity;
import com.example.crypedu.Activity.BulletinsActivity;
import com.example.crypedu.Activity.ExaminationActivity;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Activity.NoticeActivity;
import com.example.crypedu.Activity.NoticeDirectActivity;
import com.example.crypedu.Activity.OnlineTestActivity;
import com.example.crypedu.Activity.PdfSyllabusActivity;
import com.example.crypedu.Activity.PdfSyllabusDirectActivity;
import com.example.crypedu.Activity.ProfileActivity;
import com.example.crypedu.Activity.RequestActivity;
import com.example.crypedu.Activity.ResetPasswordActivity;
import com.example.crypedu.Activity.ResultActivity;
import com.example.crypedu.Activity.SmartClassActivity;
import com.example.crypedu.Activity.TeacherNoticeActivity;
import com.example.crypedu.Activity.TeacherSyllabusActivity;
import com.example.crypedu.Activity.TimeTableActivity;
import com.example.crypedu.Activity.TimeTableDirectActivity;
import com.example.crypedu.Activity.ViewRequestDirectActivity;
import com.example.crypedu.Activity.WorkActivity;
import com.example.crypedu.Adapter.MainViewAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.BannerInfo;
import com.example.crypedu.Pojo.ListSetGet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;


public class SMIMainFragment extends Fragment {
    private LinearLayout ll_rating_section;
    private String rating;
    private RequestQueue requestQueue;
    private RatingBar rb_ratingBar;
    private Button btn_submit_rating;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
    /*
     * Banner
     */
    ViewFlipper bannerflipper;
    private final ThreadLocal<Animation> slide_in_right = new ThreadLocal<>();
    private Animation slide_out_right;
    private Animation.AnimationListener mAnimationListener;
    private View myFragmentView;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<BannerInfo> bannerInfoArrayList = new ArrayList<>();
    RecyclerView rv_list;
    ArrayList<ListSetGet> viewList = new ArrayList<>();
    MainViewAdapter mainViewAdapter;

    public SMIMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_smimain, container, false);
        rv_list = myFragmentView.findViewById(R.id.rv_list);
        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);
        ll_rating_section = myFragmentView.findViewById(R.id.ll_rating_section);
        btn_submit_rating = myFragmentView.findViewById(R.id.btn_submit_rating);
        rb_ratingBar = myFragmentView.findViewById(R.id.rb_ratingBar);

        requestQueue = Volley.newRequestQueue(getActivity());

        // To showing rating section in home screen
        if (Constants.USER_RATING.equals("0"))
            ll_rating_section.setVisibility(View.VISIBLE);
        else
            ll_rating_section.setVisibility(View.GONE);

        btn_submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = String.valueOf(rb_ratingBar.getRating());
                if (rating.equals("0.0")) {
                    showAlertWithSingleButton();
//                    Toast.makeText(getActivity(), "Please select your rating first", Toast.LENGTH_SHORT).show();
                } else {
                    showAlertForRating();
                }
            }
        });


        if (Constants.USER_ROLE != null || !Constants.USER_ROLE.equalsIgnoreCase("")) {
            if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                if (InternetCheckActivity.isConnected()) {
                    fetchNotificationCount();

                } else {
                    showSnack();
                }

            } else if (Constants.USER_ROLE.equalsIgnoreCase("d")) {
                getPopulateDirectorList();

            } else if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                // Toast.makeText(getContext(), "Nothing", Toast.LENGTH_SHORT).show();
                getPopulateTeacherList();
            }

        }
         /*
          Check internet connection if available then only
          fetch banner details otherwise not.
         */


        /*
         * Banner on touch event
         */
        bannerflipper = myFragmentView.findViewById(R.id.view_flipper);
        if (InternetCheckActivity.isConnected()) {
            fetchBannerDetails();

        } else {
            showSnack();
        }
        Animation slide_in_left = AnimationUtils.loadAnimation(getContext(), R.anim.left_in);
        Animation slide_out_left = AnimationUtils.loadAnimation(getContext(), R.anim.left_out);
        bannerflipper.setAutoStart(true);
        bannerflipper.setFlipInterval(4000);
        bannerflipper.startFlipping();
        bannerflipper.setInAnimation(slide_in_left);
        bannerflipper.setOutAnimation(slide_out_left);
        bannerflipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
        mainViewAdapter = new MainViewAdapter(getActivity(), viewList);
        RecyclerView.LayoutManager layoutManager_join = new GridLayoutManager(getContext(), 2);
        rv_list.setLayoutManager(layoutManager_join);
        rv_list.setAdapter(mainViewAdapter);

        return myFragmentView;
    }


    /*
     * Teacher List
     */
    private void getPopulateTeacherList() {
        viewList.clear();
        viewList.add(new ListSetGet(R.drawable.ic_profile, "Profile", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_work, "Assignment", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_attendance, "Attendance", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_notice, "Notice", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_bulletins, "Bulletins", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_syllabus, "Syllabus", "0"));
//        viewList.add(new ListSetGet(R.drawable.ic_library, "Library", "0"));
        viewList.add(new ListSetGet(R.drawable.wallet_add, "Reset", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_account, "Logout", "0"));
        rv_list.setAdapter(mainViewAdapter);
        rv_list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View view1 = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (view1 != null && gestureDetector.onTouchEvent(motionEvent)) {

                    int recyclerViewPosition = recyclerView.getChildAdapterPosition(view1);
                    Intent intent = null;
                    switch (viewList.get(recyclerViewPosition).getMainName()) {
                        case "Attendance":
                            intent = new Intent(getContext(), AttendanceActivity.class);
                            break;
                        case "Profile":
                            intent = new Intent(getContext(), ProfileActivity.class);
                            break;
                        case "Assignment":
                            intent = new Intent(getContext(), WorkActivity.class);
                            break;
                        case "Notice":
                            intent = new Intent(getContext(), TeacherNoticeActivity.class);
                            break;
                        case "Bulletins":
                            intent = new Intent(getContext(), BulletinsActivity.class);
                            break;
                        case "Syllabus":
                            intent = new Intent(getContext(), TeacherSyllabusActivity.class);
                            break;
                        /*case "Library":
                            intent = new Intent(getContext(), BookManageActivity.class);
                            break;*/
                        case "Reset":
                            intent = new Intent(getContext(), ResetPasswordActivity.class);
                            break;
                        case "Logout":
                            logoutAlert();
                            break;
                    }
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

    }

    /*
     * Director List
     */
    private void getPopulateDirectorList() {
        viewList.clear();
        viewList.add(new ListSetGet(R.drawable.ic_profile, "Profile", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_account, "Pay Online", "0"));
//        viewList.add(new ListSetGet(R.drawable.ic_healthcard_big,"Live Class", "0" ));
        viewList.add(new ListSetGet(R.drawable.live_class_bg, "Online Classes", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_work, "Assignment", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_attendance, "Attendance", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_timetable, "Time Table", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_notice, "Notice", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_bulletins, "Bulletins", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_syllabus, "Syllabus", "0"));
//        viewList.add(new ListSetGet(R.drawable.ic_library, "Library", "0"));
//        viewList.add(new ListSetGet(R.drawable.wallet_add, "Mediwallet", "0"));
        viewList.add(new ListSetGet(R.drawable.classroom_exam, "Classroom Exam", "0"));
        viewList.add(new ListSetGet(R.drawable.online_test_bg, "Online Test", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_exam, "Examination", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_result, "Results", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_request, "Communication", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_transportation, "Transportation", "0"));
        viewList.add(new ListSetGet(R.drawable.ic_meeting_details, "PTM", "0"));
        rv_list.setAdapter(mainViewAdapter);
        rv_list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View view1 = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (view1 != null && gestureDetector.onTouchEvent(motionEvent)) {

                    int recyclerViewPosition = recyclerView.getChildAdapterPosition(view1);
                    Intent intent = null;
                    switch (viewList.get(recyclerViewPosition).getMainName()) {
                        case "Attendance":
                            intent = new Intent(getContext(), AttendanceDirectActivity.class);
                            break;
                        /*case "Mediwallet":
                            intent = new Intent(getContext(), MediwalletActivity.class);
                            break;*/
                        case "Profile":
                            intent = new Intent(getContext(), ProfileActivity.class);
                            break;
                        case "Time Table":
                            intent = new Intent(getContext(), TimeTableDirectActivity.class);
                            break;
                        case "Assignment":
                            intent = new Intent(getContext(), WorkActivity.class);
                            break;
                        case "Notice":
                            intent = new Intent(getContext(), NoticeDirectActivity.class);
                            break;
                        case "Bulletins":
                            intent = new Intent(getContext(), BulletinsActivity.class);
                            break;
                        case "Syllabus":
                            intent = new Intent(getContext(), PdfSyllabusDirectActivity.class);
                            break;
                        /*case "Library":
                            intent = new Intent(getContext(), BookManageActivity.class);
                            break;*/
                        case "Pay Online":
//                            intent = new Intent(getContext(), AccountActivity.class);
                            if (!Constants.PAY_ONLINE_URL.equals("")) {
                                Uri uri = Uri.parse(Constants.PAY_ONLINE_URL);
                                Intent intent_1 = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent_1);
                            }
                            break;
                        case "Communication":
                            intent = new Intent(getContext(), ViewRequestDirectActivity.class);
                            break;
                        case "Transportation":
                            intent = new Intent(getContext(), NewBusMapDirectActivity.class);
                            break;
                        case "Examination":
                            intent = new Intent(getContext(), ExaminationActivity.class);
                            break;
                        case "Results":
                            intent = new Intent(getContext(), ResultActivity.class);
                            break;
                        case "Online Classes":
//                            intent = new Intent(getContext(), HealthCardActivity.class);
                            intent = new Intent(getContext(), SmartClassActivity.class);
                            break;
                        case "Online Test":
                            intent = new Intent(getContext(), OnlineTestActivity.class);
                            break;
                        case "Classroom Exam":
                            intent = new Intent(getContext(), AssignmentTopicListActivity.class);
                            break;
                        case "PTM":
                            intent = new Intent(getContext(), BDMActivity.class);
                            break;
                    }
                    if (intent != null) {
                        startActivity(intent);
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

    }

    private void fetchNotificationCount_old() {
        try {
            final RequestParams params = new RequestParams();
            params.put("student_id", Constants.USER_ID);
            Log.d("NotiValue", "getNoti: " + params);
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.post(Constants.BASE_SERVER + "assignment_count", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        String status = obj.getString("status");
                        //String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONObject jsonObject = obj.getJSONObject("count");
                            Log.d("Count", "onSuccess: " + obj.getJSONObject("count"));
                            viewList.clear();
                            viewList.add(new ListSetGet(R.drawable.ic_profile, "Profile", "0"));
                            viewList.add(new ListSetGet(R.drawable.ic_account, "Pay Online", "0"));
//                            viewList.add(new ListSetGet(R.drawable.ic_healthcard_big,"Live Class", "0" ));
                            viewList.add(new ListSetGet(R.drawable.live_class_bg, "Online Classes", "0"));
                            viewList.add(new ListSetGet(R.drawable.ic_work, "Assignment", jsonObject.getString("assignment_count")));
                            viewList.add(new ListSetGet(R.drawable.ic_attendance, "Attendance", jsonObject.getString("attendance_count")));
                            viewList.add(new ListSetGet(R.drawable.ic_timetable, "Time Table", "0"));
                            viewList.add(new ListSetGet(R.drawable.ic_notice, "Notice", jsonObject.getString("notice_count")));
                            viewList.add(new ListSetGet(R.drawable.ic_bulletins, "Bulletins", jsonObject.getString("bulletin_count")));
                            viewList.add(new ListSetGet(R.drawable.ic_syllabus, "Syllabus", "0"));
//                            viewList.add(new ListSetGet(R.drawable.ic_library, "Library", "0"));
//                            viewList.add(new ListSetGet(R.drawable.wallet_add, "Mediwallet", "0"));
                            viewList.add(new ListSetGet(R.drawable.classroom_exam, "Classroom Exam", "0"));
                            viewList.add(new ListSetGet(R.drawable.online_test_bg, "Online Test", "0"));
                            viewList.add(new ListSetGet(R.drawable.ic_exam, "Examination", jsonObject.getString("exam_count")));
                            viewList.add(new ListSetGet(R.drawable.ic_result, "Results", "0"));
                            viewList.add(new ListSetGet(R.drawable.ic_request, "Communication", jsonObject.getString("communication_count")));
                            viewList.add(new ListSetGet(R.drawable.ic_transportation, "Transportation", "0"));
                            viewList.add(new ListSetGet(R.drawable.ic_meeting_details, "PTM", "0"));
                            rv_list.setAdapter(mainViewAdapter);
                            //mainViewAdapter.notifyDataSetChanged();
                            rv_list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                                GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                                    @Override
                                    public boolean onSingleTapUp(MotionEvent motionEvent) {
                                        return true;
                                    }

                                });

                                @Override
                                public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                                    View view1 = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                                    if (view1 != null && gestureDetector.onTouchEvent(motionEvent)) {

                                        int recyclerViewPosition = recyclerView.getChildAdapterPosition(view1);
                                        Intent intent = null;
                                        switch (viewList.get(recyclerViewPosition).getMainName()) {
                                            case "Attendance":
                                                intent = new Intent(getContext(), AttendanceActivity.class);
                                                break;
                                            /*case "Mediwallet":
                                                intent = new Intent(getContext(), MediwalletActivity.class);
                                                break;*/
                                            case "Profile":
                                                intent = new Intent(getContext(), ProfileActivity.class);
                                                break;
                                            case "Time Table":
                                                intent = new Intent(getContext(), TimeTableActivity.class);
                                                break;
                                            case "Assignment":
                                                intent = new Intent(getContext(), WorkActivity.class);
                                                break;
                                            case "Notice":
                                                intent = new Intent(getContext(), NoticeActivity.class);
                                                break;
                                            case "Bulletins":
                                                intent = new Intent(getContext(), BulletinsActivity.class);
                                                break;
                                            case "Syllabus":
                                                intent = new Intent(getContext(), PdfSyllabusActivity.class);
                                                break;
                                            /*case "Library":
                                                intent = new Intent(getContext(), BookManageActivity.class);
                                                break;*/
                                            case "Pay Online":
//                                                intent = new Intent(getContext(), AccountActivity.class);
                                                if (!Constants.PAY_ONLINE_URL.equals("")) {
                                                    Uri uri = Uri.parse(Constants.PAY_ONLINE_URL);
                                                    Intent intent_1 = new Intent(Intent.ACTION_VIEW, uri);
                                                    startActivity(intent_1);
                                                }
                                                break;
                                            case "Communication":
                                                intent = new Intent(getContext(), RequestActivity.class);
                                                break;
                                            case "Transportation":
                                                intent = new Intent(getContext(), NewBusMapActivity.class);
                                                break;
                                            case "Examination":
                                                intent = new Intent(getContext(), ExaminationActivity.class);
                                                break;
                                            case "Results":
                                                intent = new Intent(getContext(), ResultActivity.class);
                                                break;
                                            case "Online Classes":
//                                                intent = new Intent(getContext(), HealthCardActivity.class);
                                                intent = new Intent(getContext(), SmartClassActivity.class);
                                                break;
                                            case "Online Test":
                                                intent = new Intent(getContext(), OnlineTestActivity.class);
                                                break;
                                            case "Classroom Exam":
                                                intent = new Intent(getContext(), AssignmentTopicListActivity.class);
                                                break;
                                            case "PTM":
                                                intent = new Intent(getContext(), BDMActivity.class);
                                                break;
                                        }
                                        if (intent != null) {
                                            startActivity(intent);
                                        }
                                    }
                                    return false;
                                }

                                @Override
                                public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                                }

                                @Override
                                public void onRequestDisallowInterceptTouchEvent(boolean b) {

                                }
                            });


                            //Toast.makeText(getActivity(), ""+viewList.size(), Toast.LENGTH_SHORT).show();
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
            Constants.USER_RATING = pref.getString("rating", "");
        } else {
            Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();

        }

    }

    private void showSnack() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }


    //-----------------------
    // for logout.
    //-----------------------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loginReset() {
        SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences("VALUE", Context.MODE_PRIVATE).edit();
        editor.putString("UID", "");
        editor.putString("PWD", "");
        editor.apply();

        SharedPreferences pref = this.getActivity().getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_executed", false);
        edt.putString("student_id", "");
        edt.putString("user_role", "");
        edt.putString("profile_name", "");
        edt.putString("phoneNo", "");
        edt.apply();
    }

    /**
     * Fetch banner image from server.
     */
    public void fetchBannerDetails_old() {

        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.get(Constants.BASE_SERVER + "select_banner", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArrayBanner = obj.getJSONArray("banner_details");
                            bannerInfoArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArrayBanner.length(); i++) {
                                JSONObject jsonObject = jsonArrayBanner.getJSONObject(i);
                                String bannerId = jsonObject.getString("id");
                                String bannerImage = jsonObject.getString("banner_url");

                                BannerInfo bannerInfo = new BannerInfo(bannerId, bannerImage);
                                bannerInfoArrayList.add(i, bannerInfo);
                            }
                            for (int i = 0; i < bannerInfoArrayList.size(); i++) {
                                ImageView image = new ImageView(getContext());
                                image.setScaleType(ImageView.ScaleType.FIT_XY);
                                Picasso.with(getContext())
                                        .load(bannerInfoArrayList.get(i).bannerImage)
                                        .placeholder(R.drawable.placeholder)
                                        .noFade()
                                        .into(image);
                                bannerflipper.addView(image);
                            }
                        } else {
                            /*Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            //snackbar.show();*/
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

    /*
     * Banner gesture
     */
    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    bannerflipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.left_in));
                    bannerflipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.left_out));
                    // controlling animation
                    bannerflipper.getInAnimation().setAnimationListener(mAnimationListener);
                    bannerflipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    bannerflipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.right_in));
                    bannerflipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.right_out));
                    // controlling animation
                    bannerflipper.getInAnimation().setAnimationListener(mAnimationListener);
                    bannerflipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    //-----------------------
    // Alert dialog box
    // for logout.
    //-----------------------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage("Do you want to logout?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int id) {
                loginReset();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.colorAccent));
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.colorAccent));
    }


    private void submitRatingApiCall() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "submit_rating", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    pb_loader.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200")) {
                        Toast.makeText(getActivity(), "Thank you for your rating", Toast.LENGTH_SHORT).show();
                        ll_rating_section.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "Sorry!!..Try Again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    pb_loader.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pb_loader.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Constants.USER_ID);
                params.put("rating", rating);
                Log.e("STUDENT ID", Constants.USER_ID);
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
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        // To showing rating section in home screen
        if (Constants.USER_RATING.equals("0"))
            ll_rating_section.setVisibility(View.VISIBLE);
        else
            ll_rating_section.setVisibility(View.GONE);
    }

    public void showAlertForRating() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure to submit your rating?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        submitRatingApiCall();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showAlertWithSingleButton() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Please select your rating first.");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void fetchNotificationCount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "assignment_count", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    //String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject = obj.getJSONObject("count");
                        Log.d("Count", "onSuccess: " + obj.getJSONObject("count"));
                        viewList.clear();
                        viewList.add(new ListSetGet(R.drawable.ic_profile, "Profile", "0"));
                        viewList.add(new ListSetGet(R.drawable.ic_account, "Pay Online", "0"));
//                            viewList.add(new ListSetGet(R.drawable.ic_healthcard_big,"Live Class", "0" ));
                        viewList.add(new ListSetGet(R.drawable.live_class_bg, "Online Classes", "0"));
                        viewList.add(new ListSetGet(R.drawable.ic_work, "Assignment", jsonObject.getString("assignment_count")));
                        viewList.add(new ListSetGet(R.drawable.ic_attendance, "Attendance", jsonObject.getString("attendance_count")));
                        viewList.add(new ListSetGet(R.drawable.ic_timetable, "Time Table", "0"));
                        viewList.add(new ListSetGet(R.drawable.ic_notice, "Notice", jsonObject.getString("notice_count")));
                        viewList.add(new ListSetGet(R.drawable.ic_bulletins, "Bulletins", jsonObject.getString("bulletin_count")));
                        viewList.add(new ListSetGet(R.drawable.ic_syllabus, "Syllabus", "0"));
//                            viewList.add(new ListSetGet(R.drawable.ic_library, "Library", "0"));
//                            viewList.add(new ListSetGet(R.drawable.wallet_add, "Mediwallet", "0"));
                        viewList.add(new ListSetGet(R.drawable.classroom_exam, "Classroom Exam", "0"));
                        viewList.add(new ListSetGet(R.drawable.online_test_bg, "Online Test", "0"));
                        viewList.add(new ListSetGet(R.drawable.ic_exam, "Examination", jsonObject.getString("exam_count")));
                        viewList.add(new ListSetGet(R.drawable.ic_result, "Results", "0"));
                        viewList.add(new ListSetGet(R.drawable.ic_request, "Communication", jsonObject.getString("communication_count")));
                        viewList.add(new ListSetGet(R.drawable.ic_transportation, "Transportation", "0"));
                        viewList.add(new ListSetGet(R.drawable.ic_meeting_details, "PTM", "0"));
                        rv_list.setAdapter(mainViewAdapter);
                        //mainViewAdapter.notifyDataSetChanged();
                        rv_list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                                @Override
                                public boolean onSingleTapUp(MotionEvent motionEvent) {
                                    return true;
                                }

                            });

                            @Override
                            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                                View view1 = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                                if (view1 != null && gestureDetector.onTouchEvent(motionEvent)) {

                                    int recyclerViewPosition = recyclerView.getChildAdapterPosition(view1);
                                    Intent intent = null;
                                    switch (viewList.get(recyclerViewPosition).getMainName()) {
                                        case "Attendance":
                                            intent = new Intent(getContext(), AttendanceActivity.class);
                                            break;
                                            /*case "Mediwallet":
                                                intent = new Intent(getContext(), MediwalletActivity.class);
                                                break;*/
                                        case "Profile":
                                            intent = new Intent(getContext(), ProfileActivity.class);
                                            break;
                                        case "Time Table":
                                            intent = new Intent(getContext(), TimeTableActivity.class);
                                            break;
                                        case "Assignment":
                                            intent = new Intent(getContext(), WorkActivity.class);
                                            break;
                                        case "Notice":
                                            intent = new Intent(getContext(), NoticeActivity.class);
                                            break;
                                        case "Bulletins":
                                            intent = new Intent(getContext(), BulletinsActivity.class);
                                            break;
                                        case "Syllabus":
                                            intent = new Intent(getContext(), PdfSyllabusActivity.class);
                                            break;
                                            /*case "Library":
                                                intent = new Intent(getContext(), BookManageActivity.class);
                                                break;*/
                                        case "Pay Online":
//                                            intent = new Intent(getContext(), AccountActivity.class);
                                            if (!Constants.PAY_ONLINE_URL.equals("")) {
                                                Uri uri = Uri.parse(Constants.PAY_ONLINE_URL);
                                                Intent intent_1 = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(intent_1);
                                            }
                                            break;
                                        case "Communication":
                                            intent = new Intent(getContext(), RequestActivity.class);
                                            break;
                                        case "Transportation":
                                            intent = new Intent(getContext(), NewBusMapActivity.class);
                                            break;
                                        case "Examination":
                                            intent = new Intent(getContext(), ExaminationActivity.class);
                                            break;
                                        case "Results":
                                            intent = new Intent(getContext(), ResultActivity.class);
                                            break;
                                        case "Online Classes":
//                                                intent = new Intent(getContext(), HealthCardActivity.class);
                                            intent = new Intent(getContext(), SmartClassActivity.class);
                                            break;
                                        case "Online Test":
                                            intent = new Intent(getContext(), OnlineTestActivity.class);
                                            break;
                                        case "Classroom Exam":
                                            intent = new Intent(getContext(), AssignmentTopicListActivity.class);
                                            break;
                                        case "PTM":
                                            intent = new Intent(getContext(), BDMActivity.class);
                                            break;
                                    }
                                    if (intent != null) {
                                        startActivity(intent);
                                    }
                                }
                                return false;
                            }

                            @Override
                            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean b) {

                            }
                        });


                        //Toast.makeText(getActivity(), ""+viewList.size(), Toast.LENGTH_SHORT).show();
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

    public void fetchBannerDetails() {
        final ProgressBar pbHeaderProgress = myFragmentView.findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "select_banner", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArrayBanner = obj.getJSONArray("banner_details");
                        bannerInfoArrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArrayBanner.length(); i++) {
                            JSONObject jsonObject = jsonArrayBanner.getJSONObject(i);
                            String bannerId = jsonObject.getString("id");
                            String bannerImage = jsonObject.getString("banner_url");

                            BannerInfo bannerInfo = new BannerInfo(bannerId, bannerImage);
                            bannerInfoArrayList.add(i, bannerInfo);
                        }
                        for (int i = 0; i < bannerInfoArrayList.size(); i++) {
                            ImageView image = new ImageView(getContext());
                            image.setScaleType(ImageView.ScaleType.FIT_XY);
                            Picasso.with(getContext())
                                    .load(bannerInfoArrayList.get(i).bannerImage)
                                    .placeholder(R.drawable.placeholder)
                                    .noFade()
                                    .into(image);
                            bannerflipper.addView(image);
                        }
                    } else {
                            /*Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.parseColor(Constants.colorAccent));
                            //snackbar.show();*/
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
