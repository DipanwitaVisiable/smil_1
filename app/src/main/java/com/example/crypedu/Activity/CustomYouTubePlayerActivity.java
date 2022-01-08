package com.example.crypedu.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomYouTubePlayerActivity extends AppCompatActivity {

  private String video_url, video_id;
  private RequestQueue requestQueue;
  private YouTubePlayerView youTubePlayerView;
  public TelephonyManager tm;
  private CallStateListener callStateListener;
  private Context context;
  public static YouTubePlayer mYouTubePlayer;
  private RelativeLayout rl_view;
  private ImageView iv_play;
  private Button btn_play;
//  private RelativeLayout btn_play;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_youtube_player);

    //Start- Register telephony manager to detect phone call
    context=CustomYouTubePlayerActivity.this;
    callStateListener=new CallStateListener(context);
    tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    //End- Register telephony manager to detect phone call

    video_url=getIntent().getExtras().getString("url");
    video_id=getIntent().getExtras().getString("video_id");
    youTubePlayerView = findViewById(R.id.youtube_player_view);
    /*rl_view = findViewById(R.id.rl_view);
    iv_play = findViewById(R.id.iv_play);*/
    btn_play = findViewById(R.id.btn_play);
    initYouTubePlayerView();

    requestQueue = Volley.newRequestQueue(this);
    onlineAttendanceApiCall();
  }

  private void initYouTubePlayerView() {
    getLifecycle().addObserver(youTubePlayerView);
    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
      @Override
      public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        mYouTubePlayer=youTubePlayer;
        YouTubePlayerUtils.loadOrCueVideo (youTubePlayer, getLifecycle(), video_url,0f);

      }

      @Override
      public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
        super.onStateChange(youTubePlayer, state);

        if (state.name().trim().toString().equals("PAUSED"))
        {
          btn_play.setVisibility(View.VISIBLE);
        }
        else if (state.name().trim().toString().equals("PLAYING"))
        {
          btn_play.setVisibility(View.GONE);
        }
        else if (state.name().trim().toString().equals("ENDED"))
        {
          onBackPressed();
        }

      }


    });

    btn_play.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        btn_play.setVisibility(View.GONE);
        mYouTubePlayer.play();
      }
    });
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
