package com.example.crypedu.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.crypedu.Adapter.CustomSingleLabAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.PathologyServiceSetterGetter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class PathologyLabActivity extends AppCompatActivity {

    private ArrayList<PathologyServiceSetterGetter> pathologySetterGettersList = new ArrayList<>();
    private PopupWindow popupWindow;
    private ArrayList<String> arrayList;
    private ImageView pathology_image, pathology_phoneCall;
    private TextView pathology_name, pathology_description, pathology_address, pathology_phone_no;
    private RecyclerView recyclerView;
    private String pathId, labId;
    private CustomSingleLabAdapter servicesAdapter;
    private CoordinatorLayout coordinatorLayout;

    /*public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathology_single_lab);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }  // To prevent screen recording*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pathology_image = findViewById(R.id.iv_pathology_image);
        pathology_phoneCall = findViewById(R.id.iv_pathology_phoneCall);
        pathology_name = findViewById(R.id.tv_pathology_name);
        //pathology_description =findViewById(R.id.tv_pathology_description);
        pathology_address = findViewById(R.id.tv_pathology_address);
        pathology_phone_no = findViewById(R.id.tv_pathology_phone_no);
        recyclerView = findViewById(R.id.lab_recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        //linearLayout = findViewById(R.id.linearLayout_pathology_item);

        Intent intent = getIntent();
        pathId = intent.getStringExtra("PATH_ID");
        labId = intent.getStringExtra("LAB_ID");

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        if (InternetCheckActivity.isConnected()) {
            getAllData();
        }else {
            showSnack();
        }
        //recyclerView.setAdapter(customLabAdapter);

    }

    private void showSnack(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }
    private void getAllData() {
        /*
         *  Progress Bar
         */
        //Toast.makeText(this, pathId + ", " + labId, Toast.LENGTH_SHORT).show();
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER_MEDIWALLET + "medi_pathology_service_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    pathologySetterGettersList.clear();
                    if (status.equalsIgnoreCase("200")) {
                        //Log.e("Status ", status);
                        JSONObject pathology_service_obj = obj.getJSONObject("pathology_service");
                        final JSONArray jsonArray = pathology_service_obj.getJSONArray("lab_details");
                        //Log.e("Array ", String.valueOf(jsonArray));
                        pathology_name.setText(jsonArray.getJSONObject(0).getString("lab_name"));
                        pathology_address.setText(jsonArray.getJSONObject(0).getString("lab_address"));
                        pathology_phone_no.setText(jsonArray.getJSONObject(0).getString("lab_phone"));
                        Picasso.with(getBaseContext())
                                .load(jsonArray.getJSONObject(0).getString("lab_img_url"))
                                .fit()
                                .into(pathology_image);
                        pathology_phoneCall.setOnClickListener(new View.OnClickListener() {
                            //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
                                String[] numbers = new String[0]; // Split according to the hyphen and put them in an array
                                try {
                                    numbers = jsonArray.getJSONObject(0).getString("lab_phone").split("/");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //Log.e("NUMBERS", Arrays.toString(numbers));
                                arrayList = new ArrayList<>();
                                for (String subString : numbers) { // Cycle through the array
                                    System.out.println(subString);
                                    arrayList.add(subString);
                                    //Log.e("Arraylist Patho Numbers", arrayList.toString());
                                }
                                //Toast.makeText(getApplicationContext(), "Called", Toast.LENGTH_LONG).show();
                                if (popupWindow != null && popupWindow.isShowing()) {
                                    Snackbar.make(v, "Already opened the Window.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                } else {
                                    Snackbar.make(v, "Choose Number for calling", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                                    //instantiate the popup.xml layout file
                                    LayoutInflater layoutInflater = getLayoutInflater();
                                    @SuppressLint("InflateParams")
                                    View customView = layoutInflater.inflate(R.layout.popup_window_pathology, null);
                                    ImageView closePopupBtn = customView.findViewById(R.id.closePopupBtn);
                                    ListView listView_number = customView.findViewById(R.id.listView_number);

                                    ArrayAdapter<String> arrayAdapter =
                                            new ArrayAdapter<>(PathologyLabActivity.this, android.R.layout.simple_list_item_1, arrayList);
                                    // Set The Adapter
                                    listView_number.setAdapter(arrayAdapter);

                                    // register onClickListener to handle click events on each item
                                    listView_number.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        // argument position gives the index of item which is clicked
                                        public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                                            String selectedNumber = arrayList.get(position);
                                            Toast.makeText(PathologyLabActivity.this, "Calling : " + selectedNumber, Toast.LENGTH_LONG).show();
                                            Context context = v.getContext();
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedNumber));
                                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                Toast.makeText(context, "permission not granted", Toast.LENGTH_SHORT).show();
                                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 100);
                                            } else {
                                                context.startActivity(intent);
                                            }
                                        }
                                    });

                                    //instantiate popup window
                                    popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    popupWindow.setAnimationStyle(R.style.DialogAnimation_2);
                                    // Closes the popup window when touch outside.
                                    popupWindow.setOutsideTouchable(true);
                                    popupWindow.setFocusable(true);
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
                            }
                        });
                        JSONArray jsonArr1 = pathology_service_obj.getJSONArray("service_list");
                        //Log.e("Array 1 ", String.valueOf(jsonArr1));
                        PathologyServiceSetterGetter setterGetter;
                        int len = Integer.parseInt(jsonArray.getJSONObject(0).getString("service_number"));
                        //Log.e("Length ", String.valueOf(len));
                        for (int i = 0; i < jsonArr1.length(); i++) {
                            JSONObject object = jsonArr1.getJSONObject(i);
                            setterGetter = new PathologyServiceSetterGetter();
                            setterGetter.setPathology_service_name(object.getString("serv_name"));
                            //Log.e("Service name ", setterGetter.getPathology_service_name());
                            setterGetter.setPathology_service_id(object.getString("serv_id"));
                            //Log.e("Service id ", setterGetter.getPathology_service_id());
                            setterGetter.setPathology_discount(object.getString("discount"));
                            //Log.e("Discount ", setterGetter.getPathology_discount());
                            pathologySetterGettersList.add(setterGetter);
                            //Log.e("Service name ad ", pathologySetterGettersList.get(i).getPathology_service_name());
                            //Log.e("Service id ad ", pathologySetterGettersList.get(i).getPathology_service_id());
                            //Log.e("Discount ad ", pathologySetterGettersList.get(i).getPathology_discount());
                        }
                        servicesAdapter = new CustomSingleLabAdapter(getBaseContext(), pathologySetterGettersList);
                        servicesAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(servicesAdapter);
                        //setListViewHeightBasedOnChildren(recyclerView);
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    //pbHeaderProgress.setVisibility(View.GONE);
                    Log.e("Error ", e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e("Error ", e.getMessage());
                }
                pbHeaderProgress.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(PathologyLabActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("path_id", pathId);
                params.put("lab_no", labId);
                //Adding the parameters to the request

                return params;
            }

        };
        //Adding request the the queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
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
            Intent intent = new Intent(PathologyLabActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // Override back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
