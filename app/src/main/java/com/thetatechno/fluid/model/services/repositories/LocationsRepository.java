package com.thetatechno.fluid.model.services.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluid.model.pojo.Appointement;
import com.thetatechno.fluid.model.pojo.AppointmentItems;
import com.thetatechno.fluid.model.pojo.FacilityList;
import com.thetatechno.fluid.model.services.interfaces.MyServicesInterface;
import com.thetatechno.fluid.model.services.interfaces.RetrofitInstance;
import com.thetatechno.fluid.ui.EspressoTestingIdlingResource;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsRepository {
    private static final String TAG = LocationsRepository.class.getSimpleName();
    private MutableLiveData<FacilityList> facilityListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData getAppointmentListData(String id,String langId) {
        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<FacilityList> call = myServicesInterface.getFacilities(id, langId);
        call.enqueue(new Callback<FacilityList>() {
            @Override
            public void onResponse(Call<FacilityList> call, Response<FacilityList> response) {
                if (response.isSuccessful()) {
                   Log.i(TAG,"*********************** on response ********************");

                    if(response.body() != null ) {

                        facilityListMutableLiveData.setValue(response.body());
                    }
                } else {
                    Log.i(TAG,"no access to resources");
                    facilityListMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<FacilityList> call, Throwable t) {
                call.cancel();
                facilityListMutableLiveData.setValue(null);
            }
        });
        return facilityListMutableLiveData;
    }

}
