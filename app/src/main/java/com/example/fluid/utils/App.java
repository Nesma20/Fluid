package com.example.fluid.utils;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModelProviders;

import com.example.fluid.model.services.FirebaseDatabaseService;
import com.example.fluid.model.services.interfaces.RetrofitInstance;
import com.example.fluid.ui.activities.AppViewModel;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;

import java.util.Locale;

import static com.example.fluid.utils.Constants.ARABIC;
import static com.example.fluid.utils.Constants.ENGLISH;

public class App extends Application {
    private static App mContext;
     private static AppViewModel appViewModel = new AppViewModel();
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (!PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(ENGLISH)) {
            LanguageUtil.changeLanguageType(mContext, new Locale(ARABIC));
        } else {
            LanguageUtil.changeLanguageType(mContext, Locale.ENGLISH);
        }
        appViewModel.getIpAndPortFromFirebase();

    }

    public static Context getContext() {
        return mContext;
    }
}
