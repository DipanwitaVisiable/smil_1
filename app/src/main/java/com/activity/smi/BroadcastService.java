package com.activity.smi;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
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

public class BroadcastService extends Service {

    private String lat;
    private String lng;
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
    private final Handler handler = new Handler();
    private String angle;
    Intent intent;
    int counter = 0;

    public BroadcastService() {
    }

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
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second

    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 2000); // 2 seconds
        }
    };

    private void DisplayLoggingInfo() {
        Log.e(TAG, "entered DisplayLoggingInfo");


        RequestQueue requestQueue;

        //String url = "http://sudhirmemorialinstituteliluah.com/snrmemorialtrust/webservices/websvc/bus_location";


        requestQueue = Volley.newRequestQueue(this);

        // Constants.BASE_SERVER + "bus_location"
        StringRequest sr = new StringRequest(Request.Method.GET, Constants.BUS_LOCATION+Constants.DEVICE_IEMI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("Map", response);
                    /*if (obj.getString("status").equals("200")) {
                        JSONArray message = obj.getJSONArray("message");
                        for (int i = 0; i < message.length(); i++) {
                            JSONObject jsonObject = message.getJSONObject(i);
                            lat = jsonObject.getString("bus_lat");
                            lng = jsonObject.getString("bus_lon");
                        }
                    }*/
                    JSONObject object = obj.getJSONObject(Constants.DEVICE_IEMI);
                    lat = object.getString("lat");
                    lng = object.getString("lng");
                    angle = object.getString("angle");
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    intent.putExtra("angle1", angle);
                    intent.putExtra("counter", String.valueOf(++counter));
                    sendBroadcast(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Catch Error", e.getMessage());
                }
                //response1=response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error", error.getMessage());
            }
        }) {
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", Constants.USER_ID);
                return params;
            }*/

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

        // intent.putExtra("time", new Date().toLocaleString());

    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }
}
