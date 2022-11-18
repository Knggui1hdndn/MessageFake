package com.example.messagefake.videoCall;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messagefake.R;
import com.example.messagefake.chat;
import com.stringee.call.StringeeCall2;
import com.stringee.video.StringeeVideoTrack;

import org.json.JSONObject;

public class IncomingCallActivity extends AppCompatActivity {
    private StringeeCall2 stringeeCall2;
    private FrameLayout mLocalViewContainer;
    private FrameLayout mRemoteViewContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);
        mLocalViewContainer=findViewById(R.id. v_local);
        mRemoteViewContainer=findViewById(R.id.v_remote);
        String callId = getIntent().getStringExtra("call_id");
        stringeeCall2 = chat.callsMap.get(callId);
        stringeeCall2.setVideoCall(true);
        stringeeCall2.setCallListener(new StringeeCall2.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall2 stringeeCall2, StringeeCall2.SignalingState signalingState, String s, int i, String s1) {

            }

            @Override
            public void onError(StringeeCall2 stringeeCall2, int i, String s) {

            }

            @Override
            public void onHandledOnAnotherDevice(StringeeCall2 stringeeCall2, StringeeCall2.SignalingState signalingState, String s) {

            }

            @Override
            public void onMediaStateChange(StringeeCall2 stringeeCall2, StringeeCall2.MediaState mediaState) {

            }

            @Override
            public void onLocalStream(StringeeCall2 stringeeCall2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (stringeeCall2.isVideoCall()) {
                            mLocalViewContainer.addView(stringeeCall2.getLocalView());
                            stringeeCall2.renderLocalView(true);
                        }
                    }
                });
            }

            @Override
            public void onRemoteStream(StringeeCall2 stringeeCall2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (stringeeCall2.isVideoCall()) {
                            mRemoteViewContainer.addView(stringeeCall2.getRemoteView());
                            stringeeCall2.renderRemoteView(true);
                        }
                    }
                });
            }

            @Override
            public void onVideoTrackAdded(StringeeVideoTrack stringeeVideoTrack) {

            }

            @Override
            public void onVideoTrackRemoved(StringeeVideoTrack stringeeVideoTrack) {

            }

            @Override
            public void onCallInfo(StringeeCall2 stringeeCall2, JSONObject jsonObject) {

            }

            @Override
            public void onTrackMediaStateChange(String s, StringeeVideoTrack.MediaType mediaType, boolean b) {

            }
        });
       findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringeeCall2.answer();
             }
        });
    }
}