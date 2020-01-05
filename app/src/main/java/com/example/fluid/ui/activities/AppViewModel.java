package com.example.fluid.ui.activities;

import androidx.lifecycle.ViewModel;

import com.example.fluid.model.services.FirebaseDatabaseService;
import com.example.fluid.model.services.interfaces.RetrofitInstance;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;

public class AppViewModel {

    public void getIpAndPortFromFirebase(){
        FirebaseDatabaseService.getPortAndIpAddress(new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean b) {
                if (b.booleanValue())
                    RetrofitInstance.getService();
            }
        });
    }
}
