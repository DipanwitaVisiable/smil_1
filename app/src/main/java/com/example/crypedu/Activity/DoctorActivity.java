package com.example.crypedu.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.example.crypedu.Adapter.DoctorAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.DoctorSetterGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DoctorActivity extends AppCompatActivity {
    private RecyclerView doctor_recyclerView;
    private ArrayList<DoctorSetterGetter> doctorSetterGetters = new ArrayList<>();
    private DoctorAdapter doctorAdapter;
    private CoordinatorLayout coordinatorLayout;
    private Context context;
    private RequestQueue requestQueue;

    private Button showPopupBtn;
    private PopupWindow popupWindow;
    private ArrayList<String> arrayList;

    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 100;
    private static String[] PERMISSIONS_PHONECALL = {Manifest.permission.CALL_PHONE};
    private String selectedNumber;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
       /* if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = DoctorActivity.this;
        requestQueue = Volley.newRequestQueue(context);
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
                Intent intent = new Intent(context, MediDepartmentActivity.class);
                startActivity(intent);
            }
        });

        doctor_recyclerView = findViewById(R.id.doctor_recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        TextView tv_list_of_doctor = findViewById(R.id.tv_list_of_doctor);
        tv_list_of_doctor.setTypeface(typeface);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        doctor_recyclerView.setLayoutManager(layoutManager);

//        int resId = R.anim.slide_from_bottom;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
//        doctor_recyclerView.setLayoutAnimation(animation);


      /*
      fetching the doctors
      */

      if (InternetCheckActivity.isConnected()) {
          fetchingDoctorList();
      }else {
          showSnack();
      }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    Snackbar.make(view, "Already opened the Window.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "Choose Number for calling", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    PopUpWindowForCall();
                }

            }
        });
    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.item_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    /*
      Doctor list fetching method
    */

    public void fetchingDoctorList() {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER_MEDIWALLET + "medi_doctor_deltails",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pbHeaderProgress.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            Log.e(" doctor message", message);
                            if (status.equalsIgnoreCase("200")) {
                                String phone_number = obj.getString("chamber_phone");// The String to be split
                                String[] numbers = phone_number.split("/"); // Split according to the hyphen and put them in an array
                                arrayList = new ArrayList<>();
                                for (String subString : numbers) { // Cycle through the array
                                    System.out.println(subString);
                                    arrayList.add(subString);
                                    Log.e("PHONE NUMBERS GETTING", subString);
                                    Log.e("Arraylist", arrayList.toString());
                                }

                                JSONArray doctor_list = obj.getJSONArray("doctor_details");
                                Log.e(" doctor list", String.valueOf(doctor_list));
                                doctorSetterGetters.clear();
                                for (int i = 0; i < doctor_list.length(); i++) {
                                    JSONObject object = doctor_list.getJSONObject(i);
                                    DoctorSetterGetter setterGetter = new DoctorSetterGetter();
                                    setterGetter.setDoctor_name(object.getString("doctor_name"));
                                    setterGetter.setDoctor_cat_name(object.getString("cat_name"));
                                    setterGetter.setDoctor_degree(object.getString("doctor_degree"));
                                    setterGetter.setDoctor_discount(object.getString("doctor_discount"));
                                    setterGetter.setDoctor_image(object.getString("doctor_image"));
                                    doctorSetterGetters.add(setterGetter);
                                }
                                doctorAdapter = new DoctorAdapter(context, doctorSetterGetters);
                                doctor_recyclerView.setAdapter(doctorAdapter);


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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("chamber_id", Constants.CHAMBER_ID);
                params.put("doc_dept", Constants.DEPARTMENT_ID);
                //Log.e("CHAMBER_ID put in dr", Constants.CHAMBER_ID);
                //Log.e("DEPARTMENT_ID put in dr", Constants.DEPARTMENT_ID);
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
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    public void PopUpWindowForCall() {
        //instantiate the popup.xml layout file
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        @SuppressLint("InflateParams") View customView = layoutInflater.inflate(R.layout.popup_window_doctor, null);
        ImageView closePopupBtn = customView.findViewById(R.id.closePopupBtn);
        ListView listView_number = customView.findViewById(R.id.listView_number);

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayList);
        // Set The Adapter
        listView_number.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        listView_number.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                selectedNumber = arrayList.get(position);
                Toast.makeText(getApplicationContext(), "Calling : " + selectedNumber, Toast.LENGTH_LONG).show();
                call();
//                try {
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        callIntent.setData(Uri.parse("tel:" + selectedNumber));
//                        startActivity(callIntent);
//                    }
//
//
//                } catch (ActivityNotFoundException activityException) {
//                    Log.e("Calling a Phone Number", "Call failed", activityException);
//                }
            }
        });

        //instantiate popup window
        popupWindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.DialogAnimation_2);
        //display the popup window
        popupWindow.showAtLocation(coordinatorLayout, Gravity.CENTER, 0, 0);
        //close the popup window on button click


        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return true;
                } else {
                    startActivity(new Intent(this, MediDepartmentActivity.class));
                    return true;
                }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void call() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            //Open call function
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + selectedNumber));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                call();
            } else {
                Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
