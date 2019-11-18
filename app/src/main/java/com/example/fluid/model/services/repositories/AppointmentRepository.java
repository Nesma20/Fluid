package com.example.fluid.model.services.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.fluid.model.pojo.Item;
import com.example.fluid.model.pojo.Items;
import com.example.fluid.model.services.interfaces.GetAppointmentServiceInterface;
import com.example.fluid.model.services.interfaces.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepository {
    List<Item> myItemList;
    private MutableLiveData<List<Item>> mutableLiveData = new MutableLiveData<>();
    boolean isCheckedOut = false;

    public MutableLiveData getAllData(String clinicCode) {

        myItemList = new ArrayList<>();
        GetAppointmentServiceInterface getAppointmentServiceInterface = RetrofitInstance.getService();
        Call<Items> call = getAppointmentServiceInterface.getAppointementData(clinicCode);
        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                if (response.isSuccessful()) {
                    System.out.println("*********************** on response ********************");
                    Items items = response.body();
                    myItemList = (ArrayList<Item>) items.getItems();
                    for (Item item : myItemList) {
                        Log.i("items", "  ************** item arrival time ************   " + item.getArrivalTime()+"slot "+item.getSlotId());
                        Log.i("items", "  ************** item scheduled time ************   " + item.getScheduledTime());
                        Log.i("items", "  ************** item calling time ************   " + item.getCallingTime());
                        Log.i("items", "  ************** item checkin time ************   " + item.getCheckinTime());
                        Log.i("items", "  ************** item expected time ************   " + item.getExpectedTime());
                    }
                    mutableLiveData.setValue(myItemList);
                } else {
                    System.out.println("no access to resources");
                }
            }

            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                call.cancel();
            }
        });
return mutableLiveData;
    }
    public void callPatient(String slotId){
        GetAppointmentServiceInterface getAppointmentServiceInterface = RetrofitInstance.getService();
        Call<ResponseBody> call = getAppointmentServiceInterface.callPatient(slotId);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200)
                    System.out.println("updated ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });



    }

    public void checkIn(String slotId) {

        GetAppointmentServiceInterface getAppointmentServiceInterface = RetrofitInstance.getService();
        Call<ResponseBody> call = getAppointmentServiceInterface.checkIn(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200)
                    System.out.println("entered check in ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
    }
    public boolean checkOut(String slotId){
        GetAppointmentServiceInterface getAppointmentServiceInterface = RetrofitInstance.getService();
        Call<ResponseBody> call = getAppointmentServiceInterface.checkout(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200)
                    System.out.println("entered check out ");
                isCheckedOut = true;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });

    return isCheckedOut;
    }
    public void  confirmArrive(String slotId){
        GetAppointmentServiceInterface getAppointmentServiceInterface = RetrofitInstance.getService();
        Call<ResponseBody> call = getAppointmentServiceInterface.confirmArrive(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200)
                    System.out.println("arrive confirmed ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
    }

}
