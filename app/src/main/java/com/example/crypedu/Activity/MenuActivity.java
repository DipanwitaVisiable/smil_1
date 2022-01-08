package com.example.crypedu.Activity;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.RetryPolicy;
import com.example.crypedu.Fragment.PayOnlineFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.smi.BuildConfig;
import com.activity.smi.ForceUpdateAsync;
import com.activity.smi.InternetCheckActivity;
import com.activity.smi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Adapter.MenuItemAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Fragment.AccountFragment;
import com.example.crypedu.Fragment.SMIMainFragment;
import com.example.crypedu.Fragment.AssignmentFragment;
import com.example.crypedu.Fragment.AttendanceDirectFragment;
import com.example.crypedu.Fragment.AttendanceFragment;
import com.example.crypedu.Fragment.BulletinsFragment;
import com.example.crypedu.Fragment.CommunationFragment;
import com.example.crypedu.Fragment.CommunicationDirectFragment;
import com.example.crypedu.Fragment.ExaminationDirectFragment;
import com.example.crypedu.Fragment.ExaminationFragment;
import com.example.crypedu.Fragment.NoticeDirectFragment;
import com.example.crypedu.Fragment.NoticeFragment;
import com.example.crypedu.Fragment.ProfileFragment;
import com.example.crypedu.Fragment.SyllabusDirectFragment;
import com.example.crypedu.Fragment.SyllabusFragment;
import com.example.crypedu.Fragment.TenFragment;
import com.example.crypedu.Fragment.TimeTableDirectFragment;
import com.example.crypedu.Fragment.TimeTableFragment;
import com.example.crypedu.Helper.CustomTypefaceSpan;
import com.example.crypedu.Pojo.BookInfo;
import com.example.crypedu.Pojo.EmployeeInfo;
import com.example.crypedu.Pojo.SwitchChildInfo;
//import com.felipecsl.gifimageview.library.GifImageView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.key.Key;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERIOD = 2000;
    private static final int TIME_INTERVAL = 2000;
    String newVersion;
    Context context;
    String role = "s";
    String gifUrl = "";
