package com.activity.smi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Activity.MenuActivity;
import com.example.crypedu.Adapter.CustomBusAdapter;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Pojo.BusInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class NewBusMapDirectActivity extends AppCompatActivity implements OnMapReadyCallback {

    //static final String TAG = "BroadcastTest";
    Intent intent;
    GoogleMap mMap;
    String lat, lng, servicelat, servicelong;
    Double lati, lang, servicelati, servicelang, preLat, preLong;
    RequestQueue requestQueue;
    LatLng sydney;
    BitmapDescriptor markerIcon;
    //ArrayList<LatLng> points; //added
    //Polyline line; //added
    Geocoder geocoder;
    List<Address> addresses;
    CoordinatorLayout linearLayout;
    private Typeface typeface;
    private List<BusInfo> busListInfo = new ArrayList<>();
    private RelativeLayout relativeLayout;
    private static int bugFlag = 0;
    private PopupWindow popupWindow;
    TextView txtGeoCoder;

    Location prevLoc = new Location("service Provider");
    Location newLoc = new Location("service Provider");

    float bearing, angle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bus_map);
        relativeLayout = findViewById(R.id.mapRelative);

        txtGeoCoder = findViewById(R.id.txtGeoCoder);
        switchBus();

        bugFlag = 0;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = new Intent(this, BroadcastDirectService.class);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastDirectService.BROADCAST_ACTION));
        //startService(intent);
        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewBusMapDirectActivity.this, MenuActivity.class);
                startActivity(intent);
                unregisterReceiver(broadcastReceiver);
                finish();
            }
        });

        //textView = (TextView)findViewById(R.id.textView);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);

        linearLayout = findViewById(R.id.linearLayout);
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);
        //textView.setTypeface(typeface);
        copyRight_textView.setTypeface(typeface);


        Drawable circleDrawable = getResources().getDrawable(R.drawable.bus_icon);
        markerIcon = getMarkerIconFromDrawable(circleDrawable);

        // points = new ArrayList<LatLng>(); //added
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_bus_switch_direct, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bus_menu){
            PopUpWindowForCall();
        }
        return true;
    }

    public void PopUpWindowForCall() {
        //instantiate the popup.xml layout file
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        @SuppressLint("InflateParams")
        View customView = layoutInflater.inflate(R.layout.alert_multiple_bus, null);
        //ImageView closePopupBtn = customView.findViewById(R.id.closePopupBtn);
        ListView listView_number = customView.findViewById(R.id.busList);

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, busListInfo);
        CustomBusAdapter busAdapter = new CustomBusAdapter(this, busListInfo);
        // Set The Adapter
        listView_number.setAdapter(busAdapter);

        // register onClickListener to handle click events on each item
        listView_number.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                bugFlag = 1;
                stopService(intent);

                Constants.BUS_ID = busListInfo.get(position).busId;
                servicelat = busListInfo.get(position).busLat;
                servicelong = busListInfo.get(position).busLong;
                Constants.BUS_LAT = servicelat;
                Constants.BUS_LONG = servicelong;
                relativeLayout.setVisibility(View.VISIBLE);

                if (!Constants.BUS_ID.equalsIgnoreCase("")) {
                    mMap.clear();
                    busLatLongRequest();
                }
                popupWindow.dismiss();

                onResume();
            }
        });

        //instantiate popup window
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.DialogAnimation_2);
        popupWindow.setOutsideTouchable(true);
        //display the popup window
        popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);

    }


    /**
     * Switching bus.
     */
    public void switchBus() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "bus_list_dir", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    //String message = object.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray areaListJsonArray = object.getJSONArray("notification_status");
                        for (int i = 0; i < areaListJsonArray.length(); i++) {
                            JSONObject cuisineJsonObject = areaListJsonArray.getJSONObject(i);
                            String busCode = cuisineJsonObject.getString("bus_no");
                            String busLat = cuisineJsonObject.getString("bus_lat");
                            String busLong = cuisineJsonObject.getString("bus_lon");
                            String busStatus = cuisineJsonObject.getString("bus_status");
                            String busId = cuisineJsonObject.getString("bus_id");
                            BusInfo busInfo = new BusInfo(busCode, busLat, busLong, busStatus, busId);

                            busListInfo.add(busInfo);
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
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
    @Override
    public void onResume() {
        super.onResume();
        //startService(intent);
        //registerReceiver(broadcastReceiver, new IntentFilter(BroadcastDirectService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(broadcastReceiver);
        //stopService(intent);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(Intent intent) {

        if (intent != null) {
            try{
                String counter = intent.getStringExtra("counter");
                servicelat = intent.getStringExtra("lat");
                servicelong = intent.getStringExtra("lng");
                String s = intent.getStringExtra("angle1");
                //Log.e("Angle", s);
                angle = Float.parseFloat(intent.getStringExtra("angle1"));
                //Log.e("Counter", counter);
                //Log.e("LAT ", String.valueOf(servicelat));
                //Log.e("LONG ", String.valueOf(servicelong));
            }catch (Exception e){
                Log.e("Error", e.getMessage());
            }

        }

        TextView txtlat = findViewById(R.id.txtlat);
        TextView txtlng = findViewById(R.id.txtlng);
        //TextView txtCounter = findViewById(R.id.txtCounter);
        txtlat.setText(servicelat);
        txtlng.setText(servicelong);
        //txtCounter.setText(counter);
        if (servicelat == null || servicelat.equalsIgnoreCase("")) {
            servicelati = 22.620972;
            servicelang = 88.348741;
        } else {
            if (mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                mMap.clear();
            }
            servicelati = Double.parseDouble(servicelat);
            servicelang = Double.parseDouble(servicelong);
            preLat = servicelati;
            preLong = servicelang;
            sydney = new LatLng(servicelati, servicelang);

            //applying Polyline
           /* PolylineOptions options = new PolylineOptions().width(10).color(Color.BLACK).geodesic(true); //added
            for (int i = 0; i < points.size(); i++) {
                LatLng point = points.get(i);
                options.add(point);
            }
            line = mMap.addPolyline(options);*/
            //added Polyline
            //applying GeoCoder
            String city = "", land = "", state = "", country = "", postalCode = "", addressFull = "", comma = ",";
            try {
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(servicelati, servicelang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses != null && addresses.size() > 0) {
                    Address addresss = addresses.get(0);
                    addressFull = addresss.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    city = addresss.getLocality();
                    state = addresss.getAdminArea();
                    country = addresss.getCountryName();
                    postalCode = addresss.getPostalCode();
                    land = addresss.getSubLocality();
                    //String knownName = addresses.get(0).getFeatureName(); // Only if
                    TextView txtGeoCoder = findViewById(R.id.txtGeoCoder);
                    txtGeoCoder.setText(addressFull);
                    txtGeoCoder.setTypeface(typeface);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            prevLoc.setLatitude(preLat);
            prevLoc.setLongitude(preLong);
            newLoc.setLatitude(servicelati);
            newLoc.setLongitude(servicelang);
            bearing = prevLoc.bearingTo(newLoc);

            Log.e("Bearing ", String.valueOf(bearing)+ " Angle "+ String.valueOf(angle));

            assert intent != null;
            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("SMI bus is here")
                    .icon(markerIcon)
                    .draggable(true)
                    .flat(true)
                    .anchor(0.5f,0.5f)
                    .rotation(angle)
            );
            //CameraPosition oldPos = mMap.getCameraPosition();
            //CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
            //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

//            points.add(sydney); //added

        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (bugFlag == 1){
            relativeLayout.setVisibility(View.VISIBLE);
        }else {
            relativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        // lati = Double.parseDouble(lat);
        // lang = Double.parseDouble(lng);

        //busLatLongRequest("");

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void busLatLongRequest(){

        // Add a marker in Sydney, Australia, and move the camera.
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "bus_location_dir", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Dir response", response);
                try {
                    JSONObject obj = new JSONObject(response);

                    String status = obj.getString("status");
                    JSONObject object = obj.getJSONObject("message");
                    Log.e("Status ", status);
                    if (status.equalsIgnoreCase("200") && !object.getString("bus_imei").equalsIgnoreCase("")) {

                        Constants.DEVICE_IEMI = object.getString("bus_imei");
                        startService(intent);
                        showMap();
                    }else {
                        txtGeoCoder.setText("Sorry! your transportation facility is not available");
                        txtGeoCoder.setTextColor(Color.BLACK);
                        //txtGeoCoder.setTypeface(typeface);
                        txtGeoCoder.setAllCaps(false);
                        txtGeoCoder.setTextSize(15.0f);
                        mMap.clear();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //response1=response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e("Error ", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("bus_id", Constants.BUS_ID);
                Log.d("BusValue", "getParams: "+params);
                return params;
            }

        };
        requestQueue.add(sr);
    }

    private void showMap() {
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BUS_LOCATION+Constants.DEVICE_IEMI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject object1 = object.getJSONObject(Constants.DEVICE_IEMI);
                    lat = object1.getString("lat");
                    lng = object1.getString("lng");
                    lati = Double.parseDouble(lat);
                    lang = Double.parseDouble(lng);
                    sydney = new LatLng(lati, lang);

                    try {
                        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(lati, lang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                        if (addresses != null && addresses.size() > 0) {
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            //String knownName = addresses.get(0).getFeatureName(); // Only if
                            String comma = ",";
                            txtGeoCoder.setText(address);
                            txtGeoCoder.setTypeface(typeface);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMap.addMarker(new MarkerOptions()
                            .position(sydney)
                            .title("SMI bus is here")
                            .icon(markerIcon)
                            .draggable(true)
                            .flat(true)
                            .anchor(0.5f,0.5f)
                            .rotation(0.0f)
                    );

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    //-------------------------
    // Hardware back key.
    //-------------------------
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean check = true;
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    check = false;
                }else {
                    //stopService(intent);
                    unregisterReceiver(broadcastReceiver);
                    check =  true;
                }

        }
        return check;
    }*/

    @Override
    public void onBackPressed() {

        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();

        }else {
            //stopService(intent);
            super.onBackPressed();
            unregisterReceiver(broadcastReceiver);
            finish();
        }
    }
}
