package com.example.messagefake.notification;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender  {

    String userFcmToken;
    String titlee;
    String body;
     Activity mActivity;

//    {
//        "to":"bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
//                "notification":{
//            "title":"Portugal vs. Denmark",
//                    "body":"great match!"
//        }
//    }

     private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAAftsfCf4:APA91bF98M6V6mPVDTcolKPDelCLfdpY2V1kuMBr-ZFYkn0VA0jyGFpTUTHqrOMwJEqYZvx11gMSCK3MxIb5iT5j10LEcwTxg7H61Bn2hDZ2tCt2iMb8uvDcf0XcVfDrgivfOF9MSvqv";
    public FcmNotificationsSender(String userFcmToken, String titlee, String body, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.titlee = titlee;
        this.body = body;
        this.mActivity = mActivity;
    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", titlee);
            notiObject.put("body", body);
          //  notiObject.put("icon", "icon1"); // enter icon that exists in drawable only
            mainObj.put("notification", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response -> {
                // code run is got response
             }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);

                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
