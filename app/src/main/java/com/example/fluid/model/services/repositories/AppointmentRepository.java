package com.example.fluid.model.services.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.fluid.model.pojo.AppointmentItems;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.model.pojo.Appointement;
import com.example.fluid.model.services.interfaces.MyServicesInterface;
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
    boolean responseReturned = true;
    boolean failureOnResponse = false;
    private MutableLiveData<AppointmentItems> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData getAllData(String clinicCode) {
        myItemList = new ArrayList<>();
        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<AppointmentItems> call = myServicesInterface.getAppointementData(clinicCode);
        call.enqueue(new Callback<AppointmentItems>() {
            @Override
            public void onResponse(Call<AppointmentItems> call, Response<AppointmentItems> response) {
                if (response.isSuccessful()) {
                    System.out.println("*********************** on response ********************");
                    AppointmentItems appointmentItems = response.body();
                    if(appointmentItems != null ) {
                        myItemList = (ArrayList<Appointement>) appointmentItems.getItems();
                        if(appointmentItems.getItems()!=null)
                        for (Appointement item : myItemList) {
                            Log.i("appointmentItems", "  ************** item arrival time ************   " + item.getArrivalTime() + "slot " + item.getSlotId());
                            Log.i("appointmentItems", "  ************** item scheduled time ************   " + item.getScheduledTime());
                            Log.i("appointmentItems", "  ************** item calling time ************   " + item.getCallingTime());
                            Log.i("appointmentItems", "  ************** item checkin time ************   " + item.getCheckinTime());
                            Log.i("appointmentItems", "  ************** item expected time ************   " + item.getExpectedTime());
                            Log.i("appointmentItems", "  ************** item active booking ************   " + item.getActiveBooking());

                        }
                        mutableLiveData.setValue(appointmentItems);
                    }
                } else {
                    System.out.println("no access to resources");
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AppointmentItems> call, Throwable t) {
                call.cancel();
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }

    public void callPatient(String clinicCode, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = myServicesInterface.callPatient(clinicCode);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK) {
                    onDataChangedCallBackListener.onResponse(responseReturned);
                }
                else {
                    onDataChangedCallBackListener.onResponse(failureOnResponse);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                onDataChangedCallBackListener.onResponse(failureOnResponse);
                call.cancel();
            }
        });
    }

    public void checkIn(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = myServicesInterface.checkIn(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK)
                    onDataChangedCallBackListener.onResponse(responseReturned);
                else
                    onDataChangedCallBackListener.onResponse(failureOnResponse);

            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(failureOnResponse);
            }
        });
    }

    public void checkOut(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = myServicesInterface.checkout(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK)
                    onDataChangedCallBackListener.onResponse(responseReturned);
                else
                    onDataChangedCallBackListener.onResponse(failureOnResponse);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(failureOnResponse);
            }
        });


    }

    public void confirmArrive(String slotId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        MyServicesInterface myServicesInterface = (MyServicesInterface) RetrofitInstance.getService();
        Call<ResponseBody> call = myServicesInterface.confirmArrive(slotId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Constants.STATE_OK)
                    onDataChangedCallBackListener.onResponse(responseReturned);
                else
                    onDataChangedCallBackListener.onResponse(failureOnResponse);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(failureOnResponse);
            }
        });
    }


}
