package com.example.fluid.model.services.repositories;

import com.example.fluid.model.pojo.LocationList;
import com.example.fluid.model.pojo.ReturnedStatus;
import com.example.fluid.model.pojo.User;
import com.example.fluid.model.services.interfaces.MyServicesInterface;
import com.example.fluid.model.services.interfaces.RetrofitInstance;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.ui.listeners.UserHandler;
import com.example.fluid.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRepository {
    //TODO: call sign in method
    boolean isUserCreated = false;
   ReturnedStatus status = null;
    LocationList locationList = new LocationList();
    public void createNewUser(final User user, final UserHandler userHandler){
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<ReturnedStatus> call = myServicesInterface.createUser(user);
        call.enqueue(new Callback<ReturnedStatus>() {

            @Override
            public void onResponse(Call<ReturnedStatus> call, Response<ReturnedStatus> response) {
                if (response.code() == Constants.STATE_OK) {

                    status = response.body();
                    userHandler.onUserAddedHandler(status);
                }
                else
                {
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

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<LocationList> call = myServicesInterface.getLocationList(email);
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