//    GifImageView gif;
    FragmentManager fm;
    private ListView subjectListView;
    private ArrayList<BookInfo> bookArrayList = new ArrayList<>();
    private ArrayList<EmployeeInfo> employeeInfoArrayList = new ArrayList<>();
    private TabLayout tabLayout;
    private ListView menu_items_listView;
    private CoordinatorLayout coordinatorLayout;
    private Typeface typeface;
    private long lastPressedTime;
    private String notificationCount = null;
    private TextView itemMessagesBadgeTextView;
    private ImageView itemMessagesBadgeImageView;
    private String currentVersion;
    private RequestQueue requestQueue;
    private ImageView image;
    private ArrayList<SwitchChildInfo> switchChildInfoArrayList;
    private long mBackPressed;
    private String reg_id;
    private ProgressBar pbHeaderProgress;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        /*if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        } // To prevent screen recording*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        setSupportActionBar(toolbar);
        context = MenuActivity.this;
        requestQueue = Volley.newRequestQueue(this);

        SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
        reg_id=device_token.getString("FIREBASE_ID", "");

        PackageManager manager = context.getPackageManager();
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        remoteConfig.setConfigSettings(configSettings);
        boolean boolValue = remoteConfig.getBoolean("is_update");
        String ver = remoteConfig.getString("version");
        String url = remoteConfig.getString("update_url");
        try {
            String result = manager.getPackageInfo(context.getPackageName(), 0).versionName;
            //result = result.replaceAll("[a-zA-Z]|-", "");
            //Log.e("App version ", result);
            //Log.e("Firebase version ", ver);
            //Log.e("Firebase url ", url);
            //Log.e("Firebase bool ", String.valueOf(boolValue));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        String versionName = BuildConfig.VERSION_NAME;
//        Toast.makeText(getApplicationContext(),versionName,Toast.LENGTH_LONG).show();

        //------------------------------------------------------------------------------------------------------------------------------------------------
        // FirebaseUpdateOptions//

        /*UpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();*/

        /*
          For one time showingdialog.
         */
        if (Constants.flag) {
            buildDialog();
        }


        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        /*
          Set FloatingActionButton Visibility 'INVISIBLE'.
         */
        FloatingActionButton fab = findViewById(R.id.fabMenu);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRestart();
            }
        });

        /*
          For Drawer Layout open and closed.
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*
          Retrieved Navigation View ID and set Item Selected Listener.
         */


        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    //applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            //applyFontToMenuItem(mi);
        }


        /*
          Set BubbleGumSans Regular custom font.
         */
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);

        /*
          Set Profile name on top of Drawer Layout
          if available in Constants.PROFILENAME
          otherwise go back to Login screen.
         */
        View hView = navigationView.getHeaderView(0);

        TextView toolbarText = findViewById(R.id.toolbarText);
        toolbarText.setTypeface(typeface);
        TextView nav_header_text = findViewById(R.id.nav_header_text);
        nav_header_text.setTypeface(typeface);

        TextView profile_textView = hView.findViewById(R.id.profile_textView);
        profile_textView.setTypeface(typeface);


        profile_textView.setText(Constants.PROFILENAME);

        /*
          Set ViewPager for Tab fragment.
         */
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        /*
          Setup tab layout.
         */


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setFocusable(true);
        tabLayout.setSelectedTabIndicatorHeight(7);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorNoticeBorder));
        setupTabIcons();

        /*
          Check internet connection
           for fees notification.
         */
        if (InternetCheckActivity.isConnected()) {
            fetchFeesDetails();
            fetchNotificationDetails();
            getPayOnlineUrl();
            /*
              For forcefully app update from google play store.Stoped now for Testing
             */
//            new GetVersionCode().execute();
//            try {
//                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
        } else {
            showSnack();
        }

        /*
          ListView for displaying drawer menu list in a list.
         */
        menu_items_listView = findViewById(R.id.menu_items_listView);

        /*
          Add all available menu item into ArrayList for showing drawer list.
         */
        ArrayList<String> stringArrayList = new ArrayList<>();

        /*
          For displaying data either Teacher or Parents.
         */
        if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {
            if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                stringArrayList.add("Profile");
                stringArrayList.add("Assignment");
                stringArrayList.add("Attendance");
                stringArrayList.add("Notice");
                stringArrayList.add("Bulletins");
                stringArrayList.add("Syllabus");
//                stringArrayList.add("Library");
                stringArrayList.add("Reset");
                stringArrayList.add("Logout");
            } else if (Constants.USER_ROLE.equalsIgnoreCase("s") || Constants.USER_ROLE.equalsIgnoreCase("d")) {
                stringArrayList.add("Profile");
                stringArrayList.add("Assignment");
                stringArrayList.add("Attendance");
                stringArrayList.add("TimeTable");
                stringArrayList.add("Notice");
                stringArrayList.add("Bulletins");
//                stringArrayList.add("Pay Online");
                stringArrayList.add("Syllabus");
//                stringArrayList.add("Library");
                stringArrayList.add("Examination");
                stringArrayList.add("Communication");
                stringArrayList.add("Transportation");
            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (stringArrayList.size() > 0) {
            /*
              Set adapter into drawer menu listview.
             */
            menu_items_listView.setAdapter(new MenuItemAdapter(getBaseContext(), stringArrayList, getLayoutInflater()));
            menu_items_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String listItem = menu_items_listView.getItemAtPosition(position).toString().trim();

                    Snackbar snackbar = Snackbar.make(coordinatorLayout, listItem, Snackbar.LENGTH_LONG);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.parseColor(Constants.colorAccent));
                    snackbar.show();

                    /*
                      For displaying data either Teacher or Parents.
                     */
                    if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {

                        if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                            Intent intent = null;
                            switch (listItem) {

                                case "Syllabus":
                                    intent = new Intent(MenuActivity.this, TeacherSyllabusActivity.class);
                                    break;
                                case "Notice":
                                    intent = new Intent(MenuActivity.this, TeacherNoticeActivity.class);
                                    break;
                                case "Bulletins":
                                    intent = new Intent(MenuActivity.this, BulletinsActivity.class);
                                    break;
                                case "Reset":
                                    intent = new Intent(MenuActivity.this, ResetPasswordActivity.class);
                                    break;
                                case "Logout":
                                    logoutAlert();
                                    break;
                                case "Profile":
                                    intent = new Intent(MenuActivity.this, ProfileActivity.class);
                                    break;
                                case "Assignment":
                                    intent = new Intent(MenuActivity.this, WorkActivity.class);
                                    break;
                                case "Attendance":
                                    intent = new Intent(MenuActivity.this, AttendanceActivity.class);
                                    break;
                                /*case "Library":
                                    intent = new Intent(MenuActivity.this, BookManageActivity.class);
                                    break;*/
                            }
                            if (intent != null) {
                                startActivity(intent);
                            }
                        } else if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                            //Log.e("User Role ", "S");
                            Intent intent = null;
                            switch (listItem) {
                                case "Syllabus":
                                    intent = new Intent(MenuActivity.this, SyllabusActivity.class);
                                    break;
                                case "Communication":
                                    intent = new Intent(MenuActivity.this, RequestActivity.class);
                                    break;
                                case "Notice":
                                    intent = new Intent(MenuActivity.this, NoticeActivity.class);
                                    break;
                                case "Bulletins":
                                    intent = new Intent(MenuActivity.this, BulletinsActivity.class);
                                    break;
                                case "Reset":
                                    intent = new Intent(MenuActivity.this, ResetPasswordActivity.class);
                                    break;
                                case "Logout":
                                    logoutAlert();
                                    break;
                                case "Profile":
                                    intent = new Intent(MenuActivity.this, ProfileActivity.class);
                                    break;
                                case "Assignment":
                                    intent = new Intent(MenuActivity.this, WorkActivity.class);
                                    break;
                                case "Attendance":
                                    intent = new Intent(MenuActivity.this, AttendanceActivity.class);
                                    break;
                                case "TimeTable":
                                    intent = new Intent(MenuActivity.this, TimeTableActivity.class);
                                    break;
                                case "Pay Online":
//                                    intent = new Intent(MenuActivity.this, AccountActivity.class);
                                    if (!Constants.PAY_ONLINE_URL.equals("")) {
                                        Uri uri = Uri.parse(Constants.PAY_ONLINE_URL);
                                        Intent intent_1 = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent_1);
                                    }
                                    break;
                                case "Transportation":
                                    /*Intent intent = new Intent(MenuActivity.this, TransportationActivity.class);
                                startActivity(intent);*/
                                    break;
                                /*case "Library":
                                    intent = new Intent(MenuActivity.this, BookManageActivity.class);
                                    break;*/
                                case "Examination":
                                    intent = new Intent(MenuActivity.this, ExaminationActivity.class);
                                    break;

                            }
                            if (intent != null){
                                startActivity(intent);
                            }


                        } else if (Constants.USER_ROLE.equalsIgnoreCase("d")) {
                            //Log.e("User Role", "D");

                            Intent intent = null;
                            switch (listItem){
                                case "Syllabus":
                                    intent = new Intent(MenuActivity.this, SyllabusActivity.class);
                                    break;
                                case "Communication":
                                    intent = new Intent(MenuActivity.this, RequestActivity.class);
                                    break;
                                case "Notice":
                                    intent = new Intent(MenuActivity.this, NoticeActivity.class);
                                    break;
                                case "Bulletins":
                                    intent = new Intent(MenuActivity.this, BulletinsActivity.class);
                                    break;
                                case "Reset":
                                    intent = new Intent(MenuActivity.this, ResetPasswordActivity.class);
                                    break;
                                case "Profile":
                                    intent = new Intent(MenuActivity.this, ProfileActivity.class);
                                    break;
                                case "Assignment":
                                    intent = new Intent(MenuActivity.this, WorkActivity.class);
                                    break;
                                case "Attendance":
                                    intent = new Intent(MenuActivity.this, AttendanceActivity.class);
                                    startActivity(intent);
                                    break;
                                case "TimeTable":
                                    intent = new Intent(MenuActivity.this, TimeTableActivity.class);
                                    break;
                                case "Pay Online":
//                                    intent = new Intent(MenuActivity.this, AccountActivity.class);
                                    if (!Constants.PAY_ONLINE_URL.equals("")) {
                                        Uri uri = Uri.parse(Constants.PAY_ONLINE_URL);
                                        Intent intent_1 = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent_1);
                                    }
                                    break;
                                case "Transportation":
                                    /*Intent intent = new Intent(MenuActivity.this, TransportationActivity.class);
                                startActivity(intent);*/
                                    break;
                                /*case "Library":
                                    intent = new Intent(MenuActivity.this, BookManageActivity.class);
                                    break;*/
                                case "Examination":
                                    intent = new Intent(MenuActivity.this, ExaminationActivity.class);
                                    break;
                            }
                            if (intent != null){
                                startActivity(intent);
                            }
                        }
                    } else {
                        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                    }
                }
            });
        }
        if (Constants.updateFalg == 1) {
            forceUpdate();
            Constants.updateFalg = 0;
        }


    }



    // check version on play store and force update
    public void forceUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert packageInfo != null;
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion, MenuActivity.this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //forceUpdate();
    }

    //    public void myBackStack(){
