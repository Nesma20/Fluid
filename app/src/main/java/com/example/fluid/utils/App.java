package com.example.fluid.utils;

import android.app.Application;
import android.content.Context;

import java.util.Locale;

import static com.example.fluid.utils.Constants.ARABIC;
import static com.example.fluid.utils.Constants.ENGLISH;

public class App extends Application {
    private static App mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (!PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(ENGLISH)) {
            LanguageUtil.changeLanguageType(mContext, new Locale(ARABIC));
        } else {
            LanguageUtil.changeLanguageType(mContext, Locale.ENGLISH);
        }
    }

    public static Context getContext() {
        return mContext;
    }
}
