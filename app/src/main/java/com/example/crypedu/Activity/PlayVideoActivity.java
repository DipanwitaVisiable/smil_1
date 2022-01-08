package com.example.crypedu.Activity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.activity.smi.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.crypedu.Constants.Constants;
import com.example.crypedu.Helper.CallStateListener;
import com.example.crypedu.youtube_api.YoutubeApiKey;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.CHROMELESS;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle.MINIMAL;

public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private String video_url, video_id;
    private YouTubePlayerView youTubePlayerView;
    private RequestQueue requestQueue;

    public TelephonyManager tm;
    private CallStateListener callStateListener;
    private Context context;
    public static YouTubePlayer mYouTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        //Start- Register telephony manager to detect phone call
        context=PlayVideoActivity.this;
        callStateListener=new CallStateListener(context);
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        //End- Register telephony manager to detect phone call

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        video_url=getIntent().getExtras().getString("url");
        video_id=getIntent().getExtras().getString("video_id");
        youTubePlayerView=findViewById(R.id.youtube_player);
//        youTubePlayerView.initialize(YoutubeApiKey.YOUTUBE_API_KEY,this);
        youTubePlayerView.initialize(YoutubeApiKey.YOUTUBE_API_KEY_PART_1 + YoutubeApiKey.YOUTUBE_API_KEY_PART_2
                + YoutubeApiKey.YOUTUBE_API_KEY_PART_3 + YoutubeApiKey.YOUTUBE_API_KEY_PART_4,this);

        requestQueue = Volley.newRequestQueue(this);
        onlineAttendanceApiCall();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.mYouTubePlayer=youTubePlayer;
        mYouTubePlayer.loadVideo(video_url);

        // start to hide related video
      youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {
          mYouTubePlayer.cueVideo(video_url);
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
      });
        // end to hide related video

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    private void onlineAttendanceApiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_SERVER + "online_attendence", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("200"))
                    {

                    }
                    else
                    {
//                        Toast.makeText(context, "No Video found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                /*Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
                pb_loader.setVisibility(View.GONE);*/

            }
        }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> myParams = new HashMap<String, String>();
                myParams.put("student_id", Constants.USER_ID);
//                myParams.put("student_id", "5");
                myParams.put("days_id", Constants.DAYS_ID);
                myParams.put("periods_id", Constants.PERIOD_ID);
                myParams.put("video_id", video_id);
                return myParams;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mYouTubePlayer=null;
        tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
    }

}