//        //-------------------------
//        // frament back
//        //-------------------------
//
//        int count = getFragmentManager().getBackStackEntryCount();
//        if (count > 0) {
//            for (int i = 0; i < count; i++) {
//                getFragmentManager().popBackStack();
//            }
//        }
//    }

    //-------------------------
    // Store Profile credential
    // in shared preferences.
    //-------------------------
    public void storeUserDetails() {
        SharedPreferences.Editor editor = getSharedPreferences("VALUE", MODE_PRIVATE).edit();
        editor.putString("PROFILENAME", Constants.PROFILENAME);
        editor.putString("USERID", Constants.USER_ID);
        editor.apply();

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, true);
        edt.putString(Key.KEY_STUDENT_ID, Constants.USER_ID);
        edt.apply();
    }

    //-------------------------
    // Store Login credential
    // in shared preferences.
    //-------------------------
    public void storeLogIn() {
//        SharedPreferences.Editor editor = getSharedPreferences("VALUE", MODE_PRIVATE).edit();
//        editor.putString("UID", username_editText.getText().toString());
//        editor.putString("PWD", password_editText.getText().toString());
//        editor.commit();

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString(Key.KEY_STUDENT_ID, Constants.USER_ID);
        edt.putString(Key.KEY_USER_ROLE, Constants.USER_ROLE);
        edt.putString(Key.KEY_PROFILE_NAME, Constants.PROFILENAME);
        edt.putString(Key.KEY_PHONE_NO, Constants.PhoneNo);
        edt.putString(Key.KEY_USER_RATING, Constants.USER_RATING);
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, true);
        edt.apply();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //forceUpdate();

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Running after 3sec in Main Page.
     */
    private void buildDialog() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void run() {
                greetingImageDialog();

                Constants.flag = false;
            }
        };
        handler.postDelayed(runnable, 2000);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void greetingImageDialog() {
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = (R.style.DialogAnimation_2);
//        dialog.setContentView(R.layout.greeting_one_time_dialog);
        dialog.setContentView(R.layout.new_pop_up_image);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        image = dialog.findViewById(R.id.image);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "invitaion",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pbHeaderProgress.setVisibility(View.GONE);
                            //Creating the json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            String invitaion_details_url = jsonResponse.getString("invitaion_details");
                            String invitaion_type = jsonResponse.getString("invitaion_type");

                            if (status.equalsIgnoreCase("200") && invitaion_type.equalsIgnoreCase("1")) {
                                Picasso.with(context)
                                        .load(invitaion_details_url)
                                        .noFade()
                                        .into(image);
                                dialog.show();
                            } else if (status.equalsIgnoreCase("200") && invitaion_type.equalsIgnoreCase("2")) {
                                /* Here will be gif upload code*/
                                pbHeaderProgress.setVisibility(View.GONE);
                                gifUrl = invitaion_details_url;
                                greetingGifDialog();
                            } else {
                                pbHeaderProgress.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pbHeaderProgress.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbHeaderProgress.setVisibility(View.GONE);
                        // Toast.makeText(MenuActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //Adding the parameters to the request
//                    params.put(Config.KEY_ID, "3");

                return new HashMap<>();
            }
        };
        //Adding request the the queue
        requestQueue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void greetingGifDialog() {
        /*final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = (R.style.DialogAnimation_2);
        dialog.setContentView(R.layout.greeting_gif_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        gif = dialog.findViewById(R.id.gif);
        @SuppressLint("StaticFieldLeak")
        class RetrieveByteArray extends AsyncTask<String, Void, byte[]> {

            @Override
            protected byte[] doInBackground(String... strings) {

                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if (urlConnection.getResponseCode() == 200)//HTTP 200=ok
                    {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        int nRead;
                        byte[] data = new byte[10240];
                        while ((nRead = in.read(data, 0, data.length)) != -1) {
                            buffer.write(data, 0, nRead);
                        }
                        buffer.flush();
                        return buffer.toByteArray();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);
                gif.setBytes(bytes);
            }
        }
        new RetrieveByteArray().execute(gifUrl);
        gif.startAnimation();
        dialog.show();*/
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), Constants.BubblegumSans_Regular_font);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(MenuActivity.this, MenuActivity.class);  //your class
        startActivity(i);
        finish();

    }


    /**
     * Set no of tabs are available on Main Screen.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupTabIcons() {
        /*
          For displaying data either Teacher or Parents.
         */
        if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {
            if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                int[] tabIcons = {
                        R.drawable.all,
                        R.drawable.profile,
                        R.drawable.asignment,
                        R.drawable.tab,
                        R.drawable.bulletin,
                        R.drawable.ic_reset_tab
                };
                Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
                Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
                Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcons[2]);
                Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(tabIcons[3]);
                Objects.requireNonNull(tabLayout.getTabAt(4)).setIcon(tabIcons[4]);
                Objects.requireNonNull(tabLayout.getTabAt(5)).setIcon(tabIcons[5]);
            } else if (Constants.USER_ROLE.equalsIgnoreCase("s") || Constants.USER_ROLE.equalsIgnoreCase("d")) {
                int[] tabIcons = {
                        R.drawable.all,
                        R.drawable.profile,
                        R.drawable.asignment,
                        R.drawable.tab,
//                        R.drawable.timetable,
                        R.drawable.notice,
                        R.drawable.bulletin,
                        R.drawable.account,
                        R.drawable.syllabus,
                        R.drawable.exam,
                        R.drawable.request
                        // R.drawable.ic_transportation_tab
                };
                Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
                Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
                Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcons[2]);
                Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(tabIcons[3]);
                Objects.requireNonNull(tabLayout.getTabAt(4)).setIcon(tabIcons[4]);
                Objects.requireNonNull(tabLayout.getTabAt(5)).setIcon(tabIcons[5]);
                Objects.requireNonNull(tabLayout.getTabAt(6)).setIcon(tabIcons[6]);
                Objects.requireNonNull(tabLayout.getTabAt(7)).setIcon(tabIcons[7]);
                Objects.requireNonNull(tabLayout.getTabAt(8)).setIcon(tabIcons[8]);
