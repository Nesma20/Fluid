package com.example.fluid.model.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fluid.model.pojo.Configuration;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.utils.App;
import com.example.fluid.utils.Constants;
import com.example.fluid.utils.PreferenceController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseService {
    private static final String TAG = "FirebaseDB";
    public static  void  getPortAndIpAddress(final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("configuration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Configuration configuration =  dataSnapshot.getValue(Configuration.class);
                Log.i(TAG, configuration.getIp());
                saveIpAndPortInSharedPreferences(configuration.getIp(),configuration.getPort());
                onDataChangedCallBackListener.onResponse(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                onDataChangedCallBackListener.onResponse(false);
            }
        });

    }
    private static void saveIpAndPortInSharedPreferences(String ip, String port){
        PreferenceController.getInstance(App.getContext()).persist(Constants.BASE_URL, ip +":"+port);
        PreferenceController.getInstance(App.getContext()).persist(Constants.IP, ip);
        PreferenceController.getInstance(App.getContext()).persist(Constants.PORT, port);
    }
}
