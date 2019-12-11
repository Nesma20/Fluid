package com.example.fluid.model.services.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.fluid.model.pojo.AppointmentItems;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.model.pojo.Appointement;
import com.example.fluid.model.services.interfaces.GetAppointmentServiceInterface;
import com.example.fluid.model.services.interfaces.RetrofitInstance;
import com.example.fluid.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepository {
    List<Appointement> myItemList;
    private MutableLiveData<List<Appointement>> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData getAllData(String clinicCode) {
        myItemList = new ArrayList<>();
        GetAppointmentServiceInterface getAppointmentServiceInterface = (GetAppointmentServiceInterface) RetrofitInstance.getService();
        Call<AppointmentItems> call = getAppointmentServiceInterface.getAppointementData(clinicCode);
        call.enqueue(new Callback<AppointmentItems>() {
            @Override
            public void onResponse(Call<AppointmentItems> call, Response<AppointmentItems> response) {
                if (response.isSuccessful()) {
                    System.out.println("*********************** on response ********************");
                    AppointmentItems appointmentItems = response.body();
                    myItemList = (ArrayList<Appointement>) appointmentItems.getItems();
                    for (Appointement item : myItemList) {
                        Log.i("appointmentItems", "  ************** item arrival time ************   " + item.getArrivalTime() + "slot " + item.getSlotId());
                        Log.i("appointmentItems", "  ************** item scheduled time ************   " + item.getScheduledTime());
                        Log.i("appointmentItems", "  ************** item calling time ************   " + item.getCallingTime());
                        Log.i("appointmentItems", "  ************** item checkin time ************   " + item.getCheckinTime());
                        Log.i("appointmentItems", "  ************** item expected time ************   " + item.getExpectedTime());
                    }
                    mutableLiveData.setValue(myItemList);
                } else {
                    System.out.println("no access to resources");
                }
            }

            @Override
            public void onFailure(Call<AppointmentItems> call, Throwable t) {
                call.cancel();
            }
        });
        return mutableLiveData;
    }

    public void callPatient(String clinicCode, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        GetAppointmentServiceInterface getAppointmentServiceInterface = (GetAppointmentServiceInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = getAppointmentServiceInterface.callPatient(clinicCode);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK) {
                    onDataChangedCallBackListener.onResponse(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                onDataChangedCallBackListener.onResponse(false);
                call.cancel();
            }
        });
    }

    public void checkIn(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        GetAppointmentServiceInterface getAppointmentServiceInterface = (GetAppointmentServiceInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = getAppointmentServiceInterface.checkIn(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK)
                    onDataChangedCallBackListener.onResponse(true);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(false);
            }
        });
    }

    public void checkOut(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        GetAppointmentServiceInterface getAppointmentServiceInterface = (GetAppointmentServiceInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = getAppointmentServiceInterface.checkout(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK)
                    onDataChangedCallBackListener.onResponse(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(false);
            }
        });


    }

    public void confirmArrive(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        GetAppointmentServiceInterface getAppointmentServiceInterface = (GetAppointmentServiceInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = getAppointmentServiceInterface.confirmArrive(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK)
                    onDataChangedCallBackListener.onResponse(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(false);
            }
        });
    }


}
