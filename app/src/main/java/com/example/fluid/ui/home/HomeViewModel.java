package com.example.fluid.ui.home;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.fluid.model.pojo.Item;
import com.example.fluid.model.services.repositories.AppointmentRepository;
import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    AppointmentRepository appointmentRepository = new AppointmentRepository();
    MutableLiveData myliveData = new MutableLiveData();

    ArrayList<Item> itemArrayList = new ArrayList<>();
    public LiveData<List<Item>> getAllItems(String clinicCode){
        myliveData = appointmentRepository.getAllData(clinicCode);
        itemArrayList = (ArrayList<Item>) myliveData.getValue();
        return myliveData;
    }
    public void updateWithCalling(String clinicCode, String slotId){
        appointmentRepository.callPatient(slotId);

        getAllItems(clinicCode);
    }
    public void updateWithCheckIn(String clinicCode, String slotId){
        appointmentRepository.checkIn(slotId);
        getAllItems(clinicCode);
    }
    public void updateWithCheckOut(String clinicCode, String slotId){
        appointmentRepository.checkOut(slotId);
        getAllItems(clinicCode);
    }
    public void confirmArrival(String clinicCode,String slotId){
        appointmentRepository.confirmArrive(slotId);
        getAllItems(clinicCode);
    }

}