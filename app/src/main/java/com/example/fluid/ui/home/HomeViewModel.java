package com.example.fluid.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fluid.listeners.OnDataChangedCallBackListener;
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
    public void updateWithCalling(final String clinicCode, String slotId){
        appointmentRepository.callPatient(slotId, new OnDataChangedCallBackListener() {
            @Override
            public void onResponse(boolean isDataChanged) {
                if(isDataChanged)
                    getAllItems(clinicCode);
            }
        });


    }
    public void updateWithCheckIn(final String clinicCode, String slotId){
        appointmentRepository.checkIn(slotId, new OnDataChangedCallBackListener() {
            @Override
            public void onResponse(boolean isDataChanged) {
                if(isDataChanged)
                    getAllItems(clinicCode);
            }
        });

    }
    public void updateWithCheckOut(final String clinicCode, String slotId){
        appointmentRepository.checkOut(slotId,new OnDataChangedCallBackListener(){
            @Override
            public void onResponse(boolean isDataChanged) {
                if(isDataChanged)
                    getAllItems(clinicCode);
            }
        });

    }
    public void confirmArrival(final String clinicCode,String slotId){
        appointmentRepository.confirmArrive(slotId, new OnDataChangedCallBackListener() {
            @Override
            public void onResponse(boolean isDataChanged) {
                getAllItems(clinicCode);
            }
        });

    }
}