//                Objects.requireNonNull(tabLayout.getTabAt(9)).setIcon(tabIcons[9]);
//                Objects.requireNonNull(tabLayout.getTabAt(10)).setIcon(tabIcons[10]);
                //tabLayout.getTabAt(11).setIcon(tabIcons[11]);
            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /**
     * @param viewPager Set all available tabs name.
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /*
          For displaying data either Teacher or Parents.
         */
        if (!Constants.USER_ROLE.equalsIgnoreCase("") || Constants.USER_ROLE != null) {

            adapter.addFragment(new SMIMainFragment(), "ALL");
            adapter.addFragment(new ProfileFragment(), "PROFILE");
            adapter.addFragment(new AssignmentFragment(), "ASSIGNMENT");

            if (Constants.USER_ROLE.equalsIgnoreCase("t")) {
                adapter.addFragment(new AttendanceFragment(), "ATTENDANCE");
                adapter.addFragment(new BulletinsFragment(), "BULLETINS");
                adapter.addFragment(new TenFragment(), "RESET");
            } else if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                adapter.addFragment(new AttendanceFragment(), "ATTENDANCE");
//                adapter.addFragment(new TimeTableFragment(), "TIMETABLE");
                adapter.addFragment(new NoticeFragment(), "NOTICE");
                adapter.addFragment(new BulletinsFragment(), "BULLETINS");
//                adapter.addFragment(new AccountFragment(), "ACCOUNT");
//                adapter.addFragment(new PayOnlineFragment(), "PAY ONLINE");
                adapter.addFragment(new SyllabusFragment(), "SYLLABUS");
                adapter.addFragment(new ExaminationFragment(), "EXAMINATION");
                adapter.addFragment(new CommunationFragment(), "COMMUNICATION");

            } else if (Constants.USER_ROLE.equalsIgnoreCase("d")) {
                adapter.addFragment(new AttendanceDirectFragment(), "ATTENDANCE");
//                adapter.addFragment(new TimeTableDirectFragment(), "TIMETABLE");
                adapter.addFragment(new NoticeDirectFragment(), "NOTICE");
                adapter.addFragment(new BulletinsFragment(), "BULLETINS");
//                adapter.addFragment(new AccountFragment(), "ACCOUNT");
//                adapter.addFragment(new PayOnlineFragment(), "PAY ONLINE");
                adapter.addFragment(new SyllabusDirectFragment(), "SYLLABUS");
                adapter.addFragment(new ExaminationDirectFragment(), "EXAMINATION");
                adapter.addFragment(new CommunicationDirectFragment(), "COMMUNICATION");

            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_messages);
        MenuItemCompat.setActionView(item, R.layout.badge_layout);
        View view = MenuItemCompat.getActionView(item);
        itemMessagesBadgeTextView = view.findViewById(R.id.textView);
        itemMessagesBadgeImageView = view.findViewById(R.id.imageView);
        itemMessagesBadgeTextView.setVisibility(View.INVISIBLE); // initially hidden
        itemMessagesBadgeImageView.setVisibility(View.INVISIBLE); // initially hidden

        itemMessagesBadgeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NotificationActivity.class));
            }
        });
        itemMessagesBadgeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, NotificationActivity.class));
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switch_user) {
            SharedPreferences prefsForID = getSharedPreferences("FIREBASE_TOKEN", MODE_PRIVATE);
            String firebase_id = prefsForID.getString("FIREBASE_ID", "");

            if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                switchUser();
            }
            if (Constants.USER_ROLE.equalsIgnoreCase("d")) {

            }
            // Toast.makeText(context, "This is a switch user text", Toast.LENGTH_SHORT).show();
            return true;
        }
