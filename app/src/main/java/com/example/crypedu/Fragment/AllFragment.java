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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Activity.AssignmentTopicListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.NewBusMapActivity;
import com.activity.smi.NewBusMapDirectActivity;
import com.activity.smi.R;
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
import com.example.crypedu.Adapter.ImageAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.BannerInfo;
import com.key.Key;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class AllFragment extends Fragment {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
    /*
     * Banner
     */
    ViewFlipper bannerflipper;
    /**
     *
     */
    private final ThreadLocal<Animation> slide_in_right = new ThreadLocal<>();
    private Animation slide_out_right;
    private Animation.AnimationListener mAnimationListener;
    private View myFragmentView;
    private CoordinatorLayout coordinatorLayout;
    private ArrayList<BannerInfo> bannerInfoArrayList = new ArrayList<>();
    String student_id;

    public AllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_one, container, false);
        myFragmentView = inflater.inflate(R.layout.fragment_all, container, false);
        GridView grid = myFragmentView.findViewById(R.id.gridview);
        coordinatorLayout = myFragmentView.findViewById(R.id.coordinatorLayout);

        if (Constants.USER_ROLE != null || !Constants.USER_ROLE.equalsIgnoreCase("")) {
            if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                // Keep all Images in array
                Integer[] mThumbIds_menu = {
                        R.drawable.ic_profile, R.drawable.ic_work,
                        R.drawable.ic_attendance, R.drawable.ic_notice, R.drawable.ic_bulletins,
                        R.drawable.ic_syllabus, R.drawable.ic_library, R.drawable.ic_reset, R.drawable.ic_logout
                };
                // Keep all Text in array
                final String[] mThumbIds_text = {
                        "Profile", "Assignment", "Attendance", "Notice", "Bulletins",
                        "Syllabus", /*"Library",*/ "Reset", "Logout"
                };

                grid.setAdapter(new ImageAdapter(getContext(), mThumbIds_menu, mThumbIds_text));
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String itemText = mThumbIds_text[position];
                        Intent intent = null;
                        switch (itemText) {
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
                });
            } else if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                // Keep all Images in array
                Integer[] mThumbIds_menu = {
                        R.drawable.ic_profile, R.drawable.ic_account, R.drawable.live_class_bg, R.drawable.ic_work,
                        R.drawable.ic_attendance, R.drawable.ic_timetable, R.drawable.ic_notice, R.drawable.ic_bulletins,
                        R.drawable.ic_syllabus, R.drawable.ic_library, R.drawable.wallet_add, R.drawable.ic_exam, R.drawable.ic_result, R.drawable.ic_request, R.drawable.ic_transportation,
                        R.drawable.ic_meeting_details
                };

                // Keep all Text in array
                final String[] mThumbIds_text = {
                        "Profile", "Pay Online", "Online Classes", "Assignment", "Attendance",
                        "Time Table", "Notice", "Bulletins", "Syllabus", /*"Library",*/
                        /*"Mediwallet",*/"Classroom Exam", "Online Test", "Examination", "Results",
                        "Communication", "Transportation", "PTM"};

                grid.setAdapter(new ImageAdapter(getContext(), mThumbIds_menu, mThumbIds_text));
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String itemText = mThumbIds_text[position];
                        Intent intent = null;
                        switch (itemText) {
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
//                                intent = new Intent(getContext(), AccountActivity.class);
                                if (!Constants.PAY_ONLINE_URL.equals("")) {
                                    Uri uri = Uri.parse(Constants.PAY_ONLINE_URL);
                                    Intent intent_1 = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent_1);
                                }
                                break;
                            case "Online Test":
                                intent = new Intent(getContext(), OnlineTestActivity.class);
                                break;
                            case "Classroom Exam":
                                intent = new Intent(getContext(), AssignmentTopicListActivity.class);
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
//                                intent = new Intent(getContext(), HealthCardActivity.class);
                                intent = new Intent(getContext(), SmartClassActivity.class);
                                break;
                            case "PTM":
                                intent = new Intent(getContext(), BDMActivity.class);
                                break;
                        }
                        if (intent != null) {
                            startActivity(intent);
                        }
                    }
                });
            } else if (Constants.USER_ROLE.equalsIgnoreCase("d")) {
                // Keep all Images in array
                Integer[] mThumbIds_menu = {
                        R.drawable.ic_profile, R.drawable.ic_account, R.drawable.live_class_bg, R.drawable.ic_work,
                        R.drawable.ic_attendance, R.drawable.ic_timetable, R.drawable.ic_notice, R.drawable.ic_bulletins,
                        R.drawable.ic_syllabus, R.drawable.ic_library, R.drawable.wallet_add, R.drawable.ic_exam, R.drawable.ic_result, R.drawable.ic_request, R.drawable.ic_transportation,
                        R.drawable.ic_meeting_details
                };

                // Keep all Text in array
                final String[] mThumbIds_text = {
                        "Profile", "Pay Online", "Online Classes", "Assignment", "Attendance", "Time Table", "Notice", "Bulletins", "Syllabus", /*"Library",*/ /*"Mediwallet",*/ "Classroom Exam", "Online Test", "Examination", "Results",
                        "Communication", "Transportation", "PTM"};

                grid.setAdapter(new ImageAdapter(getContext(), mThumbIds_menu, mThumbIds_text));
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String itemText = mThumbIds_text[position];
                        Intent intent = null;
                        switch (itemText) {
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
//                                intent = new Intent(getContext(), AccountActivity.class);
                                if (!Constants.PAY_ONLINE_URL.equals("")) {
                                    Uri uri = Uri.parse(Constants.PAY_ONLINE_URL);
                                    Intent intent_1 = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent_1);
                                }
                                break;
                            case "Online Test":
                                intent = new Intent(getContext(), OnlineTestActivity.class);
                                break;
                            case "Classroom Exam":
                                intent = new Intent(getContext(), AssignmentTopicListActivity.class);
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
                                intent = new Intent(getContext(), SmartClassActivity.class);
                                break;
                            case "PTM":
                                intent = new Intent(getContext(), BDMActivity.class);
                                break;
                        }
                        if (intent != null) {
                            startActivity(intent);
                        }

                    }
                });
            }
        }

        /*
         * Banner on touch event
         */
        bannerflipper = myFragmentView.findViewById(R.id.view_flipper);
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


        FloatingActionButton fab = myFragmentView.findViewById(R.id.fabOne);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onStart();
                fetchBannerDetails();
                assert getFragmentManager() != null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(AllFragment.this).attach(AllFragment.this).commit();
            }
        });

        student_id = getActivity().getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE).getString("student_id", null);

        /*
          Check internet connection if available then only
          fetch banner details otherwise not.
         */
        if (InternetCheckActivity.isConnected()) {
            fetchBannerDetails();
            //fetchNotificationCount();
        } else {
            showSnack();
        }


        return myFragmentView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_messages:

                // Not implemented here
                return false;
            case R.id.refresh:
                fetchBannerDetails();
                assert getFragmentManager() != null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(AllFragment.this).attach(AllFragment.this).commit();
                // Do Fragment menu item stuff here
                return true;

            default:
                break;
        }

        return false;
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

    private void showSnack() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
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
                        pbHeaderProgress.setVisibility(View.GONE);
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
