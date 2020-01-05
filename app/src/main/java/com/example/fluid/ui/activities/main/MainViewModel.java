package com.example.fluid.ui.activities.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fluid.model.pojo.Location;
import com.example.fluid.model.pojo.LocationList;
import com.example.fluid.model.services.repositories.UserRepository;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.utils.App;
import com.example.fluid.utils.PreferenceController;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainActivity";
UserRepository userRepository = new UserRepository();
    private MutableLiveData<String> fullNameLivedata;
    private MutableLiveData<String> emailLiveData;
    private MutableLiveData<String> imageUrlLiveData;
    public void clearDataFromSharedPreference(){
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_EMAIL);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_IMAGE_PROFILE_URL);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_USER_NAME);
        PreferenceController.getInstance(App.getContext()).clear(PreferenceController.PREF_USER_ID);

    }
    public MutableLiveData<String> getFullName(){
        if (fullNameLivedata == null) {
            fullNameLivedata = new MutableLiveData<String>();
        }
        String name = PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_USER_NAME);
        fullNameLivedata.setValue(name);
        return fullNameLivedata;
    }
    public MutableLiveData<String> getEmail(){
        if (emailLiveData == null) {
            emailLiveData = new MutableLiveData<String>();
        }
        String email = PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_EMAIL);
        emailLiveData.setValue(email);
        return emailLiveData;
    }
    public MutableLiveData<String> getImageUrl(){
        if (imageUrlLiveData == null) {
            imageUrlLiveData = new MutableLiveData<String>();
        }
        String imageUrl = PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_IMAGE_PROFILE_URL);
        imageUrlLiveData.setValue(imageUrl);
        return imageUrlLiveData;
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
                    Log.i(TAG, "facility id : " + location.getFacilityId());
                    Log.i(TAG, "session id " + location.getSessionId());
                }
            }
                onDataChangedCallBackListener.onResponse(dataChanged);
            }
        });

    }

}