package com.example.fluid.model.services.interfaces;

import com.example.fluid.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInstance {
    private static Retrofit retrofit = null;

    public static GetAppointmentServiceInterface getService()
    {
        if(retrofit == null)
        {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            System.out.println("retrofit instance will be created");
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        System.out.println("retrofit instance was created");

        return retrofit.create(GetAppointmentServiceInterface.class);
    }
}
