package com.example.fluid.model.services.interfaces;

import com.example.fluid.model.pojo.AppointmentItems;
import com.example.fluid.model.pojo.LocationList;
import com.example.fluid.model.pojo.ReturnedStatus;
import com.example.fluid.model.pojo.User;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MyServicesInterface {
    @GET("/ords/pf/api/getClinicSchedule/{location_code}")
    Call<AppointmentItems> getAppointementData(@Path("location_code") String code);

    @PUT("/ords/pf/api/call/{location_code}")
    Call<ResponseBody> callPatient(@Path("location_code") String clinicCode);

    @PUT("/ords/pf/api/checkin/{slot_id}")
    Call<ResponseBody> checkIn(@Path("slot_id") String slot_id);

    @PUT("/ords/pf/api/checkout/{slot_id}")
    Call<ResponseBody> checkout(@Path("slot_id") String slot_id);

    @PUT("/ords/pf/api/arrive/{slot_id}")
    Call<ResponseBody> confirmArrive(@Path("slot_id") String slot_id);

    @POST("/ords/pf/api/addUser")
    Call<ReturnedStatus> createUser(@Body User user);
    @GET("/ords/pf/api/getDispatcherLocations/{email}")
    Call <LocationList> getLocationList(@Path("email") String email);

}
