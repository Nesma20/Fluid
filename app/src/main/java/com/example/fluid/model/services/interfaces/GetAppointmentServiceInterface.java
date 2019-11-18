package com.example.fluid.model.services.interfaces;

import com.example.fluid.model.pojo.Item;
import com.example.fluid.model.pojo.Items;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetAppointmentServiceInterface {
    @GET("/ords/pf/api/getClinicSchedule/{clinic_code}")
    Call<Items> getAppointementData(@Path("clinic_code") String code);

    @PUT("/ords/pf/api/call/{slot_id}")
    Call<ResponseBody> callPatient(@Path("slot_id") String slot_id);

    @PUT("/ords/pf/api/checkin/{slot_id}")
    Call<ResponseBody> checkIn(@Path("slot_id") String slot_id);

    @PUT("/ords/pf/api/checkout/{slot_id}")
    Call<ResponseBody> checkout(@Path("slot_id") String slot_id);

    @PUT("/ords/pf/api/arrive/{slot_id}")
    Call<ResponseBody> confirmArrive(@Path("slot_id") String slot_id);
}
