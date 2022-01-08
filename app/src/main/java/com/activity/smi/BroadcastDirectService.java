package com.activity.smi;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sudipta on 1/7/2018.
 */

public class BroadcastDirectService extends Service {

    private static String lat;
    private static String lng;
    private static String angle;
    private static String preLat;
    private static String preLong;
    private static final String TAG = "BroadcastDirectService";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //Constants.BUS_FLAG = 1;
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 2000); // 4 seconds
        }
    };

    private void DisplayLoggingInfo() {
        //Log.e(TAG, "entered DisplayLoggingInfo");

        RequestQueue requestQueue;

        //String url = "http://sudhirmemorialinstituteliluah.com/snrmemorialtrust/webservices/websvc/bus_location";

        requestQueue = Volley.newRequestQueue(this);

        //Constants.BASE_SERVER + "bus_location_dir"
        StringRequest request = new StringRequest(Request.Method.GET, Constants.BUS_LOCATION+Constants.DEVICE_IEMI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    /*if (obj.getString("status").equals("200")) {
                        JSONArray message = obj.getJSONArray("message");
                        for (int i = 0; i < message.length(); i++) {
                            JSONObject jsonObject = message.getJSONObject(i);
                            lat = jsonObject.getString("bus_lat");
                            lng = jsonObject.getString("bus_lon");
                        }
                    }*/
                    //Log.e("Map", response);
                    JSONObject object = obj.getJSONObject(Constants.DEVICE_IEMI);
                    lat = object.getString("lat");
                    lng = object.getString("lng");
                    angle = object.getString("angle");
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    intent.putExtra("counter", String.valueOf(++counter));
                    //Log.e("aas 1", angle);
                    intent.putExtra("angle1", angle);
                    //Log.e("aas", angle);
                    sendBroadcast(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //response1=response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("bus_id", Constants.BUS_ID);
                return params;
            }*/

        };
        requestQueue.add(request);
        // intent.putExtra("time", new Date().toLocaleString());
        /*if (lat == null || preLat.equalsIgnoreCase(lat)){
            lat = Constants.BUS_LAT;
        }
        if (lng == null || preLong.equalsIgnoreCase(lng)){
            lng = Constants.BUS_LONG;
        }
        if (lng != null)
            preLong = lng;
        if (lat != null)
            preLat = lat;*/
    }

    @Override
    public void onDestroy() {
        //Constants.BUS_FLAG = 0;
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }
}
