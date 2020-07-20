package com.thetatechno.fluid.model.services.interfaces;

import com.thetatechno.fluid.utils.App;
import com.thetatechno.fluid.utils.Constants;
import com.thetatechno.fluid.utils.PreferenceController;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInstance {
    private static Retrofit retrofit = null;
    private static String BASE_URL="";

    private static final Object LOCK = new Object();

    public static void clear() {
        synchronized (LOCK) {
            retrofit = null;
        }
    }
    public static MyServicesInterface getService() {
        if (retrofit == null) {
            BASE_URL = PreferenceController.getInstance(App.getContext()).get(Constants.BASE_URL);
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build();

            System.out.println("retrofit instance will be created");
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }
        return retrofit.create(MyServicesInterface.class);
    }
}
