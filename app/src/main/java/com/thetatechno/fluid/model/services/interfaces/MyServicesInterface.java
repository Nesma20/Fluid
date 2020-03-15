package com.thetatechno.fluid.model.services.interfaces;

import com.thetatechno.fluid.model.pojo.AppointmentItems;
import com.thetatechno.fluid.model.pojo.FacilityList;
import com.thetatechno.fluid.model.pojo.LocationList;
import com.thetatechno.fluid.model.pojo.ReturnedStatus;
import com.thetatechno.fluid.model.pojo.User;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyServicesInterface {
    @GET("/ords/fluid/api/getClinicSchedule/{location_code}")
    Call<AppointmentItems> getAppointmentListData(@Path("location_code") String code);

    @PUT("/ords/fluid/api/call/{session_id}")
    Call<ReturnedStatus> callPatient(@Path("session_id") String session_id);

    @PUT("/ords/fluid/api/checkin/{slot_id}")
    Call<ResponseBody> checkIn(@Path("slot_id") String slotId);

    @PUT("/ords/fluid/api/checkout/{slot_id}")
    Call<ResponseBody> checkout(@Path("slot_id") String slotId);

    @PUT("/ords/fluid/api/arrive/{slot_id}")
    Call<ResponseBody> confirmArrive(@Path("slot_id") String slotId);

    @POST("/ords/fluid/api/addUser")
    Call<ReturnedStatus> createUser(@Body User user);

    @GET("/ords/fluid/api/getDispatcherLocations/{email}")
    Call<LocationList> getCurrentRunningLocationList(@Path("email") String email);

    @GET("/ords/fluid/api/getAgentFacilities/{id}")
    Call<FacilityList> getFacilities(@Path("id") String id, @Query("langId") String langId);
}