//        else if (id == R.id.action_notifications){
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = null;
        switch (id) {
            case R.id.nav_gallery:
                intent = new Intent(MenuActivity.this, GalleyActivity.class);
                break;
            case R.id.nav_profile:
                intent = new Intent(MenuActivity.this, ProfileActivity.class);
                break;
            case R.id.health_card:
                intent = new Intent(MenuActivity.this, HealthCardActivity.class);
//                intent = new Intent(MenuActivity.this, SmartClassActivity.class);
                break;
            case R.id.nav_communicate:
                if (Constants.USER_ROLE.equalsIgnoreCase("s")) {
                    intent = new Intent(MenuActivity.this, RequestActivity.class);
                }
                if (Constants.USER_ROLE.equalsIgnoreCase("d")) {
                    intent = new Intent(MenuActivity.this, ViewRequestDirectActivity.class);
                }
                break;
            case R.id.nav_share:
                String shareBody = "https://play.google.com/store/apps/details?id=com.activity.smi&hl=en";
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, "SMI LILUAH (Open it in Google Play Store to Download the Application)");
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                intent.setType("text/plain");
                Intent.createChooser(intent, "Share via");
                break;
            case R.id.nav_reset:
                intent = new Intent(MenuActivity.this, ResetPasswordActivity.class);
            case R.id.nav_privacy:
                privacyDialog();
                break;
            case R.id.nav_logout:
                logoutAlert();
                break;

        }

        if (intent != null) {
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-----------------------------
    // Normal Notification
    //-----------------------------
    private void showNotification(String message) {
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setStyle(new Notification.BigTextStyle(builder)
                .bigText(message)
                .setBigContentTitle("SMI Liluah")
                .setSummaryText("Test Notification"))
                .setContentTitle("SMI Liluah")
                .setContentText("Example Notification")
                .setSmallIcon(R.drawable.ic_notification);

        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert nm != null;
        nm.notify(0, builder.build());
    }

    //-----------------------------
    // Fetch book details from server
    // now its not used.
    //-----------------------------
    public void fetchSubjectDetails(final String subjectId) {
        /*
          ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            final RequestParams params = new RequestParams();
            params.put("subject_id", Integer.valueOf(subjectId));
            AsyncHttpClient clientReg = new AsyncHttpClient();
            clientReg.post(Constants.BASE_SERVER + "svcsubjectbooks/", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray jsonArrayList = obj.getJSONArray("books");
                            bookArrayList.clear();
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObject = jsonArrayList.getJSONObject(i);
                                String bookId = jsonObject.getString("id");
                                String bookTitle = jsonObject.getString("title");
                                String bookAuthor = jsonObject.getString("author");
                                String bookPublisher = jsonObject.getString("publisher");
                                String bookEdition = jsonObject.getString("edition");
                                String bookSubjectId = jsonObject.getString("subject_id");
                                String bookPrice = jsonObject.getString("price");
                                String bookIsbnNumber = jsonObject.getString("ISBN_number");
                                String bookShortDescription = jsonObject.getString("short_desc");

                                BookInfo bookInfo = new BookInfo(bookId, bookTitle, bookAuthor, bookPublisher, bookEdition,
                                        bookSubjectId, bookPrice, bookIsbnNumber, bookShortDescription);
                                bookArrayList.add(i, bookInfo);
                            }
//                            subjectListView.setAdapter(new BookAdapter(getBaseContext(), bookArrayList, getLayoutInflater()));
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
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        minimizeApp();
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to exit.", Toast.LENGTH_SHORT).show();
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
//            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                getSupportFragmentManager().popBackStack();
//            } else {
//                this.finish();
//            }
        }
    }

    //-----------------------
    // Double click for minimize.
    //-----------------------
    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //-----------------------
    // for logout.
    //-----------------------
    public void loginReset() {
        //SharedPreferences.Editor editor = getSharedPreferences("VALUE", MODE_PRIVATE).edit();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this).edit();
        //editor.putString("UID", "");
        //editor.putString("PWD", "");
        editor.apply();
        Constants.PROFILENAME = "";
        Constants.USER_ROLE = "";
        Constants.PhoneNo = "";
        Constants.USER_ID = "";

        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean(Key.KEY_ACTIVITY_EXE, false);
        edt.putString(Key.KEY_STUDENT_ID, Constants.USER_ID);
        edt.putString(Key.KEY_USER_ROLE, Constants.USER_ROLE);
        edt.putString(Key.KEY_PROFILE_NAME, Constants.PROFILENAME);
        edt.putString(Key.KEY_PHONE_NO, Constants.PhoneNo);
        edt.apply();
        //Log.e("STUDENT ID ", Constants.USER_ID);
        //Log.e("STUDENT ROLE ", Constants.USER_ROLE);
        //Log.e("STUDENT NAME ", Constants.PROFILENAME);
        //Log.e("STUDENT PHONE ", Constants.PhoneNo);
    }

    //-----------------------
    // Alert dialog box
    // for logout.
    //-----------------------
    public void logoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        builder.setMessage("Do you want to logout?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                loginReset();
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
                MenuActivity.this.finish();
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

    //------------------------
    //privacy policy----------
    private void privacyDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.privacy_dialog);
        TextView text0 = dialog.findViewById(R.id.text0);
        text0.setTypeface(typeface);
        TextView text1 = dialog.findViewById(R.id.text1);
        text1.setTypeface(typeface);
        TextView text2 = dialog.findViewById(R.id.text2);
        text2.setTypeface(typeface);
        TextView text3 = dialog.findViewById(R.id.text3);
        text3.setTypeface(typeface);
        Button ok = dialog.findViewById(R.id.button);
        ok.setTypeface(typeface);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showSnack() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Please connect your working internet connection. ", Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor(Constants.colorAccent));
        snackbar.show();
    }


    /**
     * Switching user.
     */
    public void switchUser_old() {
        /*
         *  Progress Bar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            RequestParams params = new RequestParams();
            //Log.e("Id ", Constants.USER_ID);
            params.put("id", Constants.USER_ID);
          params.put("reg_id", reg_id);
            AsyncHttpClient clientReg = new AsyncHttpClient();
            //  AsyncHttpClient clientReg = new AsyncHttpClient(true, 80, 443);
            clientReg.post(Constants.BASE_SERVER + "switch_user_check", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONArray areaListJsonArray = obj.getJSONArray("child_details");
                            switchChildInfoArrayList = new ArrayList<>();
                            for (int i = 0; i < areaListJsonArray.length(); i++) {
                                JSONObject cuisineJsonObject = areaListJsonArray.getJSONObject(i);
                                String studentCode = cuisineJsonObject.getString("student_code");
                                String fName = cuisineJsonObject.getString("First_Name");
                                String lName = cuisineJsonObject.getString("Last_Name");
                                String studentId = cuisineJsonObject.getString("Student_id");
                                String section = cuisineJsonObject.getString("section");
                                String roll_no = cuisineJsonObject.getString("roll_no");
                                String image_url = cuisineJsonObject.getString("image_url");
                                String usr_rating = cuisineJsonObject.getString("usr_rating");

                                Constants.PROFILENAME = fName + " " + lName;
                                Constants.USER_ID = studentId;
                                Constants.USER_ROLE = role;
                                Constants.USER_RATING = usr_rating;

                                storeLogIn();
                                storeUserDetails();

                                SwitchChildInfo switchChildInfo = new SwitchChildInfo(studentCode, fName, lName, studentId, usr_rating);
                                switchChildInfoArrayList.add(i, switchChildInfo);
                                /*SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = pref.edit();
                                edt.putString("student_id",studentId);
                                edt.commit();*/
                            }
                            if (switchChildInfoArrayList != null && role != null) {
                                showMultipleChild(role, switchChildInfoArrayList);
                            }

                        }
                        /*else if (status.equalsIgnoreCase("206"))
                        {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            builder.setMessage(R.string.force_logout)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    });
                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }*/
                        else {
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

    /**
     * Alert box for choosing multiple child of a particular parent.
     */
    public void showMultipleChild(final String role, final ArrayList<SwitchChildInfo> switchChildInfoArrayList) {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        //dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_multiple_child_inside_menu);
        List<String> stringList = new ArrayList<>();  // here is list
        for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
            stringList.add(switchChildInfo.fName + " " + switchChildInfo.lName);
        }
        TextView alertTitleTextView = dialog.findViewById(R.id.alertTitleTextView);
        alertTitleTextView.setTypeface(typeface);
        final RadioGroup rg = dialog.findViewById(R.id.radioGroup);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setTypeface(typeface);
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radiobutton by returned id
                int radioButtonID = rg.getCheckedRadioButtonId();
                RadioButton radioButton = rg.findViewById(radioButtonID);
                String selectChild = radioButton.getText().toString();
                Log.i("Child name: ", selectChild);
                for (SwitchChildInfo switchChildInfo : switchChildInfoArrayList) {
                    String childName = switchChildInfo.fName + " " + switchChildInfo.lName;
                    if (childName.equalsIgnoreCase(selectChild)) {
                        Constants.PROFILENAME = childName;
                        Constants.USER_ID = switchChildInfo.studentId;
                        Constants.USER_ROLE = role;
                        Constants.USER_RATING = switchChildInfo.user_rating;
                        SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edt = pref.edit();
                        edt.putString("student_id",Constants.USER_ID);
                        edt.commit();
                        Toast.makeText(context, ""+Constants.USER_ID, Toast.LENGTH_SHORT).show();
                        //Constants.PhoneNo = phoneNo;
                        /*Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                        startActivity(intent);*/
                        storeLogIn();
                        storeUserDetails();
                        saveDeviceTokenWithUserIdApi(Constants.USER_ID);
                    }
                }
            }
        });


        dialog.show();

       /* Button cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setTypeface(typeface);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });*/

    }

    //------------------------------
    // Fetch subject details from server
    // for launching fees notification.
    //------------------------------
    public void fetchFeesDetails_old() {
        /*
          ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", Constants.USER_ID);
            clientReg.post(Constants.BASE_SERVER + "fee_status/", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            JSONObject jsonObject = obj.getJSONObject("fees_details");
                            String amount = jsonObject.getString("amount");
                            String feesStatus = jsonObject.getString("fees_status");
                            if (feesStatus.equalsIgnoreCase("true")) {
//                                showNotification(amount +" "+message);
                            }
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

    //------------------------------
    // Fetch notification details from server
    // respective of student_id.
    //------------------------------
    public void fetchNotificationDetails_old() {
        /*
          ProgressBar
         */
        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", Constants.USER_ID);
            requestParams.put("reg_id", reg_id);
            clientReg.post(Constants.BASE_SERVER + "notification_count", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equalsIgnoreCase("200")) {
                            notificationCount = obj.getString("notification_count");
                            itemMessagesBadgeTextView.setVisibility(View.VISIBLE); // initially hidden
                            itemMessagesBadgeImageView.setVisibility(View.VISIBLE); // initially hidden
                            itemMessagesBadgeTextView.setText(notificationCount);
                        }
                        else if (status.equalsIgnoreCase("206"))
                        {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            builder.setMessage(R.string.force_logout)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    });
                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
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

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //--------------------------------------------------------------------------------------------------
    // firebase update method
   /* @Override
    public void onUpdateCheckListener(final String urlApp) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MenuActivity.this)
                .setTitle("New version available")
                .setCancelable(true)
                .setMessage("Please, update app to new version to continue use.")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        redirectStore(urlApp);
                    }
                })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        dialog.show();
    }*/

    /**
     * Adapter for fragments
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();

        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void saveDeviceTokenWithUserIdApi_old(final String student_id) {

        final ProgressBar pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.VISIBLE);
        try {
            AsyncHttpClient clientReg = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("student_id", student_id);
            requestParams.put("reg_id", reg_id);
            clientReg.post(Constants.BASE_SERVER + "update_reg_id", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
                    try {
                        pbHeaderProgress.setVisibility(View.GONE);
                        String status = obj.getString("status");
                        if (status.equalsIgnoreCase("200")) {
                            Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Try again", Snackbar.LENGTH_LONG);

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

    public void switchUser() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "switch_user_check", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray areaListJsonArray = obj.getJSONArray("child_details");
                        switchChildInfoArrayList = new ArrayList<>();
                        for (int i = 0; i < areaListJsonArray.length(); i++) {
                            JSONObject cuisineJsonObject = areaListJsonArray.getJSONObject(i);
                            String studentCode = cuisineJsonObject.getString("student_code");
                            String fName = cuisineJsonObject.getString("First_Name");
                            String lName = cuisineJsonObject.getString("Last_Name");
                            String studentId = cuisineJsonObject.getString("Student_id");
                            String section = cuisineJsonObject.getString("section");
                            String roll_no = cuisineJsonObject.getString("roll_no");
                            String image_url = cuisineJsonObject.getString("image_url");
                            String usr_rating = cuisineJsonObject.getString("usr_rating");

                            Constants.PROFILENAME = fName + " " + lName;
                            Constants.USER_ID = studentId;
                            Constants.USER_ROLE = role;
                            Constants.USER_RATING = usr_rating;

                            storeLogIn();
                            storeUserDetails();

                            SwitchChildInfo switchChildInfo = new SwitchChildInfo(studentCode, fName, lName, studentId, usr_rating);
                            switchChildInfoArrayList.add(i, switchChildInfo);
                                /*SharedPreferences pref = getSharedPreferences(Key.KEY_ACTIVITY_PREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = pref.edit();
                                edt.putString("student_id",studentId);
                                edt.commit();*/
                        }
                        if (switchChildInfoArrayList != null && role != null) {
                            showMultipleChild(role, switchChildInfoArrayList);
                        }

                    }
                        /*else if (status.equalsIgnoreCase("206"))
                        {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            builder.setMessage(R.string.force_logout)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    });
                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                        }*/
                    else {
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
                params.put("id", Constants.USER_ID);
                params.put("reg_id", reg_id);
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

    public void fetchFeesDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "fee_status/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject = obj.getJSONObject("fees_details");
                        String amount = jsonObject.getString("amount");
                        String feesStatus = jsonObject.getString("fees_status");
                        if (feesStatus.equalsIgnoreCase("true")) {
//                                showNotification(amount +" "+message);
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
//                        snackbar.show();
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
        requestQueue.add(stringRequest);
    }

    public void fetchNotificationDetails() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "notification_count", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    pbHeaderProgress.setVisibility(View.GONE);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        notificationCount = obj.getString("notification_count");
                        itemMessagesBadgeTextView.setVisibility(View.VISIBLE); // initially hidden
                        itemMessagesBadgeImageView.setVisibility(View.VISIBLE); // initially hidden
                        itemMessagesBadgeTextView.setText(notificationCount);
                    }
                    else if (status.equalsIgnoreCase("206"))
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        builder.setMessage(R.string.force_logout)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(R.id.snackbar_text);
                        textView.setTextColor(Color.parseColor(Constants.colorAccent));
//                        snackbar.show();
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
                params.put("reg_id", reg_id);
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

    public void saveDeviceTokenWithUserIdApi(final String student_id) {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "update_reg_id", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Try again", Snackbar.LENGTH_LONG);

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
                params.put("student_id", student_id);
                params.put("reg_id", reg_id);
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

    private void getPayOnlineUrl() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.BASE_SERVER + "payonline", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pbHeaderProgress.setVisibility(View.GONE);
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    String online_link = obj.getString("online_link");
                    Constants.PAY_ONLINE_URL=online_link;

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
