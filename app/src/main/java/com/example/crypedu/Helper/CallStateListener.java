package com.example.crypedu.Helper;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.crypedu.Activity.CustomYouTubePlayerActivity;
import com.example.crypedu.Activity.PlayVideoActivity;
import com.example.crypedu.Activity.SmallQuizTestActivity;


public class CallStateListener extends PhoneStateListener {
    private Context context;

    public CallStateListener(Context context) {
        this.context=context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:

                if (SmallQuizTestActivity.countDownTimer!=null)
                  SmallQuizTestActivity.countDownTimer.pause();

                /*if (PlayVideoActivity.mYouTubePlayer!=null)
                    PlayVideoActivity.mYouTubePlayer.pause();*/

                if (CustomYouTubePlayerActivity.mYouTubePlayer!=null)
                    CustomYouTubePlayerActivity.mYouTubePlayer.pause();
                break;

            case TelephonyManager.CALL_STATE_IDLE:

                if (SmallQuizTestActivity.countDownTimer!=null)
                    SmallQuizTestActivity.countDownTimer.resume();

                /*if (PlayVideoActivity.mYouTubePlayer!=null)
                    PlayVideoActivity.mYouTubePlayer.play();*/

                if (CustomYouTubePlayerActivity.mYouTubePlayer!=null)
                    CustomYouTubePlayerActivity.mYouTubePlayer.play();
                break;
        }
    }
}
