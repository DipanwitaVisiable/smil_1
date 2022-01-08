package com.example.crypedu.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.PathologyServiceListAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PathologySetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

@SuppressLint("Registered")
public class PathologyServiceListActivity extends AppCompatActivity {

    private RecyclerView pathology_recyclerView;
    private ArrayList<PathologySetterGetter> pathologySetterGetters = new ArrayList<>();
    private PathologyServiceListAdapter pathologyAdapter;
    private Context context;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue requestQueue;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology_service_list);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        context = PathologyServiceListActivity.this;
        requestQueue= Volley.newRequestQueue(context);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //  Status bar :: Transparent
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, MediwalletActivity.class);
                //startActivity(intent);
                onBackPressed();
            }
        });

        pathology_recyclerView = findViewById(R.id.pathology_recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        TextView tv_list_of_pathology = findViewById(R.id.tv_list_of_pathology);
        tv_list_of_pathology.setTypeface(typeface);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        pathology_recyclerView.setLayoutManager(layoutManager);
        pathology_recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(pathology_recyclerView, false);

       /*
        fetching the pathology center
      */
       if (InternetCheckActivity.isConnected()) {
           fetchingPathologyList();
       }else {
           showSnack();
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

    @Override
    protected void onResume() {
        super.onResume();
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
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //startActivity(new Intent(context, MediwalletActivity.class));
                onBackPressed();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void fetchingPathologyList(){
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER_MEDIWALLET + "medi_pathology_center",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pbHeaderProgress.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("200")) {
                                Log.e("message PathologyPage", message);
                                JSONArray pathology_list = obj.getJSONArray("pathology_center");
                                pathologySetterGetters.clear();
                                for (int i = 0; i < pathology_list.length(); i++) {
                                    //Log.e("list PathologyPage", String.valueOf(pathology_list));
                                    JSONObject object = pathology_list.getJSONObject(i);
                                    PathologySetterGetter setterGetter = new PathologySetterGetter();
                                    setterGetter.setPathology_name(object.getString("path_name"));
                                    setterGetter.setPathology_address(object.getString("reg_office"));
                                    setterGetter.setPathology_id(object.getString("path_id"));
                                    setterGetter.setPathology_phn(object.getString("ph_no"));
                                    setterGetter.setPathology_image(object.getString("center_img"));
                                    setterGetter.setLab_id(object.getString("lab_no"));
                                    pathologySetterGetters.add(setterGetter);
                                }
                                pathologyAdapter = new PathologyServiceListAdapter(context, pathologySetterGetters);
                                pathology_recyclerView.setAdapter(pathologyAdapter);
                                pathology_recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                                    GestureDetector gestureDetector = new GestureDetector(getApplication(), new GestureDetector.SimpleOnGestureListener() {
                                        @Override
                                        public boolean onSingleTapUp(MotionEvent motionEvent) {
                                            return true;
                                        }

                                    });
                                    @Override
                                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {

                                        View view1 = rv.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                                        if (view1 != null && gestureDetector.onTouchEvent(motionEvent)) {

                                            int recyclerViewPosition = rv.getChildAdapterPosition(view1);
                                            String labId = pathologySetterGetters.get(recyclerViewPosition).getLab_id();
                                            String pathId = pathologySetterGetters.get(recyclerViewPosition).getPathology_id();
                                            Intent intent;
                                            if (labId.equalsIgnoreCase("1")){
                                                intent = new Intent(PathologyServiceListActivity.this, PathologyLabActivity.class);
                                                //intent = new Intent(PathologyServiceListActivity.this, PathologyServiceActivity.class);

                                            }else {
                                                intent = new Intent(PathologyServiceListActivity.this, PathologyServiceActivity.class);
                                            }
                                            Constants.PATHOLOGY_SRVS_ID = pathId;
                                            intent.putExtra("PATH_ID", pathId);
                                            intent.putExtra("LAB_ID", labId);
                                            startActivity(intent);

                                        }
                                        return false;
                                    }

                                    @Override
                                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                                    }

                                    @Override
                                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                                    }
                                });
                            } else {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.parseColor(Constants.colorAccent));
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            pbHeaderProgress.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbHeaderProgress.setVisibility(View.GONE);
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }
}
