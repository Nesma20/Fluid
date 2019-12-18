package com.example.fluid.ui.activities;

import android.util.Log;

import com.example.fluid.model.pojo.Location;
import com.example.fluid.model.pojo.LocationList;
import com.example.fluid.model.services.repositories.UserRepository;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.utils.App;
import com.example.fluid.utils.PreferenceController;

public class MainViewModel {
UserRepository userRepository = new UserRepository();
    public void clearDataFromSharedPreference(){
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_EMAIL);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_IMAGE_PROFILE_URL);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_USER_NAME);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_USER_ID);

    }
    public String getDataFromSharedPreference(String key){
       return PreferenceController.getInstance(App.getContext()).get(key);
    }
    public void getLocationData(String email, final OnDataChangedCallBackListener onDataChangedCallBackListener){
        userRepository.getLocationList(email, new OnDataChangedCallBackListener<LocationList>() {
            @Override
            public void onResponse(LocationList dataChanged) {
                if (dataChanged !=null && dataChanged.getItems()!= null) {
                for (Location location : dataChanged.getItems()) {
                    Log.i("MainActivity", "facility id : " + location.getFacilityId());
                    Log.i("MainActivity", "session id " + location.getSessionId());
                }
            }
                onDataChangedCallBackListener.onResponse(dataChanged);
            }
        });

    }

}
