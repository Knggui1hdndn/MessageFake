package com.example.messagefake.videoCall;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messagefake.R;
import com.example.messagefake.chat;
import com.stringee.call.StringeeCall2;
import com.stringee.video.StringeeVideoTrack;

import org.json.JSONObject;

public class OutgoingCallActivity extends AppCompatActivity {
    private StringeeCall2 stringeeCall2;
//    private StringeeAudioManager audioManager;
    private String from, to;
    private FrameLayout mLocalViewContainer;
    private FrameLayout mRemoteViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_call);
        mLocalViewContainer = findViewById(R.id.v_local);
        mRemoteViewContainer = findViewById(R.id.v_remote);

        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        stringeeCall2 = new StringeeCall2(chat.client, from, to);
        stringeeCall2.setVideoCall(true);

         //Triển khai giao diện StringeeCall.StringeeCallListener để tương tác với cuộc gọi:
        stringeeCall2.setCallListener(new StringeeCall2.StringeeCallListener() {
            @Override
            public void onSignalingStateChange(StringeeCall2 stringeeCall2, StringeeCall2.SignalingState signalingState, String s, int i, String s1) {
                if (signalingState == StringeeCall2.SignalingState.ENDED) {
                    finish();
                }
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
            public void onLocalStream(StringeeCall2 stringeeCall2) {//để hiển thị video của người gọi
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
            public void onRemoteStream(StringeeCall2 stringeeCall2) {//để hiển thị video của người nhận
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
//        Khi máy khách không thực hiện được cuộc gọi, phương thức onError (StringeeCall stringeeCall, int code, String description) sẽ được gọi.
//        Khi máy khách thực hiện cuộc gọi thành công, cuộc gọi sẽ đi qua rất nhiều trạng thái như: đang gọi, đổ chuông, đã trả lời, đã kết thúc, đang bận
//        Mỗi trạng thái của cuộc gọi, onSignalingStateChange (cuộc gọi StringeeCall cuối cùng, StringeeCall cuối cùng. , Phương thức chuỗi lý do, int SiroCode, String SipReason) được gọi.
//        Khi luồng phương tiện của cuộc gọi được kết nối hoặc ngắt kết nối, phương thức onMediaStateChange (StringeeCall stringeeCall, StringeeCall.MediaState mediaState) phương thức được gọi.
//        Khi cuộc gọi được xử lý trên một thiết bị khác, phương thức onHandledOnAosystemDevice (StringeeCall stringeeCall, StringeeCall.SignalingState signalState, String description)
//        được gọi. Một cuộc gọi thực sự được thiết lập khi nó được trả lời và trạng thái của phương tiện là MediaState.CONNECTED.
// Khởi tạo trình quản lý âm thanh để quản lý định tuyến âm thanh

         stringeeCall2.makeCall();

    }
}