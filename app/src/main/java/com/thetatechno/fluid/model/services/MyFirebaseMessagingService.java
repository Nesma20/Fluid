package com.thetatechno.fluid.model.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
private static final String TAG = "MyFirebaseMessaging" ;
String notificationTitle = "";
    String notificationData = "";
    String notificationMessage = "";
    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {


            notificationData = remoteMessage.getData().toString();
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationMessage = remoteMessage.getNotification().getBody();
        }
        catch (NullPointerException ex){
            Log.e(TAG,"on messageReceived : nullPointerMessage  "+ex.getMessage());
        }
        Log.i(TAG,"on messageReceived : Title: "+notificationTitle);
        Log.i(TAG,"on messageReceived : Data:"+notificationData);
        Log.i(TAG,"on messageReceived : Body: "+notificationMessage);

    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
      Log.i(TAG,"Token is "+token);
    }
}
