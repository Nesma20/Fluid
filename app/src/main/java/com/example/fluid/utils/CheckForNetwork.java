package com.example.fluid.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckForNetwork {
    /*
     * Check if there is Internet connection or not
     *
     * @param activity parent activity
     * @return true if the internet connection is one
     * false if the internet connection is off
     */
    public static boolean isConnectionOn(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
