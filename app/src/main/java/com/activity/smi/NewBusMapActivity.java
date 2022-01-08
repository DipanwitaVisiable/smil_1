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
import android.hardware.GeomagneticField;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Activity.MenuActivity;
import com.example.crypedu.Constants.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class NewBusMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    static final String TAG = "BroadcastTest";
    Intent intent;
    GoogleMap mMap;
    String lat, lng, servicelat, servicelng;
    Double lati, lang, servicelati, servicelang, preLat, preLong;
    RequestQueue requestQueue;
    LatLng sydney;
    BitmapDescriptor markerIcon;
    ArrayList<LatLng> points; //added
    Polyline line; //added
    Geocoder geocoder;
    List<Address> addresses;
    CoordinatorLayout linearLayout;
    private Typeface typeface;
    boolean isMarkerRotating = false;

    Location prevLoc = new Location("service Provider");
    Location newLoc = new Location("service Provider");
    float bearing, angle;

    TextView txtGeoCoder;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bus_map);
        txtGeoCoder = findViewById(R.id.txtGeoCoder);
        getDeviceIMEI();
        RelativeLayout relativeLayout = findViewById(R.id.mapRelative);
        intent = new Intent(NewBusMapActivity.this, BroadcastService.class);
        relativeLayout.setVisibility(View.VISIBLE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewBusMapActivity.this, MenuActivity.class);
                startActivity(intent);
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

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }
    private void getDeviceIMEI(){
        StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_SERVER+"track_student", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("IMEI response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("200")){
                        Constants.DEVICE_IEMI = object.getString("message");
                        //Toast.makeText(NewBusMapActivity.this, ""+object.getString("message"), Toast.LENGTH_SHORT).show();
                        startService(intent);
                    }else {
                        //Toast.makeText(NewBusMapActivity.this, "Sorry! your transportation facility is not available", Toast.LENGTH_LONG).show();
                        txtGeoCoder.setText("Sorry! your transportation facility is not available");
                        txtGeoCoder.setTextColor(Color.BLACK);
                        txtGeoCoder.setAllCaps(false);
                        txtGeoCoder.setTextSize(15.0f);
                        mMap.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new Hashtable<>();
                map.put("student_id", Constants.USER_ID);
                Log.d("Location", "getParams: "+map);

                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());

        queue.add(request);
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
    public void onResume() {
        super.onResume();
        //startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intent);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(Intent intent) {

        String counter = intent.getStringExtra("counter");
        servicelat = intent.getStringExtra("lat");
        servicelng = intent.getStringExtra("lng");
        angle = Float.parseFloat(intent.getStringExtra("angle1"));
        Log.e(TAG, counter);
        // Log.e(TAG, time);


        TextView txtlat = findViewById(R.id.txtlat);
        TextView txtlng = findViewById(R.id.txtlng);
        TextView txtCounter = findViewById(R.id.txtCounter);
        txtlat.setText(servicelat);
        txtlng.setText(servicelng);
        txtCounter.setText(counter);
        if (servicelat == null) {
            servicelati = 22.620972;
            servicelang = 88.348741;
        } else {
            if (mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                mMap.clear();
            }
            servicelati = Double.parseDouble(servicelat);
            servicelang = Double.parseDouble(servicelng);
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
            try {
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(servicelati, servicelang, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if
                String comma=",";
                txtGeoCoder.setText(address+comma+postalCode+comma+city+comma+state+comma+country);
                txtGeoCoder.setTypeface(typeface);
            } catch (Exception e) {
                e.printStackTrace();
            }

            prevLoc.setLatitude(preLat);
            prevLoc.setLongitude(preLong);
            newLoc.setLatitude(servicelati);
            newLoc.setLongitude(servicelang);
            bearing = prevLoc.bearingTo(newLoc);

            Log.e("Bearing ", String.valueOf(bearing));

            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("SMI bus is here")
                    .icon(markerIcon)
                    .draggable(true)
                    .flat(true)
                    .anchor(0.5f,0.5f)
                    .rotation(angle)
            );

            CameraPosition oldPos = mMap.getCameraPosition();
            CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//            points.add(sydney); //added

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //  lati = Double.parseDouble(lat);
        // lang = Double.parseDouble(lng);

        // Add a marker in Sydney, Australia, and move the camera.
        StringRequest sr = new StringRequest(Request.Method.GET, Constants.BUS_LOCATION+Constants.DEVICE_IEMI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    //Log.e("First Map ", response);
                    //Log.e("Status ", obj.getString("status"));
                    JSONObject object = obj.getJSONObject(Constants.DEVICE_IEMI);
                    lat = object.getString("lat");
                    lng = object.getString("lng");
                    lati = Double.parseDouble(lat);
                    lang = Double.parseDouble(lng);
                    sydney = new LatLng(lati, lang);
                    mMap.addMarker(new MarkerOptions()
                            .position(sydney)
                            .title("SMI bus is here")
                            .icon(markerIcon)
                            .draggable(true)
                            .flat(true)
                            .anchor(0.5f,0.5f)
                            .rotation(0.0f)
                    );
                    Log.d(TAG, "onResponse: "+Constants.DEVICE_IEMI);
                    CameraPosition oldPos = mMap.getCameraPosition();
                    CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                    /*if (obj.getString("status").equals("200")) {
                        JSONArray message = obj.getJSONArray("message");
                        for (int i = 0; i < message.length(); i++) {
                            JSONObject jsonObject = message.getJSONObject(i);
                            lat = jsonObject.getString("bus_lat");
                            lng = jsonObject.getString("bus_lon");
                            lati = Double.parseDouble(lat);
                            lang = Double.parseDouble(lng);
                            sydney = new LatLng(lati, lang);
                            mMap.addMarker(new MarkerOptions().position(sydney).title("SMI bus is here").icon(markerIcon));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                        }
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Catch Error ", e.getMessage());
                }
                //response1=response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.e("Error ", error.getMessage());
                System.out.println(error.getMessage());
            }
        }) {
           /* @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("student_id", Constants.USER_ID);
                return params;
            }*/
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }

    @Override
    public void onLocationChanged(Location location) {
        GeomagneticField field = new GeomagneticField(
                (float)location.getLatitude(),
                (float)location.getLongitude(),
                (float)location.getAltitude(),
                System.currentTimeMillis()
        );
    }

    /*@Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix , event.values);
            float[] orientation = new float[3];
            SensorManager.getOrientation(mRotationMatrix, orientation);
            float bearing = Math.toDegrees(orientation[0]) + mDeclination;
            updateCamera(bearing);
        }
    }*/
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private void updateCamera(float bearing) {
        CameraPosition oldPos = mMap.getCameraPosition();
        CameraPosition pos = CameraPosition.builder(oldPos).bearing(bearing).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }
}
