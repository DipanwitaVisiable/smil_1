package com.activity.smi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Activity.LoginActivity;
import com.example.crypedu.Activity.MenuActivity;
import com.example.crypedu.Constants.Constants;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BusMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    static final String TAG = "BroadcastTest";
    Intent intent;
    GoogleMap mMap;
    String lat, lng, servicelat, servicelng;
    Double lati, lang, servicelati, servicelang;
    RequestQueue requestQueue;
    LatLng sydney;
    BitmapDescriptor markerIcon;
    //String url = "http://sudhirmemorialinstituteliluah.com/snrmemorialtrust/webservices/websvc/bus_location";
    //ArrayList<LatLng> points; //added
    //Polyline line; //added
    Geocoder geocoder;
    List<Address> addresses;
    LinearLayout linearLayout;
    private Typeface typeface;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = new Intent(this, BroadcastService.class);
        startService(intent);
        requestQueue = Volley.newRequestQueue(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusMapActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Constants.USER_ID = pref.getString("student_id", "");
            Constants.USER_ROLE = pref.getString("user_role", "");
            Constants.PROFILENAME = pref.getString("profile_name", "");
            Constants.PhoneNo = pref.getString("phoneNo", "");
        } else {
            Intent intent = new Intent(BusMapActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        //textView = (TextView)findViewById(R.id.textView);
        TextView copyRight_textView = findViewById(R.id.copyRight_textView);
        copyRight_textView.setText(Constants.strCopyright);

        linearLayout = findViewById(R.id.linearLayout);
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.BubblegumSans_Regular_font);
        //textView.setTypeface(typeface);
        copyRight_textView.setTypeface(typeface);


        Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_bus);
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
    public void onResume() {
        super.onResume();
        startService(intent);
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
                //String knownName = addresses.get(0).getFeatureName(); // Only if
                TextView txtGeoCoder = findViewById(R.id.txtGeoCoder);
                String comma=",";
                txtGeoCoder.setText(address+comma+postalCode+comma+city+comma+state+comma+country);
                txtGeoCoder.setTypeface(typeface);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMap.addMarker(new MarkerOptions().position(sydney).title("SMI bus is here").icon(markerIcon).draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

//            points.add(sydney); //added

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //  lati = Double.parseDouble(lat);
        // lang = Double.parseDouble(lng);

        // Add a marker in Sydney, Australia, and move the camera.
        StringRequest sr = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "bus_location", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("Status ", obj.getString("status"));
                    if (obj.getString("status").equals("200")) {
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //response1=response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("student_id", Constants.USER_ID);
                return params;
            }


        };
        requestQueue.add(sr);


    }

}
