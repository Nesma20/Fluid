//package com.example.fluid.model.services;
//
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.util.Log;
//
//import java.io.IOException;
//
//import okhttp3.Interceptor;
//import okhttp3.Response;
//
//public class MyNetworkInterceptor implements Interceptor {
//    Context context;
//    private Context myAppContext;
//    public  MyNetworkInterceptor(Context context){
//        this.context = context;
//        myAppContext =this.context.getApplicationContext();
//
//    }
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        return null;
//    }
//    public boolean isNetworkConnectionAvailable(){
//        ConnectivityManager cm =
//                (ConnectivityManager)myAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnected();
//        if(isConnected) {
//            Log.i("Network", "Connected");
//            return true;
//        }
//        else{
//
//            Log.i("Network","Not Connected");
//            return false;
//        }
//    }
//}
