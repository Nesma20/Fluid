package com.thetatechno.fluid.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluid.model.pojo.AppointmentItems;
import com.thetatechno.fluid.model.pojo.ReturnedStatus;
import com.thetatechno.fluid.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluid.model.services.repositories.AppointmentRepository;

public class HomeViewModel extends ViewModel {
    AppointmentRepository appointmentRepository = new AppointmentRepository();
    MutableLiveData myliveData = new MutableLiveData();
    public LiveData<AppointmentItems> getAllItems(String clinicCode){
        EspressoTestingIdlingResource.increment();
        myliveData = appointmentRepository.getAppointmentListData(clinicCode);
        return myliveData;
    }
    public LiveData<AppointmentItems> getAllProviderItems(String doctorCode){
        EspressoTestingIdlingResource.increment();
        myliveData = appointmentRepository.getProviderAppointment(doctorCode);
        return myliveData;
    }
    public void updateWithCalling(final String sessionId, final OnDataChangedCallBackListener<Integer> onDataChangedCallBackListener){
        appointmentRepository.callPatient(sessionId, new OnDataChangedCallBackListener<ReturnedStatus>() {
            @Override
            public void onResponse(ReturnedStatus status) {
                Log.i("AppointmentListFragment","status return from call "+ status.getReturnStatus().intValue());
                if(status.getReturnStatus().intValue()>0)
                onDataChangedCallBackListener.onResponse(status.getReturnStatus());

            }
        });


    }
    public void updateWithCheckIn(String slotId, final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
        appointmentRepository.checkIn(slotId, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean dataChanged) {
                    onDataChangedCallBackListener.onResponse(dataChanged);
            }
        });

    }
    public void updateWithCheckOut( String slotId,final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
        appointmentRepository.checkOut(slotId,new OnDataChangedCallBackListener<Boolean>(){
            @Override
            public void onResponse(Boolean dataChanged) {
                onDataChangedCallBackListener.onResponse(dataChanged);

            }
        });

    }
    public void confirmArrival(String slotId, final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
        appointmentRepository.confirmArrive(slotId, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean dataChanged) {
                onDataChangedCallBackListener.onResponse(dataChanged);
            }
        });

    }
}