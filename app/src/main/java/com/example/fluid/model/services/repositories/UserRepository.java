package com.example.fluid.model.services.repositories;

import android.content.Context;
import android.util.Log;

import com.example.fluid.model.pojo.Location;
import com.example.fluid.model.pojo.LocationList;
import com.example.fluid.model.pojo.ReturnedStatus;
import com.example.fluid.model.pojo.User;
import com.example.fluid.model.services.interfaces.GetAppointmentServiceInterface;
import com.example.fluid.model.services.interfaces.RetrofitInstance;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.ui.listeners.UserHandler;
import com.example.fluid.utils.Constants;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRepository {
    //TODO: call sign in method
    boolean isUserCreated = false;
   ReturnedStatus status = null;
    LocationList locationList = new LocationList();
    public void createNewUser(final User user, final UserHandler userHandler){

        GetAppointmentServiceInterface getAppointmentServiceInterface = RetrofitInstance.getService();
        Call<ReturnedStatus> call = getAppointmentServiceInterface.createUser(user);
        call.enqueue(new Callback<ReturnedStatus>() {

            @Override
            public void onResponse(Call<ReturnedStatus> call, Response<ReturnedStatus> response) {
                if (response.code() == Constants.STATE_OK) {

                    status = response.body();
                    userHandler.onUserAddedHandler(status);
                }
            }

            @Override
            public void onFailure(Call<ReturnedStatus> call, Throwable t) {
                call.cancel();
                userHandler.onUserAddedHandler(status);
            }

        });



    }
    public void getLocationList (final String email, final OnDataChangedCallBackListener<LocationList> onDataChangedCallBackListener){

        GetAppointmentServiceInterface getAppointmentServiceInterface = RetrofitInstance.getService();
        Call<LocationList> call = getAppointmentServiceInterface.getLocationList(email);
        call.enqueue(new Callback<LocationList>() {

            @Override
            public void onResponse(Call<LocationList> call, Response<LocationList> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

                   locationList =response.body();
                   onDataChangedCallBackListener.onResponse(response.body());

                }
            }

            @Override
            public void onFailure(Call<LocationList> call, Throwable t) {
                call.cancel();
                locationList = null;
                onDataChangedCallBackListener.onResponse(locationList);


            }

        });

    }



}
