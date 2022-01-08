package com.example.crypedu.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.volley.RetryPolicy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.crypedu.Adapter.EmergencyListAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.EmergencyListSetterGetter;
import com.example.crypedu.Pojo.SubSetGet;
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

public class MediwalletActivity extends AppCompatActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    BottomSheetBehavior behavior;
    private Context context;
    private TextView tv_wallet_amount;
    private CoordinatorLayout coordinatorLayout;
    private RequestQueue requestQueue;
    private RecyclerView bottomSheet_recyclerView;
    private ArrayList<EmergencyListSetterGetter> emergencyListSetterGetterArrayList = new ArrayList<>();
    private ArrayList<SubSetGet> arrayListsub = new ArrayList<>();
    private SubSetGet subSetGet;
    private EmergencyListAdapter emergencyListAdapter;
    private SliderLayout sliderLayout;
    private HashMap<String, Integer> hashMap;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediwallet);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        sliderLayout = findViewById(R.id.slider);
        //Call this method to add images from local drawable folder .
        AddImageUrlFormLocalRes();

        for (String name : hashMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(hashMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(2000);

        sliderLayout.addOnPageChangeListener(this);
        context = MediwalletActivity.this;
        requestQueue = Volley.newRequestQueue(this);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MediwalletActivity.this, MenuActivity.class));
                finish();
            }
        });

        bottomSheet_recyclerView = findViewById(R.id.bottomSheet_recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        bottomSheet_recyclerView.setLayoutManager(layoutManager);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        LinearLayout doctorLinearLayout = findViewById(R.id.doctorLinearLayout);
        LinearLayout pathologyLinearLayout = findViewById(R.id.pathologyLinearLayout);
        LinearLayout medicalStoreLinearLayout = findViewById(R.id.medicalStoreLinearLayout);


        doctorLinearLayout.setOnClickListener(this);
        pathologyLinearLayout.setOnClickListener(this);
        medicalStoreLinearLayout.setOnClickListener(this);

        /*
          Set BubbleGumSans Regular font.
         */
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);

        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);
        copyRight_textView.setTypeface(typeface);

        TextView tv_doctor = findViewById(R.id.tv_doctor);
        tv_doctor.setTypeface(typeface);

        TextView tv_pathology = findViewById(R.id.tv_pathology);
        tv_pathology.setTypeface(typeface);

        TextView tv_medical_store = findViewById(R.id.tv_medical_store);
        tv_medical_store.setTypeface(typeface);

        TextView tv_wallet = findViewById(R.id.tv_wallet);
        tv_wallet.setTypeface(typeface);

        tv_wallet_amount = findViewById(R.id.tv_wallet_amount);
        tv_wallet_amount.setTypeface(typeface);


        /*
          If internet connection is working and
          userId is available otherwise go to login screen.
         */
        if (InternetCheckActivity.isConnected()) {
            if (Constants.USER_ID != null) {
                fetchingWalletBalance();
                fetchingEmergencyList();
                sendNotificationStatus();
            } else {
                startActivity(new Intent(MediwalletActivity.this, LoginActivity.class));
                finish();
            }
        }else {
            showSnack();
        }


        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        ImageView bottomSheet_text = findViewById(R.id.bottomSheet_text);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        // toolbar.setVisibility(View.VISIBLE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        toolbar.setVisibility(View.GONE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        toolbar.setVisibility(View.GONE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        toolbar.setVisibility(View.VISIBLE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        toolbar.setVisibility(View.VISIBLE);
                        Log.e("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });

        bottomSheet_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

    }

    public void AddImageUrlFormLocalRes() {

        hashMap = new HashMap<>();

        hashMap.put("Education", R.drawable.mediwallet_header);
        /*hashMap.put("Medicine", R.drawable.add_img1);
        hashMap.put("Health Care", R.drawable.add_img2);
        hashMap.put("Diagnostic Test", R.drawable.add_img4);*/
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
            Intent intent = new Intent(MediwalletActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sliderLayout.stopAutoCycle();
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderLayout.startAutoCycle();
        //Log.e("ON RESUME CALLED", "RESUMMED");
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(context, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }

    //--------------------------------
    // fetchStudentProfile from server
    // with the 'studentID' param.
    //--------------------------------
    public void fetchingWalletBalance() {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER_MEDIWALLET + "medi_credits", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    Log.e("STATUS", status);
                    Log.e("MESSAGE", message);
                    if (status.equalsIgnoreCase("200")) {
                        String getWalletAmount = obj.getString("medi_credits");
                        String rupee = " ₹ ";
                        String rupee_end = "/-";
                        tv_wallet_amount.setText(getWalletAmount);

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


    public void fetchingEmergencyList() {
        String url = "https://sudhirmemorialinstituteliluah.com/mediwallet/index.php/main/emergency_list";
        //  if (isConnected(LoginwithRegActivity.this)) {
        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");
                            String message = obj.getString("message");
                            if (status.equalsIgnoreCase("200")) {
                                JSONArray emergency_list = obj.getJSONArray("emergency_list_deltails");
                                emergencyListSetterGetterArrayList.clear();
                                Log.e("list EmergencyPage", String.valueOf(emergency_list));
                                for (int i = 0; i < emergency_list.length(); i++) {
                                    JSONObject object = emergency_list.getJSONObject(i);
                                    EmergencyListSetterGetter setterGetter = new EmergencyListSetterGetter();
                                    setterGetter.setService_name(object.getString("service_name"));
                                    setterGetter.setService_icon(object.getString("service_icon"));
                                    JSONArray subarray = object.getJSONArray("service_list");
                                    ArrayList<SubSetGet> arrayListsub = new ArrayList<>();

                                    for (int j = 0; j < subarray.length(); j++) {
                                        JSONObject subobject = subarray.getJSONObject(j);
                                        subSetGet = new SubSetGet();
                                        subSetGet.setEmrg_id(subobject.getString("emrg_ph"));
                                        subSetGet.setEmrg_name(subobject.getString("emrg_name"));
                                        subSetGet.setEmrg_add(subobject.getString("emrg_add"));
                                        arrayListsub.add(subSetGet);

                                    }
                                    setterGetter.setSublist(arrayListsub);
                                    emergencyListSetterGetterArrayList.add(setterGetter);

                                }
                                emergencyListAdapter = new EmergencyListAdapter(context, emergencyListSetterGetterArrayList);
                                bottomSheet_recyclerView.setAdapter(emergencyListAdapter);
                            } else {
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
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
        }
        ) {

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hasData = new HashMap<>();

                //Log.e("has", hasData.toString());
                return hasData;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 80,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
//           /*
//         *  Progress Bar
//         */
////        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
////        pbHeaderProgress.setVisibility(View.VISIBLE);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_SERVER_MEDIWALLET + "emergency_list",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            //  pbHeaderProgress.setVisibility(View.GONE);
//                            JSONObject obj = new JSONObject(response);
//                            String status = obj.getString("status");
//                            String message = obj.getString("message");
//                            if (status.equalsIgnoreCase("200")) {
//                                JSONArray emergency_list = obj.getJSONArray("emergency_list_deltails");
//                                emergencyListSetterGetterArrayList.clear();
//                                Log.e("list EmergencyPage", String.valueOf(emergency_list));
//                                for (int i = 0; i < emergency_list.length(); i++) {
//                                    JSONObject object = emergency_list.getJSONObject(i);
//                                    EmergencyListSetterGetter setterGetter = new EmergencyListSetterGetter();
//                                    setterGetter.setService_name(object.getString("service_name"));
//                                    JSONArray service_list = object.getJSONArray("service_list");
//                                    Log.e("list service_list", String.valueOf(service_list));
//                                    List<EmergencyListSetterGetter.SERVICES> servicesArrayList = new ArrayList<>();
//                                    EmergencyListSetterGetter.SERVICES services = new EmergencyListSetterGetter.SERVICES();
//                                    services.setEmrg_name(service_list.getJSONObject(service_list.length()).getString("emrg_name"));
//                                    services.setEmrg_ph(service_list.getJSONObject(service_list.length()).getString("emrg_ph"));
//                                    services.setEmrg_add(service_list.getJSONObject(service_list.length()).getString("emrg_add"));
//                                    servicesArrayList.add(services);
//                                    setterGetter.setServicesList(servicesArrayList);
////                                    for (int j = 0; j < service_list.length(); j++) {
////                                        JSONObject object1 = service_list.getJSONObject(j);
////                                        EmergencySingleSetterGetter emergencySingleSetterGetter = new EmergencySingleSetterGetter();
////                                        emergencySingleSetterGetter.setEmrg_name(object1.getString("emrg_name"));
////                                        emergencySingleSetterGetter.setEmrg_ph(object1.getString("emrg_ph"));
////                                        emergencySingleSetterGetter.setEmrg_add(object1.getString("emrg_add"));
////                                        emergencySingleSetterGetterArrayList.add(emergencySingleSetterGetter);
////                                    }
////                                    emergencySingleAdapter = new EmergencySingleAdapter(context, emergencySingleSetterGetterArrayList);
////                                    single_recyclerView.setAdapter(emergencySingleAdapter);
//
//                                    emergencyListSetterGetterArrayList.add(setterGetter);
//                                }
//                                emergencyListAdapter = new EmergencyListAdapter(context, emergencyListSetterGetterArrayList);
//                                bottomSheet_recyclerView.setAdapter(emergencyListAdapter);
//                            } else {
//                                Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
//                                View sbView = snackbar.getView();
//                                TextView textView = sbView.findViewById(R.id.snackbar_text);
//                                textView.setTextColor(Color.parseColor(Constants.colorAccent));
//                                snackbar.show();
//                            }
//                        } catch (JSONException e) {
//                            //  pbHeaderProgress.setVisibility(View.GONE);
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // pbHeaderProgress.setVisibility(View.GONE);
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                //Adding the parameters to the request
//                // params.put("serv_id", Constants.PATHOLOGY_SRVS_ID);
//                // Log.e("ID put PathologyPage", Constants.PATHOLOGY_SRVS_ID);
//                return checkParams(params);
//            }
//
//            private Map<String, String> checkParams(Map<String, String> map) {
//                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry<String, String> pairs = it.next();
//                    if (pairs.getValue() == null) {
//                        map.put(pairs.getKey(), "");
//                    }
//                }
//                return map;
//            }
//
//        };
//        //Adding request the the queue
//        requestQueue.add(stringRequest);
    }

    //------------------------------
    // Send notification status to server
    // respective of student_id and notification_type.
    //------------------------------
    public void sendNotificationStatus_old() {
        /*
          ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Constants.USER_ID);
            requestParams.put("notification_type", "Mediwallet");
            clientReg.post(Constants.BASE_SERVER + "notification_status/", requestParams, new JsonHttpResponseHandler() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.doctorLinearLayout:
                startActivity(new Intent(MediwalletActivity.this, MediChamberActivity.class));
                break;
            case R.id.pathologyLinearLayout:
                startActivity(new Intent(MediwalletActivity.this, PathologyServiceListActivity.class));
                break;
            case R.id.medicalStoreLinearLayout:
                startActivity(new Intent(MediwalletActivity.this, MedicalStoreActivity.class));
                break;
            default:
                break;
        }
    }


    //-------------------------
    // Hardware back key.
    //-------------------------
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true;
                } else {
                    startActivity(new Intent(context, MenuActivity.class));
                    finish();
                    //onBackPressed();
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendNotificationStatus() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_status/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
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
                params.put("notification_type", "Mediwallet");
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

}
