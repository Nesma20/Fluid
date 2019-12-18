package com.example.fluid.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.model.pojo.Appointement;
import com.example.fluid.model.services.repositories.AppointmentRepository;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    AppointmentRepository appointmentRepository = new AppointmentRepository();
    MutableLiveData myliveData = new MutableLiveData();
    ArrayList<Appointement> itemArrayList = new ArrayList<>();
    public LiveData<List<Appointement>> getAllItems(String clinicCode){
        myliveData = appointmentRepository.getAllData(clinicCode);
        itemArrayList = (ArrayList<Appointement>) myliveData.getValue();
        return myliveData;
    }
    public void updateWithCalling(final String sessionId, final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
        appointmentRepository.callPatient(sessionId, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean dataChanged) {
                onDataChangedCallBackListener.onResponse(dataChanged);
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