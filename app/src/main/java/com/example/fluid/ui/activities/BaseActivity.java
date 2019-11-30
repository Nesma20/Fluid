package com.example.fluid.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fluid.utils.Constants;
import com.example.fluid.utils.MyContextWrapper;
import com.example.fluid.utils.PreferenceController;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        String lang;
        try {
            if (!PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
                PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, Constants.ENGLISH);
                lang = Constants.ENGLISH;
            } else {
                lang = Constants.ARABIC;
            }
        } catch (Exception e) {
            lang = Constants.ENGLISH;
        }
        super.attachBaseContext(MyContextWrapper.wrap(newBase, new Locale(lang)));
    }

//    public void checkNetworkConnection(){
//        AlertDialog.Builder builder =new AlertDialog.Builder(this);
//        builder.setTitle("No internet Connection");
//        builder.setMessage("Please turn on internet connection to continue");
//        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Log.i("Network", "Connected");
            return true;
        }
        else{

            Log.i("Network","Not Connected");
            return false;
        }
    }

}
